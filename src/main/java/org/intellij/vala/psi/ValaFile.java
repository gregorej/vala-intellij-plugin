package org.intellij.vala.psi;

import com.google.common.collect.ImmutableList;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.ValaLanguageFileType;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ValaFile extends PsiFileBase implements ValaDeclarationContainer {
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
    public List<ValaClassDeclaration> getDeclaredClasses() {
        ImmutableList.Builder<ValaClassDeclaration> declarations = ImmutableList.builder();
        for (ValaNamespaceMember member : getNamespaceMemberList()) {
            if (member.getClassDeclaration() != null) {
                declarations.add(member.getClassDeclaration());
            }
        }
        return declarations.build();
    }

    @NotNull
    private List<ValaNamespaceMember> getNamespaceMemberList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, ValaNamespaceMember.class);
    }

    @Override
    public List<ValaDeclaration> getDeclarations() {
        return ValaPsiImplUtil.toDeclarations(this.getNamespaceMemberList());
    }
}
