package org.intellij.vala.braces;


import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

public class ValaBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS = new BracePair[]{
            new BracePair(ValaTypes.LEFT_CURLY, ValaTypes.RIGHT_CURLY, true),
            new BracePair(ValaTypes.LEFT_PAREN, ValaTypes.RIGHT_PAREN, false),
            new BracePair(ValaTypes.LEFT_SQUARE, ValaTypes.RIGHT_SQUARE, false)
    };


    @Override
    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, IElementType iElementType1) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile psiFile, int i) {
        return i;
    }
}
