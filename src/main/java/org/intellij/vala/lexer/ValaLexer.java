package org.intellij.vala.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.TokenSet;
import org.intellij.vala.psi.ValaTypes;


public class ValaLexer extends LookAheadLexer {

    public static final TokenSet KEYWORDS = TokenSet.create(
            ValaTypes.KEY_CLASS,
            ValaTypes.KEY_REF,
            ValaTypes.KEY_ABSTRACT,
            ValaTypes.KEY_PUBLIC,
            ValaTypes.KEY_PRIVATE,
            ValaTypes.KEY_IN,
            ValaTypes.KEY_INT,
            ValaTypes.KEY_NEW,
            ValaTypes.KEY_STRING,
            ValaTypes.KEY_VAR,
            ValaTypes.KEY_INTERNAL,
            ValaTypes.KEY_INTERFACE,
            ValaTypes.KEY_PROTECTED,
            ValaTypes.KEY_NAMESPACE);

    public ValaLexer() {
        super(new FlexAdapter(new _ValaLexer()));
    }
}
