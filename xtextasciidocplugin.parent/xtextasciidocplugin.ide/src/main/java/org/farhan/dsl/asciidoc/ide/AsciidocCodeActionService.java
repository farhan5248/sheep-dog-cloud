package org.farhan.dsl.asciidoc.ide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CreateFile;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.xtext.ide.server.Document;
import org.eclipse.xtext.ide.server.ILanguageServerAccess;
import org.eclipse.xtext.ide.server.codeActions.QuickFixCodeActionService;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.XtextResource;
import org.farhan.dsl.asciidoc.LanguageAccessImpl;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.generator.AsciiDocGenerator;
import org.farhan.dsl.asciidoc.validation.AsciiDocValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.Position;

public class AsciidocCodeActionService extends QuickFixCodeActionService {

    private static final Logger logger = LoggerFactory.getLogger(AsciidocCodeActionService.class);

    @Override
    public List<Either<Command, CodeAction>> getCodeActions(Options options) {
        logger.debug("Entering getCodeActions with options {}", options.getURI());

        List<Either<Command, CodeAction>> codeActions = new ArrayList<>();
        codeActions.addAll(super.getCodeActions(options));
        for (Diagnostic diagnostic : options.getCodeActionParams().getContext().getDiagnostics()) {
            logger.debug("Examining diagnostic {} ",
                    diagnostic.getCode().get().toString());
            if (handlesWorkspaceDiagnostic(diagnostic)) {
                logger.debug("Handling diagnostic {} ",
                        diagnostic.getCode().get().toString());
                codeActions.addAll(options.getLanguageServerAccess()
                        .doSyncRead(options.getURI(), (ILanguageServerAccess.Context context) -> {
                            options.setDocument(context.getDocument());
                            options.setResource(context.getResource());
                            return getWorkspaceCodeActions(options, diagnostic);
                        }));
            }
        }
        logger.debug("Exiting getCodeActions");
        return codeActions;
    }

    private boolean handlesWorkspaceDiagnostic(Diagnostic diagnostic) {
        if (AsciiDocValidator.MISSING_STEP_DEF.equals(diagnostic.getCode().get().toString())) {
            return true;
        }
        return false;
    }

    private List<Either<Command, CodeAction>> getWorkspaceCodeActions(Options options, Diagnostic diagnostic) {
        logger.debug("Entering getWorkspaceCodeActions with diagnostic {} and options {} ",
                diagnostic.getCode().get().toString(), options.getURI());
        List<Either<Command, CodeAction>> codeActions = new ArrayList<>();
        if (AsciiDocValidator.MISSING_STEP_DEF.equals(diagnostic.getCode().get().toString())) {
            codeActions.add(getCreateDefinitionAction(options, diagnostic));
        }
        logger.debug("Exiting {}", "getWorkspaceCodeActions");
        return codeActions;
    }

    private Either<Command, CodeAction> getCreateDefinitionAction(Options options, Diagnostic diagnostic) {
        logger.debug("Entering getCreateDefinitionAction with options {} and diagnostic {}", options.getURI(),
                diagnostic.getCode().get().toString());
        CodeAction action = new CodeAction();
        action.setKind(CodeActionKind.QuickFix);
        action.setTitle("Create TestStep definition");
        action.setDiagnostics(Collections.singletonList(diagnostic));
        
        TestStep testStep = (TestStep) getEObjectFromDiagnostic(options, diagnostic);
        logger.debug("TestStep name {}", testStep.getName());
        LanguageAccessImpl la = AsciiDocGenerator.doGenerateFromTestStep(testStep, new ByteArrayOutputStream());
        String content = "";//la.getStepObjectContent();
        logger.debug("content {}", content);
        String fileName = "";//la.getStepObjectFileName();
        logger.debug("fileName {}", fileName);
        
        // Create file operation
        CreateFile createFile = new CreateFile();
        createFile.setUri(fileName);
        
        // Create text document edit to insert content
        VersionedTextDocumentIdentifier textDocumentId = new VersionedTextDocumentIdentifier();
        textDocumentId.setUri(fileName);
        textDocumentId.setVersion(null); // null for new files
        
        TextEdit textEdit = new TextEdit();
        textEdit.setRange(new Range(new Position(0, 0), new Position(0, 0)));
        textEdit.setNewText(content);
        
        TextDocumentEdit textDocumentEdit = new TextDocumentEdit();
        textDocumentEdit.setTextDocument(textDocumentId);
        textDocumentEdit.setEdits(List.of(textEdit));
        
        // Create workspace edit with document changes
        WorkspaceEdit workspaceEdit = new WorkspaceEdit();
        workspaceEdit.setDocumentChanges(List.of(Either.forRight(createFile), Either.forLeft(textDocumentEdit)));
        
        action.setEdit(workspaceEdit);
        logger.debug("Exiting {}", "getCreateDefinitionAction");
        return Either.forRight(action);
    }

    private EObject getEObjectFromDiagnostic(Options options, Diagnostic diagnostic) {
        Resource resource = options.getResource();
        if (resource == null || !(resource instanceof XtextResource)) {
            return null;
        }

        XtextResource xtextResource = (XtextResource) resource;
        IParseResult parseResult = xtextResource.getParseResult();
        if (parseResult == null) {
            return null;
        }

        // Use the Document from options to convert position to offset
        Document document = options.getDocument();
        try {
            int offset = document.getOffSet(diagnostic.getRange().getStart());

            // Find EObject at offset using NodeModelUtils
            ICompositeNode rootNode = parseResult.getRootNode();
            ILeafNode leafNode = NodeModelUtils.findLeafNodeAtOffset(rootNode, offset);

            if (leafNode != null) {
                return NodeModelUtils.findActualSemanticObjectFor(leafNode);
            }
        } catch (Exception e) {
            logger.error("Error getting offset from document", e);
        }

        return null;
    }

}