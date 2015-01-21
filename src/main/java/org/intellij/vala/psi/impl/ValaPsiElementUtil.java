package org.intellij.vala.psi.impl;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;
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
        ValaPrimaryExpression parent = (ValaPrimaryExpression) simpleName.getParent();
        return !parent.getChainAccessPartList().isEmpty() && parent.getChainAccessPartList().get(0) instanceof ValaMethodCall;
    }

    public static PsiReference getTypeReference(ValaFieldDeclaration fieldDeclaration) {
        ValaSymbol symbol = fieldDeclaration.getTypeWeak().getSymbol();
        if (symbol != null) {
            List<ValaSymbolPart> parts = symbol.getSymbolPartList();
            return parts.get(parts.size() - 1).getReference();
        }
        return null;
    }

    public static ValaDeclaration findTypeDeclaration(PsiElement resolved) {
        if (resolved instanceof ValaLocalVariable) {
            return (ValaDeclaration) LocalVariableUtil.getTypeReference((ValaLocalVariable) resolved).resolve();
        } else if (resolved instanceof ValaFieldDeclaration) {
            return (ValaDeclaration) getTypeReference((ValaFieldDeclaration) resolved).resolve();
        }
        return null;
    }

    private static PsiElement resolveReference(PsiElement referenceHolder) {
        if (referenceHolder == null) {
            return null;
        }
        PsiReference reference = referenceHolder.getReference();
        if (reference != null) {
            return reference.resolve();
        }
        return null;
    }

    private static PsiElement resolveReference(ValaSymbol symbol) {
        List<ValaSymbolPart> symbolParts = symbol.getSymbolPartList();
        if (symbolParts.isEmpty()) {
            return null;
        }
        return resolveReference(symbolParts.get(symbolParts.size() - 1));
    }

    @Nullable
    public static ValaMethodDeclaration getMethodDeclaration(ValaMethodCall valaMethodCall) {
        ValaChainAccessPart previousPart = valaMethodCall.getPrevious();
        if (previousPart == null) {
            PsiElement parent = valaMethodCall.getParent();
            if (parent instanceof ValaPrimaryExpression) {
                return getValaMethodDeclaration((ValaPrimaryExpression) parent);
            }
            return null;
        }
        if (!(previousPart instanceof ValaMemberAccess)) {
            return null;
        }
        ValaMemberAccess memberAccess = (ValaMemberAccess) previousPart;
        PsiElement resolved = resolveReference(getLastPart(memberAccess.getMember()));
        if (resolved instanceof ValaMethodDeclaration) {
            return (ValaMethodDeclaration) resolved;
        }
        return null;
    }
    @Nullable
    public static ValaTypeDeclaration getReturningTypeDeclaration(@NotNull ValaMethodDeclaration valaMethodDeclaration) {
        ValaType type = valaMethodDeclaration.getType();
        return getTypeDeclaration(type);
    }

    @Nullable
    private static ValaTypeDeclaration getTypeDeclaration(@NotNull ValaType type) {
        ValaSymbol symbol = type.getSymbol();
        if (symbol == null) {
            return null;
        }
        PsiElement resolved = resolveReference(symbol);
        if (resolved instanceof ValaTypeDeclaration) {
            return (ValaTypeDeclaration) resolved;
        }
        return null;
    }

    @NotNull
    public static ValaMemberPart getLastPart(ValaMember valaMember) {
        List<ValaMemberPart> valaMemberParts = valaMember.getMemberPartList();
        if (valaMemberParts.isEmpty()) {
            throw new IllegalStateException("Member can not have 0 parts");
        }
        return valaMemberParts.get(valaMemberParts.size() - 1);
    }

    private static ValaMethodDeclaration getValaMethodDeclaration(ValaPrimaryExpression parent) {
        PsiElement resolvedElement = resolveReference(parent.getSimpleName());
        if (resolvedElement instanceof ValaMethodDeclaration) {
            return (ValaMethodDeclaration) resolvedElement;
        }
        return null;
    }
}
