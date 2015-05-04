package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaEnumDeclaration;

public interface ValaEnumDeclarationStub extends StubElement<ValaEnumDeclaration> {
    QualifiedName getQName();

}
