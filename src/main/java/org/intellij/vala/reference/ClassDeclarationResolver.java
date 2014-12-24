package org.intellij.vala.reference;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.psi.ClassNameIndex;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;

import java.util.List;

public class ClassDeclarationResolver {

    private Project project;

    public ClassDeclarationResolver(Project project) {
        this.project = project;
    }

    public ValaClassDeclaration resolve(String name) {
        return Iterables.getFirst(getAllClassesWithName(name), null);
    }

    private List<ValaClassDeclaration> getAllClassesWithName(String expectedName) {
        final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        final ClassNameIndex index = ClassNameIndex.getInstance();
        ImmutableList.Builder<ValaClassDeclaration> declarations = ImmutableList.builder();
        for (String name : index.getAllKeys(project)) {
            if (name.equals(expectedName)) {
                declarations.addAll(index.get(name, project, scope));
            }
        }
        return declarations.build();
    }
}
