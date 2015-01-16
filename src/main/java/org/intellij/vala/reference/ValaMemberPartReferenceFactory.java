package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaMember;
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
        } else {
            return resolveAsObjectFieldReference(memberPart);
        }

    }

    private static PsiReference resolveAsObjectFieldReference(ValaMemberPart memberPart) {
        PsiElement precedingReference = getPrecedingReference(memberPart);
        if (precedingReference != null) {
            return new ValaFieldReference(precedingReference, memberPart);
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

    private static PsiElement getPrecedingReference(ValaMemberPart memberPart) {
        ValaMember member = (ValaMember) memberPart.getParent();
        int partIndex = member.getMemberPartList().indexOf(memberPart);
        if (partIndex > 0) {
            return member.getMemberPartList().get(partIndex - 1);
        }
        PsiElement maybePrimaryExpression = member.getParent().getParent();
        if (maybePrimaryExpression instanceof ValaPrimaryExpression) {
            return ((ValaPrimaryExpression) maybePrimaryExpression).getSimpleName();
        }
        return null;
    }

    private static boolean isPartOfObjectCreation(ValaMemberPart memberPart) {
        return memberPart.getParent().getParent() instanceof ValaObjectOrArrayCreationExpression;

    }
}
