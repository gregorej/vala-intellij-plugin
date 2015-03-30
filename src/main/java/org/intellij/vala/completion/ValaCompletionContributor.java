package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TextExpression;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.patterns.PlatformPatterns.psiElement;
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
                getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix)
                        .map(declaration -> LookupElementBuilder.create(declaration.getQName().getTail()))
                        .forEach(result::addElement);
            }
        };
    }

    private CompletionProvider<CompletionParameters> completeConstructorNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                ValaMemberPart memberPart = (ValaMemberPart) parameters.getPosition().getParent();
                ValaMember member = (ValaMember) memberPart.getParent();
                String classNamePrefix = parameters.getOriginalPosition().getText();
                final int memberPartIndex = member.getMemberPartList().indexOf(memberPart);
                final boolean fullClassNamePresent = memberPartIndex > 0;
                if (fullClassNamePresent) {
                    classNamePrefix = member.getMemberPartList().get(memberPartIndex - 1).getText();
                }
                Function<ValaCreationMethodDeclaration, LookupElement> constructorToLookupElement = fullClassNamePresent
                        ? ValaCompletionContributor::constructorToLookupElementWithOnlyExplicitName
                        : ValaCompletionContributor::constructorToLookupElement;
                final Stream<ValaDeclaration> typeDeclarations = getAllClassesWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                typeDeclarations
                        .flatMap(declaration -> collectConstructorLookups(declaration, constructorToLookupElement))
                        .forEach(result::addElement);
            }
        };
    }

    private static LookupElement constructorToLookupElement(ValaCreationMethodDeclaration constructor) {
        String constructorName = constructor.getName();
        String typeName = constructor.getTypeDeclaration().getQName().getTail();
        String fullConstructorName = constructorName;
        if (!constructorName.equals(typeName)) {
            fullConstructorName = typeName + "." + constructorName;
        }
        return lookupItem(constructor, fullConstructorName);
    }

    private static LookupElement lookupItem(ValaCreationMethodDeclaration constructor, String constructorName) {
        final Template constructorTemplate = new TemplateImpl(constructorName, "constructors");
        constructorTemplate.addTextSegment(constructorName);
        constructorTemplate.addTextSegment("(");
        if (constructor.getParameters() != null) {
            final List<ValaParameter> parameters = constructor.getParameters().getParameterList();
            for (int i = 0; i < parameters.size(); i++) {
                ValaParameter parameter = parameters.get(i);
                final String paramName = parameter.getName();
                constructorTemplate.addVariable(paramName, new TextExpression(paramName), true);
                if (i < parameters.size() - 1) {
                    constructorTemplate.addTextSegment(", ");
                }
            }
        }
        constructorTemplate.addTextSegment(")");
        LookupItem<Template> item = LookupItem.fromString(constructorName);
        item.setObject(constructorTemplate);
        item.setInsertHandler(new DefaultInsertHandler());
        return item;
    }

    private static LookupElement constructorToLookupElementWithOnlyExplicitName(ValaCreationMethodDeclaration constructor) {
        return lookupItem(constructor, constructor.getName());
    }

    private static Stream<LookupElement> collectConstructorLookups(ValaDeclaration declarationContainer, Function<ValaCreationMethodDeclaration, LookupElement> constructorToLookupElement) {
        if (!(declarationContainer instanceof ValaDeclarationContainer)) {
            return Stream.empty();
        }
        final List<LookupElement> constructorLookups = ((ValaDeclarationContainer) declarationContainer).getDeclarations()
                .stream()
                .filter(declaration -> declaration instanceof ValaCreationMethodDeclaration)
                .map(declaration -> constructorToLookupElement.apply((ValaCreationMethodDeclaration) declaration))
                .collect(Collectors.toList());
        if (constructorLookups.isEmpty()) {
            return Stream.of(LookupElementBuilder.create(declarationContainer.getQName().getTail())
                    .withInsertHandler(new DefaultConstructorInsertHandler()));
        } else {
            return constructorLookups.stream();
        }
    }

    private static Stream<ValaDeclaration> getAllClassesWithNameStartingWith(PsiElement psiElement, String namePrefix) {
        final Project project = psiElement.getProject();
        final GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        final DeclarationsInNamespaceIndex index = DeclarationsInNamespaceIndex.getInstance();
        return ValaPsiImplUtil.getImportedNamespacesAvailableFor(psiElement).stream()
                .flatMap(importedName -> index.get(importedName, project, globalSearchScope).stream())
                .filter(declaration -> declaration.getQName().getTail().startsWith(namePrefix));
    }

}
