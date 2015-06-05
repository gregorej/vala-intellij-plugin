package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaObjectOrArrayCreationExpression;

import java.util.Optional;

public class ValaMemberPartReferenceFactory {

    private ValaMemberPartReferenceFactory () {}

    public static final ValaMemberPartReferenceFactory INSTANCE = new ValaMemberPartReferenceFactory();

    public PsiReference create(ValaMemberPart memberPart) {
        if (isPartOfObjectCreation(memberPart)) {
            return new ValaConstructorReference(memberPart);
        } else {
            return null;
        }
    }

    public static Optional<? extends PsiElement> resolve(ValaMemberPart memberPart) {
        if (isPartOfObjectCreation(memberPart)) {
            return ValaConstructorReference.resolve(memberPart);
        } else {
            return Optional.empty();
        }
    }

    private static boolean isPartOfObjectCreation(ValaMemberPart memberPart) {
        return memberPart.getParent().getParent() instanceof ValaObjectOrArrayCreationExpression;

    }
}
