package org.intellij.vala.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface ValaDeclaration extends PsiElement {

    @NotNull
    QualifiedName getQName();
}
