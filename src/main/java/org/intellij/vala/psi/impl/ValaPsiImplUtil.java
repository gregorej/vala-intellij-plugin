package org.intellij.vala.psi.impl;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.*;
import org.intellij.vala.resolve.ValaClassReference;

public class ValaPsiImplUtil {

    public static PsiReference getReference(ValaMethodCall methodCall) {
        return null;
    }

    public static PsiReference getReference(ValaSimpleName methodCall) {
        return null;
    }

    public static PsiReference getReference(ValaTypeWeak typeWeak) {
        return new ValaClassReference(typeWeak, new TextRange(0, 1));
    }

    public static PsiReference getReference(ValaType typeWeak) {
        return null;
    }
}
