package org.intellij.vala.psi.impl;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ValaPsiElementUtil {

    public static ValaSymbolPart getLastPart(ValaSymbol symbol) {
        ValaSymbolPart lastPart = null;
        if (!symbol.getSymbolPartList().isEmpty()) {
            lastPart = symbol.getSymbolPartList().get(symbol.getSymbolPartList().size() - 1);
        }
        return lastPart;
    }

    public static boolean isMethodCall(ValaSimpleName simpleName) {
        if (! (simpleName.getParent() instanceof ValaPrimaryExpression)) {
            return false;
        }
        ValaPrimaryExpression parent = (ValaPrimaryExpression) simpleName.getParent();
        return !parent.getChainAccessPartList().isEmpty() && parent.getChainAccessPartList().get(0) instanceof ValaMethodCall;
    }

    public static ValaDeclaration findTypeDeclaration(PsiElement resolved) {
        if (resolved instanceof HasTypeDescriptor) {
            ValaTypeDescriptor typeDescriptor = ((HasTypeDescriptor) resolved).getTypeDescriptor();
            if (typeDescriptor != null) {
                final QualifiedName qualifiedName = typeDescriptor.getQualifiedName();
                if (qualifiedName != null) {
                    final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
                    return index.get(typeDescriptor.getQualifiedName(), resolved.getProject());
                }
            }
        }
        return null;
    }

    @Nullable
    public static ValaDelegateDeclaration getMethodDeclaration(ValaMethodCall valaMethodCall) {
        ValaChainAccessPart previousPart = valaMethodCall.getPrevious();
        if (previousPart == null) {
            PsiElement parent = valaMethodCall.getParent();
            if (parent instanceof ValaPrimaryExpression) {
                return getValaDelegateDeclaration((ValaPrimaryExpression) parent);
            }
            return null;
        }
        if (!(previousPart instanceof ValaMemberAccess)) {
            return null;
        }
        ValaMemberAccess memberAccess = (ValaMemberAccess) previousPart;
        return memberAccess.resolve().map( resolved -> {
            if (resolved instanceof ValaDelegateDeclaration) {
                return (ValaDelegateDeclaration) resolved;
            }
            return null;
        }).orElse(null);
    }

    @NotNull
    public static ValaMemberPart getLastPart(ValaMember valaMember) {
        List<ValaMemberPart> valaMemberParts = valaMember.getMemberPartList();
        if (valaMemberParts.isEmpty()) {
            throw new IllegalStateException("Member can not have 0 parts");
        }
        return valaMemberParts.get(valaMemberParts.size() - 1);
    }

    @Nullable
    public static ValaChainAccessPart getLastPart(ValaPrimaryExpression valaPrimaryExpression) {
        List<ValaChainAccessPart> parts = valaPrimaryExpression.getChainAccessPartList();
        int size = parts.size();
        if (size == 0) {
            return null;
        }
        return parts.get(size - 1);
    }

    private static ValaDelegateDeclaration getValaDelegateDeclaration(ValaPrimaryExpression parent) {
        final ValaExpression parentExpression = parent.getExpression();
        if (parentExpression instanceof ValaSimpleName) {
            return (ValaDelegateDeclaration) ((ValaSimpleName) parentExpression).resolve().orElse(null);
        }
        return null;
    }
}
