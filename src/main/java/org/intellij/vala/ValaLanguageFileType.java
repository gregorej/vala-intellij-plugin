package org.intellij.vala;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ValaLanguageFileType extends LanguageFileType {

    public static final String DEFAULT_EXTENSION = "vala";
    public static final String DEFAULT_EXTENSION_WITH_DOT = "." + DEFAULT_EXTENSION;
    public static final ValaLanguageFileType INSTANCE = new ValaLanguageFileType();
    public static final String ALL_EXTENSIONS = DEFAULT_EXTENSION;

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
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ValaIcons.FILE;
    }
}
