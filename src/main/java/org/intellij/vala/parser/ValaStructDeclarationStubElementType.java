package org.intellij.vala.parser;

import com.intellij.psi.stubs.*;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaStructDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.impl.ValaStructDeclarationImpl;
import org.intellij.vala.psi.index.TypeNameIndex;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.intellij.vala.psi.stub.ValaStructDeclarationStub;
import org.intellij.vala.psi.stub.impl.ValaStructDeclarationStubImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ValaStructDeclarationStubElementType extends IStubElementType<ValaStructDeclarationStub, ValaStructDeclaration> {
    public ValaStructDeclarationStubElementType() {
        super("STRUCT_DECLARATION", ValaLanguage.INSTANCE);
    }

    @Override
    public ValaStructDeclaration createPsi(@NotNull ValaStructDeclarationStub valaNamespaceLikeStub) {
        return new ValaStructDeclarationImpl(valaNamespaceLikeStub, (IStubElementType) ValaTypes.STRUCT_DECLARATION);
    }

    @Override
    public ValaStructDeclarationStub createStub(@NotNull ValaStructDeclaration classDeclaration, StubElement parent) {
        return new ValaStructDeclarationStubImpl(parent, classDeclaration.getQName());
    }

    @Override
    public void serialize(@NotNull ValaStructDeclarationStub valaNamespaceLikeStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
        valaNamespaceLikeStub.getQName().write(stubOutputStream);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "vala.structDeclaration";
    }

    @NotNull
    @Override
    public ValaStructDeclarationStub deserialize(@NotNull StubInputStream stubInputStream, StubElement parent) throws IOException {
        QualifiedName qName = QualifiedNameBuilder.read(stubInputStream);
        return new ValaStructDeclarationStubImpl(parent, qName);
    }

    @Override
    public void indexStub(@NotNull ValaStructDeclarationStub valaNamespaceLikeStub, @NotNull IndexSink indexSink) {
        indexSink.occurrence(TypeNameIndex.KEY, valaNamespaceLikeStub.getQName().getTail());
        final QualifiedName qualifiedName = valaNamespaceLikeStub.getQName();
        final QualifiedName namespaceQualifiedName = qualifiedName.getPrefix(qualifiedName.length() - 1);
        indexSink.occurrence(DeclarationQualifiedNameIndex.KEY, qualifiedName);
        indexSink.occurrence(DeclarationsInNamespaceIndex.KEY, namespaceQualifiedName);
    }
}
