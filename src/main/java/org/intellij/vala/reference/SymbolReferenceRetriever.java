package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaSymbol;
import org.intellij.vala.psi.ValaSymbolPart;
import org.intellij.vala.psi.ValaType;
import org.intellij.vala.psi.ValaTypeWeak;

import java.util.Optional;

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

    public static Optional<PsiElement> resolve(ValaSymbolPart symbolPart) {
        ValaSymbol symbol = getParentOfType(symbolPart, ValaSymbol.class);
        if (symbol == null) {
            return Optional.empty();
        }
        return resolve(symbol);
    }

    public static Optional<PsiElement> resolve(ValaSymbol symbol) {
        if (isPartOfVariableDeclaration(symbol)) {
            return ValaTypeReference.resolve(symbol);
        }
        return Optional.empty();
    }

    private static boolean isPartOfVariableDeclaration(ValaSymbol symbol) {
        return getParentOfType(symbol, ValaTypeWeak.class) != null || getParentOfType(symbol, ValaType.class) != null;
    }
}
