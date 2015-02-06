package org.intellij.vala.psi;

import org.jetbrains.annotations.Nullable;

public interface ValaTypeDeclaration extends ValaDeclaration {

    @Nullable
    ValaSymbol getSymbol();
}
