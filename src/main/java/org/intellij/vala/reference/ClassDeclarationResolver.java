package org.intellij.vala.reference;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.intellij.vala.psi.ClassNameIndex;
import org.intellij.vala.psi.ValaClassDeclaration;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class ClassDeclarationResolver {

    private Project project;

    public ClassDeclarationResolver(Project project) {
        this.project = project;
    }

    public ValaClassDeclaration resolve(String name) {
        return Iterables.getFirst(getAllClassesWithName(name), null);
    }

    public List<ValaClassDeclaration> getAllClassesWithNameStartingWith(String namePrefix) {
        return getAllClassesWithNameThat(Matchers.startsWith(namePrefix));
    }

    private List<ValaClassDeclaration> getAllClassesWithName(String expectedName) {
        return getAllClassesWithNameThat(equalTo(expectedName));
    }

    private List<ValaClassDeclaration> getAllClassesWithNameThat(Matcher<String> matches) {
        final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        final ClassNameIndex index = ClassNameIndex.getInstance();
        ImmutableList.Builder<ValaClassDeclaration> declarations = ImmutableList.builder();
        for (String name : index.getAllKeys(project)) {
            if (matches.matches(name)) {
                declarations.addAll(index.get(name, project, scope));
            }
        }
        return declarations.build();
    }
}
