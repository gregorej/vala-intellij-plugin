package org.intellij.vala.psi;


import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated
public interface ValaNamespaceLike extends ValaPsiElement {

    @NotNull
    List<ValaNamespaceMember> getNamespaceMemberList();
}
