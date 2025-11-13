package org.farhan.dsl.asciidoc.ide.contentassist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistEntry;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;
import org.farhan.dsl.lang.ITestProject;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.TestStepIssueProposal;
import org.farhan.dsl.lang.TestStepIssueResolver;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map.Entry;

import org.eclipse.xtext.Assignment;
//import org.farhan.dsl.common.*;
import org.farhan.dsl.asciidoc.LanguageAccessImpl;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.impl.SourceFileRepository;
import org.farhan.dsl.asciidoc.impl.TestProjectImpl;
import org.farhan.dsl.asciidoc.impl.TestStepImpl;

/**
 * See
 * https://www.eclipse.org/Xtext/documentation/310_eclipse_support.html#content-assist
 * on how to customize the content assistant.
 */
public class AsciiDocIdeContentProposalProvider extends IdeContentProposalProvider {

	private static final Logger logger = LoggerFactory.getLogger(AsciiDocIdeContentProposalProvider.class);

	@Override
	protected void _createProposals(Assignment assignment, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
		logger.debug("Entering _createProposals with assignment: {}",
				assignment != null ? assignment.getFeature() : "null");
		try {
			if (context == null) {
				logger.warn("ContentAssistContext is null, cannot provide proposals");
				return;
			}

			if (context.getCurrentModel() != null && context.getCurrentModel() instanceof TestStep) {
				logger.debug("Current model is TestStep: {}", ((TestStep) context.getCurrentModel()).getName());
				completeName((TestStep) context.getCurrentModel(), assignment, context, acceptor);
			} else {
				logger.debug("Current model is not TestStep or is null: {}",
						context.getCurrentModel() != null ? context.getCurrentModel().getClass() : "null");
			}

			super._createProposals(assignment, context, acceptor);
			logger.debug("Exiting {}", "_createProposals");
		} catch (Exception e) {
			logger.error("Error creating proposals for assignment '{}': {}",
					assignment != null ? assignment.getFeature() : "null", e.getMessage(), e);
		}
	}

	private void completeName(TestStep step, Assignment assignment, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
		logger.debug("Entering completeName for step: {}", step != null ? step.getName() : "null");
		try {
			if (step == null) {
				logger.warn("TestStep is null, cannot provide completion proposals");
				return;
			}

			logger.debug("Creating TestStep name proposals for: {}", step.getName());
			int proposalCount = 0;

			ITestProject testProject = new TestProjectImpl(
					new SourceFileRepository(step.eResource().getURI().toFileString().replace(File.separator, "/")));
			ITestStep iTestStep = new TestStepImpl(step);
			iTestStep.getParent().getParent().setParent(testProject);
			for (Entry<String, TestStepIssueProposal> p : TestStepIssueResolver.proposeName(iTestStep, testProject)
					.entrySet()) {
				ContentAssistEntry proposal = getProposalCreator().createSnippet(p.getValue().getReplacement(),
						p.getValue().getDisplay(), context);
				if (proposal != null) {
					proposal.setDocumentation(p.getValue().getDocumentation());
					acceptor.accept(proposal, 0);
					proposalCount++;
					logger.debug("Added TestStep name proposal: {}", p.getValue().getDisplay());
				}
			}

			logger.debug("Created {} TestStep name proposals", proposalCount);

			logger.debug("Creating step parameter proposals for: {}", step.getName());
			int paramProposalCount = 0;
			for (Entry<String, TestStepIssueProposal> p : TestStepIssueResolver
					.proposeStepParameters(iTestStep, testProject).entrySet()) {
				// TODO this is an ugly hack to make the proposals work. The |=== and ----
				// shouldn't be hard-coded here. Move them into the languageAccessImpl class
				String replacement;
				if (p.getValue().getReplacement().contentEquals(LanguageAccessImpl.STEP_PARAMETER_TEXT)) {
					replacement = "----\n" + "todo" + "\n----";
					logger.debug("Creating text parameter proposal");
				} else {
					replacement = "|===\n" + p.getValue().getReplacement() + "\n|===";
					logger.debug("Creating table parameter proposal with replacement: {}",
							p.getValue().getReplacement());
				}

				ContentAssistEntry proposal = getProposalCreator().createSnippet(replacement, p.getValue().getDisplay(),
						context);
				if (proposal != null) {
					proposal.setDocumentation(p.getValue().getDocumentation());
					acceptor.accept(proposal, 0);
					paramProposalCount++;
					logger.debug("Added parameter proposal: {}", p.getValue().getDisplay());
				}
			}
			logger.debug("Created {} parameter proposals", paramProposalCount);

		} catch (Exception e) {
			logger.error("Error creating completion proposals for step '{}': {}",
					step != null ? step.getName() : "null", e.getMessage(), e);

			try {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				ContentAssistEntry errorProposal = getProposalCreator().createSnippet(sw.toString(),
						"There was an error", context);
				if (errorProposal != null) {
					acceptor.accept(errorProposal, 0);
				}
			} catch (Exception innerE) {
				logger.error("Failed to create error proposal: {}", innerE.getMessage(), innerE);
			}
		}
		logger.debug("Exiting {}", "completeName");
	}
}
