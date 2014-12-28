package org.intellij.vala.reference;

import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaTypeWeak;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ValaTypeReference extends PsiReferenceBase<ValaTypeWeak> {

    public ValaTypeReference(@NotNull ValaTypeWeak element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        final Project project = myElement.getProject();
        final GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
        final QualifiedName qualifiedName = QualifiedNameBuilder.from(myElement.getSymbol());
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        return Iterables.getFirst(index.get(qualifiedName, project, scope), null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
