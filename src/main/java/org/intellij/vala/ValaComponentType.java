package org.intellij.vala;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.intellij.vala.psi.*;

public enum ValaComponentType {

    CLASS,
    METHOD,
    CONSTRUCTOR;

    public static ValaComponentType typeOf(@Nullable PsiElement element) {
        if (element instanceof ValaElementType) {
            return typeOf(element.getParent());
        }
        if (element instanceof ValaClassDeclaration) {
            return CLASS;
        }
        if (element instanceof ValaConstructorDeclaration) {
            return CONSTRUCTOR;
        }
        if (element instanceof ValaMethodDeclaration) {
            return METHOD;
        }

        return null;
    }

}
