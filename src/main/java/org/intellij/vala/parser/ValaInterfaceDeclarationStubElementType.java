package org.intellij.vala.parser;

import com.intellij.psi.stubs.*;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.impl.ValaInterfaceDeclarationImpl;
import org.intellij.vala.psi.index.TypeNameIndex;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.intellij.vala.psi.stub.ValaInterfaceDeclarationStub;
import org.intellij.vala.psi.stub.impl.ValaInterfaceDeclarationStubImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ValaInterfaceDeclarationStubElementType extends IStubElementType<ValaInterfaceDeclarationStub, ValaInterfaceDeclaration> {
    public ValaInterfaceDeclarationStubElementType() {
        super("INTERFACE_DECLARATION", ValaLanguage.INSTANCE);
    }

    @Override
    public ValaInterfaceDeclaration createPsi(@NotNull ValaInterfaceDeclarationStub valaNamespaceLikeStub) {
        return new ValaInterfaceDeclarationImpl(valaNamespaceLikeStub, (IStubElementType) ValaTypes.CLASS_DECLARATION);
    }

    @Override
    public ValaInterfaceDeclarationStub createStub(@NotNull ValaInterfaceDeclaration classDeclaration, StubElement parent) {
        return new ValaInterfaceDeclarationStubImpl(parent, classDeclaration.getQName());
    }

    @Override
    public void serialize(@NotNull ValaInterfaceDeclarationStub valaNamespaceLikeStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
        valaNamespaceLikeStub.getQName().write(stubOutputStream);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "vala.interfaceDeclaration";
    }

    @NotNull
    @Override
    public ValaInterfaceDeclarationStub deserialize(@NotNull StubInputStream stubInputStream, StubElement parent) throws IOException {
        QualifiedName qName = QualifiedNameBuilder.read(stubInputStream);
        return new ValaInterfaceDeclarationStubImpl(parent, qName);
    }

    @Override
    public void indexStub(@NotNull ValaInterfaceDeclarationStub valaNamespaceLikeStub, @NotNull IndexSink indexSink) {
        indexSink.occurrence(TypeNameIndex.KEY, valaNamespaceLikeStub.getQName().getTail());
        final QualifiedName qualifiedName = valaNamespaceLikeStub.getQName();
        final QualifiedName namespaceQualifiedName = qualifiedName.getPrefix(qualifiedName.length() - 1);
        indexSink.occurrence(DeclarationQualifiedNameIndex.KEY, qualifiedName);
        indexSink.occurrence(DeclarationsInNamespaceIndex.KEY, namespaceQualifiedName);
    }
}
