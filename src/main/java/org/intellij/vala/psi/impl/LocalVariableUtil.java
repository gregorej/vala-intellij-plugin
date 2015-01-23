package org.intellij.vala.psi.impl;


import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;

import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class LocalVariableUtil {

    public static PsiReference getTypeReference(ValaLocalVariable localVariable) {
        ValaLocalVariableDeclarations declarations = getParentOfType(localVariable, ValaLocalVariableDeclarations.class, false);
        if (declarations == null) {
            return null;
        }
        if (declarations.getType() != null) {
            List<ValaSymbolPart> parts = declarations.getType().getTypeBase().getSymbol().getSymbolPartList();
            return parts.get(parts.size() - 1).getReference();
        }
        return null;
    }
}
