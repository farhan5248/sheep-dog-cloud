package org.farhan.dsl.asciidoc.ide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
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
import org.eclipse.xtext.ide.server.codeActions.ICodeActionService2;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.Position;

public class MyDslCodeActionService implements ICodeActionService2 {

    // Replace with the actual value of INVALID_NAME from your validator
    private static final String INVALID_NAME = "INVALID_NAME";

    @Override
    public List<Either<Command, CodeAction>> getCodeActions(Options options) {

        Resource resource = options.getResource();
        Document document = options.getDocument();
        CodeActionParams params = options.getCodeActionParams();
        List<Either<Command, CodeAction>> result = new ArrayList<>();
        if (params.getContext() != null && params.getContext().getDiagnostics() != null) {
            for (Diagnostic d : params.getContext().getDiagnostics()) {
                // TODO replace with actual code check
                if (INVALID_NAME.equals(d.getCode().get().toString())) {
                    String text = document.getSubstring(d.getRange());
                    CodeAction action = new CodeAction();
                    action.setKind(CodeActionKind.QuickFix);
                    action.setTitle("Capitalize Name");
                    action.setDiagnostics(Collections.singletonList(d));
                    TextEdit textEdit = new TextEdit();
                    textEdit.setRange(d.getRange());
                    textEdit.setNewText(text + text);
                    WorkspaceEdit workspaceEdit = new WorkspaceEdit();
                    workspaceEdit.getChanges().put(resource.getURI().toString(), List.of(textEdit));
                    action.setEdit(workspaceEdit);
                    result.add(Either.forRight(action));
                }
            }
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