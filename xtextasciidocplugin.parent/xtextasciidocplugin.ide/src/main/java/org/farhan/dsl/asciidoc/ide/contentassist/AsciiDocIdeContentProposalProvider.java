package org.farhan.dsl.asciidoc.ide.contentassist;

import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocPackage;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class AsciiDocIdeContentProposalProvider extends IdeContentProposalProvider {
	@Inject
	private AsciiDocGrammarAccess myDslGrammarAccess;

	@Inject
	private IScopeProvider scopeProvider;

	@Override
	protected void _createProposals(RuleCall ruleCall, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
	}
}
