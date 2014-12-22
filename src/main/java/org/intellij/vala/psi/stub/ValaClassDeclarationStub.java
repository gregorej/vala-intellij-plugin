package org.intellij.vala.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.ValaNamespaceLike;

public interface ValaClassDeclarationStub extends StubElement<ValaNamespaceLike> {

    String getName();
}
