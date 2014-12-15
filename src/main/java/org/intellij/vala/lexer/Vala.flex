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

Digits         = [0-9]+
EqualTo        = \=
StringLiteral  = "\"" [^\"&]* "\""
Identifier     = [A-Za-z_][A-Za-z_0-9]*
LineTerminator = \r|\n|\r\n
WhiteSpace     = ({LineTerminator} | [ \t\f])+
%%

<YYINITIAL> {
    "class"                 {return ValaTypes.KEY_CLASS; }
    "ref"                   {return ValaTypes.KEY_REF; }
    {Identifier}            {return ValaTypes.IDENTIFIER; }
    {StringLiteral}         {return ValaTypes.STRING_LITERAL; }
    {Digits}                {return ValaTypes.INT; }
    {WhiteSpace}            {return TokenType.WHITE_SPACE; }
//    .                   {return TokenType.BAD_CHARACTER;}
}

.                       {return TokenType.BAD_CHARACTER;}