/**
 * LSP Middleware - Intercepts and logs all Language Server Protocol communication
 * Part of Step 8: Add request/response logging for LSP communication in the TypeScript client
 * 
 * This middleware integrates with the VSCode language client to provide comprehensive logging
 * of LSP communication while being compatible with vscode-languageclient v9.0.1
 */

import * as vscode from 'vscode';
import { LSPLogger } from './lspLogger';

/**
 * LSP Middleware for comprehensive communication logging
 * This is a simplified version that works with the language client's configuration options
 */
export class LSPMiddleware {
    private lspLogger: LSPLogger;
    private outputChannel: vscode.OutputChannel;
    private isEnabled: boolean = true;

    constructor(outputChannel: vscode.OutputChannel) {
        this.outputChannel = outputChannel;
        this.lspLogger = new LSPLogger(outputChannel);

        this.outputChannel.appendLine('LSPMiddleware: Initialized with comprehensive logging support (v9.0.1 compatible)');
    }

    /**
     * Enable or disable middleware logging
     */
    public setEnabled(enabled: boolean): void {
        this.isEnabled = enabled;
        this.lspLogger.setEnabled(enabled);
        this.outputChannel.appendLine(`LSPMiddleware: ${enabled ? 'Enabled' : 'Disabled'}`);
    }

    /**
     * Configure logging details
     */
    public configure(options: {
        detailedLogging?: boolean;
        performanceLogging?: boolean;
    }): void {
        if (options.detailedLogging !== undefined) {
            this.lspLogger.setDetailedLogging(options.detailedLogging);
        }
        if (options.performanceLogging !== undefined) {
            this.lspLogger.setPerformanceLogging(options.performanceLogging);
        }
    }

    /**
     * Get the internal LSP logger instance
     */
    public getLogger(): LSPLogger {
        return this.lspLogger;
    }

