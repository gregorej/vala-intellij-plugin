package org.intellij.vala;

import com.intellij.psi.tree.IElementType;

public interface ValaTypes {

    IElementType GT = new IElementType("greater", ValaLanguage.INSTANCE);
    IElementType LT = new IElementType("lesser", ValaLanguage.INSTANCE);
    IElementType STRING_LITERAL = new IElementType("string literal", ValaLanguage.INSTANCE);
    IElementType EQ = new IElementType("equal to", ValaLanguage.INSTANCE);
    IElementType ASGN = new IElementType("assignment", ValaLanguage.INSTANCE);
    IElementType INT = new IElementType("integer", ValaLanguage.INSTANCE);
    IElementType KEY_CLASS = new IElementType("class keyword", ValaLanguage.INSTANCE);
    IElementType KEY_REF = new IElementType("ref keyword", ValaLanguage.INSTANCE);
    IElementType IDENTIFIER = new IElementType("Identifier", ValaLanguage.INSTANCE);
}
