package org.intellij.vala.project;

import com.intellij.openapi.roots.impl.libraries.LibraryEx;
import com.intellij.openapi.roots.libraries.Library;

/**
 * Created by sharky on 12.03.15.
 */
public class ValaSdk {

    private ValaLibraryProperties getProperties() {
        return (ValaLibraryProperties) ((LibraryEx) library).getProperties();
    }

    private final Library library;

    public ValaSdk(Library library) {
        this.library = library;
    }
}
