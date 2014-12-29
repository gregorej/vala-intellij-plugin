package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaCreationMethodDeclaration;

public interface ValaCreationMethodDeclarationStub extends StubElement<ValaCreationMethodDeclaration> {

    QualifiedName getQName();
}
