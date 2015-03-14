package org.intellij.vala.project;

import java.util.Objects;

public class ValaLibraryPropertiesState {

    public String valaVersion;

    public int hashCode() {
        return Objects.hashCode(valaVersion);
    }

    public boolean equals(Object other) {
        if (! (other instanceof ValaLibraryPropertiesState)) {
            return false;
        }
        ValaLibraryPropertiesState propertiesState = (ValaLibraryPropertiesState) other;
        return Objects.equals(valaVersion, propertiesState.valaVersion);
    }
}
