package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ValaLibraryPropertiesEditor extends LibraryPropertiesEditor {

    @NotNull
    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {

    }

    @Override
    public void reset() {

    }
}
