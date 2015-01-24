package org.intellij.vala.psi.impl;


import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.psi.*;
import org.intellij.vala.reference.SymbolReferenceRetriever;
import org.intellij.vala.reference.ValaMemberPartReferenceFactory;
import org.intellij.vala.reference.ValaSimpleNameReferenceFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.intellij.vala.psi.impl.ValaPsiElementUtil.getLastPart;

public class ValaPsiImplUtil {

    public static PsiReference getReference(ValaSimpleName simpleName) {
        return ValaSimpleNameReferenceFactory.INSTANCE.create(simpleName);
    }

    public static PsiReference getReference(ValaSymbolPart part) {
        return SymbolReferenceRetriever.getReference(part);
    }

    public static PsiReference getReference(ValaType type) {
        return null;
    }

    public static ValaNamespaceDeclaration getNamespace(ValaNamespaceMember valaClassDeclaration) {
        return PsiTreeUtil.getParentOfType(valaClassDeclaration, ValaNamespaceDeclaration.class, false);
    }

    public static String getName(ValaClassDeclaration classDeclaration) {
        return classDeclaration.getSymbol().getText();
    }

    public static String getName(ValaSymbolPart symbolPart) {
        return symbolPart.getIdentifier().getText();
    }

    public static String getName(ValaMemberPart memberPart) {
        return memberPart.getIdentifier().getText();
    }

    public static String getName(ValaCreationMethodDeclaration creationMethodDeclaration) {
        return getLastPart(creationMethodDeclaration.getSymbol()).getText();
    }

    public static String getName(ValaLocalVariable localVariable) {
        return localVariable.getIdentifier().getText();
    }

    public static String getName(ValaFieldDeclaration fieldDeclaration) {
        return fieldDeclaration.getIdentifier().getText();
    }

    public static int getTextOffset(ValaCreationMethodDeclaration creationMethodDeclaration) {
        return getLastPart(creationMethodDeclaration.getSymbol()).getTextOffset();
    }

    public static String getName(ValaSimpleName simpleName) {
        return simpleName.getIdentifier().getText();
    }

    public static String getName(ValaMethodDeclaration methodDeclaration) {
        return methodDeclaration.getIdentifier().getText();
    }

    public static String getName(ValaParameter parameter) {
        return parameter.getIdentifier().getText();
    }

    public static PsiElement setName(PsiElement valaPsiElement, String newName) {
        throw new IncorrectOperationException("changing name of this element is not supported");
    }

    public static QualifiedName getQName(ValaCreationMethodDeclaration creationMethodDeclaration) {
        return QualifiedNameBuilder.from(creationMethodDeclaration);
    }

    public static PsiReference getReference(ValaMemberPart memberPart) {
        return ValaMemberPartReferenceFactory.INSTANCE.create(memberPart);
    }

    public static List<ValaMethodDeclaration> getMethodDeclarations(ValaClassDeclaration classDeclaration) {
        return ClassDeclarationUtil.getMethodDeclarations(classDeclaration);
    }

    public static String toString(StubBasedPsiElement<?> element) {
        return element.getClass().getSimpleName() + "(" + element.getElementType().toString() + ")";
    }

