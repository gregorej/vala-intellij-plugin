package org.intellij.vala.psi.impl;


import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.index.DeclarationQualifiedNameIndex;
import org.intellij.vala.psi.inference.ExpressionTypeInference;
import org.intellij.vala.reference.*;
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

    public static String getName(ValaStructDeclaration classDeclaration) {
        return classDeclaration.getSymbol().getText();
    }

    public static String getName(ValaInterfaceDeclaration classDeclaration) {
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

    public static String getName(ValaMemberAccess memberAccess) {
        return memberAccess.getIdentifier().getText();
    }

    public static String getName(ValaPointerMemberAccess memberAccess) {
        return memberAccess.getIdentifier().getText();
    }

    public static int getTextOffset(ValaCreationMethodDeclaration creationMethodDeclaration) {
        return getLastPart(creationMethodDeclaration.getSymbol()).getTextOffset();
    }

    public static String getName(ValaSimpleName simpleName) {
        return simpleName.getIdentifier().getText();
    }

    public static String getName(ValaDelegateDeclaration methodDeclaration) {
        return methodDeclaration.getIdentifier().getText();
    }

    public static QualifiedName getQName(ValaDelegateDeclaration methodDeclaration) {
        return QualifiedNameBuilder.forMethodDeclaration(methodDeclaration);
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

    public static PsiReference getReference(ValaThisAccess thisAccess) {
        return new ThisAccessReference(thisAccess);
    }

    public static PsiReference getReference(ValaMemberAccess memberAccess) {
        return new ValaMemberAccessReference(memberAccess);
    }

    public static PsiReference getReference(ValaMethodCall methodCall) {
        ValaChainAccessPart previous = methodCall.getPrevious();
        if (previous instanceof ValaMemberAccess) {
            return previous.getReference();
        }
        return null;
    }

    public static List<ValaMethodDeclaration> getMethodDeclarations(ValaClassDeclaration classDeclaration) {
        return TypeDeclarationUtil.getMethodDeclarations(classDeclaration);
    }

    public static String toString(StubBasedPsiElement<?> element) {
        return element.getClass().getSimpleName() + "(" + element.getElementType().toString() + ")";
    }

    public static List<ValaNamespaceMember> getNamespaceMemberList(ValaClassDeclaration classDeclaration) {
        return TypeDeclarationUtil.getNamespaceMemberList(classDeclaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaClassDeclaration declaration) {
        return TypeDeclarationUtil.getDeclarations(declaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaEnumDeclaration declaration) {
        return TypeDeclarationUtil.getDeclarations(declaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaInterfaceDeclaration declaration) {
        return TypeDeclarationUtil.getDeclarations(declaration);
    }

    public static List<ValaDeclaration> getDeclarations(ValaStructDeclaration declaration) {
        return TypeDeclarationUtil.getDeclarations(declaration);
    }

    public static List<ValaDelegateDeclaration> getDelegates(ValaTypeDeclaration typeDeclaration) {
        return TypeDeclarationUtil.getDelegates(typeDeclaration);
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

    public static QualifiedName getQName(ValaNamespaceDeclaration classDeclaration) {
        return QualifiedNameBuilder.forNamespaceDeclaration(classDeclaration);
    }

    public static QualifiedName getQName(ValaMethodDeclaration classDeclaration) {
        return QualifiedNameBuilder.forMethodDeclaration(classDeclaration);
    }

    public static QualifiedName getQName(ValaTypeDeclaration structDeclaration) {
        return QualifiedNameBuilder.forTypeDeclaration(structDeclaration);
    }

    public static QualifiedName getQName(ValaFieldDeclaration fieldDeclaration) {
        return QualifiedNameBuilder.forFieldDeclaration(fieldDeclaration);
    }

    public static ValaDelegateDeclaration getMethodDeclaration(ValaMethodCall methodCall) {
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

    public static ValaTypeDescriptor getTypeDescriptor(ValaType type) {
        if (type.getTypeBase() == null) {
            return null;
        }
        ValaTypeBase typeBase = type.getTypeBase();
        return getTypeDescriptor(typeBase);
    }

    private static ValaTypeDescriptor getTypeDescriptor(ValaTypeBase typeBase) {
        if (typeBase.getBuiltInType() != null) {
            return ReferenceTypeDescriptor.forType(typeBase.getBuiltInType());
        }
        PsiElement referenced = new ValaTypeReference(typeBase.getSymbol(), typeBase.getTextRange()).resolve();
        if (referenced instanceof ValaDeclaration) {
            return ReferenceTypeDescriptor.forQualifiedName(((ValaDeclaration) referenced).getQName());
        }
        return null;
    }

    public static List<ValaTypeDeclaration> getSuperTypeDeclarations(ValaClassDeclaration classDeclaration) {
        return getSuperTypeDeclarations(classDeclaration.getBaseTypes());
    }

    private static List<ValaTypeDeclaration> getSuperTypeDeclarations(@Nullable ValaBaseTypes baseTypes) {
        ImmutableList.Builder<ValaTypeDeclaration> declarations = ImmutableList.builder();
        if (baseTypes == null) {
            return declarations.build();
        }
        final DeclarationQualifiedNameIndex index = DeclarationQualifiedNameIndex.getInstance();
        for (ValaType type : baseTypes.getTypeList()) {
            ValaTypeDescriptor descriptor = type.getTypeDescriptor();
            if (descriptor != null) {
                ValaDeclaration declaration = index.get(descriptor.getQualifiedName(), baseTypes.getProject());
                if (declaration instanceof ValaTypeDeclaration) {
                    declarations.add((ValaTypeDeclaration) declaration);
                }
            }
        }
        return declarations.build();
    }

    public static List<ValaTypeDeclaration> getSuperTypeDeclarations(ValaInterfaceDeclaration interfaceDeclaration) {
        return getSuperTypeDeclarations(interfaceDeclaration.getBaseTypes());
    }

    public static List<ValaTypeDeclaration> getSuperTypeDeclarations(ValaStructDeclaration structDeclaration) {
        return getSuperTypeDeclarations(structDeclaration.getBaseTypes());
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaParameter parameter) {
        if (parameter.getType() == null) {
            return null;
        }
        return ReferenceTypeDescriptor.forType(parameter.getType());
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaLocalVariableDeclaration valaLocalVariableDeclaration) {
        ValaLocalVariableDeclarations declarations = (ValaLocalVariableDeclarations) valaLocalVariableDeclaration.getParent();
        ValaType type = declarations.getType();
        if (type != null) {
            return type.getTypeDescriptor();
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaObjectOrArrayCreationExpression objectOrArrayCreationExpression) {
        ValaMember member = objectOrArrayCreationExpression.getMember();
        PsiElement referenced = new ValaConstructorReference(ValaPsiElementUtil.getLastPart(member)).resolve();
        if (referenced instanceof ValaClassDeclaration) {
            return ReferenceTypeDescriptor.forQualifiedName(((ValaClassDeclaration) referenced).getQName());
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaLocalVariable valaLocalVariable) {
        if (valaLocalVariable.getExpression() != null) {
            return ExpressionTypeInference.inferType(valaLocalVariable.getExpression());
        }
        return getTypeDescriptor((ValaLocalVariableDeclaration) valaLocalVariable.getParent());
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaFieldDeclaration fieldDeclaration) {
        ValaTypeBase typeBase = fieldDeclaration.getTypeWeak().getTypeBase();
        if (typeBase != null) {
            ValaBuiltInType builtInType = typeBase.getBuiltInType();
            if (builtInType != null) {
                return BasicTypeDescriptor.forType(builtInType);
            }
            else {
                return ReferenceTypeDescriptor.forQualifiedName(fieldDeclaration.getQName().getPrefix(fieldDeclaration.getQName().length() - 1));
            }
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaMethodCall valaMethodCall) {
        ValaDelegateDeclaration declaration = ValaPsiElementUtil.getMethodDeclaration(valaMethodCall);
        if (declaration == null) {
            return null;
        }
        return getTypeDescriptor(declaration);
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaDelegateDeclaration declaration) {
        return getTypeDescriptor(declaration.getType());
    }

    @Nullable
    public static ValaTypeDescriptor getTypeDescriptor(ValaSimpleName simpleName) {
        PsiReference reference = simpleName.getReference();
        if (reference != null) {
            PsiElement resolved = reference.resolve();
            if (resolved instanceof HasTypeDescriptor) {
                return ((HasTypeDescriptor) resolved).getTypeDescriptor();
            }
        }
        return null;
    }

    @Nullable
    public static ValaTypeDescriptor getTypeDescriptor(ValaLiteral literal) {
        if (literal.getCharacterLiteral() != null) return BasicTypeDescriptor.CHARACTER;
        if (literal.getStringLiteral() != null) return BasicTypeDescriptor.STRING;
        if (literal.getIntegerLiteral() != null) return BasicTypeDescriptor.INTEGER;
        if (literal.getRealLiteral() != null) return BasicTypeDescriptor.DOUBLE;
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

    public static ValaTypeDescriptor getTypeDescriptor(ValaMemberAccess memberAccess) {
        PsiReference reference = memberAccess.getReference();
        if (reference != null) {
            PsiElement resolved = reference.resolve();
            if (resolved instanceof HasTypeDescriptor) {
                return ((HasTypeDescriptor) resolved).getTypeDescriptor();
            }
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaPointerMemberAccess pointerMemberAccess) {
        return null;
    }

    public static ValaInstantiableTypeDeclaration getTypeDeclaration(ValaCreationMethodDeclaration creationMethodDeclaration) {
        return PsiTreeUtil.getParentOfType(creationMethodDeclaration, ValaInstantiableTypeDeclaration.class);
    }

    private static ValaTypeDescriptor getTypeDescriptorPointedByLastMemberPart(ValaMember member) {
        ValaMemberPart lastMemberPart = getLastPart(member);
        PsiReference reference = lastMemberPart.getReference();
        if (reference == null) {
            return null;
        }
        PsiElement resolvedElement = reference.resolve();
        if (resolvedElement instanceof HasTypeDescriptor) {
            return ((HasTypeDescriptor) resolvedElement).getTypeDescriptor();
        }
        return null;
    }

    public static ValaTypeDescriptor getTypeDescriptor(ValaChainAccessPart accessPart) {
        if (accessPart instanceof ValaMemberAccess) {
            return getTypeDescriptor((ValaMemberAccess) accessPart);
        }
        else if (accessPart instanceof ValaMethodCall) {
            return getTypeDescriptor((ValaMethodCall) accessPart);
        }
        return null;
    }
}
