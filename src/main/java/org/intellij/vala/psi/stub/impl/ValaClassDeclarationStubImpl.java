package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.ValaNamespaceLike;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaClassDeclarationStub;


public class ValaClassDeclarationStubImpl extends StubBase<ValaNamespaceLike> implements ValaClassDeclarationStub {

    private String name;

    public ValaClassDeclarationStubImpl(StubElement parent, String name) {
        super(parent, (IStubElementType) ValaTypes.CLASS_DECLARATION);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
