# MCP Server Configuration

## Overview

This application now includes MCP (Model Context Protocol) server capabilities with weather tools integration. The MCP server exposes weather forecasting and alert tools that can be consumed by AI systems.

## Available Tools

### Weather Tools
- **Get weather forecast by co-ordinates**: Get weather forecast for a specific latitude/longitude
- **Get alerts for a state**: Get weather alerts for a US state (input: Two-letter US state code e.g. CA, NY)

### Utility Tools
- **toUpperCase**: Convert text to upper case

## Adding MCP Server to Claude Code

Once the application is running on `http://localhost:8080`, you can add this MCP server to Claude Code using the following command:

```bash
claude add mcp my-weather-server http://localhost:8080/mcp
```

## Usage

1. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

2. The application will be available at `http://localhost:8080`

3. The MCP endpoint will be available at `http://localhost:8080/mcp`

4. Add the server to Claude Code using the command above

5. You can now use weather tools in your Claude Code conversations

## Testing the Weather Tools

The weather service uses the US National Weather Service API and supports:
- Weather forecasting by latitude/longitude coordinates
- Weather alerts by US state code

Example coordinates for testing:
- Seattle, WA: 47.6062, -122.3321
- New York, NY: 40.7128, -74.0060
- Los Angeles, CA: 34.0522, -118.2437

Example state codes for alerts:
- CA (California)
- NY (New York)
- TX (Texas)
- FL (Florida)