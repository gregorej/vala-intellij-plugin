package org.intellij.vala.reference;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getImportedNamespacesAvailableFor;

public class ValaConstructorReference extends PsiReferenceBase<ValaMemberPart> {

    public ValaConstructorReference(ValaMemberPart element) {
        super(element, new TextRange(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return resolve(myElement).orElse(null);
    }

    public static Optional<? extends PsiElement> resolve(ValaMemberPart memberPart) {
        for (QualifiedName name : getImportedNamespacesAvailableFor(memberPart)) {
            Optional<? extends PsiElement> result = resolveWithRoot(memberPart, name);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    private static Optional<? extends PsiElement> resolveWithRoot(ValaMemberPart memberPart, QualifiedName rootName) {
        QualifiedName originalQualifiedName = rootName.append(QualifiedNameBuilder.from((ValaMember) memberPart.getParent()));
        QualifiedName maybeDefaultConstructorQualifiedName = originalQualifiedName.append(originalQualifiedName.getTail());
        Optional<ValaDeclaration> defaultConstructor = resolve(memberPart, maybeDefaultConstructorQualifiedName);
        if (defaultConstructor.isPresent()) {
            return defaultConstructor;
        }
        Optional<ValaDeclaration> classDeclaration = resolve(memberPart, originalQualifiedName);
        if (classDeclaration.isPresent()) {
            return classDeclaration;
        }
        if (originalQualifiedName.length() > 1) {
            Optional<ValaDeclaration> namedConstructorDeclaration = resolveAssumingIsNamedConstructor(memberPart, originalQualifiedName);
            if (namedConstructorDeclaration.isPresent()) {
                return namedConstructorDeclaration;
            }
        }
        return Optional.empty();
    }

    private static Optional<ValaDeclaration> resolveAssumingIsNamedConstructor(ValaMemberPart memberPart, QualifiedName originalQualifiedName) {
        String maybeConstructorName = originalQualifiedName.getTail();
        QualifiedName maybeClassQualifiedName = originalQualifiedName.getPrefix(originalQualifiedName.length() - 1);
        String maybeClassName = maybeClassQualifiedName.getTail();
        QualifiedName maybeNamedConstructorQualifiedName = maybeClassQualifiedName.append(maybeClassName).append(maybeConstructorName);
        return resolve(memberPart, maybeNamedConstructorQualifiedName);
    }

    private static Optional<ValaDeclaration> resolve(ValaMemberPart memberPart, QualifiedName qualifiedName) {
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        ValaDeclaration foundDeclaration = index.get(qualifiedName, memberPart.getProject());
        if (foundDeclaration instanceof ValaClassDeclaration || foundDeclaration instanceof ValaCreationMethodDeclaration) {
            return Optional.of(foundDeclaration);
        }
        return Optional.empty();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
