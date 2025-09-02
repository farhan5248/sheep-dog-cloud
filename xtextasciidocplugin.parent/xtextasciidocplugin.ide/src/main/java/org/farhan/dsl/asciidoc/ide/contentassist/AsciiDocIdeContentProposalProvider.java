package org.farhan.dsl.asciidoc.ide.contentassist;

import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistEntry;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;

import com.google.inject.Inject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map.Entry;

import org.eclipse.xtext.Assignment;
import org.farhan.dsl.common.*;
import org.farhan.dsl.asciidoc.LanguageAccessImpl;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;

/**
 * See
 * https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#content-assist
 * on how to customize the content assistant.
 */
public class AsciiDocIdeContentProposalProvider extends IdeContentProposalProvider {

	@Inject
	private AsciiDocGrammarAccess myDslGrammarAccess;

	@Override
	protected void _createProposals(Assignment assignment, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {

		if (context.getCurrentModel() != null && context.getCurrentModel() instanceof TestStep) {
			completeName((TestStep) context.getCurrentModel(), assignment, context, acceptor);
		}
		super._createProposals(assignment, context, acceptor);
	}

	private void completeName(TestStep step, Assignment assignment, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
		try {
			for (Entry<String, Proposal> p : LanguageHelper.proposeTestStepName(new LanguageAccessImpl(step))
					.entrySet()) {
				ContentAssistEntry proposal = getProposalCreator().createSnippet(p.getValue().getReplacement(),
						p.getValue().getDisplay(), context);
				if (proposal != null) {
					proposal.setDocumentation(p.getValue().getDocumentation());
					acceptor.accept(proposal, 0);
				}
			}

			for (Entry<String, Proposal> p : LanguageHelper.proposeStepParameters(new LanguageAccessImpl(step))
					.entrySet()) {
				// TODO this is an ugly hack to make the proposals work. The |=== and ----
				// shouldn't be hard-coded here. Move them into the languageAccessImpl class
				String replacement;
				if (p.getValue().getReplacement().contentEquals(LanguageAccessImpl.STEP_PARAMETER_TEXT)) {
					replacement = "----\n" + "todo" + "\n----";
				} else {
					replacement = "|===\n" + p.getValue().getReplacement() + "\n|===";
				}

				ContentAssistEntry proposal = getProposalCreator().createSnippet(replacement, p.getValue().getDisplay(),
						context);
				if (proposal != null) {
					proposal.setDocumentation(p.getValue().getDocumentation());
					acceptor.accept(proposal, 0);
				}
			}

		} catch (Exception e) {
			// TODO temp hack
			getProposalCreator().createSnippet(logError(e, step.getName()), "There was an error", context);
		}
	}

	private String logError(Exception e, String name) {
		// TODO inject the logger instead
		System.out.println("There was an error listing proposals for: " + name);
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		System.out.println(sw.toString());
		return sw.toString();
	}
}
