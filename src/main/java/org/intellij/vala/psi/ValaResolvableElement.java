package org.intellij.vala.psi;

import com.intellij.psi.PsiElement;

import java.util.Optional;

public interface ValaResolvableElement extends ValaPsiElement {

    Optional<PsiElement> resolve();
}
