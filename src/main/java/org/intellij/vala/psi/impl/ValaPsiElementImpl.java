package org.intellij.vala.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.intellij.vala.psi.ValaPsiElement;
import org.jetbrains.annotations.NotNull;

public class ValaPsiElementImpl extends ASTWrapperPsiElement implements ValaPsiElement {

    public ValaPsiElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state,
                                       PsiElement lastParent, @NotNull PsiElement place) {
        return false;
    }
}
