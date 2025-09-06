# Xtext AsciiDoc Extension - Log Viewing Guide

## Overview

The Xtext AsciiDoc extension provides comprehensive logging for both the TypeScript client and Java language server components. This guide explains where logs are stored and how to access them for debugging and troubleshooting.

## Log File Locations

### Default Log Directory
- **Location**: `~/.vscode/logs/` (User home directory)
- **Windows**: `C:\Users\{username}\.vscode\logs\`
- **Linux/Mac**: `~/.vscode/logs/`

### Custom Log Directory
You can customize the log directory by setting the `asciidoc.logDirectory` configuration in VS Code:
```json
{
  "asciidoc.logDirectory": "/path/to/your/logs"
}
```

### Log File Structure

#### 1. Main Language Server Logs
- **File**: `xtextasciidocplugin-lsp.log`
- **Contains**: All language server activity (INFO level and above)
- **Rotation**: Daily, compressed after 24 hours
- **Size Limit**: 1GB total
- **Format**: `YYYY-MM-DD HH:mm:ss.SSS [thread] LEVEL logger - message`

#### 2. Custom Package Logs (org.farhan.*)
- **File**: `xtextasciidocplugin-lsp.org.farhan.log`
- **Contains**: Detailed logs from custom classes (DEBUG level and above)
- **Includes**: 
  - AsciidocGenerator operations
  - Validation workflows
  - Content proposal provider actions
  - Quick fix provider executions
- **Rotation**: Daily, compressed after 24 hours
- **Size Limit**: 1GB total

#### 3. LSP Communication Traces
- **File**: `xtextasciidocplugin-lsp-trace.log`
- **Contains**: Detailed LSP protocol communication traces
- **Includes**: 
  - All LSP requests and responses
  - Method execution traces
  - Performance timing information
- **Rotation**: Daily, compressed after 24 hours
- **Size Limit**: 200MB total

#### 4. Archived Files (Previous Days)
- **Pattern**: `{logname}-YYYY-MM-DD.log.gz`
- **Examples**: 
  - `xtextasciidocplugin-lsp-2025-01-05.log.gz`
  - `farhan-2025-01-05.log.gz`
  - `lsp-trace-2025-01-05.log.gz`

## Viewing Logs in VS Code

### Built-in Commands

#### 1. Show Extension Logs
- **Command**: `AsciiDoc: Show Extension Logs`
- **Shortcut**: Open Command Palette (Ctrl+Shift+P) → type "AsciiDoc: Show Extension Logs"
- **Shows**: TypeScript client-side logs in VS Code Output panel
- **Channel**: "Xtext AsciiDoc Extension"

#### 2. Show Language Server Logs
- **Command**: `AsciiDoc: Show Language Server Logs`
- **Shows**: Java language server logs in VS Code Output panel  
- **Channel**: "Xtext AsciiDoc Language Server"

#### 3. Export Logs to File
- **Command**: `AsciiDoc: Export Logs to File`
- **Function**: Exports current logs to a timestamped file for sharing
- **Location**: Saves to workspace root or user-selected directory

### Output Panel Access
1. Open VS Code Output panel: `View` → `Output` (or `Ctrl+Shift+U`)
2. Select channel from dropdown:
   - "Xtext AsciiDoc Extension" (client logs)
   - "Xtext AsciiDoc Language Server" (server logs)

## Log Level Configuration

### Available Levels
- **OFF**: No logging
- **ERROR**: Only critical errors
- **WARN**: Warnings and errors
- **INFO**: General information (default)
- **DEBUG**: Detailed debugging information
- **TRACE**: All activity including LSP communication

### Setting Log Level
1. **Via Settings UI**: 
   - Open Settings (Ctrl+,)
   - Search for "asciidoc.logging.level"
   - Select desired level

2. **Via settings.json**:
   ```json
   {
     "asciidoc.logging.level": "DEBUG"
   }
   ```

3. **Runtime Change**:
   - Command: `AsciiDoc: Set Logging Level`
   - Select level from dropdown

### Specific Logging Controls
Enable/disable specific types of logging:
```json
{
  "asciidoc.logging.logRequests": true,        // LSP requests
  "asciidoc.logging.logResponses": true,       // LSP responses
  "asciidoc.logging.logNotifications": true,   // LSP notifications
  "asciidoc.logging.logLifecycleEvents": true, // Start/stop/restart
  "asciidoc.logging.logServerOutput": false    // Raw server output
}
```

## Troubleshooting Common Issues

### Issue: No Log Files Created
**Check**:
1. Log directory permissions: `~/.vscode/logs/` should be writable
2. Environment variables: `LOG_PATH` if set custom path
3. Extension activation: Ensure extension is active for .asciidoc files

**Solution**:
```bash
# Windows
mkdir "%USERPROFILE%\.vscode\logs"
# Linux/Mac  
mkdir -p ~/.vscode/logs
```

### Issue: Log Files Too Large
**Solution**:
1. Reduce log level from TRACE/DEBUG to INFO
2. Disable unnecessary logging options
3. Clear logs: `AsciiDoc: Clear All Logs` command

### Issue: Missing Specific Method Traces
**Solution**:
1. Set logging level to DEBUG or TRACE
2. Enable specific logging categories:
   ```json
   {
     "asciidoc.logging.level": "DEBUG",
     "asciidoc.logging.logRequests": true,
     "asciidoc.logging.logResponses": true
   }
   ```
3. Restart language server: `AsciiDoc: Restart Language Server`

## Reading Log Files Externally

### Viewing Compressed Archives
```bash
# Windows (with 7-zip)
7z x xtextasciidocplugin-lsp-2025-01-05.log.gz

