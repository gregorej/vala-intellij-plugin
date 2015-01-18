package org.intellij.vala.psi;


import com.intellij.lang.*;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.*;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import org.intellij.vala.ValaLanguage;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.intellij.vala.psi.ValaTypes.*;

public interface ValaTokenTypeSets {

    IFileElementType VALA_FILE = new IFileElementType("VALAFILE", ValaLanguage.INSTANCE);

    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    TokenSet METHOD_DECLARATIONS = TokenSet.create(
            METHOD_DECLARATION,
            CREATION_METHOD_DECLARATION,
            DESTRUCTOR_DECLARATION
    );

    TokenSet COMMENTS = TokenSet.create(
            LINE_COMMENT,
            BLOCK_COMMENT
    );

    TokenSet LOGIC_OPERATORS = TokenSet.create(
            OP_AND,
            OP_LOR
    );

    TokenSet BLOCKS = TokenSet.create(
            BLOCK,
            CLASS_MEMBER,
            VALA_FILE
    );

    TokenSet DECLARATIONS = TokenSet.create(
            CLASS_DECLARATION,
            METHOD_DECLARATION,
            CREATION_METHOD_DECLARATION,
            DESTRUCTOR_DECLARATION,
            ARGUMENTS
    );

}
