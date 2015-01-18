package org.intellij.vala.formatter;

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.util.UsefulPsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import static org.intellij.vala.psi.ValaTypes.*;
import static org.intellij.vala.psi.ValaTokenTypeSets.*;

public class ValaIndentProcessor {
    private final CommonCodeStyleSettings settings;

    public ValaIndentProcessor(CommonCodeStyleSettings settings) {
        this.settings = settings;
    }

    public Indent getChildIndent(final ASTNode node) {
        final IElementType elementType = node.getElementType();
        final ASTNode prevSibling = UsefulPsiTreeUtil.getPrevSiblingSkipWhiteSpacesAndComments(node);
        final IElementType prevSiblingType = prevSibling == null ? null : prevSibling.getElementType();
        final ASTNode parent = node.getTreeParent();
        final IElementType parentType = parent != null ? parent.getElementType() : null;
        final ASTNode superParent = parent == null ? null : parent.getTreeParent();
        final IElementType superParentType = superParent == null ? null : superParent.getElementType();

        final int braceStyle = METHOD_DECLARATIONS.contains(superParentType) ?
                settings.METHOD_BRACE_STYLE : settings.BRACE_STYLE;

        if (parent == null || parent.getTreeParent() == null || parentType == EMBEDDED_STATEMENT) {
            return Indent.getNoneIndent();
        }

//        if (elementType == BLOCK_COMMENT) {
//            return Indent.getContinuationIndent();
//        }
//    if (elementType == DOC_COMMENT_LEADING_ASTERISK || elementType == MULTI_LINE_COMMENT_END) {
//      return Indent.getSpaceIndent(1, true);
//    }

        if (settings.KEEP_FIRST_COLUMN_COMMENT && (elementType == LINE_COMMENT)) {
            final ASTNode previousNode = node.getTreePrev();
            if (previousNode != null && previousNode.getElementType() == WHITE_SPACE && previousNode.getText().endsWith("\n")) {
                return Indent.getAbsoluteNoneIndent();
            }
        }

        if (COMMENTS.contains(elementType) && prevSiblingType == LEFT_CURLY && parentType == CLASS_BODY) {
            return Indent.getNormalIndent();
        }
        if (elementType == LEFT_CURLY || elementType == RIGHT_CURLY) {
            switch (braceStyle) {
                case CommonCodeStyleSettings.END_OF_LINE:
                case CommonCodeStyleSettings.NEXT_LINE:
                case CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED:
                    return Indent.getNoneIndent();
                case CommonCodeStyleSettings.NEXT_LINE_SHIFTED:
                case CommonCodeStyleSettings.NEXT_LINE_SHIFTED2:
                    return Indent.getNormalIndent();
                default:
                    return Indent.getNoneIndent();
            }
        }
//    if (parentType == PARENTHESIZED_EXPRESSION) {
//      if (elementType == LPAREN || elementType == RPAREN) {
//        return Indent.getNoneIndent();
//      }
//      return Indent.getContinuationIndent();
//    }
//    if (parentType == MAP_LITERAL_EXPRESSION) {
//      if (elementType == LBRACE || elementType == RBRACE) {
//        return Indent.getNoneIndent();
//      }
//      return Indent.getContinuationIndent();
//    }
//    if (parentType == LIST_LITERAL_EXPRESSION) {
//      if (elementType == LBRACKET || elementType == RBRACKET) {
//        return Indent.getNoneIndent();
//      }
//      return Indent.getContinuationIndent();
//    }
        if (elementType == CLASS_MEMBER) {
            return Indent.getNormalIndent();
        }
        if (elementType == NAMESPACE_MEMBER) {
            return Indent.getNormalIndent();
        }
        if (needIndent(parentType)) {
            final PsiElement psi = node.getPsi();
            if (psi.getParent() instanceof PsiFile) {
                return Indent.getNoneIndent();
            }
            return Indent.getNormalIndent();
        }
        if (parentType == ARGUMENTS) {
            return Indent.getContinuationIndent();
        }
//    if (parentType == FORMAL_PARAMETER_LIST) {
//      return Indent.getContinuationIndent();
//    }
        if (parentType == FOR_STATEMENT && prevSiblingType == FOR_LOOP_PARTS_IN_BRACES && elementType != BLOCK) {
          return Indent.getNormalIndent();
        }
        if (parentType == SWITCH_STATEMENT && prevSiblingType == RIGHT_PAREN) {
            return Indent.getNormalIndent();
        }
        if (superParentType == SWITCH_STATEMENT && parentType == STATEMENT) {
            return Indent.getNormalIndent();
        }
//    if (superParentType == DEFAULT_CASE && parentType == STATEMENT) {
//      return Indent.getNormalIndent();
//    }
        if (parentType == WHILE_STATEMENT && prevSiblingType == RIGHT_PAREN && elementType != BLOCK) {
            return Indent.getNormalIndent();
        }
        if (parentType == DO_STATEMENT && prevSiblingType == KEY_DO && elementType != BLOCK) {
            return Indent.getNormalIndent();
        }
        if ((parentType == RETURN_STATEMENT) &&
                prevSiblingType == KEY_RETURN &&
                elementType != BLOCK) {
            return Indent.getNormalIndent();
        }
        if (parentType == IF_STATEMENT && (prevSiblingType == RIGHT_PAREN || prevSiblingType == KEY_ELSE) && elementType != BLOCK) {
            return Indent.getNormalIndent();
        }
//    if (prevSiblingType != DOT && elementType == DOT && parentType == CASCADE_REFERENCE_EXPRESSION) {
//      return Indent.getNormalIndent();
//    }
        return Indent.getNoneIndent();
    }

    private static boolean needIndent(@Nullable IElementType type) {
        if (type == null) {
            return false;
        }
        boolean result = type == BLOCK;
        result = result || type == SWITCH_SECTION;
        return result;
    }
}
