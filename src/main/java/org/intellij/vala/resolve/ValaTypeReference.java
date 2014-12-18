package org.intellij.vala.resolve;

import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaTypeWeak;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ValaTypeReference extends PsiReferenceBase<ValaTypeWeak> {

    public ValaTypeReference(@NotNull ValaTypeWeak element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
        ProjectRootManager projectRootManager = ProjectRootManager.getInstance(myElement.getProject());
        ClassFindingIterator iterator = new ClassFindingIterator(psiManager);
        projectRootManager.getFileIndex().iterateContent(iterator);
        return iterator.getMatch();
    }

    private class ClassFindingIterator implements ContentIterator {

        public ClassFindingIterator(PsiManager psiManager) {
            this.psiManager = psiManager;
        }

        private PsiManager psiManager;
        private PsiElement match;

        @Override
        public boolean processFile(VirtualFile virtualFile) {
            PsiFile file = psiManager.findFile(virtualFile);
            if (file instanceof ValaFile) {
                match = findMatchIn(file);
            }
            return match == null;
        }

        public PsiElement getMatch() {
            return match;
        }
    }

    private PsiElement findMatchIn(PsiFile psiFile) {
        List<ValaClassDeclaration> classDeclarations = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, ValaClassDeclaration.class);
        for (ValaClassDeclaration classDeclaration : classDeclarations) {
            if (classDeclaration.getSymbol().getText().equals(myElement.getSymbol().getText())) {
                return classDeclaration;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
