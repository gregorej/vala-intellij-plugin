package org.intellij.vala.resolve;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaNamespaceMember;
import org.intellij.vala.psi.ValaTypeWeak;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;


public class ValaClassReference extends PsiReferenceBase<ValaTypeWeak> {

    public ValaClassReference(@NotNull ValaTypeWeak element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ValaFile file = PsiTreeUtil.getParentOfType(myElement, ValaFile.class);
        List<ValaNamespaceMember> namespaceMembers = PsiTreeUtil.getChildrenOfTypeAsList(file, ValaNamespaceMember.class);
        Iterable<ValaClassDeclaration> classDeclarations = concat(transform(namespaceMembers, classesInNamespace()));
        for (ValaClassDeclaration classDeclaration : classDeclarations) {
            if (classDeclaration.getSymbol().getText().equals(myElement.getSymbol().getText())) {
                return classDeclaration;
            }
        }
        return null;
    }

    private static Function<ValaNamespaceMember, Iterable<ValaClassDeclaration>> classesInNamespace() {
        return new Function<ValaNamespaceMember, Iterable<ValaClassDeclaration>>() {
            @Override
            public Iterable<ValaClassDeclaration> apply(@Nullable ValaNamespaceMember valaNamespaceMember) {
                return PsiTreeUtil.getChildrenOfTypeAsList(valaNamespaceMember, ValaClassDeclaration.class);
            }
        };
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