    public static List<ValaNamespaceMember> getNamespaceMemberList(ValaClassDeclaration classDeclaration) {
        return ClassDeclarationUtil.getNamespaceMemberList(classDeclaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaClassDeclaration declaration) {
        return ClassDeclarationUtil.getDeclarations(declaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaNamespaceDeclaration valaNamespaceDeclaration) {
        return toDeclarations(valaNamespaceDeclaration.getNamespaceMemberList());
    }

    public static List<ValaDeclaration> toDeclarations(List<ValaNamespaceMember> namespaceMembers) {
        ImmutableList.Builder<ValaDeclaration> builder =  ImmutableList.builder();
        for (ValaNamespaceMember member : namespaceMembers) {
            if (member.getClassDeclaration() != null) {
                builder.add(member.getClassDeclaration());
            }
            if (member.getNamespaceMember() instanceof ValaDeclaration) {
                builder.add((ValaDeclaration) member.getNamespaceMember());
            }
            if (member instanceof ValaDeclaration) {
                builder.add((ValaDeclaration) member);
            }
        }
        return builder.build();
    }

    public static QualifiedName getQName(ValaClassDeclaration classDeclaration) {
        return QualifiedNameBuilder.forClassDeclaration(classDeclaration);
    }

    public static QualifiedName getQName(ValaNamespaceDeclaration classDeclaration) {
        return QualifiedNameBuilder.forNamespaceDeclaration(classDeclaration);
    }

    public static QualifiedName getQName(ValaMethodDeclaration classDeclaration) {
        return QualifiedNameBuilder.forMethodDeclaration(classDeclaration);
    }

    public static QualifiedName getQName(ValaFieldDeclaration fieldDeclaration) {
        return QualifiedNameBuilder.forFieldDeclaration(fieldDeclaration);
    }

    public static ValaMethodDeclaration getMethodDeclaration(ValaMethodCall methodCall) {
        return ValaPsiElementUtil.getMethodDeclaration(methodCall);
    }

    @Nullable
    public static ValaMemberPart getPrevious(ValaMemberPart part) {
        ValaMember parent = (ValaMember) part.getParent();
        final List<ValaMemberPart> memberPartList = parent.getMemberPartList();
        int myIndex = memberPartList.indexOf(part);
        if (myIndex > 0) {
            return memberPartList.get(myIndex - 1);
        }
        return null;
    }

    @Nullable
    public static ValaChainAccessPart getPrevious(ValaChainAccessPart part) {
        final PsiElement parent = part.getParent();
        if (!(parent instanceof ValaPrimaryExpression)) {
            return null;
        }
        ValaPrimaryExpression primaryExpression = (ValaPrimaryExpression) parent;
        final List<ValaChainAccessPart> chainAccessList = primaryExpression.getChainAccessPartList();
        int myIndex = chainAccessList.indexOf(part);
        if (myIndex > 0) {
            return chainAccessList.get(myIndex - 1);
        }
        return null;
    }

    public static ValaTypeDescriptor getDescriptor(ValaType type) {
        return ValaTypeDescriptor.forType(type);
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaParameter parameter) {
        if (parameter.getType() == null) {
            return null;
        }
        return ValaTypeDescriptor.forType(parameter.getType());
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaLocalVariableDeclaration valaLocalVariableDeclaration) {
        ValaLocalVariableDeclarations declarations = (ValaLocalVariableDeclarations) valaLocalVariableDeclaration.getParent();
        ValaType type = declarations.getType();
        if (type != null) {
            return type.getDescriptor();
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaLocalVariable valaLocalVariable) {
        return getTypeDescriptor((ValaLocalVariableDeclaration) valaLocalVariable.getParent());
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaMethodCall valaMethodCall) {
        ValaMethodDeclaration declaration = ValaPsiElementUtil.getMethodDeclaration(valaMethodCall);
        if (declaration == null) {
            return null;
        }
        return ValaTypeDescriptor.forType(declaration.getType());
    }

    @Nullable
    public static ValaTypeDescriptor getTypeDescriptor(ValaLiteral literal) {
        if (literal.getCharacterLiteral() != null) return ValaTypeDescriptor.CHARACTER;
        if (literal.getStringLiteral() != null) return ValaTypeDescriptor.STRING;
        return null;
    }

    public static List<QualifiedName> getImportedNamespacesAvailableFor(PsiElement symbol) {
        ValaFile containingFile = (ValaFile) symbol.getContainingFile();
        ImmutableList.Builder<QualifiedName> names = ImmutableList.builder();
        names.add(QualifiedNameBuilder.ROOT);
        for (ValaUsingDirective directive : containingFile.getUsingDirectives()) {
            names.addAll(toQNames(directive.getSymbolList()));
        }
        return names.build();
    }

    private static List<QualifiedName> toQNames(List<ValaSymbol> symbols) {
        ImmutableList.Builder<QualifiedName> names = ImmutableList.builder();
        for (ValaSymbol symbol : symbols) {
            names.add(QualifiedNameBuilder.from(symbol));
        }
        return names.build();
    }
}
