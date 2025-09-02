package org.farhan.dsl.asciidoc.ide;

import org.eclipse.xtext.ide.editor.quickfix.AbstractDeclarativeIdeQuickfixProvider;
import org.eclipse.xtext.ide.editor.quickfix.DiagnosticResolutionAcceptor;
import org.eclipse.xtext.ide.editor.quickfix.QuickFix;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.farhan.dsl.asciidoc.validation.AsciiDocValidator;

public class AsciiDocIdeQuickfixProvider extends AbstractDeclarativeIdeQuickfixProvider {
	
	@QuickFix(AsciiDocValidator.INVALID_NAME)
	public void textFixLowerCaseName(DiagnosticResolutionAcceptor acceptor) {
		acceptor.accept("Capitalize Name",  (diagnostic, obj, document) -> {
			return createTextEdit(diagnostic, StringExtensions.toFirstUpper(document.getSubstring(diagnostic.getRange())));
		}
			
		);
	}

}
