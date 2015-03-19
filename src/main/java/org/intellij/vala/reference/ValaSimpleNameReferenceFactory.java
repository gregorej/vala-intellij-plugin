package org.intellij.vala.reference;


import com.intellij.psi.PsiReference;
import org.intellij.vala.psi.ValaSimpleName;
import org.intellij.vala.reference.method.ValaDelegateReference;

import static org.intellij.vala.psi.impl.ValaPsiElementUtil.isMethodCall;

public class ValaSimpleNameReferenceFactory {

    public static final ValaSimpleNameReferenceFactory INSTANCE = new ValaSimpleNameReferenceFactory();

    public PsiReference create(ValaSimpleName simpleName) {
        if (isMethodCall(simpleName)) {
            return new ValaDelegateReference(simpleName);
        } else if (isVariableReference(simpleName)) {
            return new ValaVariableReference(simpleName);
        }
        return null;
    }

    private static boolean isVariableReference(ValaSimpleName simpleName) {
        return !isMethodCall(simpleName);
    }
}
