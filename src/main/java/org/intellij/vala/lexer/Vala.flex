package org.intellij.vala.lexer;
import com.intellij.psi.TokenType;
import org.intellij.vala.ValaTypes;
import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

%%
%{

public ValaFlexLexer() {
    this((java.io.Reader)null);
  }
%}

%class ValaFlexLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

Digits         =[0-9]+
StringLiteral  =\"[^\"&]*\"
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
EqualTo        = \=
%%

<YYINITIAL> {
    {StringLiteral} { return ValaTypes.STRING_LITERAL; }
    {Digits} {return ValaTypes.INT; }
}