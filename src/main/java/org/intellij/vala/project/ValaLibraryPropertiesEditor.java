package org.intellij.vala.project;

import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent;
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ValaLibraryPropertiesEditor extends LibraryPropertiesEditor {

    private ValaLibraryProperties properties;

    public ValaLibraryPropertiesEditor(LibraryEditorComponent<ValaLibraryProperties> editorComponent) {
        this.properties = editorComponent.getProperties();
    }

    private ValaLibraryEditorForm form = new ValaLibraryEditorForm();

    @NotNull
    @Override
    public JComponent createComponent() {
        return form.getComponent();
    }

    @Override
    public boolean isModified() {
        return !form.getState().equals(properties.getState());
    }

    @Override
    public void apply() {
        properties.loadState(form.getState());
    }

    @Override
    public void reset() {
        form.setState(properties.getState());
    }
}
