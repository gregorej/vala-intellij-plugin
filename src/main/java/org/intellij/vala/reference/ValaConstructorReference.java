package org.intellij.vala.reference;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ValaConstructorReference extends PsiReferenceBase<ValaObjectOrArrayCreationExpression> {

    private ClassDeclarationResolver resolver;

    public ValaConstructorReference(ValaObjectOrArrayCreationExpression element) {
        super(element, calculateRange(element));
        resolver = new ClassDeclarationResolver(element.getProject());
    }

    private static TextRange calculateRange(ValaObjectOrArrayCreationExpression expression) {
        ValaMemberPart memberPart = expression.getMember().getMemberPartList().get(0);
        int offset = memberPart.getTextOffset() - expression.getTextOffset();
        return new TextRange(offset, offset + memberPart.getName().length());
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        List<ValaMemberPart> parts = myElement.getMember().getMemberPartList();
        if (parts.size() > 1) {
            return resolveForMultiPartName(parts);
        }
        else {
            return resolveForSinglePartName(parts);
        }
    }

    private PsiElement resolveForMultiPartName(List<ValaMemberPart> parts) {
        ValaCreationMethodDeclaration namedConstructor = resolveAssumingNamedConstructor(parts);
        if (namedConstructor != null) {
            return namedConstructor;
        }
        return resolveAssumingClassNameWithFullNamespace(parts);
    }

    private PsiElement resolveForSinglePartName(List<ValaMemberPart> parts) {
        String className = parts.get(0).getName();
        return resolver.resolve(className);
    }

    private ValaCreationMethodDeclaration resolveAssumingNamedConstructor(List<ValaMemberPart> names) {
        String className = names.get(0).getName();
        ValaClassDeclaration classDeclaration = resolver.resolve(className);
        String constructorName = names.get(1).getName();
        if (classDeclaration != null) {
            return getNamedConstructor(classDeclaration, constructorName);
        }
        return null;
    }

    private static ValaCreationMethodDeclaration getNamedConstructor(ValaClassDeclaration classDeclaration, String constructorName) {
        for (ValaClassMember member : classDeclaration.getClassMemberList()) {
            ValaCreationMethodDeclaration creationMethodDeclaration = member.getCreationMethodDeclaration();
            if (creationMethodDeclaration != null) {
                List<ValaSymbolPart> symbolParts = creationMethodDeclaration.getSymbol().getSymbolPartList();
                if (symbolParts.size() > 1 && symbolParts.get(1).getName().equals(constructorName)) {
                    return creationMethodDeclaration;
                }
            }
        }
        return null;
    }

    private ValaClassDeclaration resolveAssumingClassNameWithFullNamespace(List<ValaMemberPart> names) {
        String className = names.get(names.size() - 1).getName();
        return resolver.resolve(className);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
