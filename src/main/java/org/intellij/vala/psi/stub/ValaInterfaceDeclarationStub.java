package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaInterfaceDeclaration;

public interface ValaInterfaceDeclarationStub extends StubElement<ValaInterfaceDeclaration> {

    QualifiedName getQName();
}
