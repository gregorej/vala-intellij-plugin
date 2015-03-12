package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.LibraryProperties;
import org.jetbrains.annotations.Nullable;

public class ValaLibraryProperties extends LibraryProperties<ValaLibraryPropertiesState> {

    private ValaLibraryPropertiesState state;

    @Override
    public boolean equals(Object o) {
        return o instanceof ValaLibraryProperties;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Nullable
    @Override
    public ValaLibraryPropertiesState getState() {
        return state;
    }

    @Override
    public void loadState(ValaLibraryPropertiesState state) {
        this.state = state;
    }
}
