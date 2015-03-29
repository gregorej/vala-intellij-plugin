package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import org.intellij.vala.psi.ValaCreationMethodDeclaration;

public class ConstructorParenthesesInsertHandler extends ParenthesesInsertHandler<LookupElement> {

    public ConstructorParenthesesInsertHandler(ValaCreationMethodDeclaration creationMethodDeclaration) {
        this.creationMethodDeclaration = creationMethodDeclaration;
    }

    private ValaCreationMethodDeclaration creationMethodDeclaration;

    @Override
    protected boolean placeCaretInsideParentheses(InsertionContext insertionContext, LookupElement lookupElement) {
        return creationMethodDeclaration.getParameters() != null;
    }
}
