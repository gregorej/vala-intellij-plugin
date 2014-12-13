package org.intellij.vala;


import com.intellij.lang.Language;

public class ValaLanguage extends Language {

    public static final ValaLanguage INSTANCE = new ValaLanguage();

    protected ValaLanguage() {
        super("Vala");
    }
}
