package org.intellij.vala.project;


import com.intellij.framework.FrameworkTypeEx;
import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable;
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.openapi.module.ModuleType;
import org.intellij.vala.project.template.ValaFrameworkType;
import org.intellij.vala.project.template.ValaSupportConfigurable;
import org.jetbrains.annotations.NotNull;

public class ValaSupportProvider extends FrameworkSupportInModuleProvider {

    @NotNull
    @Override
    public FrameworkTypeEx getFrameworkType() {
        return ValaFrameworkType.INSTANCE;
    }

    @NotNull
    @Override
    public FrameworkSupportInModuleConfigurable createConfigurable(@NotNull FrameworkSupportModel frameworkSupportModel) {
        return new ValaSupportConfigurable();
    }

    @Override
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return true;
    }
}
