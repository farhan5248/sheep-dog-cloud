# LSP Request/Response Logging Implementation

## Overview
Step 8 of the logging enhancement plan has been completed successfully. This implementation adds comprehensive LSP request/response logging to the TypeScript client of the VSCode extension.

## Components Implemented

### 1. LSPLogger (`src/lspLogger.ts`)
**Purpose**: Core logging engine for LSP communication
**Features**:
- **Request Tracking**: Logs all outgoing requests with timing and correlation IDs
- **Response Correlation**: Matches responses to requests for end-to-end tracing
- **Notification Logging**: Captures all LSP notifications from server to client
- **Performance Monitoring**: Tracks response times, averages, and performance metrics
- **Statistics Collection**: Comprehensive statistics on communication patterns
- **Diagnostic Enhancement**: Special handling for diagnostic notifications with severity counts
- **Error Tracking**: Detailed error logging with context

**Key Methods**:
- `logRequest(id, method, params)` - Log outgoing LSP requests
- `logResponse(id, result, error)` - Log incoming responses with correlation
- `logNotification(method, params)` - Log server notifications
- `getStatistics()` - Get comprehensive communication statistics
- `resetStatistics()` - Reset statistics for fresh tracking

### 2. LSPMiddleware (`src/lspMiddleware.ts`)
**Purpose**: Middleware layer for intercepting LSP communication
**Features**:
- **Request Interception**: Intercepts all LSP requests before they're sent
- **Response Interception**: Captures responses and correlates with requests
- **Provider Wrapping**: Wraps VSCode language providers (completion, hover, definition, etc.)
- **Timing Tracking**: Measures request/response timing for performance analysis
- **Error Handling**: Comprehensive error logging with context preservation
- **Configurable Logging**: Allows runtime configuration of logging detail levels

**Provider Coverage**:
- Completion requests (`textDocument/completion`)
- Hover requests (`textDocument/hover`)
- Definition requests (`textDocument/definition`)
- Code action requests (`textDocument/codeAction`)
- Formatting requests (`textDocument/formatting`)
- Diagnostic notifications (`textDocument/publishDiagnostics`)
- Progress notifications (`$/progress`)

### 3. Enhanced CommunicationService (`src/communicationService.ts`)
**Purpose**: Integration layer for LSP logging with existing communication infrastructure
**Enhancements**:
- **Middleware Integration**: Seamlessly integrates LSPMiddleware with language client
- **Configuration-Based Setup**: Configures logging based on extension settings
- **Statistics Access**: Provides access to LSP communication statistics
- **Runtime Reconfiguration**: Allows updating logging settings without restart

**New Methods**:
- `getLSPMiddleware()` - Access to middleware for direct control
- `getLSPStatistics()` - Retrieve communication statistics
- `logLSPStatistics()` - Output detailed statistics to output channel
- `resetLSPStatistics()` - Reset statistics counters

### 4. Extension Commands (`src/extension.ts`)
**Purpose**: User-facing commands for managing LSP logging
**New Commands**:

#### `asciidoc.lsp.showStatistics`
- **Title**: "AsciiDoc: Show LSP Communication Statistics"
- **Function**: Displays comprehensive LSP communication statistics
- **Output**: Shows request counts, response times, error rates, method-specific metrics
- **UI**: Modal dialog with summary + detailed output channel display

#### `asciidoc.lsp.resetStatistics`
- **Title**: "AsciiDoc: Reset LSP Statistics"
- **Function**: Resets all LSP communication statistics counters
- **Use Case**: Start fresh tracking for specific testing sessions

#### `asciidoc.lsp.toggleLogging`
- **Title**: "AsciiDoc: Toggle LSP Detailed Logging"
- **Function**: Toggles detailed parameter/result logging on/off
- **Benefit**: Allows reducing log verbosity while maintaining request/response tracking

### 5. Package.json Updates
**Changes**: Added command registrations for new LSP logging commands
**Category**: "AsciiDoc LSP" for easy discovery in Command Palette

## Technical Architecture

