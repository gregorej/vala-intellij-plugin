package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaInterfaceDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaClassDeclarationStub;
import org.intellij.vala.psi.stub.ValaInterfaceDeclarationStub;

public class ValaInterfaceDeclarationStubImpl extends StubBase<ValaInterfaceDeclaration> implements ValaInterfaceDeclarationStub {

    public QualifiedName getQName() {
        return qualifiedName;
    }

    private QualifiedName qualifiedName;

    public ValaInterfaceDeclarationStubImpl(StubElement parent, QualifiedName qualifiedName) {
        super(parent, (IStubElementType) ValaTypes.INTERFACE_DECLARATION);
        this.qualifiedName = qualifiedName;
    }

}
