package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;


public class ValaFieldReference extends PsiReferenceBase<PsiNamedElement> {
    public ValaFieldReference(PsiNamedElement psiNamedElement) {
        super(psiNamedElement, new TextRange(0, psiNamedElement.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ValaDeclarationContainer containingClass = getParentOfType(myElement, ValaDeclarationContainer.class, false);
        if (containingClass == null) {
            return null;
        }
        for (ValaDeclaration declaration : containingClass.getDeclarations()) {
            if (declaration instanceof ValaFieldDeclaration) {
                ValaFieldDeclaration fieldDeclaration = (ValaFieldDeclaration) declaration;
                if (myElement.getName().equals(fieldDeclaration.getName())) {
                    return fieldDeclaration;
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
