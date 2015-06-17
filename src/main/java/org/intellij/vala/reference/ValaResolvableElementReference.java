package org.intellij.vala.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.psi.ValaPsiNameIdentifierOwner;
import org.intellij.vala.psi.ValaResolvableElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValaResolvableElementReference<T extends ValaResolvableElement> extends PsiReferenceBase<T> {

    public ValaResolvableElementReference(@NotNull T element) {
        super(element, new TextRange(0, element.getTextLength()));
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return myElement.resolve().map(resolved -> {
            if (resolved instanceof ValaPsiNameIdentifierOwner) {
                return ((ValaPsiNameIdentifierOwner) resolved).getNameIdentifier();
            } else {
                return resolved;
            }
        }).orElse(null);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new UnsupportedOperationException("not implemented for " + myElement);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
