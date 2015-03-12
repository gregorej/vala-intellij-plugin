package org.intellij.vala.project;

import com.google.common.collect.Sets;
import com.intellij.openapi.roots.libraries.LibraryKind;
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.Set;

public class ValaLibraryDescription extends CustomLibraryDescription {
    @NotNull
    @Override
    public Set<? extends LibraryKind> getSuitableLibraryKinds() {
        return Sets.newHashSet(ValaLibraryKind.INSTANCE);
    }

    public static final ValaLibraryDescription INSTANCE = new ValaLibraryDescription();

    @Nullable
    @Override
    public NewLibraryConfiguration createNewLibrary(@NotNull JComponent jComponent, VirtualFile virtualFile) {
        try {
            ValaSdkDescriptor sdk = ValaSdkDescriptor.discoverFromEnvironmentVariable();
            if (sdk == null) {
                return null;
            }
            return sdk.createNewLibraryConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
