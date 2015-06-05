package org.intellij.vala.psi;

import org.jetbrains.annotations.NotNull;

public interface ValaDeclaration extends ValaPsiElement, ValaPsiNameIdentifierOwner {

    @NotNull
    QualifiedName getQName();
}
