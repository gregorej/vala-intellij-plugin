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
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.impl.ValaPsiImplUtil;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.index.DeclarationsInNamespaceIndex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.not;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class ValaCompletionContributor extends CompletionContributor {

    private KeywordCollector keywordCollector = new KeywordCollector();

    public ValaCompletionContributor() {
        extendForKeywords();
        extendForConstructors();
        extendForVariables();
        extendForWithoutContext();
        extendForMethods();
        //extend(CompletionType.BASIC, isMemberAccess(), completeMethodNames());
    }

    private void extendForWithoutContext() {
        extend(CompletionType.BASIC, anythingOther(), completeClassNames());
    }

    private void extendForMethods() {
        extend(CompletionType.BASIC, withinPrimaryExpression(), completeMethodNames());
        extend(CompletionType.BASIC, withinSimpleName(), completeMethodNamesFromCurrentClass());
    }

    private static PsiElementPattern.Capture<PsiElement> withinSimpleName() {
        return psiElement().withSuperParent(2, psiElement(ValaTypes.SIMPLE_NAME));
    }

    private CompletionProvider<CompletionParameters> completeMethodNamesFromCurrentClass() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                Optional<ValaDeclaration> containingDeclaration = currentDeclaration(completionParameters.getPosition());

                containingDeclaration.ifPresent(declaration -> collectDelegates(declaration).forEach(delegateDeclaration ->
                        completionResultSet.addElement(lookupItem(delegateDeclaration.getParameters(), delegateDeclaration.getName()))));
            }
        };
    }

    private static Optional<ValaDeclaration> currentDeclaration(PsiElement psiElement) {
        return Optional.ofNullable(getParentOfType(psiElement, ValaTypeDeclaration.class));
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
        return psiElement().withSuperParent(4, psiElement(ValaTypes.OBJECT_OR_ARRAY_CREATION_EXPRESSION));
    }

    private static ElementPattern<PsiElement> withinPrimaryExpression() {
        return psiElement().withSuperParent(3, psiElement(ValaTypes.PRIMARY_EXPRESSION));
    }

    private static ElementPattern<PsiElement> isMemberAccess() {
        return psiElement().withSuperParent(2, psiElement(ValaTypes.MEMBER_ACCESS));
    }

    private void extendForVariables() {
        extend(CompletionType.BASIC, variableDeclaration(), completeClassNames());
    }

    private static ElementPattern<PsiElement> anythingOther() {
        return not(StandardPatterns.<PsiElement>or(withinInstanceCreation(), variableDeclaration(), isMemberAccess()));
    }

    private static ElementPattern<PsiElement> variableDeclaration() {
        return psiElement().withSuperParent(1, psiElement(ValaTypes.BLOCK));
    }

    private CompletionProvider<CompletionParameters> completeClassNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                final String classNamePrefix = parameters.getOriginalPosition().getText();
                getAllDeclarationsWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix)
                        .map(declaration -> LookupElementBuilder.create(declaration.getQName().getTail()))
                        .forEach(result::addElement);
            }
        };
    }

    private CompletionProvider<CompletionParameters> completeConstructorNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext processingContext, @NotNull CompletionResultSet result) {
                final ValaMemberPart memberPart = (ValaMemberPart) parameters.getPosition().getParent().getParent();
                final ValaMember member = (ValaMember) memberPart.getParent();
                String classNamePrefix = parameters.getOriginalPosition().getText();
                final int memberPartIndex = member.getMemberPartList().indexOf(memberPart);
                final boolean fullClassNamePresent = memberPartIndex > 0;
                if (fullClassNamePresent) {
                    classNamePrefix = member.getMemberPartList().get(memberPartIndex - 1).getText();
                }
                Function<ValaCreationMethodDeclaration, LookupElement> constructorToLookupElement = fullClassNamePresent
                        ? ValaCompletionContributor::constructorToLookupElementWithOnlyExplicitName
                        : ValaCompletionContributor::constructorToLookupElement;
                final Stream<ValaDeclaration> typeDeclarations = getAllDeclarationsWithNameStartingWith(parameters.getOriginalPosition(), classNamePrefix);
                typeDeclarations
                        .flatMap(declaration -> collectConstructorLookups(declaration, constructorToLookupElement))
                        .forEach(result::addElement);
            }
        };
    }

    private CompletionProvider<CompletionParameters> completeMethodNames() {
        return new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                final ValaMemberAccess memberAccess = (ValaMemberAccess) completionParameters.getPosition().getParent().getParent();
                final ValaPrimaryExpression primaryExpression = (ValaPrimaryExpression) memberAccess.getParent();
                Optional<ValaDeclaration> containingDeclaration = Optional.empty();
                int memberAccessIndex = primaryExpression.getChainAccessPartList().indexOf(memberAccess);

                final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
                final ValaSimpleExpression simpleExpression = (ValaSimpleExpression) primaryExpression.getExpression();
                if (memberAccessIndex > 0) {
                    ValaChainAccessPart previousPart = primaryExpression.getChainAccessPartList().get(memberAccessIndex - 1);
                    QualifiedName qualifiedName = previousPart.getTypeDescriptor().getQualifiedName();
                    if (qualifiedName != null) {
                        containingDeclaration = Optional.ofNullable(index.get(qualifiedName, memberAccess.getProject()));
                    }
                } else if (simpleExpression instanceof ValaSimpleName) {
                    ValaSimpleName simpleName = (ValaSimpleName) simpleExpression;
                    containingDeclaration = Optional.ofNullable(index.get(simpleName.getTypeDescriptor().getQualifiedName(), memberAccess.getProject()));
                } else if (simpleExpression instanceof ValaThisAccess) {
                    containingDeclaration = currentDeclaration(completionParameters.getPosition());
                }
                containingDeclaration.ifPresent(declaration -> collectDelegates(declaration).forEach(delegateDeclaration ->
                        completionResultSet.addElement(lookupItem(delegateDeclaration.getParameters(), delegateDeclaration.getName()))));
            }
        };
    }

    private static Stream<ValaDelegateDeclaration> collectDelegates(ValaDeclaration declaration) {
        Stream<ValaDelegateDeclaration> result = Stream.empty();
        if (declaration instanceof ValaTypeDeclaration) {
            ValaTypeDeclaration typeDeclaration = (ValaTypeDeclaration) declaration;
            result = typeDeclaration.getDelegates().stream();
        }
        if (declaration instanceof ValaTypeWithSuperTypes) {
            result = Stream.concat(result, ((ValaTypeWithSuperTypes) declaration).getSuperTypeDeclarations().stream().flatMap(ValaCompletionContributor::collectDelegates));
        }
        return result;
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
        return lookupItem(constructor.getParameters(), constructorName);
    }

    private static LookupElement lookupItem(ValaParameters parameters, String methodName) {
        final Template constructorTemplate = new TemplateImpl(methodName, "callable");
        constructorTemplate.addTextSegment(methodName);
        constructorTemplate.addTextSegment("(");
        if (parameters != null) {
            final List<ValaParameter> parameterList = parameters.getParameterList();
            for (int i = 0; i < parameterList.size(); i++) {
                ValaParameter parameter = parameterList.get(i);
                final String paramName = parameter.getName();
                constructorTemplate.addVariable(paramName, new TextExpression(paramName), true);
                if (i < parameterList.size() - 1) {
                    constructorTemplate.addTextSegment(", ");
                }
            }
        }
        constructorTemplate.addTextSegment(")");
        LookupItem<Template> item = LookupItem.fromString(methodName);
        item.setObject(constructorTemplate);
        item.setInsertHandler(new DefaultInsertHandler());
        item.setPresentableText(methodName);
        return item;
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
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

    private static Stream<ValaDeclaration> getAllDeclarationsWithNameStartingWith(PsiElement psiElement, String namePrefix) {
        final Project project = psiElement.getProject();
        final GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        final DeclarationsInNamespaceIndex index = DeclarationsInNamespaceIndex.getInstance();
        return ValaPsiImplUtil.getImportedNamespacesAvailableFor(psiElement).stream()
                .flatMap(importedName -> index.get(importedName, project, globalSearchScope).stream())
                .filter(declaration -> declaration.getQName().getTail().startsWith(namePrefix));
    }

}
