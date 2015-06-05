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

import java.util.Optional;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getImportedNamespacesAvailableFor;


public class ValaTypeReference extends PsiReferenceBase<ValaSymbol> {

    private Project project;

    public ValaTypeReference(@NotNull ValaSymbol element, TextRange textRange) {
        super(element, textRange);
        project = element.getProject();
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return resolve(myElement).orElse(null);
    }

    public static Optional<PsiElement> resolve(ValaSymbol symbol) {
        final QualifiedName qualifiedName = QualifiedNameBuilder.from(symbol);
        Optional<PsiElement> resolvedWithFullName = resolve(symbol, qualifiedName);
        if (resolvedWithFullName.isPresent()) {
            return resolvedWithFullName;
        } else {
            return resolveInImportedNamespaces(symbol);
        }
    }

    private static Optional<PsiElement> resolve(ValaSymbol symbol, QualifiedName qualifiedName) {
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        return Optional.ofNullable(index.get(qualifiedName, symbol.getProject()));
    }

    private static Optional<PsiElement> resolveInImportedNamespaces(ValaSymbol symbol) {
        for (QualifiedName importedName : getImportedNamespacesAvailableFor(symbol)) {
            QualifiedName nameToTry = QualifiedNameBuilder.append(importedName, symbol);
            Optional<PsiElement> foundElement = resolve(symbol, nameToTry);
            if (foundElement.isPresent()) {
                return foundElement;
            }
        }
        return Optional.empty();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
