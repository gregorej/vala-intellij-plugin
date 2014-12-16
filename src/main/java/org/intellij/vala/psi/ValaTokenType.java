package org.intellij.vala.psi;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.ValaLanguage;


public class ValaTokenType extends IElementType {
    public ValaTokenType(String debugName) {
        super(debugName, ValaLanguage.INSTANCE);
    }
}
