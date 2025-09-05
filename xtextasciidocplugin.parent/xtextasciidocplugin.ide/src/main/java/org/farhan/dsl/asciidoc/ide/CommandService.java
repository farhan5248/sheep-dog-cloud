package org.farhan.dsl.asciidoc.ide;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.commands.IExecutableCommandService;
import org.eclipse.xtext.util.CancelIndicator;
import org.farhan.dsl.asciidoc.generator.AsciiDocGenerator;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonPrimitive;

public class CommandService implements IExecutableCommandService {
	@Override
	public List<String> initialize() {
		return Lists.newArrayList("asciidoc.generate");
	}

	@Override
	public Object execute(ExecuteCommandParams params, ILanguageServerAccess access, CancelIndicator cancelIndicator) {
		if ("asciidoc.generate".equals(params.getCommand())) {
			JsonPrimitive uriArg = (JsonPrimitive) Iterables.getFirst(params.getArguments(), null);
			String uri = null;
			if (uriArg != null) {
				uri = uriArg.getAsString();
			}
			if (uri != null && !uri.isEmpty()) {
				try {
					return access.doRead(uri, (ILanguageServerAccess.Context context) -> {
						Resource resource = context.getResource();
						if (resource != null) {
							AsciiDocGenerator.generateFromResource(resource);
							return "Code generation completed successfully";
						}
						return "Resource not found";
					}).get();
				} catch (InterruptedException | ExecutionException e) {
					return "Code generation failed: " + e.getMessage();
				}
			} else {
				return "Document URI missing";
			}
		}
		return "Bad Command";
	}
}
