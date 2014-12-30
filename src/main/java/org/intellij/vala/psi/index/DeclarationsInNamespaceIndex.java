package org.intellij.vala.psi.index;

import com.intellij.psi.stubs.AbstractStubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.io.KeyDescriptor;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaDeclaration;
import org.jetbrains.annotations.NotNull;

public class DeclarationsInNamespaceIndex extends AbstractStubIndex<QualifiedName, ValaDeclaration> {

    public static final StubIndexKey<QualifiedName, ValaDeclaration> KEY = StubIndexKey.createIndexKey("vala.index.declaration.namespace");

    private static final DeclarationsInNamespaceIndex INSTANCE = new DeclarationsInNamespaceIndex();

    public static DeclarationsInNamespaceIndex getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public StubIndexKey<QualifiedName, ValaDeclaration> getKey() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @NotNull
    @Override
    public KeyDescriptor<QualifiedName> getKeyDescriptor() {
        return QualifiedNameKeyDescriptor.INSTANCE;
    }
}
