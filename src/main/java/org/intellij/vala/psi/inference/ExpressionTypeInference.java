package org.intellij.vala.psi.inference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiElementUtil;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getTypeDescriptor;

public class ExpressionTypeInference {

    public static ValaTypeDescriptor inferType(ValaExpression valaExpression) {
        if (valaExpression instanceof ValaPrimaryExpression) {
            return inferType((ValaPrimaryExpression) valaExpression);
        } else if (valaExpression instanceof ValaObjectOrArrayCreationExpression) {
            return getTypeDescriptor((ValaObjectOrArrayCreationExpression) valaExpression);
        }
        return null;
    }

    public static ValaTypeDescriptor inferType(ValaPrimaryExpression primaryExpression) {
        if (primaryExpression.getLiteral() != null && hasNoChainedAccess(primaryExpression)) {
            return primaryExpression.getLiteral().getTypeDescriptor();
        }
        if (primaryExpression.getSimpleName() != null) {
            ValaChainAccessPart lastPart = ValaPsiElementUtil.getLastPart(primaryExpression);
            if (lastPart == null) {
                return inferType(primaryExpression.getSimpleName());
            } else {
                return lastPart.getTypeDescriptor();
            }
        }
        return null;
    }

    public static ValaTypeDescriptor inferType(ValaSimpleName valaSimpleName) {
        PsiReference reference = valaSimpleName.getReference();
        if (reference == null) {
            return null;
        }
        PsiElement resolved = reference.resolve();

        if (resolved instanceof HasTypeDescriptor) {
            return ((HasTypeDescriptor) resolved).getTypeDescriptor();
        }
        return null;
    }

    private static boolean hasNoChainedAccess(ValaPrimaryExpression primaryExpression) {
        return (primaryExpression.getChainAccessPartList().isEmpty());
    }
}
