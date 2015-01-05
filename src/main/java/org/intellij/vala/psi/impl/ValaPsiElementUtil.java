package org.intellij.vala.psi.impl;


import org.intellij.vala.psi.ValaPrimaryExpression;
import org.intellij.vala.psi.ValaSimpleName;
import org.intellij.vala.psi.ValaSymbol;
import org.intellij.vala.psi.ValaSymbolPart;

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
        return !parent.getMethodCallList().isEmpty() && parent.getMemberAccessList().isEmpty();
    }
}
