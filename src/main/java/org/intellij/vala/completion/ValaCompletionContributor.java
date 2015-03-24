package org.intellij.vala.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        extend(CompletionType.BASIC, withinInstanceCreation(),
                completeConstructorNames());
    }

    private static PsiElementPattern.Capture<PsiElement> withinInstanceCreation() {
        return psiElement().withSuperParent(3, psiElement(ValaTypes.OBJECT_OR_ARRAY_CREATION_EXPRESSION));
    }

    private void extendForVariables() {
        extend(CompletionType.BASIC, variableDeclaration(), completeClassNames());
    }

    private static PsiElementPattern.Capture<PsiElement> variableDeclaration() {
        return psiElement().withSuperParent(1, psiElement(ValaTypes.BLOCK));
        //return psiElement().inFile(instanceOf(ValaFile.class));
    }

    private CompletionProvider<CompletionParameters> completeClassNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                final String classNamePrefix = parameters.getOriginalPosition().getText();
                final List<ValaDeclaration> declarations = getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                addResultsForDeclarations(declarations.stream(), result);
            }
        };
    }

    private CompletionProvider<CompletionParameters> completeConstructorNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                final String classNamePrefix = parameters.getOriginalPosition().getText();
                final List<ValaDeclaration> typeDeclarations = getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                typeDeclarations.stream().flatMap(ValaCompletionContributor::collectConstructorLookups).forEach(result::addElement);
            }
        };
    }

    private static LookupElement constructorToLookupElement(ValaCreationMethodDeclaration constructor) {
        String constructorName = constructor.getName();
        String typeName = constructor.getTypeDeclaration().getQName().getTail();
        if (!constructorName.equals(typeName)) {
            constructorName = typeName + "." + constructorName;
        }
        return LookupElementBuilder.create(constructorName);
    }

    private static Stream<LookupElement> collectConstructorLookups(ValaDeclaration declarationContainer) {
        if (!(declarationContainer instanceof ValaDeclarationContainer)) {
            return Stream.empty();
        }
        List<LookupElement> constructorLookups = ((ValaDeclarationContainer) declarationContainer).getDeclarations()
                .stream()
                .filter(declaration -> declaration instanceof ValaCreationMethodDeclaration)
                .map(declaration -> constructorToLookupElement((ValaCreationMethodDeclaration) declaration))
                .collect(Collectors.toList());
        if (constructorLookups.isEmpty()) {
            return Stream.of(LookupElementBuilder.create(declarationContainer.getQName().getTail()));
        } else {
            return constructorLookups.stream();
        }
    }

    private static List<ValaDeclaration> getAllClassesWithNameStartingWith(PsiElement psiElement, String namePrefix) {
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

    private static void addResultsForDeclarations(Stream<ValaDeclaration> classes, CompletionResultSet resultSet) {
        classes.forEach(declaration ->
                        resultSet.addElement(LookupElementBuilder.create(declaration.getQName().getTail()))
        );
    }
}
