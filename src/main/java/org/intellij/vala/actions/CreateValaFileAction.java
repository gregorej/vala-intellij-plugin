package org.intellij.vala.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import org.intellij.vala.ValaIcons;

import static org.intellij.vala.ValaLanguageFileType.DEFAULT_EXTENSION_WITH_DOT;
import static org.intellij.vala.actions.ValaCreateFromTemplateHandler.VALA_GENERIC_SOURCE_FILE;


public class CreateValaFileAction extends CreateFileFromTemplateAction implements DumbAware {

    public static final String NEW_VALA_FILE = "New Vala File";

    public CreateValaFileAction() {
        super(NEW_VALA_FILE, "", ValaIcons.FILE);
    }

    @Override
    protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
        builder
                .setTitle(NEW_VALA_FILE)
                .addKind("Source file", ValaIcons.FILE, VALA_GENERIC_SOURCE_FILE + DEFAULT_EXTENSION_WITH_DOT)
                .setValidator(getValidator());
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s, String s1) {
        return NEW_VALA_FILE;
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof CreateValaFileAction;
    }

    private InputValidatorEx getValidator() {
        return new InputValidatorEx() {
            @Override
            public boolean checkInput(String inputString) {
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
            }

            @Override
            public String getErrorText(String inputString) {
                String error = " is not a valid Vala file name";
                if (StringUtil.isEmpty(inputString)) return null;
                if (inputString.equals(FileUtil.sanitizeFileName(inputString))) {
                    return null;
                }
                return "'" + inputString + "'" + error;
            }
        };
    }
}
