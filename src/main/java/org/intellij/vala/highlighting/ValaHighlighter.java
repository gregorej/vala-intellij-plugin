package org.intellij.vala.highlighting;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.ValaTypes;
import org.intellij.vala.lexer.ValaLexer;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ValaHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey STRING = createTextAttributesKey("VALA_STRING",
            DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey INTEGER = createTextAttributesKey("VALA_INTEGER", DefaultLanguageHighlighterColors.NUMBER);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ValaLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType == ValaTypes.STRING_LITERAL) return pack(STRING);
        if (tokenType == ValaTypes.INT) return pack(INTEGER);
        return EMPTY;
    }
}
