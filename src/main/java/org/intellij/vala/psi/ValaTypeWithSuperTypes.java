package org.intellij.vala.psi;

import java.util.List;

public interface ValaTypeWithSuperTypes extends ValaTypeDeclaration {

    List<ValaTypeDeclaration> getSuperTypeDeclarations();
}
