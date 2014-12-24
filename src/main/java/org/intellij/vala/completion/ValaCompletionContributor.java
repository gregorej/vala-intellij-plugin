package org.intellij.vala.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.intellij.vala.psi.ClassNameIndex;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.instanceOf;

public class ValaCompletionContributor extends CompletionContributor {

    private KeywordCollector keywordCollector = new KeywordCollector();

    public ValaCompletionContributor() {
        extendForKeywords();
        extendForConstructors();
    }

    private void extendForKeywords() {
        extend(CompletionType.BASIC, psiElement().inFile(instanceOf(ValaFile.class)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        result.addAllElements(keywordCollector.getProposedLookUpItems());
                    }
                });
    }

    private void extendForConstructors() {
        extend(CompletionType.BASIC, psiElement().withSuperParent(3, psiElement(ValaTypes.OBJECT_OR_ARRAY_CREATION_EXPRESSION)),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                        final Project project = parameters.getOriginalFile().getProject();
                        final String classNamePrefix = parameters.getOriginalPosition().getText();
                        final List<ValaClassDeclaration> declarations = getAllClassesWithNameStartingWith(project, classNamePrefix);
                        addResultsForClasses(declarations, result);
                    }
                });
    }

    private List<ValaClassDeclaration> getAllClassesWithNameStartingWith(Project project, String namePrefix) {
        final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        final ClassNameIndex index = ClassNameIndex.getInstance();
        ImmutableList.Builder<ValaClassDeclaration> declarations = ImmutableList.builder();
        for (String name : index.getAllKeys(project)) {
            if (name.startsWith(namePrefix)) {
                declarations.addAll(index.get(name, project, scope));
            }
        }
        return declarations.build();
    }

    private static void addResultsForClasses(Iterable<ValaClassDeclaration> classes, CompletionResultSet resultSet) {
        for (ValaClassDeclaration declaration : classes) {
            resultSet.addElement(LookupElementBuilder.create(declaration.getName()));
        }
    }
}
