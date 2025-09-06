/**
 * LSP Logger - Comprehensive request/response logging for Language Server Protocol communication
 * Part of Step 8: Add request/response logging for LSP communication in the TypeScript client
 * 
 * This module provides detailed logging of all LSP communication between the VSCode client
 * and the Java language server, including request/response correlation, timing, and error tracking.
 */

import * as vscode from 'vscode';

/**
 * LSP Request metadata for tracking and correlation
 */
export interface LSPRequestInfo {
    id: string | number;
    method: string;
    params?: any;
    timestamp: number;
    startTime: Date;
}

/**
 * LSP Response metadata for correlation with requests
 */
export interface LSPResponseInfo {
    id: string | number;
    method: string;
    result?: any;
    error?: any;
    timestamp: number;
    endTime: Date;
    duration: number;
}

/**
 * LSP Notification metadata
 */
export interface LSPNotificationInfo {
    method: string;
    params?: any;
    timestamp: number;
    time: Date;
}

/**
 * Statistics for LSP communication monitoring
 */
export interface LSPStatistics {
    totalRequests: number;
    totalResponses: number;
    totalNotifications: number;
    totalErrors: number;
    averageResponseTime: number;
    longestResponseTime: number;
    shortestResponseTime: number;
    methodStats: Map<string, {
        count: number;
        totalTime: number;
        averageTime: number;
        errors: number;
    }>;
}

/**
 * Comprehensive LSP Logger for detailed communication tracking
 */
export class LSPLogger {
    private outputChannel: vscode.OutputChannel;
    private pendingRequests: Map<string | number, LSPRequestInfo> = new Map();
    private statistics: LSPStatistics;
    private isEnabled: boolean = true;
    private detailedLogging: boolean = true;
    private performanceLogging: boolean = true;

    constructor(outputChannel: vscode.OutputChannel) {
        this.outputChannel = outputChannel;
        this.statistics = this.initializeStatistics();
    }

    /**
     * Initialize statistics tracking
     */
    private initializeStatistics(): LSPStatistics {
        return {
            totalRequests: 0,
            totalResponses: 0,
            totalNotifications: 0,
            totalErrors: 0,
            averageResponseTime: 0,
            longestResponseTime: 0,
            shortestResponseTime: Infinity,
            methodStats: new Map()
        };
    }

    /**
     * Enable or disable LSP logging
     */
    public setEnabled(enabled: boolean): void {
        this.isEnabled = enabled;
        this.outputChannel.appendLine(`LSPLogger: Logging ${enabled ? 'enabled' : 'disabled'}`);
    }

    /**
     * Enable or disable detailed parameter/result logging
     */
    public setDetailedLogging(enabled: boolean): void {
        this.detailedLogging = enabled;
        this.outputChannel.appendLine(`LSPLogger: Detailed logging ${enabled ? 'enabled' : 'disabled'}`);
    }

    /**
     * Enable or disable performance timing logging
     */
    public setPerformanceLogging(enabled: boolean): void {
        this.performanceLogging = enabled;
        this.outputChannel.appendLine(`LSPLogger: Performance logging ${enabled ? 'enabled' : 'disabled'}`);
    }

    /**
     * Log an outgoing LSP request
     */
    public logRequest(id: string | number, method: string, params?: any): void {
        if (!this.isEnabled) return;

        const timestamp = Date.now();
        const startTime = new Date();
        
        const requestInfo: LSPRequestInfo = {
            id,
            method,
            params,
            timestamp,
            startTime
        };

        // Store for correlation with response
        this.pendingRequests.set(id, requestInfo);
        
        // Update statistics
        this.statistics.totalRequests++;
        this.updateMethodStats(method, 0, false);

        // Log the request
        this.outputChannel.appendLine(`LSP Request [${id}] → ${method} at ${startTime.toISOString()}`);
        
        if (this.detailedLogging && params !== undefined) {
            const paramsStr = this.formatParams(params);
            this.outputChannel.appendLine(`  Parameters: ${paramsStr}`);
        }

        if (this.performanceLogging) {
            this.outputChannel.appendLine(`  Request queued at: ${timestamp}`);
        }
    }

