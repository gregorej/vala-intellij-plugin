package org.intellij.vala.psi;

import org.jetbrains.annotations.NotNull;

public interface ValaDeclaration extends ValaPsiElement {

    @NotNull
    QualifiedName getQName();
}
