package org.intellij.vala.parser;


import com.intellij.psi.stubs.*;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaEnumDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.impl.ValaEnumDeclarationImpl;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.intellij.vala.psi.index.TypeNameIndex;
import org.intellij.vala.psi.stub.ValaEnumDeclarationStub;
import org.intellij.vala.psi.stub.impl.ValaEnumDeclarationStubImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ValaEnumDeclarationStubElementType extends IStubElementType<ValaEnumDeclarationStub, ValaEnumDeclaration> {
    public ValaEnumDeclarationStubElementType() {
        super("ENUM_DECLARATION", ValaLanguage.INSTANCE);
    }

    @Override
    public ValaEnumDeclaration createPsi(@NotNull ValaEnumDeclarationStub valaNamespaceLikeStub) {
        return new ValaEnumDeclarationImpl(valaNamespaceLikeStub, (IStubElementType) ValaTypes.CLASS_DECLARATION);
    }

    @Override
    public ValaEnumDeclarationStub createStub(@NotNull ValaEnumDeclaration classDeclaration, StubElement parent) {
        return new ValaEnumDeclarationStubImpl(parent, classDeclaration.getQName());
    }

    @Override
    public void serialize(@NotNull ValaEnumDeclarationStub valaNamespaceLikeStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
        valaNamespaceLikeStub.getQName().write(stubOutputStream);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "vala.enumDeclaration";
    }

    @NotNull
    @Override
    public ValaEnumDeclarationStub deserialize(@NotNull StubInputStream stubInputStream, StubElement parent) throws IOException {
        QualifiedName qName = QualifiedNameBuilder.read(stubInputStream);
        return new ValaEnumDeclarationStubImpl(parent, qName);
    }

    @Override
    public void indexStub(@NotNull ValaEnumDeclarationStub valaNamespaceLikeStub, @NotNull IndexSink indexSink) {
        indexSink.occurrence(TypeNameIndex.KEY, valaNamespaceLikeStub.getQName().getTail());
        final QualifiedName qualifiedName = valaNamespaceLikeStub.getQName();
        final QualifiedName namespaceQualifiedName = qualifiedName.getPrefix(qualifiedName.length() - 1);
        indexSink.occurrence(DeclarationQualifiedNameIndex.KEY, qualifiedName);
        indexSink.occurrence(DeclarationsInNamespaceIndex.KEY, namespaceQualifiedName);
    }
}

