package org.intellij.vala.psi.impl;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.psi.*;
import org.intellij.vala.resolve.ValaConstructorReference;
import org.intellij.vala.resolve.ValaTypeReference;

public class ValaPsiImplUtil {

    public static PsiReference getReference(ValaMethodCall methodCall) {
        return null;
    }

    public static PsiReference getReference(ValaSimpleName methodCall) {
        return null;
    }

    public static PsiReference getReference(ValaTypeWeak typeWeak) {
        int nameLength = typeWeak.getSymbol().getText().length();
        return new ValaTypeReference(typeWeak, new TextRange(0, nameLength));
    }

    public static PsiReference getReference(ValaType type) {
        return null;
    }

    public static ValaNamespaceDeclaration getNamespace(ValaNamespaceMember valaClassDeclaration) {
        return PsiTreeUtil.getParentOfType(valaClassDeclaration, ValaNamespaceDeclaration.class, false);
    }

    public static String getName(ValaClassDeclaration classDeclaration) {
        return classDeclaration.getSymbol().getText();
    }

    public static String getName(ValaSymbolPart symbolPart) {
        return symbolPart.getIdentifier().getText();
    }

    public static String getName(ValaMemberPart memberPart) {
        return memberPart.getIdentifier().getText();
    }

    public static String getName(ValaSimpleName simpleName) {
        return simpleName.getIdentifier().getText();
    }

    public static String getName(ValaMethodDeclaration methodDeclaration) {
        return methodDeclaration.getIdentifier().getText();
    }

    public static PsiElement setName(ValaPsiElement valaPsiElement, String newName) {
        throw new IncorrectOperationException("changing name of this element is not supported");
    }

    public static PsiReference getReference(ValaObjectOrArrayCreationExpression objectCreationExpression) {
        return new ValaConstructorReference(objectCreationExpression);
    }
}
