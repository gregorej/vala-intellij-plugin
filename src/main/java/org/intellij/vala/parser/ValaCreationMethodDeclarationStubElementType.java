package org.intellij.vala.parser;

import com.intellij.psi.stubs.*;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaCreationMethodDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.impl.ValaCreationMethodDeclarationImpl;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.stub.ValaCreationMethodDeclarationStub;
import org.intellij.vala.psi.stub.impl.ValaCreationMethodDeclarationStubImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class ValaCreationMethodDeclarationStubElementType extends IStubElementType<ValaCreationMethodDeclarationStub, ValaCreationMethodDeclaration> {
    public ValaCreationMethodDeclarationStubElementType() {
        super("CREATION_METHOD_DECLARATION", ValaLanguage.INSTANCE);
    }

    @Override
    public ValaCreationMethodDeclaration createPsi(ValaCreationMethodDeclarationStub valaCreationMethodDeclarationStub) {
        return new ValaCreationMethodDeclarationImpl(valaCreationMethodDeclarationStub, (IStubElementType) ValaTypes.CREATION_METHOD_DECLARATION);
    }

    @Override
    public ValaCreationMethodDeclarationStub createStub(ValaCreationMethodDeclaration creationMethodDeclaration, StubElement stubElement) {
        return new ValaCreationMethodDeclarationStubImpl(stubElement, creationMethodDeclaration.getQName());
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "vala.creationMethodDeclaration";
    }

    @Override
    public void serialize(@NotNull ValaCreationMethodDeclarationStub valaCreationMethodDeclarationStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
        valaCreationMethodDeclarationStub.getQName().write(stubOutputStream);
    }

    @NotNull
    @Override
    public ValaCreationMethodDeclarationStub deserialize(@NotNull StubInputStream stubInputStream, StubElement stubElement) throws IOException {
        QualifiedName qName = QualifiedNameBuilder.read(stubInputStream);
        return new ValaCreationMethodDeclarationStubImpl(stubElement, qName);
    }

    @Override
    public void indexStub(@NotNull ValaCreationMethodDeclarationStub valaCreationMethodDeclarationStub, @NotNull IndexSink indexSink) {
        indexSink.occurrence(DeclarationQualifiedNameIndex.KEY, valaCreationMethodDeclarationStub.getQName());
    }
}
