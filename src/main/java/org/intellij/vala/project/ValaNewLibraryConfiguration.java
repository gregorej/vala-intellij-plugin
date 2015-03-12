package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.ui.configuration.libraryEditor.LibraryEditor;

public class ValaNewLibraryConfiguration extends NewLibraryConfiguration {
    protected ValaNewLibraryConfiguration() {
        super("Vala library");
    }

    @Override
    public void addRoots(LibraryEditor libraryEditor) {

    }
}
