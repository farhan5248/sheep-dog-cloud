package org.xtext.example.mydsl.ide;

import org.eclipse.xtext.ide.server.ServerModule;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;

/**
 * Custom Language Server Module for MyDsl.
 * This module extends Xtext's ServerModule to provide MyDsl-specific
 * language server configuration and services.
 */
public class MyDslLanguageServerModule extends ServerModule {
	
	/**
	 * Configure custom output configuration provider to ensure
	 * code generation goes to the correct project directory.
	 */
	public Class<? extends IOutputConfigurationProvider> bindIOutputConfigurationProvider() {
		return MyDslOutputConfigurationProvider.class;
	}
}