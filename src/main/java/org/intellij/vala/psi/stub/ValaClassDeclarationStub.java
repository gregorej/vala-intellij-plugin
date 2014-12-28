package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaClassDeclaration;

public interface ValaClassDeclarationStub extends StubElement<ValaClassDeclaration> {

    @Deprecated
    String getName();

    QualifiedName getQName();
}
