package org.intellij.vala.reference;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getImportedNamespacesAvailableFor;

public class ValaConstructorReference extends PsiReferenceBase<ValaMemberPart> {

    private Project project;

    public ValaConstructorReference(ValaMemberPart element) {
        super(element, new TextRange(0, element.getTextLength()));
        project = element.getProject();

    }

    @Nullable
    @Override
    public PsiElement resolve() {
        for (QualifiedName name : getImportedNamespacesAvailableFor(myElement)) {
            PsiElement result = resolveWithRoot(name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private PsiElement resolveWithRoot(QualifiedName rootName) {
        QualifiedName originalQualifiedName = rootName.append(QualifiedNameBuilder.from((ValaMember) myElement.getParent()));
        QualifiedName maybeDefaultConstructorQualifiedName = originalQualifiedName.append(originalQualifiedName.getTail());
        ValaDeclaration defaultConstructor = resolve(maybeDefaultConstructorQualifiedName);
        if (defaultConstructor != null) {
            return defaultConstructor;
        }
        ValaDeclaration classDeclaration = resolve(originalQualifiedName);
        if (classDeclaration != null) {
            return classDeclaration;
        }
        if (originalQualifiedName.length() > 1) {
            ValaDeclaration namedConstructorDeclaration = resolveAssumingIsNamedConstructor(originalQualifiedName);
            if (namedConstructorDeclaration != null) {
                return namedConstructorDeclaration;
            }
        }
        return null;
    }

    private ValaDeclaration resolveAssumingIsNamedConstructor(QualifiedName originalQualifiedName) {
        String maybeConstructorName = originalQualifiedName.getTail();
        QualifiedName maybeClassQualifiedName = originalQualifiedName.getPrefix(originalQualifiedName.length() - 1);
        String maybeClassName = maybeClassQualifiedName.getTail();
        QualifiedName maybeNamedConstructorQualifiedName = maybeClassQualifiedName.append(maybeClassName).append(maybeConstructorName);
        return resolve(maybeNamedConstructorQualifiedName);
    }

    @Nullable
    private ValaDeclaration resolve(QualifiedName qualifiedName) {
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        ValaDeclaration foundDeclaration = index.get(qualifiedName, project);
        if (foundDeclaration instanceof ValaClassDeclaration || foundDeclaration instanceof ValaCreationMethodDeclaration) {
            return foundDeclaration;
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
