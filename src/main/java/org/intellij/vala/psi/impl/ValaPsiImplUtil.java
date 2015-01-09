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

    public static List<String> getPartNames(ValaMember valaMember) {
        ImmutableList.Builder<String> list = ImmutableList.builder();
        for (ValaMemberPart part : valaMember.getMemberPartList()) {
            list.add(part.getName());
        }
        return list.build();
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
