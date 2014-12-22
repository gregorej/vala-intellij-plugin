package org.intellij.vala.reference;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;

import java.util.List;

public class ClassDeclarationResolver {

    private PsiManager psiManager;
    private ProjectRootManager projectRootManager ;

    public ClassDeclarationResolver(Project project) {
        this.psiManager = PsiManager.getInstance(project);
        this.projectRootManager = ProjectRootManager.getInstance(project);
    }

    public ValaClassDeclaration resolve(String name) {
        ClassFindingIterator iterator = new ClassFindingIterator(psiManager, name);
        projectRootManager.getFileIndex().iterateContent(iterator);
        return iterator.getMatch();
    }

    private class ClassFindingIterator implements ContentIterator {

        private String name;

        public ClassFindingIterator(PsiManager psiManager, String name) {
            this.psiManager = psiManager;
            this.name = name;
        }

        private PsiManager psiManager;
        private ValaClassDeclaration match;

        @Override
        public boolean processFile(VirtualFile virtualFile) {
            PsiFile file = psiManager.findFile(virtualFile);
            if (file instanceof ValaFile) {
                match = findMatchIn((ValaFile) file, name);
            }
            return match == null;
        }

        public ValaClassDeclaration getMatch() {
            return match;
        }
    }

    private ValaClassDeclaration findMatchIn(ValaFile psiFile, String name) {
        for (ValaClassDeclaration classDeclaration : psiFile.getDeclaredClasses()) {
            if (classDeclaration.getSymbol().getText().equals(name)) {
                return classDeclaration;
            }
        }
        return null;
    }
}
