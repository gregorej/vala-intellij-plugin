package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.LibraryProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ValaLibraryProperties extends LibraryProperties<ValaLibraryPropertiesState> {

    public String valaVersion;

    @Override
    public boolean equals(Object o) {
        return (o instanceof ValaLibraryProperties) && Objects.equals(this.valaVersion, ((ValaLibraryProperties)o).valaVersion);
    }

    @Override
    public int hashCode() {
        return (valaVersion != null) ? valaVersion.hashCode() : 0;
    }

    @Nullable
    @Override
    public ValaLibraryPropertiesState getState() {
        ValaLibraryPropertiesState state = new ValaLibraryPropertiesState();
        state.valaVersion = valaVersion;
        return state;
    }

    @Override
    public void loadState(ValaLibraryPropertiesState state) {
        this.valaVersion = state.valaVersion;
    }
}
