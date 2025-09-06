package org.farhan.dsl.asciidoc.ide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.ide.server.codeActions.QuickFixCodeActionService;
import org.farhan.dsl.asciidoc.validation.AsciiDocValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.Position;

public class MyDslCodeActionService extends QuickFixCodeActionService {

    private static final Logger logger = LoggerFactory.getLogger(MyDslCodeActionService.class);

    @Override
    public List<Either<Command, CodeAction>> getCodeActions(Options options, Diagnostic diagnostic) {

        Resource resource = options.getResource();
        Document document = options.getDocument();
        List<Either<Command, CodeAction>> result = new ArrayList<>();
        logger.debug("Processing diagnostic: {}", diagnostic.getMessage());
        logger.debug("Processing diagnostic: {}", options.getURI());
        logger.debug("Processing diagnostic: {}", diagnostic.getData().toString());
        if (AsciiDocValidator.INVALID_NAME.equals(diagnostic.getCode().get().toString())) {
            String text = document.getSubstring(diagnostic.getRange());
            CodeAction action = new CodeAction();
            action.setKind(CodeActionKind.QuickFix);
            action.setTitle("Duplicate the name");
            action.setDiagnostics(Collections.singletonList(diagnostic));
            TextEdit textEdit = new TextEdit();
            textEdit.setRange(diagnostic.getRange());
            textEdit.setNewText(text + text);
            WorkspaceEdit workspaceEdit = new WorkspaceEdit();
            workspaceEdit.getChanges().put(resource.getURI().toString(), List.of(textEdit));
            action.setEdit(workspaceEdit);
            result.add(Either.forRight(action));
        }
        return result;
    }

    public CodeAction createNewFileAction(String uri, String content) {
        CodeAction action = new CodeAction();
        action.setKind(CodeActionKind.QuickFix);
        action.setTitle("Create File");
        //
        TextEdit textEdit = new TextEdit();
        textEdit.setRange(new Range(new Position(0, 0), new Position(0, 0)));
        textEdit.setNewText(content);
        WorkspaceEdit workspaceEdit = new WorkspaceEdit();
        workspaceEdit.getChanges().put(uri, List.of(textEdit));
        action.setEdit(workspaceEdit);

        return action;
    }

}