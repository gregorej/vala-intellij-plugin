package org.intellij.vala.parser;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.psi.stubs.*;
import com.intellij.util.io.StringRef;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.impl.ValaClassDeclarationImpl;
import org.intellij.vala.psi.stub.ValaClassDeclarationStub;
import org.intellij.vala.psi.stub.impl.ValaClassDeclarationStubImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ValaClassDeclarationStubElementType extends ILightStubElementType<ValaClassDeclarationStub, ValaClassDeclaration> {
    public ValaClassDeclarationStubElementType() {
        super("CLASS_DECLARATION", ValaLanguage.INSTANCE);
    }

    @Override
    public ValaClassDeclarationStub createStub(LighterAST lighterAST, LighterASTNode lighterASTNode, StubElement parentStub) {
        return new ValaClassDeclarationStubImpl(parentStub, QualifiedNameBuilder.nameOf("MyClass"));
    }

    @Override
    public ValaClassDeclaration createPsi(@NotNull ValaClassDeclarationStub valaNamespaceLikeStub) {
        return new ValaClassDeclarationImpl(valaNamespaceLikeStub, (IStubElementType) ValaTypes.CLASS_DECLARATION);
    }

    @Override
    public ValaClassDeclarationStub createStub(@NotNull ValaClassDeclaration classDeclaration, StubElement parent) {
        return new ValaClassDeclarationStubImpl(parent, classDeclaration.getQName());
    }

    @Override
    public void serialize(@NotNull ValaClassDeclarationStub valaNamespaceLikeStub, @NotNull StubOutputStream stubOutputStream) throws IOException {
        valaNamespaceLikeStub.getQName().write(stubOutputStream);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "vala.classDeclaration";
    }

    @NotNull
    @Override
    public ValaClassDeclarationStub deserialize(@NotNull StubInputStream stubInputStream, StubElement parent) throws IOException {
        QualifiedName qName = QualifiedNameBuilder.read(stubInputStream);
        return new ValaClassDeclarationStubImpl(parent, qName);
    }

    @Override
    public void indexStub(@NotNull ValaClassDeclarationStub valaNamespaceLikeStub, @NotNull IndexSink indexSink) {
        indexSink.occurrence(ClassNameIndex.KEY, valaNamespaceLikeStub.getName());
        indexSink.occurrence(DeclarationQualifiedNameIndex.KEY, valaNamespaceLikeStub.getQName());
    }
}
