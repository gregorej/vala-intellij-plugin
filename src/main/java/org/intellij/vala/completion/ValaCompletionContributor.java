package org.intellij.vala.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.instanceOf;

public class ValaCompletionContributor extends CompletionContributor {

    private KeywordCollector keywordCollector = new KeywordCollector();

    public ValaCompletionContributor() {
        extendForKeywords();
        extendForConstructors();
        extendForVariables();
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
                completeConstructorNames());
    }

    private void extendForVariables() {
        extend(CompletionType.BASIC, psiElement().inFile(instanceOf(ValaFile.class)),
                completeConstructorNames());
    }

    private CompletionProvider<CompletionParameters> completeConstructorNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                final String classNamePrefix = parameters.getOriginalPosition().getText();
                final List<ValaDeclaration> declarations = getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                addResultsForClasses(declarations, result);
            }
        };
    }

    private List<ValaDeclaration> getAllClassesWithNameStartingWith(PsiElement psiElement, String namePrefix) {
        final Project project = psiElement.getProject();
        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        DeclarationsInNamespaceIndex index = DeclarationsInNamespaceIndex.getInstance();
        ImmutableList.Builder<ValaDeclaration> declarations = ImmutableList.builder();
        for (QualifiedName importedName : ValaPsiImplUtil.getImportedNamespacesAvailableFor(psiElement)) {
            for (ValaDeclaration foundDeclaration: index.get(importedName, project, globalSearchScope)) {
                if (foundDeclaration.getQName().getTail().startsWith(namePrefix)) {
                    declarations.add(foundDeclaration);
                }
            }
        }
        return declarations.build();
    }

    private static void addResultsForClasses(Iterable<ValaDeclaration> classes, CompletionResultSet resultSet) {
        for (ValaDeclaration declaration : classes) {
            resultSet.addElement(LookupElementBuilder.create(declaration.getQName().getTail()));
        }
    }
}
