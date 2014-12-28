package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.ValaClassDeclaration;

public interface ValaClassDeclarationStub extends StubElement<ValaClassDeclaration> {

    String getName();
}
