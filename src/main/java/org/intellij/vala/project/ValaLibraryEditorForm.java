package org.intellij.vala.project;

import javax.swing.*;

public class ValaLibraryEditorForm {
    private JPanel myContentPanel;
    private JTextField versionField;

    public JComponent getComponent() {
        return myContentPanel;
    }

    public void setState(ValaLibraryPropertiesState state) {
        versionField.setText(state.valaVersion);
    }

    public ValaLibraryPropertiesState getState() {
        ValaLibraryPropertiesState state = new ValaLibraryPropertiesState();
        state.valaVersion = versionField.getText();
        return state;
    }
}
