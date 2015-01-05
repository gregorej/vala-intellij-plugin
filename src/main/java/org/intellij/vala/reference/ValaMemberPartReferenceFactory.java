package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaObjectOrArrayCreationExpression;
import org.intellij.vala.psi.ValaPrimaryExpression;

public class ValaMemberPartReferenceFactory {

    private ValaMemberPartReferenceFactory () {}

    public static final ValaMemberPartReferenceFactory INSTANCE = new ValaMemberPartReferenceFactory();

    public PsiReference create(ValaMemberPart memberPart) {
        if (isPartOfObjectCreation(memberPart)) {
            return new ValaConstructorReference(memberPart);
        } else if (isThisClassFieldAccess(memberPart)) {
            return new ValaFieldReference(memberPart);
        }
        return null;
    }

    private static boolean isThisClassFieldAccess(ValaMemberPart memberPart) {
        PsiElement parent = memberPart.getParent().getParent().getParent();
        if (parent instanceof ValaPrimaryExpression) {
            ValaPrimaryExpression vpe = (ValaPrimaryExpression) parent;
            if (vpe.getThisAccess() != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPartOfObjectCreation(ValaMemberPart memberPart) {
        return memberPart.getParent().getParent() instanceof ValaObjectOrArrayCreationExpression;

    }
}
