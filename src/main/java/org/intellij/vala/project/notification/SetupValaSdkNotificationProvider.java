package org.intellij.vala.project.notification;

import com.intellij.ProjectTopics;
import com.intellij.framework.addSupport.impl.AddSupportForSingleFrameworkDialog;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.project.ValaModuleUtil;
import org.intellij.vala.project.ValaSupportProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetupValaSdkNotificationProvider extends EditorNotifications.Provider<EditorNotificationPanel> {

    private Project project;

    public SetupValaSdkNotificationProvider(Project project, EditorNotifications notifications) {
        this.project = project;
        project.getMessageBus().connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter()  {
            @Override
            public void rootsChanged(ModuleRootEvent event) {
                notifications.updateAllNotifications();
            }
        });
    }

    private static final Key<EditorNotificationPanel> KEY = Key.create("Setup Vala SDK");

    @NotNull
    @Override
    public Key<EditorNotificationPanel> getKey() {
        return KEY;
    }

    @Nullable
    @Override
    public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor) {
        final PsiManager psiManager = PsiManager.getInstance(project);
        PsiFile psiFile = psiManager.findFile(virtualFile);
        if (!hasSdk(psiFile)) {
            return createPanel(psiFile);
        }
        return null;
    }

    private boolean hasSdk(PsiFile psiFile) {
        if (psiFile.getLanguage() == ValaLanguage.INSTANCE) {
            Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
            return module == null || hasVala(module);
        } else {
            return true;
        }
    }

    private boolean hasVala(Module module) {
        return ValaModuleUtil.getValaSdk(module) != null;
    }

    private static EditorNotificationPanel createPanel(final PsiFile psiFile) {
        final EditorNotificationPanel panel = new EditorNotificationPanel();
        panel.setText("No Vala SDK in module");
        panel.createActionLabel("Setup Vala SDK", () -> setupSdk(panel, psiFile));
        return panel;
    }

    private static void setupSdk(EditorNotificationPanel panel, PsiFile psiFile) {
        Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
        if (module != null) {
            DialogWrapper dialog = AddSupportForSingleFrameworkDialog.createDialog(module, new ValaSupportProvider());
            dialog.showAndGet();
        }
    }
}
