package org.intellij.vala.psi.impl;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.*;
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
}