    /**
     * Create middleware configuration for the language client
     * This creates middleware compatible with vscode-languageclient v9.0.1
     */
    public createMiddleware(): any {
        const self = this;
        
        return {
            // Handle diagnostics notifications
            handleDiagnostics: (uri: vscode.Uri, diagnostics: vscode.Diagnostic[], next: any) => {
                if (self.isEnabled) {
                    // Log the diagnostic notification
                    self.lspLogger.logNotification('textDocument/publishDiagnostics', {
                        uri: uri.toString(),
                        diagnostics: diagnostics.map(d => ({
                            range: {
                                start: { line: d.range.start.line, character: d.range.start.character },
                                end: { line: d.range.end.line, character: d.range.end.character }
                            },
                            severity: d.severity,
                            message: d.message,
                            source: d.source,
                            code: d.code
                        }))
                    });
                }
                
                return next(uri, diagnostics);
            },

            // Handle progress notifications  
            handleWorkDoneProgress: (token: any, params: any, next: any) => {
                if (self.isEnabled) {
                    self.lspLogger.logNotification('$/progress', { token, ...params });
                }
                
                return next(token, params);
            },

            // Create a custom request wrapper for logging
            provideCompletionItem: (document: vscode.TextDocument, position: vscode.Position, context: any, next: any) => {
                if (!self.isEnabled) {
                    return next(document, position, context);
                }

                const requestId = self.generateRequestId();
                const params = {
                    textDocument: { uri: document.uri.toString() },
                    position: { line: position.line, character: position.character },
                    context
                };
                self.lspLogger.logRequest(requestId, 'textDocument/completion', params);

                return next(document, position, context).then(
                    (result: any) => {
                        // Log result with useful summary for completions
                        const resultSummary = Array.isArray(result?.items) ? 
                            { itemCount: result.items.length, isIncomplete: result.isIncomplete } :
                            result;
                        self.lspLogger.logResponse(requestId, resultSummary);
                        return result;
                    },
                    (error: any) => {
                        self.lspLogger.logResponse(requestId, undefined, error);
                        throw error;
                    }
                );
            },

            // Create hover provider wrapper
            provideHover: (document: vscode.TextDocument, position: vscode.Position, next: any) => {
                if (!self.isEnabled) {
                    return next(document, position);
                }

                const requestId = self.generateRequestId();
                const params = {
                    textDocument: { uri: document.uri.toString() },
                    position: { line: position.line, character: position.character }
                };
                self.lspLogger.logRequest(requestId, 'textDocument/hover', params);

                return next(document, position).then(
                    (result: any) => {
                        self.lspLogger.logResponse(requestId, result);
                        return result;
                    },
                    (error: any) => {
                        self.lspLogger.logResponse(requestId, undefined, error);
                        throw error;
                    }
                );
            },

            // Create definition provider wrapper  
            provideDefinition: (document: vscode.TextDocument, position: vscode.Position, next: any) => {
                if (!self.isEnabled) {
                    return next(document, position);
                }

                const requestId = self.generateRequestId();
                const params = {
                    textDocument: { uri: document.uri.toString() },
                    position: { line: position.line, character: position.character }
                };
                self.lspLogger.logRequest(requestId, 'textDocument/definition', params);

                return next(document, position).then(
                    (result: any) => {
                        self.lspLogger.logResponse(requestId, result);
                        return result;
                    },
                    (error: any) => {
                        self.lspLogger.logResponse(requestId, undefined, error);
                        throw error;
                    }
                );
            },

            // Create code action provider wrapper
            provideCodeActions: (document: vscode.TextDocument, range: vscode.Range, context: any, next: any) => {
                if (!self.isEnabled) {
                    return next(document, range, context);
                }

                const requestId = self.generateRequestId();
                const params = {
                    textDocument: { uri: document.uri.toString() },
                    range: {
                        start: { line: range.start.line, character: range.start.character },
                        end: { line: range.end.line, character: range.end.character }
                    },
                    context
                };
                self.lspLogger.logRequest(requestId, 'textDocument/codeAction', params);

                return next(document, range, context).then(
                    (result: any) => {
                        // Log result with useful summary for code actions
                        const resultSummary = Array.isArray(result) ? 
                            { actionCount: result.length, actions: result.map((a: any) => a.title || a.command) } : 
                            result;
                        self.lspLogger.logResponse(requestId, resultSummary);
                        return result;
                    },
                    (error: any) => {
                        self.lspLogger.logResponse(requestId, undefined, error);
                        throw error;
                    }
                );
            },

            // Create document formatting provider wrapper
            provideDocumentFormattingEdits: (document: vscode.TextDocument, options: any, next: any) => {
                if (!self.isEnabled) {
                    return next(document, options);
                }

                const requestId = self.generateRequestId();
                const params = {
                    textDocument: { uri: document.uri.toString() },
                    options
                };
                self.lspLogger.logRequest(requestId, 'textDocument/formatting', params);

                return next(document, options).then(
                    (result: any) => {
                        // Log result with useful summary for formatting
                        const resultSummary = Array.isArray(result) ? 
                            { editCount: result.length } : result;
                        self.lspLogger.logResponse(requestId, resultSummary);
                        return result;
                    },
                    (error: any) => {
                        self.lspLogger.logResponse(requestId, undefined, error);
                        throw error;
                    }
                );
            }
        };
    }

    /**
     * Generate a unique request ID for correlation
     */
    private generateRequestId(): string {
        return `MW_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    }

    /**
     * Get current statistics
     */
    public getStatistics() {
        return this.lspLogger.getStatistics();
    }

    /**
     * Log current statistics
     */
    public logStatistics(): void {
        this.lspLogger.logStatistics();
    }

    /**
     * Reset statistics
     */
    public resetStatistics(): void {
        this.lspLogger.resetStatistics();
    }

    /**
     * Dispose of resources
     */
    public dispose(): void {
        this.lspLogger.dispose();
        this.outputChannel.appendLine('LSPMiddleware: Disposed');
    }
}