package org.intellij.vala.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.intellij.vala.ValaTypes;


public class ValaLexer extends LookAheadLexer {

    public static final TokenSet KEYWORDS = TokenSet.create(ValaTypes.KEY_CLASS, ValaTypes.KEY_REF);

    public ValaLexer() {
        super(new FlexAdapter(new ValaFlexLexer()));
    }

    public void advance() {
        IElementType currentToken = getTokenType();
        super.advance();
    }

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        startOffset = startOffset;
        super.start(buffer, startOffset, endOffset, initialState);
    }
}
