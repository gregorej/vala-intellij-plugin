package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;
import org.intellij.vala.reference.method.ValaMethodReference;

import java.util.List;

public class ValaMemberPartReferenceFactory {

    private ValaMemberPartReferenceFactory () {}

    public static final ValaMemberPartReferenceFactory INSTANCE = new ValaMemberPartReferenceFactory();

    public PsiReference create(ValaMemberPart memberPart) {
        if (isPartOfObjectCreation(memberPart)) {
            return new ValaConstructorReference(memberPart);
        } else if (isThisClassFieldAccess(memberPart)) {
            return new ValaFieldReference(memberPart);
        } else if (isMethodCall(memberPart)) {
            return resolveAsMethodReference(memberPart);
        } else {
            return resolveAsObjectFieldReference(memberPart);
        }

    }

    private static PsiReference resolveAsMethodReference(ValaMemberPart memberPart) {
        PsiElement precedingReference = getPrecedingReference(memberPart);
        if (precedingReference != null) {
            return new ValaMethodReference(precedingReference, memberPart);
        }
        return null;
    }

    private static boolean isMethodCall(ValaMemberPart memberPart) {
        ValaMemberAccess memberAccess = (ValaMemberAccess) memberPart.getParent().getParent();
        if (memberAccess.getParent() instanceof ValaPrimaryExpression) {
            ValaPrimaryExpression primaryExpression = (ValaPrimaryExpression) memberAccess.getParent();
            final List<ValaChainAccessPart> accessPart = primaryExpression.getChainAccessPartList();
            int index = accessPart.indexOf(memberAccess);
            return index < accessPart.size() - 1 && accessPart.get(index + 1) instanceof ValaMethodCall;
        }
        return false;
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