### Request/Response Flow
1. **Request Origination**: User action triggers LSP request (e.g., completion, hover)
2. **Middleware Interception**: LSPMiddleware intercepts request before transmission
3. **Request Logging**: LSPLogger records request with correlation ID and timing
4. **Server Processing**: Request sent to Java language server
5. **Response Interception**: Middleware captures response from server
6. **Response Logging**: LSPLogger correlates response with original request
7. **Statistics Update**: Communication statistics updated with timing and success/error info
8. **User Feedback**: Response returned to VSCode for display to user

### Logging Levels
- **Basic**: Request/response method names and timing
- **Detailed**: Includes parameters and results (configurable)
- **Performance**: Timing analysis and slow request warnings
- **Error**: Comprehensive error context and debugging information

### Statistics Tracking
- **Request Counts**: Total requests sent by method
- **Response Times**: Average, minimum, maximum response times
- **Error Rates**: Error counts by method and overall error rate
- **Method Statistics**: Per-method breakdown of performance and reliability
- **Pending Requests**: Detection of hung or slow requests

## Configuration Integration

### Extension Settings Integration
- Respects `debug.verboseLogging` for detailed parameter/result logging
- Uses `performance.enableBackgroundProcessing` for performance logging
- Integrates with existing error notification preferences

### Runtime Configuration
- Logging can be enabled/disabled without server restart
- Logging detail level can be adjusted dynamically
- Statistics can be reset for focused testing periods

## Benefits

### For Developers
- **Complete LSP Visibility**: See every request/response with timing
- **Performance Analysis**: Identify slow operations and bottlenecks
- **Error Debugging**: Comprehensive error context for troubleshooting
- **Request Correlation**: Match responses to originating requests

### For Users
- **Transparent Operation**: Logging doesn't impact normal extension functionality
- **On-Demand Statistics**: View communication health via commands
- **Configurable Detail**: Control log verbosity based on needs

### For Support
- **Diagnostic Data**: Rich information for troubleshooting user issues
- **Performance Metrics**: Data for optimizing language server performance
- **Error Context**: Detailed error information for bug reports

## Testing Integration

### Development Testing
- Use `asciidoc.lsp.showStatistics` to verify request/response patterns
- Monitor performance metrics during development
- Reset statistics between test runs for clean data

### Production Monitoring
- Enable basic logging for production deployments
- Use detailed logging for debugging specific issues
- Statistics provide ongoing health monitoring

## Future Enhancements
- Integration with Steps 9-10 for enhanced diagnostic and code action logging
- Export statistics to JSON format for analysis
- Request replay functionality for testing
- Performance threshold alerts

## Compatibility
- **VSCode**: Compatible with VS Code 1.103.0+
- **Language Client**: Compatible with vscode-languageclient v9.0.1
- **TypeScript**: Compiled and tested with TypeScript
- **Windows**: Tested on Windows development environment

## Files Modified/Created
### New Files
- `src/lspLogger.ts` - Core LSP logging functionality
- `src/lspMiddleware.ts` - LSP communication middleware
- `LSP_LOGGING_IMPLEMENTATION.md` - This documentation

### Modified Files
- `src/communicationService.ts` - Integration with LSP middleware
- `src/extension.ts` - New commands for LSP logging management
- `package.json` - Command registrations for Command Palette
- `logging-plan.md` - Updated Step 8 completion status

## Command Usage Examples

### View Communication Statistics
1. Open Command Palette (Ctrl+Shift+P)
2. Type "AsciiDoc: Show LSP Communication Statistics"
3. View modal dialog summary
4. Check Output Channel for detailed method statistics

### Reset Statistics for Clean Testing
1. Open Command Palette
2. Type "AsciiDoc: Reset LSP Statistics"
3. Confirm reset completion
4. Begin new testing session with clean counters

### Toggle Detailed Logging
1. Open Command Palette  
2. Type "AsciiDoc: Toggle LSP Detailed Logging"
3. Observe confirmation message
4. Check output for detailed parameter logging

This implementation provides the foundation for Steps 9 and 10 of the logging plan, which will build upon this infrastructure for enhanced diagnostic and code action logging.