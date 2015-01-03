package org.intellij.vala.reference;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
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

import java.util.Collection;
import java.util.List;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getImportedNamespacesAvailableFor;

public class ValaConstructorReference extends PsiReferenceBase<ValaObjectOrArrayCreationExpression> {

    private Project project;
    private GlobalSearchScope scope;

    public ValaConstructorReference(ValaObjectOrArrayCreationExpression element) {
        super(element, calculateRange(element));
        project = element.getProject();
        scope = GlobalSearchScope.projectScope(project);

    }

    private static TextRange calculateRange(ValaObjectOrArrayCreationExpression expression) {
        final List<ValaMemberPart> memberPartList = expression.getMember().getMemberPartList();
        ValaMemberPart memberPart = memberPartList.get(memberPartList.size() - 1);
        int offset = memberPart.getTextOffset() - expression.getTextOffset();
        return new TextRange(offset, offset + memberPart.getName().length());
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
        QualifiedName originalQualifiedName = rootName.append(QualifiedNameBuilder.from(myElement.getMember()));
        QualifiedName maybeDefaultConstructorQualifiedName = originalQualifiedName.append(originalQualifiedName.getTail());
        ValaDeclaration defaultConstructor = resolveFirst(maybeDefaultConstructorQualifiedName);
        if (defaultConstructor != null) {
            return defaultConstructor;
        }
        ValaDeclaration classDeclaration = resolveFirst(originalQualifiedName);
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
        return resolveFirst(maybeNamedConstructorQualifiedName);
    }

    private ValaDeclaration resolveFirst(QualifiedName maybeClassQualifiedName) {
        return Iterables.getFirst(resolve(maybeClassQualifiedName), null);
    }

    private Collection<ValaDeclaration> resolve(QualifiedName qualifiedName) {
        ImmutableList.Builder<ValaDeclaration> declarations = ImmutableList.builder();
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        for (ValaDeclaration foundDeclaration : index.get(qualifiedName, project, scope)) {
            if (foundDeclaration instanceof ValaClassDeclaration || foundDeclaration instanceof ValaCreationMethodDeclaration) {
                declarations.add(foundDeclaration);
            }
        }
        return declarations.build();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
