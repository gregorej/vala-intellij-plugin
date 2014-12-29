package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaCreationMethodDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaCreationMethodDeclarationStub;

public class ValaCreationMethodDeclarationStubImpl extends StubBase<ValaCreationMethodDeclaration> implements ValaCreationMethodDeclarationStub {

    private QualifiedName qualifiedName;

    public ValaCreationMethodDeclarationStubImpl(StubElement parent, QualifiedName qualifiedName) {
        super(parent, (IStubElementType) ValaTypes.CREATION_METHOD_DECLARATION);
        this.qualifiedName = qualifiedName;
    }

    @Override
    public QualifiedName getQName() {
        return qualifiedName;
    }
}
