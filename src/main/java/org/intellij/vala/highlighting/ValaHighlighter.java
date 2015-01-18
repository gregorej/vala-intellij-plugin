package org.intellij.vala.highlighting;


import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.lexer.ValaLexer;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class ValaHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey STRING = createTextAttributesKey("VALA_STRING",
            DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey NUMBER = createTextAttributesKey("VALA_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    private static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("VALA_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    private static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("VALA_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey KEYWORD = createTextAttributesKey("VALA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new ValaLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (ValaLexer.KEYWORDS.contains(tokenType) || ValaLexer.BUILT_IN_TYPES.contains(tokenType)) return pack(KEYWORD);
        if (tokenType == ValaTypes.STRING || tokenType == ValaTypes.VERBATIM_STRING_LITERAL) return pack(STRING);
        if (tokenType == ValaTypes.INTEGER_LITERAL || tokenType == ValaTypes.REAL_LITERAL) return pack(NUMBER);
        if (tokenType == ValaTypes.LINE_COMMENT) return pack(LINE_COMMENT);
        if (tokenType == ValaTypes.BLOCK_COMMENT) return pack(BLOCK_COMMENT);
        return EMPTY;
    }
}
