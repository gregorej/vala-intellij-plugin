package org.intellij.vala.psi.index;


import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.jetbrains.annotations.NotNull;

public class TypeNameIndex extends StringStubIndexExtension<ValaClassDeclaration> {
    public static final StubIndexKey<String, ValaClassDeclaration> KEY = StubIndexKey.createIndexKey("vala.index.type.name");

    private static final TypeNameIndex INSTANCE = new TypeNameIndex();

    public static TypeNameIndex getInstance() {
        return INSTANCE;
    }

    @NotNull
    public StubIndexKey<String, ValaClassDeclaration> getKey() {
        return KEY;
    }
}
