package org.intellij.vala.reference.method;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaMethodDeclaration;
import org.intellij.vala.psi.ValaNamespaceLike;
import org.intellij.vala.psi.ValaNamespaceMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValaMethodReference extends PsiReferenceBase<PsiNamedElement> {

    public ValaMethodReference(PsiNamedElement element) {
        super(element, new TextRange(0, element.getName().length()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return resolveInContainingClass();
    }

    private PsiElement resolveInContainingClass() {
        String name = myElement.getName();
        ValaNamespaceLike classDeclaration = PsiTreeUtil.getParentOfType(myElement, ValaNamespaceLike.class, false);
        if (classDeclaration != null) {
            return getMatchingMethodDeclaration(name, classDeclaration);
        }
        return null;
    }

    private static PsiElement getMatchingMethodDeclaration(String name, ValaNamespaceLike classDeclaration) {
        for (ValaNamespaceMember namespaceMember : classDeclaration.getNamespaceMemberList()) {
            if (namespaceMember instanceof ValaMethodDeclaration) {
                ValaMethodDeclaration methodDeclaration = (ValaMethodDeclaration) namespaceMember;
                if (name.equals(methodDeclaration.getName())) {
                    return methodDeclaration;
                }
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
