package org.intellij.vala.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.libraries.LibraryType;
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.libraries.ui.LibraryEditorComponent;
import com.intellij.openapi.roots.libraries.ui.LibraryPropertiesEditor;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.vala.ValaIcons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ValaLibraryType extends LibraryType<ValaLibraryProperties> {

    protected ValaLibraryType() {
        super(ValaLibraryKind.INSTANCE);
    }

    @Nullable
    @Override
    public String getCreateActionName() {
        return "Vala SDK";
    }

    @Nullable
    @Override
    public NewLibraryConfiguration createNewLibrary(JComponent jComponent, @Nullable VirtualFile virtualFile, Project project) {
        return ValaLibraryDescription.INSTANCE.createNewLibrary(jComponent, virtualFile);
    }

    @Nullable
    @Override
    public LibraryPropertiesEditor createPropertiesEditor(LibraryEditorComponent libraryEditorComponent) {
        return new ValaLibraryPropertiesEditor();
    }

    public static ValaLibraryType instance() {
        return (ValaLibraryType) LibraryType.findByKind(ValaLibraryKind.INSTANCE);
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ValaIcons.FILE;
    }
}
