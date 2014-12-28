package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaClassDeclarationStub;


public class ValaClassDeclarationStubImpl extends StubBase<ValaClassDeclaration> implements ValaClassDeclarationStub {

    public QualifiedName getQName() {
        return qualifiedName;
    }

    private QualifiedName qualifiedName;

    public ValaClassDeclarationStubImpl(StubElement parent, QualifiedName qualifiedName) {
        super(parent, (IStubElementType) ValaTypes.CLASS_DECLARATION);
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String getName() {
        return qualifiedName.getTail();
    }
}
