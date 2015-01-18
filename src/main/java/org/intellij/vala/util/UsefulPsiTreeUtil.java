package org.intellij.vala.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

public class UsefulPsiTreeUtil {
    @Nullable
    public static ASTNode getPrevSiblingSkipWhiteSpacesAndComments(@Nullable ASTNode sibling) {
        if (sibling == null) return null;
        ASTNode result = sibling.getTreePrev();
        while (result != null && isWhitespaceOrComment(result.getPsi())) {
            result = result.getTreePrev();
        }
        return result;
    }

    public static boolean isWhitespaceOrComment(PsiElement element) {
        return element instanceof PsiWhiteSpace || element instanceof PsiComment;
    }
}
