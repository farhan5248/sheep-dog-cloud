'use strict';

import * as path from 'path';
import * as os from 'os';

import {Trace} from 'vscode-jsonrpc';
import { commands, window, workspace, ExtensionContext, Uri } from 'vscode';
import { LanguageClient, LanguageClientOptions, ServerOptions } from 'vscode-languageclient/node';

let lc: LanguageClient;

export function activate(context: ExtensionContext) {
    // The server is a locally installed in src/asciidoc
    let launcher = os.platform() === 'win32' ? 'asciidoc-standalone.bat' : 'asciidoc-standalone';
    let script = context.asAbsolutePath(path.join('src', 'asciidoc', 'bin', launcher));
    
    console.log(`DEBUG: Resolved script path: ${script}`);
    console.log(`DEBUG: Platform: ${os.platform()}`);

    let serverOptions: ServerOptions;
    if (os.platform() === 'win32') {
        // On Windows, use shell: true for better batch file handling
        console.log(`DEBUG: Will spawn: ${script}`);
        serverOptions = {
            run : { command: script, options: { shell: true } },
            debug: { command: script, options: { shell: true, env: createDebugEnv() } }
        };
    } else {
        serverOptions = {
            run : { command: script },
            debug: { command: script, args: [], options: { env: createDebugEnv() } }
        };
    }
    
    let clientOptions: LanguageClientOptions = {
        documentSelector: ['asciidoc'],
        synchronize: {
            fileEvents: workspace.createFileSystemWatcher('**/*.*')
        }
    };
    
    // Create the language client and start the client.
    lc = new LanguageClient('Xtext AsciiDoc Server', serverOptions, clientOptions);
    
    var disposable2 =commands.registerCommand("asciidoc.a.proxy", async () => {
        let activeEditor = window.activeTextEditor;
        if (!activeEditor || !activeEditor.document || activeEditor.document.languageId !== 'asciidoc') {
            return;
        }

        if (activeEditor.document.uri instanceof Uri) {
            commands.executeCommand("asciidoc.a", activeEditor.document.uri.toString());
        }
    })
    context.subscriptions.push(disposable2);
    
    // enable tracing (.Off, .Messages, Verbose)
    lc.setTrace(Trace.Verbose);
    lc.start();
}
export function deactivate() {
    return lc.stop();
}
function createDebugEnv() {
    return Object.assign({
        JAVA_OPTS:"-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n,quiet=y"
    }, process.env)
}