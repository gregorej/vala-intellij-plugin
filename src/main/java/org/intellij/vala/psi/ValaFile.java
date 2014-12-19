package org.intellij.vala.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.ValaLanguageFileType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ValaFile extends PsiFileBase implements ValaNamespaceLike {
    public ValaFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ValaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ValaLanguageFileType.INSTANCE;
    }

    public List<ValaNamespaceDeclaration> getNamespaces() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ValaNamespaceDeclaration.class);
    }

    @NotNull
    @Override
    public List<ValaNamespaceMember> getNamespaceMemberList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ValaNamespaceMember.class);
    }
}
