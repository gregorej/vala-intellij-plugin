package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.intellij.vala.psi.impl.ValaPsiElementUtil.findTypeDeclaration;
import static org.intellij.vala.psi.impl.ValaPsiElementUtil.isMethodCall;


public class ValaIdentifierReference extends PsiReferenceBase<ValaIdentifier> {

    public ValaIdentifierReference(ValaIdentifier valaMemberAccess) {
        super(valaMemberAccess, new TextRange(0, valaMemberAccess.getTextLength()));
    }

    private static PsiElement getPrecedingReference(ValaMemberAccess memberAccess) {
        PsiElement precedingReference;
        ValaPrimaryExpression primaryExpression = (ValaPrimaryExpression) memberAccess.getParent();
        final int memberAccessIndex = primaryExpression.getChainAccessPartList().indexOf(memberAccess);
        if (memberAccessIndex > 0 ) {
            precedingReference = primaryExpression.getChainAccessPartList().get(memberAccessIndex - 1);
        } else {
            precedingReference = primaryExpression.getExpression();
        }
        return precedingReference;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiElement parent = myElement.getParent();
        if (parent instanceof ValaMemberAccess) {
            return resolveAsOtherObjectFieldReference((ValaMemberAccess) parent);
        } else if (parent instanceof ValaSimpleName) {
            return resolveAsDirectReference((ValaSimpleName) parent);
        } else {
            return null;
        }
    }

    private static PsiElement resolveAsDirectReference(ValaSimpleName simpleName) {
        if (isMethodCall(simpleName)) {
            return resolveAsThisClassReference(simpleName, delegateWithName(simpleName));
        }
        else {
            PsiElement variable = ValaVariableReference.resolve(simpleName);
            if (variable != null) {
                return variable;
            }
            return resolveAsDirectTypeReference(simpleName);
        }
    }

    private static PsiElement resolveAsDirectTypeReference(ValaSimpleName simpleName) {
        DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        for (QualifiedName qualifiedName : ValaPsiImplUtil.getImportedNamespacesAvailableFor(simpleName)) {
            final QualifiedName directQualifiedName = qualifiedName.append(simpleName.getName());
            ValaDeclaration foundDeclaration = index.get(directQualifiedName, simpleName.getProject());
            if (foundDeclaration != null) {
                return foundDeclaration;
            }
        }
        return null;
    }

    private PsiElement resolveAsOtherObjectFieldReference(ValaMemberAccess memberAccess) {
        ValaDeclaration objectTypeDeclaration = resolveObjectType(memberAccess);
        if (objectTypeDeclaration instanceof ValaEnumDeclaration) {
            return resolveAsEnumValue(memberAccess, (ValaEnumDeclaration) objectTypeDeclaration);
        } else if (objectTypeDeclaration instanceof ValaDeclarationContainer) {
            return resolveAsClassFieldReference(memberAccess, (ValaDeclarationContainer) objectTypeDeclaration);
        }
        return null;
    }

    private static PsiElement resolveAsEnumValue(ValaMemberAccess memberAccess, ValaEnumDeclaration enumDeclaration) {
        return enumDeclaration.getEnumBody().getEnumvalues().getEnumvalueList().stream()
                .filter((enumValue) -> enumValue.getName() != null && enumValue.getName().equals(memberAccess.getName()))
                .findAny()
                .orElse(null);
    }

    private static PsiElement resolveAsThisClassReference(PsiNamedElement reference, Predicate<ValaDeclaration> declarationPredicate) {
        ValaDeclarationContainer containingClass = getParentOfType(reference, ValaDeclarationContainer.class, false);
        if (containingClass == null) {
            return null;
        }
        return declarationsIncludingSuperclasses(containingClass).filter(declarationPredicate).findAny().orElse(null);
    }

    public static PsiElement resolveAsThisClassFieldReference(PsiNamedElement myElement) {
        return resolveAsThisClassReference(myElement, fieldWithName(myElement));
    }

    private static PsiElement resolveAsClassFieldReference(ValaMemberAccess myElement, ValaDeclarationContainer containingClass) {
        Predicate<ValaDeclaration> declarationPredicate;
        if (shouldLookForDelegates(myElement)) {
            declarationPredicate = delegateWithName(myElement);
        } else {
            declarationPredicate = fieldWithName(myElement);
        }
        return declarationsIncludingSuperclasses(containingClass).filter(declarationPredicate).findAny().orElse(null);
    }

    private static Predicate<ValaDeclaration> fieldWithName(PsiNamedElement myElement) {
        return (declaration) -> declaration instanceof ValaFieldDeclaration && myElement.getName().equals(((ValaFieldDeclaration) declaration).getName());
    }

    private static Predicate<ValaDeclaration> delegateWithName(PsiNamedElement myElement) {
        return (declaration) -> declaration instanceof ValaDelegateDeclaration && myElement.getName().equals(((ValaDelegateDeclaration) declaration).getName());
    }

    private static boolean shouldLookForDelegates(ValaMemberAccess myElement) {
        ValaPrimaryExpression primaryExpression = (ValaPrimaryExpression) myElement.getParent();
        final List<ValaChainAccessPart> chainAccessPartList = primaryExpression.getChainAccessPartList();
        final int myIndex = chainAccessPartList.indexOf(myElement);
        return myIndex < chainAccessPartList.size() - 1 && chainAccessPartList.get(myIndex + 1) instanceof ValaMethodCall;
    }

    private static Stream<ValaDeclaration> declarationsIncludingSuperclasses(ValaDeclarationContainer container) {
        Stream<ValaDeclaration> result = container.getDeclarations().stream();
        if (container instanceof ValaTypeWithSuperTypes) {
            return Stream.concat(result, ((ValaTypeWithSuperTypes) container).getSuperTypeDeclarations().stream().flatMap(superType -> superType.getDeclarations().stream()));
        } else {
            return result;
        }
    }

    @Nullable
    private static ValaDeclaration resolveObjectType(ValaMemberAccess memberAccess) {
        PsiReference parentRef = getPrecedingReference(memberAccess).getReference();
        if (parentRef == null) {
            return null;
        }
        PsiElement resolved = parentRef.resolve();
        if (resolved instanceof ValaTypeDeclaration) {
            return (ValaDeclaration) resolved;
        }
        if (resolved != null) {
            return findTypeDeclaration(resolved);
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
