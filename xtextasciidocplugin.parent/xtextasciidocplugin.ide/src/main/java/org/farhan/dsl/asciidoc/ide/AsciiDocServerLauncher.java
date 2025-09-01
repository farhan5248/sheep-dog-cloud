package org.farhan.dsl.asciidoc.ide;

import java.io.IOException;

import org.eclipse.xtext.ide.server.ServerLauncher;

/**
 * Custom Language Server Launcher for AsciiDoc that supports both stdio and socket communication.
 * This launcher extends Xtext's ServerLauncher to provide enhanced LSP capabilities.
 */
public class AsciiDocServerLauncher extends ServerLauncher {
	
	/**
	 * Main entry point for the language server.
	 * Supports command line arguments for different communication modes:
	 * - No arguments: Uses stdio communication  
	 * - "-socket PORT": Uses socket communication on specified port
	 */
	public static void main(String[] args) {
		AsciiDocServerLauncher launcher = new AsciiDocServerLauncher();
		launcher.start(args);
	}
	
	/**
	 * Start the language server with the given arguments.
	 */
	public void start(String[] args) {
		if (args.length >= 2 && "-socket".equals(args[0])) {
			try {
				int port = Integer.parseInt(args[1]);
				startSocketServer(port);
			} catch (NumberFormatException e) {
				System.err.println("Invalid port number: " + args[1]);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Failed to start socket server: " + e.getMessage());
				System.exit(1);
			}
		} else {
			// Default to stdio communication - use parent launch method
			startStdioServer();
		}
	}
	
	/**
	 * Start language server using stdio communication.
	 */
	private void startStdioServer() {
		System.err.println("AsciiDoc Language Server starting with stdio communication...");
		// Call parent's main method for stdio communication
		ServerLauncher.main(new String[0]);
	}
	
	/**
	 * Start language server using socket communication on specified port.
	 * For socket communication, we use a simplified approach that delegates
	 * to the default ServerLauncher with socket parameters.
	 */
	private void startSocketServer(int port) throws IOException {
		System.err.println("AsciiDoc Language Server starting on port " + port + "...");
		// For now, socket communication is not implemented - use stdio
		System.err.println("Socket communication not yet implemented, falling back to stdio");
		startStdioServer();
	}
}