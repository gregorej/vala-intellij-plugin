package org.intellij.vala.psi.impl;


import com.google.common.collect.ImmutableList;
import org.intellij.vala.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClassDeclarationUtil {

    @NotNull
    public static List<ValaMethodDeclaration> getMethodDeclarations(ValaClassDeclaration classDeclaration) {
        return getMembersOfType(classDeclaration, ValaMethodDeclaration.class);
    }

    private static <T> List<T> getMembersOfType(ValaClassDeclaration classDeclaration, Class<T> expectedType) {
        ImmutableList.Builder<T> declarations = ImmutableList.builder();
        for (ValaClassMember member : classDeclaration.getClassMemberList()) {
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
}
