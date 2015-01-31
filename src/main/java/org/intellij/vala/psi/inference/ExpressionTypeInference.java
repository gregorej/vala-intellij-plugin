package org.intellij.vala.psi.inference;


import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.apache.http.util.Asserts;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiElementUtil;

import java.util.List;

import static org.intellij.vala.psi.impl.ValaPsiImplUtil.getTypeDescriptor;

public class ExpressionTypeInference {

    public static ValaTypeDescriptor inferType(ValaExpression valaExpression) {
        if (valaExpression instanceof ValaPrimaryExpression) {
            return inferType((ValaPrimaryExpression) valaExpression);
        } else if (valaExpression instanceof ValaObjectOrArrayCreationExpression) {
            return getTypeDescriptor((ValaObjectOrArrayCreationExpression) valaExpression);
        } else if(valaExpression instanceof ValaSizeofExpression) {
            return inferType((ValaSizeofExpression) valaExpression);
        } else if(valaExpression instanceof ValaMultiplicativeExpression) {
            return inferType((ValaMultiplicativeExpression) valaExpression);
        } else if(valaExpression instanceof ValaAdditiveExpression) {
            return inferType((ValaAdditiveExpression) valaExpression);
        } else if (valaExpression instanceof ValaUnaryExpression) {
            return inferType((ValaUnaryExpression) valaExpression);
        } else if (valaExpression instanceof ValaConditionalExpression) {
            return inferType((ValaConditionalExpression) valaExpression);
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

    public static ValaTypeDescriptor inferType(ValaShiftOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return left;
    }

    public static ValaTypeDescriptor inferType(ValaMultiplicativeOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return inferTypeForNumericTypes(left, right);
    }

    private static ValaTypeDescriptor inferTypeForNumericTypes(ValaTypeDescriptor left, ValaTypeDescriptor right) {
        if (left == ValaTypeDescriptor.DOUBLE || right == ValaTypeDescriptor.DOUBLE) return ValaTypeDescriptor.DOUBLE;
        if (left == ValaTypeDescriptor.LONG || right == ValaTypeDescriptor.LONG) return ValaTypeDescriptor.LONG;
        return left;
    }

    public static ValaTypeDescriptor inferType(ValaAdditiveOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return inferTypeForNumericTypes(left, right);
    }

    public static ValaTypeDescriptor inferType(ValaEqualityOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return ValaTypeDescriptor.BOOL;
    }

    public static ValaTypeDescriptor inferType(ValaRelationalOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return ValaTypeDescriptor.BOOL;
    }

    public static ValaTypeDescriptor inferType(ValaBinaryOperator op, ValaTypeDescriptor left, ValaTypeDescriptor right) {
        return ValaTypeDescriptor.BOOL;
    }

    public static ValaTypeDescriptor inferType(ValaMultiplicativeExpression multiplicativeExpression) {
        return inferTypeFromBinaryNumericExpression(multiplicativeExpression.getExpression(), multiplicativeExpression.getExpressionList());
    }

    public static ValaTypeDescriptor inferType(ValaAdditiveExpression additiveExpression) {
        return inferTypeFromBinaryNumericExpression(additiveExpression.getExpression(), additiveExpression.getExpressionList());
    }

    public static ValaTypeDescriptor inferType(ValaConditionalExpression conditionalExpression) {
        final List<ValaExpression> expressionList = conditionalExpression.getExpressionList();
        Asserts.check(expressionList.size() == 3, "Invalid conditional expression");
        return inferTypeFromBinaryNumericExpression(expressionList.get(1), ImmutableList.of(expressionList.get(2)));
    }

    private static ValaTypeDescriptor inferTypeFromBinaryNumericExpression(ValaExpression first, List<ValaExpression> remainingExpressions) {
        if (first == null) {
            return null;
        }
        ValaTypeDescriptor firstType = inferType(first);
        if (firstType == null) {
            return null;
        }
        for (ValaExpression right : remainingExpressions) {
            ValaTypeDescriptor rightType = inferType(right);
            if (rightType == null) {
                return null;
            }
            firstType = inferTypeForNumericTypes(firstType, rightType);
        }
        return firstType;
    }

    public static ValaTypeDescriptor inferType(ValaUnaryExpression unaryExpression) {
        if (unaryExpression.getType() != null) {
            return unaryExpression.getType().getTypeDescriptor();
        }
        return ExpressionTypeInference.inferType(unaryExpression.getExpression());
    }

    public static ValaTypeDescriptor inferType(ValaSizeofExpression sizeofExpression) {
        return ValaTypeDescriptor.LONG;
    }

    public static ValaTypeDescriptor inferType(ValaTypeofExpression typeofExpression) {
        return new ValaTypeDescriptor.ValaTypeTypeDescriptor();
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
