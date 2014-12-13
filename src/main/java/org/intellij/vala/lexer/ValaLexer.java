package org.intellij.vala.lexer;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;


public class ValaLexer extends LookAheadLexer {
    public ValaLexer() {
        super(new FlexAdapter(new ValaFlexLexer()));
    }
}
