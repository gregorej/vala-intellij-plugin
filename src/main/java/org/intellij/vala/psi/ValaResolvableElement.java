package org.intellij.vala.psi;

import java.util.Optional;

public interface ValaResolvableElement extends ValaPsiElement {

    Optional<ValaPsiElement> resolve();
}
