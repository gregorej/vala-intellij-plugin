package org.intellij.vala.reference;

import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaSymbol;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getImportedNamespacesAvailableFor;


public class ValaTypeReference extends PsiReferenceBase<ValaSymbol> {

    private GlobalSearchScope scope;
    private Project project;

    public ValaTypeReference(@NotNull ValaSymbol element, TextRange textRange) {
        super(element, textRange);
        project = element.getProject();
        scope = GlobalSearchScope.projectScope(project);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        final QualifiedName qualifiedName = QualifiedNameBuilder.from(myElement);
        PsiElement resolvedWithFullName = resolve(qualifiedName);
        if (resolvedWithFullName != null) {
            return resolvedWithFullName;
        } else {
            return resolveInImportedNamespaces();
        }
    }

    private PsiElement resolve(QualifiedName qualifiedName) {
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        return Iterables.getFirst(index.get(qualifiedName, project, scope), null);
    }

    private PsiElement resolveInImportedNamespaces() {
        for (QualifiedName importedName : getImportedNamespacesAvailableFor(myElement)) {
            QualifiedName nameToTry = QualifiedNameBuilder.append(importedName, myElement);
            PsiElement foundElement = resolve(nameToTry);
            if (foundElement != null) {
                return foundElement;
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
