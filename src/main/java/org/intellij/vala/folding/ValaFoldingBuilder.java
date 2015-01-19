package org.intellij.vala.folding;

import com.intellij.psi.PsiComment;
import org.intellij.vala.psi.*;
import org.intellij.vala.ValaComponentType;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.CustomFoldingBuilder;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.openapi.editor.Document;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ValaFoldingBuilder extends CustomFoldingBuilder {

    protected void buildLanguageFoldRegions(@NotNull final List<FoldingDescriptor> descriptors,
                                            @NotNull final PsiElement root,
                                            @NotNull final Document document,
                                            final boolean quick) {
        if (!(root instanceof ValaFile)) return;

        final ValaFile valaFile = (ValaFile)root;

        foldComments(descriptors, root);
        foldStatements(descriptors, root);
        
        foldNamespaceBodies(descriptors, root);
        foldClassBodies(descriptors, valaFile);
        foldFunctionBodies(descriptors, root);
    }

    public String getLanguagePlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
        final IElementType elementType = node.getElementType();
        final PsiElement psiElement = node.getPsi();

        if(psiElement instanceof ValaStatement)
            return "{...}";

        if (elementType == ValaTypes.BLOCK_COMMENT) {
            return "/*...*/";
        }

        if (psiElement instanceof ValaNamespaceDeclaration)
            return "{...}";
        if (psiElement instanceof ValaClassBody)
            return "{...}";
        if (psiElement instanceof ValaMethodDeclaration ||
                psiElement instanceof ValaCreationMethodDeclaration) return "{...}";

        return "...";
    }

    public boolean isRegionCollapsedByDefault(@NotNull final ASTNode node) {
        //TODO: get this data from settings

        return false;
    }



    private static void foldComments(List<FoldingDescriptor> descriptors, PsiElement root) {
        for (PsiComment valaComment : PsiTreeUtil.findChildrenOfAnyType(root, PsiComment.class)) {
            if (valaComment != null && valaComment.getTokenType() == ValaTypes.BLOCK_COMMENT) {
                descriptors.add(new FoldingDescriptor(valaComment, valaComment.getTextRange()));
            }
        }
    }

    private static void foldClassBodies (@NotNull final List<FoldingDescriptor> descriptors, @NotNull final ValaFile valaFile) {
        for (ValaClassDeclaration valaClass : PsiTreeUtil.findChildrenOfAnyType(valaFile, ValaClassDeclaration.class)) {
            if (valaClass != null) {
                final ValaClassBody body = valaClass.getClassBody();
                if (body != null && body.getTextLength() > 2) {
                    descriptors.add(new FoldingDescriptor(body, body.getTextRange()));
                }
            }
        }
    }

    private static void foldNamespaceBodies (@NotNull final List<FoldingDescriptor> descriptors, @NotNull final PsiElement root) {
        for (ValaNamespaceDeclaration valaNamespace : PsiTreeUtil.findChildrenOfAnyType(root, ValaNamespaceDeclaration.class)) {
            if (valaNamespace != null) {
                final ASTNode lBrace = valaNamespace.getNode().findChildByType(ValaTypes.LEFT_CURLY);
                final ASTNode rBrace = valaNamespace.getNode().findChildByType(ValaTypes.RIGHT_CURLY, lBrace);
                if (lBrace != null && rBrace != null && (rBrace.getStartOffset() - lBrace.getStartOffset() > 2)) {
                    descriptors.add(new FoldingDescriptor(valaNamespace, TextRange.create(lBrace.getStartOffset(), rBrace.getStartOffset() + 1)));
                }
            }
        }
    }

    private static void foldStatements (@NotNull final List<FoldingDescriptor> descriptors, @NotNull final PsiElement root) {
        for (ValaStatement valaStatement : PsiTreeUtil.findChildrenOfAnyType(root, ValaStatement.class)) {
            if (valaStatement != null && statementFoldingAllowed(valaStatement)) {
                final ASTNode lBrace = valaStatement.getNode().findChildByType(ValaTypes.LEFT_CURLY);
                final ASTNode rBrace = valaStatement.getNode().findChildByType(ValaTypes.RIGHT_CURLY, lBrace);
                if (lBrace != null && rBrace != null && (rBrace.getStartOffset() - lBrace.getStartOffset() > 2)) {
                    descriptors.add(new FoldingDescriptor(valaStatement, TextRange.create(lBrace.getStartOffset(), rBrace.getStartOffset() + 1)));
                }
            }
        }
    }

    /**
     * Check statement
     * @param valaStatement Statement
     * @return Is statement folding allowed
     */
    private static boolean statementFoldingAllowed(ValaStatement valaStatement) {
        boolean types = (valaStatement instanceof ValaForeachStatement
                || valaStatement  instanceof ValaForStatement
                || valaStatement  instanceof ValaWhileStatement
                || valaStatement  instanceof ValaDoStatement
                || valaStatement  instanceof ValaLambdaExpressionBody);

        boolean parentCheck = (valaStatement.getParent() instanceof ValaForeachStatement
                || valaStatement.getParent()  instanceof ValaForStatement
                || valaStatement.getParent()  instanceof ValaWhileStatement
                || valaStatement.getParent()  instanceof ValaDoStatement
                || valaStatement.getParent()  instanceof ValaLambdaExpressionBody);

        return (types || parentCheck);
    }

    private static void foldFunctionBodies(@NotNull final List<FoldingDescriptor> descriptors, @NotNull final PsiElement root) {
        for (PsiElement valaToken : PsiTreeUtil.findChildrenOfAnyType(root, ValaPsiElement.class)) {
            final ValaComponentType componentType = ValaComponentType.typeOf(valaToken);
            if (componentType == null) continue;

            switch (componentType) {
                case CLASS:
                    ValaClassDeclaration valaClass = (ValaClassDeclaration)valaToken;
                    final ValaClassBody classBlock = valaClass.getClassBody();
                    if(classBlock != null) {
                        final List<ValaClassMember> classMembers = classBlock.getClassMemberList();
                        if (!classMembers.isEmpty()) {
                            for ( ValaClassMember classMember: classMembers) {
                                foldMethod(descriptors, classMember);
                            }
                        }
                    }
                    break;
                case CONSTRUCTOR:
                    foldMethod(descriptors, valaToken);
                    break;
                case METHOD:
                    foldMethod(descriptors, valaToken);
                    break;
                default:
                    break;
            }
        }
    }

    private static void foldConstructorDeclaration(@NotNull final List<FoldingDescriptor> descriptors,
                                              @NotNull final ValaCreationMethodDeclaration valaConstructorDeclaration) {
        final ValaBlock block = valaConstructorDeclaration.getBlock();
        if (block != null && block.getTextLength() > 2) {
            descriptors.add(new FoldingDescriptor(valaConstructorDeclaration, block.getTextRange()));
        }
    }

    private static void foldMethodDeclaration(@NotNull final List<FoldingDescriptor> descriptors,
                                         @NotNull final ValaMethodDeclaration methodDeclaration) {

        final ValaBlock block = methodDeclaration.getBlock();
        if (block != null && block.getTextLength() > 2) {
            descriptors.add(new FoldingDescriptor(methodDeclaration, block.getTextRange()));
        }
    }

    private static void foldMethod(@NotNull final List<FoldingDescriptor> descriptors,
                                   @NotNull final PsiElement valaComponentOrOperatorDeclaration) {

        /**
         * CONSTRUCTOR (CREATION METHOD) CASE
         */
        final ValaCreationMethodDeclaration constructorDeclaration = (valaComponentOrOperatorDeclaration instanceof ValaCreationMethodDeclaration)?
                (ValaCreationMethodDeclaration)valaComponentOrOperatorDeclaration :
                PsiTreeUtil.getChildOfType(valaComponentOrOperatorDeclaration, ValaCreationMethodDeclaration.class);

        if(constructorDeclaration != null) {
            foldConstructorDeclaration(descriptors, constructorDeclaration);
        }

        /**
         * METHOD CASE
         */
        final ValaMethodDeclaration methodDeclaration = (valaComponentOrOperatorDeclaration instanceof ValaMethodDeclaration)?
                (ValaMethodDeclaration)valaComponentOrOperatorDeclaration :
                PsiTreeUtil.getChildOfType(valaComponentOrOperatorDeclaration, ValaMethodDeclaration.class);

        if(methodDeclaration != null) {
            foldMethodDeclaration(descriptors, methodDeclaration);
        }
    }
}
