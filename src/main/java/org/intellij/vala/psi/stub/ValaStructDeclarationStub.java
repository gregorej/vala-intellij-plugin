package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaStructDeclaration;

public interface ValaStructDeclarationStub extends StubElement<ValaStructDeclaration> {

    QualifiedName getQName();
}
