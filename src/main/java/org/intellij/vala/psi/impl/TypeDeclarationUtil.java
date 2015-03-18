package org.intellij.vala.psi.impl;


import com.google.common.collect.ImmutableList;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TypeDeclarationUtil {

    @NotNull
    public static List<ValaMethodDeclaration> getMethodDeclarations(ValaClassDeclaration classDeclaration) {
        return getMembersOfType(classDeclaration, ValaMethodDeclaration.class);
    }

    private static <T> List<T> getMembersOfType(ValaClassDeclaration classDeclaration, Class<T> expectedType) {
        ImmutableList.Builder<T> declarations = ImmutableList.builder();
        for (ValaClassMember member : classDeclaration.getClassBody().getClassMemberList()) {
            ValaNamespaceMember namespaceMember = member.getNamespaceMember();
            if (namespaceMember !=null && expectedType.isAssignableFrom(namespaceMember.getClass())) {
                declarations.add((T) member.getNamespaceMember());
            }
        }
        return declarations.build();
    }

    @NotNull
    public static List<ValaNamespaceMember> getNamespaceMemberList(ValaClassDeclaration valaClassDeclaration) {
        return getMembersOfType(valaClassDeclaration, ValaNamespaceMember.class);
    }

    public static List<ValaDeclaration> getDeclarations(ValaClassDeclaration declaration) {
        return getMembersOfType(declaration, ValaDeclaration.class);
    }

    public static List<ValaDeclaration> getDeclarations(ValaInterfaceDeclaration declaration) {
        ImmutableList.Builder<ValaDeclaration> declarations = ImmutableList.builder();
        for (ValaInterfaceMember interfaceMember : declaration.getInterfaceBody().getInterfaceMemberList()) {
            if (interfaceMember.getClassDeclaration() != null) {
                declarations.add(interfaceMember.getClassDeclaration());
            }
            if (interfaceMember.getNamespaceMember() instanceof ValaDeclaration) {
                declarations.add((ValaDeclaration) interfaceMember.getNamespaceMember());
            }
        }
        return declarations.build();
    }

    public static List<ValaDeclaration> getDeclarations(ValaStructDeclaration declaration) {
        ImmutableList.Builder<ValaDeclaration> declarations = ImmutableList.builder();
        for (ValaStructMember interfaceMember : declaration.getStructBody().getStructMemberList()) {
            if (interfaceMember.getNamespaceMember() instanceof ValaDeclaration) {
                declarations.add((ValaDeclaration) interfaceMember.getNamespaceMember());
            }
        }
        return declarations.build();
    }
}
