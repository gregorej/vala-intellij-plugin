package org.intellij.vala.project.template;


import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import org.intellij.vala.ValaIcons;
import org.intellij.vala.project.ValaSupportProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ValaFrameworkType extends FrameworkTypeEx {
    private ValaFrameworkType() {
        super("Vala");
    }

    public static final ValaFrameworkType INSTANCE = new ValaFrameworkType();

    @NotNull
    @Override
    public FrameworkSupportInModuleProvider createProvider() {
        return new ValaSupportProvider();
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Vala";
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return ValaIcons.FILE;
    }
}
