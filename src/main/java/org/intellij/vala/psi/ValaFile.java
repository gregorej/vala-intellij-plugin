package org.intellij.vala.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.ValaLanguageFileType;
import org.jetbrains.annotations.NotNull;

public class ValaFile extends PsiFileBase {
    public ValaFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ValaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ValaLanguageFileType.INSTANCE;
    }
}
