package org.farhan.dsl.asciidoc.ide;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.commands.IExecutableCommandService;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.farhan.dsl.asciidoc.generator.AsciiDocGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;

public class CommandService implements IExecutableCommandService {

	private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

	@Inject
	private IResourceValidator resourceValidator;

	@Override
	public List<String> initialize() {
		logger.debug("Entering initialize");
		List<String> commands = Lists.newArrayList("asciidoc.generate", "asciidoc.validate");
		logger.debug("Exiting initialize");
		return commands;
	}

	@Override
	public Object execute(ExecuteCommandParams params, ILanguageServerAccess access, CancelIndicator cancelIndicator) {
		logger.info("Entering command: {} with parameters: {}", params.getCommand(), params.getArguments());
		final String commandName = params.getCommand();
		final long startTime = System.currentTimeMillis();

		try {
			if ("asciidoc.generate".equals(commandName)) {
				JsonPrimitive uriArg = (JsonPrimitive) Iterables.getFirst(params.getArguments(), null);
				final String uri = (uriArg != null) ? uriArg.getAsString() : null;

				logger.debug("Command {} parameters: {uri: {}}", commandName, uri);

				if (uri != null && !uri.isEmpty()) {
					try {
						Object result = access.doRead(uri, (ILanguageServerAccess.Context context) -> {
							Resource resource = context.getResource();
							if (resource != null) {
								logger.debug("Command {} executing generator for resource: {}", commandName,
										resource.getURI());
								AsciiDocGenerator.generateFromResource(resource);
								return "Code generation completed successfully";
							}
							logger.warn("Command {} failed: Resource not found for uri: {}", commandName, uri);
							return "Resource not found";
						}).get();

						long duration = System.currentTimeMillis() - startTime;
						logger.info("Command {} completed successfully in {}ms: {}", commandName, duration, result);
						return result;
					} catch (InterruptedException | ExecutionException e) {
						long duration = System.currentTimeMillis() - startTime;
						String errorMsg = "Code generation failed: " + e.getMessage();
						logger.error("Command {} failed after {}ms: {}", commandName, duration, errorMsg, e);
						return errorMsg;
					}
				} else {
					String errorMsg = "Document URI missing";
					logger.error("Command {} failed: {}", commandName, errorMsg);
					return errorMsg;
				}
			} else if ("asciidoc.validate".equals(commandName)) {
				JsonPrimitive uriArg = (JsonPrimitive) Iterables.getFirst(params.getArguments(), null);
				final String uri = (uriArg != null) ? uriArg.getAsString() : null;

				logger.debug("Command {} parameters: {uri: {}}", commandName, uri);

				if (uri != null && !uri.isEmpty()) {
					try {
						Object result = access.doRead(uri, (ILanguageServerAccess.Context context) -> {
							Resource resource = context.getResource();
							if (resource != null) {
								logger.debug("Command {} executing validation for resource: {}", commandName,
										resource.getURI());
								
								// Use the injected validator to validate the resource
								List<Issue> issues = resourceValidator.validate(resource, CheckMode.ALL, cancelIndicator);
								
								// Count issues by severity
								int errorCount = 0;
								int warningCount = 0;
								int infoCount = 0;
								
								for (Issue issue : issues) {
									switch (issue.getSeverity()) {
										case ERROR:
											errorCount++;
											break;
										case WARNING:
											warningCount++;
											break;
										case INFO:
											infoCount++;
											break;
									}
								}
								
								String summary = String.format("Validation completed: %d errors, %d warnings, %d info messages", 
									errorCount, warningCount, infoCount);
								logger.debug("Validation summary: {}", summary);
								return summary;
							}
							logger.warn("Command {} failed: Resource not found for uri: {}", commandName, uri);
							return "Resource not found";
						}).get();

						// Note: Diagnostics should show automatically when validation is triggered
						// The manual validation above should be sufficient to identify issues
						// If diagnostics aren't showing, check if the language server has automatic validation enabled

						long duration = System.currentTimeMillis() - startTime;
						logger.info("Command {} completed successfully in {}ms: {}", commandName, duration, result);
						return result;
					} catch (InterruptedException | ExecutionException e) {
						long duration = System.currentTimeMillis() - startTime;
						String errorMsg = "Validation failed: " + e.getMessage();
						logger.error("Command {} failed after {}ms: {}", commandName, duration, errorMsg, e);
						return errorMsg;
					}
				} else {
					String errorMsg = "Document URI missing";
					logger.error("Command {} failed: {}", commandName, errorMsg);
					return errorMsg;
				}
			}

			String errorMsg = "Bad Command: " + commandName;
			logger.error("Command execution failed: {}", errorMsg);
			return errorMsg;
		} finally {
			logger.debug("Exiting execute for command: {}", commandName);
		}
	}
}