    /**
     * Log an incoming LSP response
     */
    public logResponse(id: string | number, result?: any, error?: any): void {
        if (!this.isEnabled) return;

        const timestamp = Date.now();
        const endTime = new Date();
        
        // Find the corresponding request
        const requestInfo = this.pendingRequests.get(id);
        if (!requestInfo) {
            this.outputChannel.appendLine(`LSP Response [${id}] ← (WARNING: No matching request found)`);
            return;
        }

        // Calculate duration
        const duration = timestamp - requestInfo.timestamp;
        
        // Response info created for future extensibility (currently unused)
        // const responseInfo: LSPResponseInfo = {
        //     id,
        //     method: requestInfo.method,
        //     result,
        //     error,
        //     timestamp,
        //     endTime,
        //     duration
        // };

        // Remove from pending requests
        this.pendingRequests.delete(id);

        // Update statistics
        this.statistics.totalResponses++;
        if (error) {
            this.statistics.totalErrors++;
        }
        this.updateResponseStatistics(duration);
        this.updateMethodStats(requestInfo.method, duration, !!error);

        // Log the response
        const statusIcon = error ? '✗' : '✓';
        this.outputChannel.appendLine(`LSP Response [${id}] ← ${requestInfo.method} ${statusIcon} at ${endTime.toISOString()}`);
        
        if (this.performanceLogging) {
            this.outputChannel.appendLine(`  Duration: ${duration}ms (${requestInfo.startTime.toISOString()} → ${endTime.toISOString()})`);
        }

        if (error) {
            const errorStr = this.formatError(error);
            this.outputChannel.appendLine(`  Error: ${errorStr}`);
        } else if (this.detailedLogging && result !== undefined) {
            const resultStr = this.formatResult(result);
            this.outputChannel.appendLine(`  Result: ${resultStr}`);
        }

        // Log performance warnings for slow requests
        if (this.performanceLogging && duration > 5000) { // 5 seconds
            this.outputChannel.appendLine(`  ⚠ SLOW REQUEST: ${requestInfo.method} took ${duration}ms`);
        }
    }

    /**
     * Log an incoming LSP notification
     */
    public logNotification(method: string, params?: any): void {
        if (!this.isEnabled) return;

        const time = new Date();

        // Notification info created for future extensibility (currently unused)
        // const timestamp = Date.now();
        // const notificationInfo: LSPNotificationInfo = {
        //     method,
        //     params,
        //     timestamp,
        //     time
        // };

        // Update statistics
        this.statistics.totalNotifications++;

        // Log the notification
        this.outputChannel.appendLine(`LSP Notification ← ${method} at ${time.toISOString()}`);
        
        if (this.detailedLogging && params !== undefined) {
            const paramsStr = this.formatParams(params);
            this.outputChannel.appendLine(`  Parameters: ${paramsStr}`);
        }

        // Special handling for important notifications
        if (method === 'textDocument/publishDiagnostics') {
            this.logDiagnosticNotification(params);
        } else if (method === 'window/showMessage') {
            this.logServerMessage(params);
        } else if (method === 'window/logMessage') {
            this.logServerLogMessage(params);
        }
    }

    /**
     * Log diagnostic notifications with enhanced context
     */
    private logDiagnosticNotification(params: any): void {
        if (!params || !params.diagnostics) return;

        const uri = params.uri || 'unknown';
        const diagnosticCount = params.diagnostics.length;
        const severityCounts = this.countDiagnosticsBySeverity(params.diagnostics);

        this.outputChannel.appendLine(`  Diagnostics for: ${uri}`);
        this.outputChannel.appendLine(`  Total: ${diagnosticCount}, Errors: ${severityCounts.error}, Warnings: ${severityCounts.warning}, Info: ${severityCounts.info}, Hints: ${severityCounts.hint}`);

        if (this.detailedLogging && diagnosticCount > 0) {
            params.diagnostics.forEach((diagnostic: any, index: number) => {
                const severity = this.getSeverityName(diagnostic.severity);
                const range = diagnostic.range ? `${diagnostic.range.start.line}:${diagnostic.range.start.character}-${diagnostic.range.end.line}:${diagnostic.range.end.character}` : 'unknown';
                this.outputChannel.appendLine(`    [${index + 1}] ${severity}: "${diagnostic.message}" at ${range}`);
            });
        }
    }

    /**
     * Log server messages
     */
    private logServerMessage(params: any): void {
        if (!params) return;
        const type = this.getMessageTypeName(params.type);
        this.outputChannel.appendLine(`  Server Message [${type}]: ${params.message}`);
    }

    /**
     * Log server log messages  
     */
    private logServerLogMessage(params: any): void {
        if (!params) return;
        const type = this.getMessageTypeName(params.type);
        this.outputChannel.appendLine(`  Server Log [${type}]: ${params.message}`);
    }

    /**
     * Get current LSP communication statistics
     */
    public getStatistics(): LSPStatistics {
        return {
            ...this.statistics,
            methodStats: new Map(this.statistics.methodStats)
        };
    }

    /**
     * Reset statistics
     */
    public resetStatistics(): void {
        this.statistics = this.initializeStatistics();
        this.outputChannel.appendLine('LSPLogger: Statistics reset');
    }

