package org.intellij.vala.psi;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ValaTypeDeclaration extends ValaDeclaration, ValaDeclarationContainer {

    @Nullable
    ValaSymbol getSymbol();

    List<ValaDelegateDeclaration> getDelegates();
}