# Linux/Mac
gunzip -c xtextasciidocplugin-lsp-2025-01-05.log.gz | less
```

### Searching Logs
```bash
# Search for specific method
grep "doGenerateResource" xtextasciidocplugin-lsp.log

# Search for errors
grep "ERROR" xtextasciidocplugin-lsp.log

# Search for specific timeframe
grep "2025-01-06 14:" xtextasciidocplugin-lsp.log
```

### PowerShell Examples (Windows)
```powershell
# View last 50 lines
Get-Content -Tail 50 "~/.vscode/logs/xtextasciidocplugin-lsp.log"

# Search for pattern
Select-String -Path "~/.vscode/logs/xtextasciidocplugin-lsp.log" -Pattern "doGenerateResource"

# Monitor log file in real-time
Get-Content -Wait -Tail 10 "~/.vscode/logs/xtextasciidocplugin-lsp.log"
```

## Environment Variables

### LOG_LEVEL
- **Purpose**: Override default log level
- **Values**: OFF, ERROR, WARN, INFO, DEBUG, TRACE
- **Example**: `export LOG_LEVEL=DEBUG`

### LOG_PATH
- **Purpose**: Override default log directory
- **Default**: `${user.home}/.vscode/logs`
- **Example**: `export LOG_PATH=/tmp/xtext-logs`

## Performance Considerations

### Log File Rotation
- Files automatically rotate daily
- Compressed archives older than 1 day
- Total size caps prevent disk space issues

### Memory Usage
- In-memory log entries limited (default: 1000)
- Configurable via `asciidoc.logging.maxLogEntries`

### Impact on Performance
- **INFO level**: Minimal impact
- **DEBUG level**: Slight impact
- **TRACE level**: Noticeable impact, use only for debugging

## Best Practices

### For Normal Use
```json
{
  "asciidoc.logging.level": "INFO",
  "asciidoc.logging.logLifecycleEvents": true,
  "asciidoc.logging.logRequests": false,
  "asciidoc.logging.logResponses": false
}
```

### For Debugging
```json
{
  "asciidoc.logging.level": "DEBUG",
  "asciidoc.logging.logRequests": true,
  "asciidoc.logging.logResponses": true,
  "asciidoc.logging.logNotifications": true,
  "asciidoc.logging.logServerOutput": true
}
```

### For Issue Reporting
1. Set logging to DEBUG or TRACE level
2. Reproduce the issue
3. Export logs: `AsciiDoc: Export Logs to File`
4. Include exported file in issue report

## Log Message Format

### Standard Format
```
2025-01-06 14:30:15.123 [main] INFO  o.f.d.a.generator.AsciidocGenerator - Starting doGenerateResource for: example.asciidoc
```

### Format Components
- **Timestamp**: `2025-01-06 14:30:15.123`
- **Thread**: `[main]`
- **Level**: `INFO`
- **Logger**: `o.f.d.a.generator.AsciidocGenerator` (abbreviated)
- **Message**: Actual log message

### LSP Trace Format
```
2025-01-06 14:30:15.123 [JsonRpcDispatcher-1] TRACE o.f.d.a.lsp.LSPTraceLogger - REQUEST: textDocument/completion
2025-01-06 14:30:15.124 [JsonRpcDispatcher-1] TRACE o.f.d.a.lsp.LSPTraceLogger - RESPONSE: 15 completion items returned
```

## Summary

This logging system provides comprehensive visibility into both client and server operations. Use the built-in VS Code commands for quick access, or access log files directly for detailed analysis. Adjust logging levels based on your needs, keeping in mind the performance impact of verbose logging.