package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.PersistentLibraryKind;
import org.jetbrains.annotations.NotNull;

public class ValaLibraryKind extends PersistentLibraryKind<ValaLibraryProperties> {

    private ValaLibraryKind() {
        super("Vala");
    }

    public static final ValaLibraryKind INSTANCE = new ValaLibraryKind();

    @NotNull
    public ValaLibraryProperties createDefaultProperties() {
        return new ValaLibraryProperties();
    }
}
