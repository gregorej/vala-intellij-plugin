package org.intellij.vala.psi;



import com.intellij.psi.StubBasedPsiElement;
import org.intellij.vala.psi.stub.ValaClassDeclarationStub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValaNamespaceLike extends ValaPsiElement {

    @NotNull
    List<ValaNamespaceMember> getNamespaceMemberList();
}
