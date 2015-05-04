package org.intellij.vala.psi.stub.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaEnumDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.stub.ValaEnumDeclarationStub;

public class ValaEnumDeclarationStubImpl extends StubBase<ValaEnumDeclaration> implements ValaEnumDeclarationStub {
    public QualifiedName getQName() {
        return qualifiedName;
    }

    private QualifiedName qualifiedName;

    public ValaEnumDeclarationStubImpl(StubElement parent, QualifiedName qualifiedName) {
        super(parent, (IStubElementType) ValaTypes.ENUM_DECLARATION);
        this.qualifiedName = qualifiedName;
    }
}
