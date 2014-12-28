package org.intellij.vala.psi;

import java.util.List;

public interface ValaDeclarationContainer extends ValaPsiElement {

    List<ValaDeclaration> getDeclarations();
}
