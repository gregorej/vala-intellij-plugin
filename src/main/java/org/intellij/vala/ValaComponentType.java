package org.intellij.vala;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.commenter.ValaCommenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.intellij.vala.psi.*;

import javax.swing.*;


public enum ValaComponentType {

    CLASS,
    METHOD,
    CONSTRUCTOR;
    //FUNCTION,
//    VARIABLE(AllIcons.Nodes.Variable),
//    FIELD(AllIcons.Nodes.Field),
//    PARAMETER(AllIcons.Nodes.Parameter),
//    TYPEDEF(AllIcons.Nodes.Annotationtype),
//    OPERATOR(AllIcons.Nodes.ClassInitializer),
//    LABEL(AllIcons.Nodes.Variable);

    public static ValaComponentType typeOf(@Nullable PsiElement element) {
        if (element instanceof ValaElementType) {
            return typeOf(element.getParent());
        }
//        if ((element instanceof DartComponent && PsiTreeUtil.getParentOfType(element, DartNormalFormalParameter.class, false) != null) ||
//                element instanceof DartNormalFormalParameter) {
//            return PARAMETER;
//        }
        if (element instanceof ValaClassDeclaration) {
            return CLASS;
        }
//        if (element instanceof DartEnumConstantDeclaration) {
//            return FIELD;
//        }
//        if (element instanceof DartFunctionTypeAlias) {
//            return TYPEDEF;
//        }
        if (element instanceof ValaConstructorDeclaration) {
            return CONSTRUCTOR;
        }
        if (element instanceof ValaMethodDeclaration) {
            System.out.print(element.getText());
            System.out.print(element.getTextRange());
            return METHOD;
        }
//        if (element instanceof DartOperatorDeclaration) {
//            return OPERATOR;
//        }
//        if (element instanceof DartMethodDeclaration) {
//            final DartClass dartClass = PsiTreeUtil.getParentOfType(element, DartClass.class);
//            final String dartClassName = dartClass != null ? dartClass.getName() : null;
//            return dartClassName != null && dartClassName.equals(((DartComponent)element).getName()) ? CONSTRUCTOR : METHOD;
//        }
//        if (element instanceof DartVarAccessDeclaration
//                || element instanceof DartVarDeclarationListPart) {
//            return PsiTreeUtil.getParentOfType(element, DartComponent.class, DartOperator.class) instanceof DartClass ? FIELD : VARIABLE;
//        }
//
//        if (element instanceof DartForInPart) {
//            return VARIABLE;
//        }
//
//        if (element instanceof DartLabel) {
//            return LABEL;
//        }

        return null;
    }

}
