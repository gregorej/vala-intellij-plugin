package org.intellij.vala.reference;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaSymbol;
import org.intellij.vala.psi.ValaUsingDirective;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


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

    private static List<QualifiedName> getImportedNamespacesAvailableFor(ValaSymbol symbol) {
        ValaFile containingFile = (ValaFile) symbol.getContainingFile();
        ImmutableList.Builder<QualifiedName> names = ImmutableList.builder();
        for (ValaUsingDirective directive : containingFile.getUsingDirectives()) {
            names.addAll(toQNames(directive.getSymbolList()));
        }
        return names.build();
    }

    private static List<QualifiedName> toQNames(List<ValaSymbol> symbols) {
        ImmutableList.Builder<QualifiedName> names = ImmutableList.builder();
        for (ValaSymbol symbol : symbols) {
            names.add(QualifiedNameBuilder.from(symbol));
        }
        return names.build();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
