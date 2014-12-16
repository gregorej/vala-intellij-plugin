package org.intellij.vala.psi;


import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.ValaLanguage;

public class ValaElementType extends IElementType {
    public ValaElementType(String debugName) {
        super(debugName, ValaLanguage.INSTANCE);
    }
}
