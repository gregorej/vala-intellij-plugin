package org.intellij.vala.formatter;

import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.WrappingUtil;
import com.intellij.psi.tree.IElementType;

import org.intellij.vala.psi.ValaTypes;

/**
 * @author: Fedor.Korotkov
 */
public class ValaWrappingProcessor {
  private final ASTNode myNode;
  private final CommonCodeStyleSettings mySettings;

  public ValaWrappingProcessor(ASTNode node, CommonCodeStyleSettings settings) {
    myNode = node;
    mySettings = settings;
  }

  Wrap createChildWrap(ASTNode child, Wrap defaultWrap, Wrap childWrap) {
    final IElementType childType = child.getElementType();
    final IElementType elementType = myNode.getElementType();
    if (childType == ValaTypes.COMA || childType == ValaTypes.SEMICOLON) return defaultWrap;

    //
    // Function definition/call
    //
    if (elementType == ValaTypes.ARGUMENTS) {
      if (mySettings.CALL_PARAMETERS_WRAP != CommonCodeStyleSettings.DO_NOT_WRAP) {
        if (myNode.getFirstChildNode() == child) {
          return createWrap(mySettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE);
        }
        if (!mySettings.PREFER_PARAMETERS_WRAP && childWrap != null) {
          return Wrap.createChildWrap(childWrap, WrappingUtil.getWrapType(mySettings.CALL_PARAMETERS_WRAP), true);
        }
        return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.CALL_PARAMETERS_WRAP), true);
      }
    }

    if (elementType == ValaTypes.PARAMETERS) {
      if (mySettings.METHOD_PARAMETERS_WRAP != CommonCodeStyleSettings.DO_NOT_WRAP) {
        if (myNode.getFirstChildNode() == child) {
          return createWrap(mySettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE);
        }
        if (childType == ValaTypes.RIGHT_PAREN) {
          return createWrap(mySettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE);
        }
        return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.METHOD_PARAMETERS_WRAP), true);
      }
    }

    if (elementType == ValaTypes.METHOD_CALL) {
      if (mySettings.CALL_PARAMETERS_WRAP != CommonCodeStyleSettings.DO_NOT_WRAP) {
        if (childType == ValaTypes.RIGHT_PAREN) {
          return createWrap(mySettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE);
        }
      }
    }

    //
    // If
    //
    if (elementType == ValaTypes.IF_STATEMENT && childType == ValaTypes.KEY_ELSE) {
      return createWrap(mySettings.ELSE_ON_NEW_LINE);
    }

    //
    //Binary expressions
    //
//    if (BINARY_EXPRESSIONS.contains(elementType) && mySettings.BINARY_OPERATION_WRAP != CommonCodeStyleSettings.DO_NOT_WRAP) {
//      if ((mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE && BINARY_OPERATORS.contains(childType)) ||
//          (!mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE && isRightOperand(child))) {
//        return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.BINARY_OPERATION_WRAP), true);
//      }
//    }

    //
    // Assignment
    //
//    if (elementType == ValaTypes.ASS && mySettings.ASSIGNMENT_WRAP != CommonCodeStyleSettings.DO_NOT_WRAP) {
//      if (childType != ASSIGNMENT_OPERATOR) {
//        if (FormatterUtil.isPrecededBy(child, ASSIGNMENT_OPERATOR) &&
//            mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE) {
//          return Wrap.createWrap(WrapType.NONE, true);
//        }
//        return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.ASSIGNMENT_WRAP), true);
//      }
//      else if (mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE) {
//        return Wrap.createWrap(WrapType.NORMAL, true);
//      }
//    }

    //
    // Ternary expressions
    //
//    if (elementType == ValaTypes.TERNARY_EXPRESSION) {
//      if (myNode.getFirstChildNode() != child) {
//        if (mySettings.TERNARY_OPERATION_SIGNS_ON_NEXT_LINE) {
//          if (!FormatterUtil.isPrecededBy(child, QUEST) &&
//              !FormatterUtil.isPrecededBy(child, COLON)) {
//            return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.TERNARY_OPERATION_WRAP), true);
//          }
//        }
//        else if (childType != QUEST && childType != COLON) {
//          return Wrap.createWrap(WrappingUtil.getWrapType(mySettings.TERNARY_OPERATION_WRAP), true);
//        }
//      }
//      return Wrap.createWrap(WrapType.NONE, true);
//    }
    return defaultWrap;
  }

  private boolean isRightOperand(ASTNode child) {
    return myNode.getLastChildNode() == child;
  }

  private static Wrap createWrap(boolean isNormal) {
    return Wrap.createWrap(isNormal ? WrapType.NORMAL : WrapType.NONE, true);
  }

  private static Wrap createChildWrap(ASTNode child, int parentWrap, boolean newLineAfterLBrace, boolean newLineBeforeRBrace) {
    IElementType childType = child.getElementType();
    if (childType != ValaTypes.LEFT_PAREN && childType != ValaTypes.RIGHT_PAREN) {
      if (FormatterUtil.isPrecededBy(child, ValaTypes.LEFT_SQUARE)) {
        if (newLineAfterLBrace) {
          return Wrap.createChildWrap(Wrap.createWrap(parentWrap, true), WrapType.ALWAYS, true);
        }
        else {
          return Wrap.createWrap(WrapType.NONE, true);
        }
      }
      return Wrap.createWrap(WrappingUtil.getWrapType(parentWrap), true);
    }
    if (childType == ValaTypes.RIGHT_SQUARE && newLineBeforeRBrace) {
      return Wrap.createWrap(WrapType.ALWAYS, true);
    }
    return Wrap.createWrap(WrapType.NONE, true);
  }
}
