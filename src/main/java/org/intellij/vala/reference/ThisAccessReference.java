package org.intellij.vala.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaDeclarationContainer;
import org.intellij.vala.psi.ValaThisAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ThisAccessReference extends PsiReferenceBase<ValaThisAccess> {

    public ThisAccessReference(@NotNull ValaThisAccess element) {
        super(element);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return PsiTreeUtil.getParentOfType(myElement, ValaDeclarationContainer.class, false);
    }

    public static Optional<PsiElement> resolve(ValaThisAccess thisAccess) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(thisAccess, ValaDeclarationContainer.class, false));
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
