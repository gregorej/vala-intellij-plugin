package org.intellij.vala.psi.impl;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;

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
}
