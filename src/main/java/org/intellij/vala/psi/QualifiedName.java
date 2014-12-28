package org.intellij.vala.psi;


import com.intellij.psi.stubs.StubOutputStream;

import java.io.IOException;
import java.io.Serializable;

public interface QualifiedName extends Serializable {

    String toString();

    String getTail();

    QualifiedName append(String member);

    void write(StubOutputStream stubOutputStream) throws IOException;
}
