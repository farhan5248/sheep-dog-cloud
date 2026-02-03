cd ..
call gradle clean
call gradle publishToMavenLocal
call gradle xtextasciidocplugin.vscode:installExtension 
cd scripts 