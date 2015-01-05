package org.intellij.vala.reference;


import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaObjectOrArrayCreationExpression;

public class ValaMemberPartReferenceFactory {

    private ValaMemberPartReferenceFactory () {}

    public static final ValaMemberPartReferenceFactory INSTANCE = new ValaMemberPartReferenceFactory();

    public PsiReference create(ValaMemberPart memberPart) {
        if (isPartOfObjectCreation(memberPart)) {
            return new ValaConstructorReference(memberPart);
        }
        return null;
    }

    private static boolean isPartOfObjectCreation(ValaMemberPart memberPart) {
        return memberPart.getParent().getParent() instanceof ValaObjectOrArrayCreationExpression;

    }
}
