package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.intellij.vala.reference.ValaFieldReference.resolveAsThisClassFieldReference;

public class ValaVariableReference extends PsiReferenceBase<PsiNamedElement> {

    public ValaVariableReference(PsiNamedElement simpleName) {
        super(simpleName, new TextRange(0, simpleName.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiElement localVariable = resolveAsLocalVariable(myElement);
        if (localVariable != null) {
            return localVariable;
        }
        return resolveAsThisClassFieldReference(myElement);
    }

    private static PsiElement resolveAsLocalVariable(PsiNamedElement myElement) {
        ValaBlock block = PsiTreeUtil.getParentOfType(myElement, ValaBlock.class);
        if (block == null) {
            return null;// should not happen in valid Vala file
        }

        for (ValaStatement statement : block.getStatementList()) {
            if (PsiTreeUtil.isAncestor(statement, myElement, false)) {
                return null;
            }
            if (statement instanceof ValaLocalVariableDeclarations) {
                ValaLocalVariableDeclarations declarations = (ValaLocalVariableDeclarations) statement;
                for (ValaLocalVariableDeclaration declaration : declarations.getLocalVariableDeclarationList()) {
                    if (declaration.getLocalVariable() != null) {
                        ValaLocalVariable localVariable = declaration.getLocalVariable();
                        if (localVariable.getName().equals(myElement.getName())) {
                            return localVariable;
                        }
                    }
                }
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
