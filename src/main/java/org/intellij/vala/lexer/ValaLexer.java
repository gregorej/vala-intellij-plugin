package org.intellij.vala.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;
import com.intellij.psi.tree.TokenSet;

import static org.intellij.vala.psi.ValaTypes.*;


public class ValaLexer extends LookAheadLexer {

    public static final TokenSet KEYWORDS = TokenSet.create(
            KEY_TRUE,
            KEY_FALSE,
            KEY_NULL,
            KEY_IS,
            KEY_AS,
            KEY_NAMESPACE,
            KEY_USING,
            KEY_CLASS,
            KEY_REF,
            KEY_PROTECTED,
            KEY_PUBLIC,
            KEY_INTERNAL,
            KEY_PRIVATE,
            KEY_THROWS,
            KEY_IF,
            KEY_ELSE,
            KEY_SWITCH,
            KEY_CASE,
            KEY_DEFAULT,
            KEY_DO,
            KEY_WHILE,
            KEY_FOR,
            KEY_FOREACH,
            KEY_VAR,
            KEY_IN,
            KEY_BREAK,
            KEY_CONTINUE,
            KEY_RETURN,
            KEY_YIELD,
            KEY_TRY,
            KEY_THROW,
            KEY_FINALLY,
            KEY_CATCH,
            KEY_LOCK,
            KEY_DELETE,
            KEY_BASE,
            KEY_THIS,
            KEY_NEW,
            KEY_OUT,
            KEY_INTERFACE,
            KEY_STRUCT,
            KEY_PARAMS,
            KEY_ENSURES,
            KEY_REQUIRES,
            KEY_ASYNC,
            KEY_INLINE,
            KEY_ABSTRACT,
            KEY_EXTERN,
            KEY_VIRTUAL,
            KEY_OVERRIDE,
            KEY_CONST,
            KEY_GET,
            KEY_SET,
            KEY_CONSTRUCT,
            KEY_SIGNAL,
            KEY_STATIC,
            KEY_TYPEOF,
            KEY_SIZEOF,
            KEY_OWNED,
            KEY_DYNAMIC,
            KEY_UNOWNED,
            KEY_WEAK,
            KEY_ERRORDOMAIN,
            KEY_ENUM,
            KEY_DELEGATE);

    public static final TokenSet BUILT_IN_TYPES = TokenSet.create(
            TYPE_VOID
    );

    public ValaLexer() {
        super(new FlexAdapter(new _ValaLexer()));
    }
}
