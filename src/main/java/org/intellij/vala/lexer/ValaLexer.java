package org.intellij.vala.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.intellij.vala.psi.ValaTypes;


public class ValaLexer extends LookAheadLexer {

    public static final TokenSet KEYWORDS = TokenSet.create(ValaTypes.KEY_CLASS, ValaTypes.KEY_REF);

    public ValaLexer() {
        super(new FlexAdapter(new _ValaLexer()));
    }
}
