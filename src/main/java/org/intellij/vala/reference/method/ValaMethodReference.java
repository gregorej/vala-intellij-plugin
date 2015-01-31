package org.intellij.vala.reference.method;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.collect.Iterables.getFirst;

public class ValaMethodReference extends PsiReferenceBase<PsiNamedElement> {

    private PsiElement objectReference;
    private Project project;
    private GlobalSearchScope scope;

    public ValaMethodReference(PsiNamedElement element) {
        this(null, element);
    }

    public ValaMethodReference(PsiElement objectReference, PsiNamedElement element) {
        super(element, new TextRange(0, element.getName().length()));
        this.objectReference = objectReference;
        this.project = element.getProject();
        scope = GlobalSearchScope.projectScope(project);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        if (objectReference != null) {
            return resolveAsOtherObjectMethodCallReference();
        }
        return resolveInContainingClass();
    }

    private PsiElement resolveAsOtherObjectMethodCallReference() {
        ValaDeclaration parentType = resolveObjectType();
        if (parentType == null) {
            return null;
        }
        if (parentType instanceof ValaDeclarationContainer) {
            return getMatchingMethodDeclaration(myElement, (ValaDeclarationContainer) parentType);
        }
        return null;
    }

    private PsiElement resolveInContainingClass() {
        ValaDeclarationContainer classDeclaration = PsiTreeUtil.getParentOfType(myElement, ValaDeclarationContainer.class, false);
        if (classDeclaration != null) {
            return getMatchingMethodDeclaration(myElement, classDeclaration);
        }
        return null;
    }

    private static PsiElement getMatchingMethodDeclaration(PsiNamedElement name, ValaDeclarationContainer classDeclaration) {
        for (ValaDeclaration namespaceMember : classDeclaration.getDeclarations()) {
            if (namespaceMember instanceof ValaMethodDeclaration) {
                ValaMethodDeclaration methodDeclaration = (ValaMethodDeclaration) namespaceMember;
                if (name.getName().equals(methodDeclaration.getName())) {
                    return methodDeclaration;
                }
            }
        }
        return null;
    }

    private ValaDeclaration resolveObjectType() {
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        if (objectReference instanceof HasTypeDescriptor) {
            ValaTypeDescriptor descriptor = ((HasTypeDescriptor) objectReference).getTypeDescriptor();
            if (descriptor != null) {
                QualifiedName qualifiedName = descriptor.getQualifiedName();
                if (qualifiedName != null) {
                    return getFirst(index.get(qualifiedName, project, scope), null);
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
