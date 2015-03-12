package org.intellij.vala.project.template;


import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableModelsProvider;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import org.intellij.vala.project.ValaLibraryDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ValaSupportConfigurable extends FrameworkSupportInModuleConfigurable {

    @Nullable
    @Override
    public CustomLibraryDescription createLibraryDescription() {
        return ValaLibraryDescription.INSTANCE;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return null;
    }

    @Override
    public void addSupport(@NotNull Module module,
                           @NotNull ModifiableRootModel modifiableRootModel,
                           @NotNull ModifiableModelsProvider modifiableModelsProvider) {

    }
}
