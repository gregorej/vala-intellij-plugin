package org.intellij.vala.psi;


import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public interface QualifiedName extends Serializable {

    String toString();

    String getTail();

    QualifiedName append(String member);

    void write(DataOutput stubOutputStream) throws IOException;
}
