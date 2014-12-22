package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
                        ValaFile containingNamespace = PsiTreeUtil.getParentOfType(parameters.getOriginalPosition(), ValaFile.class, false);
                        if (containingNamespace != null) {
                            addResultsForClassesIn(result, containingNamespace);
                        }
                    }
                });
    }

    private static void addResultsForClassesIn(CompletionResultSet result, ValaFile containingNamespace) {
        for (ValaClassDeclaration declaration : containingNamespace.getDeclaredClasses()) {
            result.addElement(LookupElementBuilder.create(declaration.getName()));
        }
    }
}
