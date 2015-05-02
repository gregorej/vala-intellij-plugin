package org.intellij.vala.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.intellij.vala.ValaLanguageFileType;
import org.intellij.vala.lexer.ValaLexer;
import org.intellij.vala.psi.ValaElementType;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

public class ValaParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);

    public static final TokenSet COMMENTS = TokenSet.create(ValaTypes.BLOCK_COMMENT, ValaTypes.LINE_COMMENT);

    public static final TokenSet STRINGS = TokenSet.create(ValaTypes.STRING_LITERAL);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ValaLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new ValaParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return ValaLanguageFileType.FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode astNode) {
        return ValaTypes.Factory.createElement(astNode);
    }

    @NotNull
    public static IElementType createElementType(String string) {
        if ("CLASS_DECLARATION".equals(string)) {
            return new ValaClassDeclarationStubElementType();
        } else if ("CREATION_METHOD_DECLARATION".equals(string)) {
            return new ValaCreationMethodDeclarationStubElementType();
        }
        else if ("INTERFACE_DECLARATION".equals(string)) {
            return new ValaInterfaceDeclarationStubElementType();
        } else {
            return new ValaElementType(string);
        }
    }

    @Override
    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new ValaFile(fileViewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
        return SpaceRequirements.MAY;
    }
}
