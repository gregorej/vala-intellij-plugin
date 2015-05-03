package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaStructDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaStructDeclarationStub;

public class ValaStructDeclarationStubImpl extends StubBase<ValaStructDeclaration> implements ValaStructDeclarationStub {

    public QualifiedName getQName() {
        return qualifiedName;
    }

    private QualifiedName qualifiedName;

    public ValaStructDeclarationStubImpl(StubElement parent, QualifiedName qualifiedName) {
        super(parent, (IStubElementType) ValaTypes.STRUCT_DECLARATION);
        this.qualifiedName = qualifiedName;
    }

}

