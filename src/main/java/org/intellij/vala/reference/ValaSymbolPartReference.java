package org.intellij.vala.reference;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.psi.ValaElementFactory;
import org.intellij.vala.psi.ValaIdentifier;
import org.intellij.vala.psi.ValaSymbolPart;
import org.jetbrains.annotations.NotNull;

/**
 * Created by sharky on 17.06.15.
 */
public class ValaSymbolPartReference extends ValaResolvableElementReference<ValaSymbolPart> {
    public ValaSymbolPartReference(@NotNull ValaSymbolPart element) {
        super(element);
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        ValaIdentifier renamed = ValaElementFactory.createIdentifier(myElement.getProject(), newElementName);
        myElement.getIdentifier().replace(renamed);
        return myElement;
    }
}
