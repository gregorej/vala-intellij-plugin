package org.intellij.vala.psi;


import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.ValaLanguageFileType;

public final class ValaElementFactory {

    private ValaElementFactory() {
    }

    public static ValaFile createFile(Project project, String content) {
        String name = "dummy.vala";
        return (ValaFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, ValaLanguageFileType.INSTANCE, content);
    }

    public static ValaIdentifier createIdentifier(Project project, String name) {
        String content = "void " + name + "();";
        ValaFile file = createFile(project, content);
        return PsiTreeUtil.findChildOfType(file, ValaIdentifier.class);
    }
}
