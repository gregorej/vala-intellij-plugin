package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaSymbol;
import org.intellij.vala.psi.ValaSymbolPart;
import org.intellij.vala.psi.ValaType;
import org.intellij.vala.psi.ValaTypeWeak;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class SymbolReferenceRetriever {

    public static PsiReference getReference(ValaSymbolPart symbolPart) {
        ValaSymbol symbol = getParentOfType(symbolPart, ValaSymbol.class);
        TextRange textRange = new TextRange(0, symbolPart.getText().length());
        if (symbol == null) {
            return null;
        }
        if (isPartOfVariableDeclaration(symbol)) {
            return new ValaTypeReference(symbol, textRange);
        }
        return null;
    }

    private static boolean isPartOfVariableDeclaration(ValaSymbol symbol) {
        return getParentOfType(symbol, ValaTypeWeak.class) != null || getParentOfType(symbol, ValaType.class) != null;
    }
}
