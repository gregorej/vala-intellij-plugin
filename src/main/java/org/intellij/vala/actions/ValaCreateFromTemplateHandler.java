package org.intellij.vala.actions;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.ValaLanguageFileType;

import java.util.Map;

import static org.intellij.vala.ValaLanguageFileType.DEFAULT_EXTENSION_WITH_DOT;

public class ValaCreateFromTemplateHandler extends DefaultCreateFromTemplateHandler {
    private Project project;

    public static final String VALA_GENERIC_SOURCE_FILE = "Generic Source File";

    @Override
    public PsiElement createFromTemplate(Project project, PsiDirectory directory, String fileName, FileTemplate template, String templateText, Map<String, Object> props) throws IncorrectOperationException {
        this.project = project;
        return super.createFromTemplate(project, directory, fileName, template, templateText, props);
    }

    @Override
    public boolean handlesTemplate(FileTemplate template) {
        return template.getName().equals(VALA_GENERIC_SOURCE_FILE + DEFAULT_EXTENSION_WITH_DOT);
    }

    @Override
    protected String checkAppendExtension(String fileName, FileTemplate template) {
        String extension = getExtension(template);
        final String suggestedFileNameEnd = "." + extension;

        if (!fileName.endsWith(suggestedFileNameEnd)) {
            fileName += suggestedFileNameEnd;
        }
        return fileName;
    }

    private String getExtension(FileTemplate template) {
        if (template.getName().equals(VALA_GENERIC_SOURCE_FILE + DEFAULT_EXTENSION_WITH_DOT)) return ValaLanguageFileType.DEFAULT_EXTENSION;
        return template.getExtension();
    }

}
