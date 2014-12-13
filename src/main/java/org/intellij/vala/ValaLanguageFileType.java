package org.intellij.vala;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ValaLanguageFileType extends LanguageFileType {

    private Icon valaIcon = new ImageIcon(ValaLanguageFileType.class.getResource("gnome-icon.png"));

    protected ValaLanguageFileType() {
        super(ValaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Vala";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Vala programming language module";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "vala";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return valaIcon;
    }
}
