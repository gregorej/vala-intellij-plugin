package org.intellij.vala.psi.impl;


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
}
