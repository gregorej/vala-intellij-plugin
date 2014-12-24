package org.intellij.vala.psi;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValaNamespaceLike extends ValaPsiElement {

    @NotNull
    List<ValaNamespaceMember> getNamespaceMemberList();
}