    /**
     * Log current statistics summary
     */
    public logStatistics(): void {
        this.outputChannel.appendLine('=== LSP Communication Statistics ===');
        this.outputChannel.appendLine(`Total Requests: ${this.statistics.totalRequests}`);
        this.outputChannel.appendLine(`Total Responses: ${this.statistics.totalResponses}`);
        this.outputChannel.appendLine(`Total Notifications: ${this.statistics.totalNotifications}`);
        this.outputChannel.appendLine(`Total Errors: ${this.statistics.totalErrors}`);
        
        if (this.statistics.totalResponses > 0) {
            this.outputChannel.appendLine(`Average Response Time: ${this.statistics.averageResponseTime.toFixed(2)}ms`);
            this.outputChannel.appendLine(`Longest Response Time: ${this.statistics.longestResponseTime}ms`);
            this.outputChannel.appendLine(`Shortest Response Time: ${this.statistics.shortestResponseTime}ms`);
        }

        // Log pending requests (potential issues)
        if (this.pendingRequests.size > 0) {
            this.outputChannel.appendLine(`Pending Requests: ${this.pendingRequests.size}`);
            for (const [id, request] of this.pendingRequests) {
                const age = Date.now() - request.timestamp;
                this.outputChannel.appendLine(`  [${id}] ${request.method} (${age}ms ago)`);
            }
        }

        // Log method statistics
        if (this.statistics.methodStats.size > 0) {
            this.outputChannel.appendLine('Method Statistics:');
            for (const [method, stats] of this.statistics.methodStats) {
                const errorRate = stats.count > 0 ? (stats.errors / stats.count * 100).toFixed(1) : '0.0';
                this.outputChannel.appendLine(`  ${method}: ${stats.count} calls, ${stats.averageTime.toFixed(2)}ms avg, ${errorRate}% errors`);
            }
        }
        this.outputChannel.appendLine('=====================================');
    }

    /**
     * Update response time statistics
     */
    private updateResponseStatistics(duration: number): void {
        // Update average
        const totalTime = this.statistics.averageResponseTime * (this.statistics.totalResponses - 1) + duration;
        this.statistics.averageResponseTime = totalTime / this.statistics.totalResponses;

        // Update min/max
        this.statistics.longestResponseTime = Math.max(this.statistics.longestResponseTime, duration);
        this.statistics.shortestResponseTime = Math.min(this.statistics.shortestResponseTime, duration);
    }

    /**
     * Update method-specific statistics
     */
    private updateMethodStats(method: string, duration: number, isError: boolean): void {
        let stats = this.statistics.methodStats.get(method);
        if (!stats) {
            stats = { count: 0, totalTime: 0, averageTime: 0, errors: 0 };
            this.statistics.methodStats.set(method, stats);
        }

        stats.count++;
        stats.totalTime += duration;
        stats.averageTime = stats.totalTime / stats.count;
        
        if (isError) {
            stats.errors++;
        }
    }

    /**
     * Format parameters for logging
     */
    private formatParams(params: any): string {
        try {
            if (params === null || params === undefined) {
                return 'null';
            }
            
            // Truncate very large parameter objects
            const paramStr = JSON.stringify(params, null, 2);
            if (paramStr.length > 500) {
                return paramStr.substring(0, 500) + '... (truncated)';
            }
            return paramStr;
        } catch (error) {
            return `[Object: ${typeof params}] (serialization failed)`;
        }
    }

    /**
     * Format result for logging
     */
    private formatResult(result: any): string {
        try {
            if (result === null || result === undefined) {
                return 'null';
            }
            
            // Special handling for common result types
            if (Array.isArray(result)) {
                return `Array[${result.length}] ${result.length <= 10 ? JSON.stringify(result) : '[truncated]'}`;
            }
            
            // Truncate very large results
            const resultStr = JSON.stringify(result, null, 2);
            if (resultStr.length > 300) {
                return resultStr.substring(0, 300) + '... (truncated)';
            }
            return resultStr;
        } catch (error) {
            return `[Object: ${typeof result}] (serialization failed)`;
        }
    }

    /**
     * Format error for logging
     */
    private formatError(error: any): string {
        try {
            if (error === null || error === undefined) {
                return 'null';
            }
            
            if (typeof error === 'string') {
                return error;
            }
            
            if (error.message) {
                return `${error.code || 'ERROR'}: ${error.message}`;
            }
            
            return JSON.stringify(error, null, 2);
        } catch (e) {
            return `[Error: ${typeof error}] (serialization failed)`;
        }
    }

    /**
     * Count diagnostics by severity
     */
    private countDiagnosticsBySeverity(diagnostics: any[]): { error: number, warning: number, info: number, hint: number } {
        const counts = { error: 0, warning: 0, info: 0, hint: 0 };
        
        diagnostics.forEach(diagnostic => {
            switch (diagnostic.severity) {
                case 1: counts.error++; break;
                case 2: counts.warning++; break;
                case 3: counts.info++; break;
                case 4: counts.hint++; break;
                default: counts.info++; break;
            }
        });
        
        return counts;
    }

    /**
     * Get severity name from severity code
     */
    private getSeverityName(severity: number): string {
        switch (severity) {
            case 1: return 'ERROR';
            case 2: return 'WARNING';
            case 3: return 'INFO';
            case 4: return 'HINT';
            default: return 'UNKNOWN';
        }
    }

    /**
     * Get message type name from message type code
     */
    private getMessageTypeName(type: number): string {
        switch (type) {
            case 1: return 'ERROR';
            case 2: return 'WARNING';
            case 3: return 'INFO';
            case 4: return 'LOG';
            default: return 'UNKNOWN';
        }
    }

    /**
     * Dispose of resources
     */
    public dispose(): void {
        this.pendingRequests.clear();
        this.statistics.methodStats.clear();
    }
}