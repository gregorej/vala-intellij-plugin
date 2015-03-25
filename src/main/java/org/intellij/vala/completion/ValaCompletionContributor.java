package org.intellij.vala.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.StandardPatterns;
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
import static com.intellij.patterns.StandardPatterns.not;

public class ValaCompletionContributor extends CompletionContributor {

    private KeywordCollector keywordCollector = new KeywordCollector();

    public ValaCompletionContributor() {
        extendForKeywords();
        extendForConstructors();
        extendForVariables();
        extendForWithoutContext();
    }

    private void extendForWithoutContext() {
        extend(CompletionType.BASIC, anythingOther(), completeClassNames());
    }

    private void extendForKeywords() {
        extend(CompletionType.BASIC, anythingOther(),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        result.addAllElements(keywordCollector.getProposedLookUpItems());
                    }
                });
    }

    private void extendForConstructors() {
        extend(CompletionType.BASIC, withinInstanceCreation(), completeConstructorNames());
    }

    private static ElementPattern<PsiElement> withinInstanceCreation() {
        return psiElement().withSuperParent(3, psiElement(ValaTypes.OBJECT_OR_ARRAY_CREATION_EXPRESSION));
    }

    private void extendForVariables() {
        extend(CompletionType.BASIC, variableDeclaration(), completeClassNames());
    }

    private static ElementPattern<PsiElement> anythingOther() {
        return not(StandardPatterns.<PsiElement>or(withinInstanceCreation(), variableDeclaration()));
    }

    private static ElementPattern<PsiElement> variableDeclaration() {
        return psiElement().withSuperParent(1, psiElement(ValaTypes.BLOCK));
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
                ValaMemberPart memberPart = (ValaMemberPart) parameters.getOriginalPosition().getParent();
                ValaMember member = (ValaMember) memberPart.getParent();
                String classNamePrefix = memberPart.getText();
                final int memberPartIndex = member.getMemberPartList().indexOf(memberPart);
                boolean onlyExplicitName = false;
                if (memberPartIndex > 0) {
                    classNamePrefix = member.getMemberPartList().get(memberPartIndex - 1).getText();
                    onlyExplicitName = true;
                }
                final boolean finalFlag = onlyExplicitName;
                final List<ValaDeclaration> typeDeclarations = getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                typeDeclarations.stream()
                        .flatMap(constructor -> collectConstructorLookups(constructor, finalFlag))
                        .forEach(result::addElement);
            }
        };
    }

    private static LookupElement constructorToLookupElement(ValaCreationMethodDeclaration constructor, boolean onlyExplicitName) {
        String constructorName = constructor.getName();
        String typeName = constructor.getTypeDeclaration().getQName().getTail();
        String fullConstructorName = constructorName;
        if (!constructorName.equals(typeName) && !onlyExplicitName) {
            fullConstructorName = typeName + "." + constructorName;
        }
        LookupElementBuilder builder = LookupElementBuilder.create(fullConstructorName);
        return builder;
    }

    private static Stream<LookupElement> collectConstructorLookups(ValaDeclaration declarationContainer, boolean onlyExplicitName) {
        if (!(declarationContainer instanceof ValaDeclarationContainer)) {
            return Stream.empty();
        }
        final List<LookupElement> constructorLookups = ((ValaDeclarationContainer) declarationContainer).getDeclarations()
                .stream()
                .filter(declaration -> declaration instanceof ValaCreationMethodDeclaration)
                .map(declaration -> constructorToLookupElement((ValaCreationMethodDeclaration) declaration, onlyExplicitName))
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
