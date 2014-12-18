package org.intellij.vala.resolve;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.intellij.vala.psi.ValaTypeWeak;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ValaTypeReference extends PsiReferenceBase<ValaTypeWeak> {

    public ValaTypeReference(@NotNull ValaTypeWeak element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ClassDeclarationResolver classDeclarationResolver = new ClassDeclarationResolver(myElement.getProject());
        String name = myElement.getSymbol().getText();
        return classDeclarationResolver.resolve(name);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
