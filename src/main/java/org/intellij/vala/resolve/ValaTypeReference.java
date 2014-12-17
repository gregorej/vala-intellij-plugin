package org.intellij.vala.resolve;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaTypeWeak;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ValaTypeReference extends PsiReferenceBase<ValaTypeWeak> {

    public ValaTypeReference(@NotNull ValaTypeWeak element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ValaFile file = PsiTreeUtil.getParentOfType(myElement, ValaFile.class);
        List<ValaClassDeclaration> classDeclarations = PsiTreeUtil.getChildrenOfTypeAsList(file, ValaClassDeclaration.class);
        for (ValaClassDeclaration classDeclaration : classDeclarations) {
            if (classDeclaration.getSymbol().getText().equals(myElement.getSymbol().getText())) {
                return classDeclaration;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
