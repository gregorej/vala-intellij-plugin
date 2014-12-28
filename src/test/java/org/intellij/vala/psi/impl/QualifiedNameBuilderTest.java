package org.intellij.vala.psi.impl;

import org.intellij.vala.psi.QualifiedName;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class QualifiedNameBuilderTest {

    @Test
    public void shouldWriteQualifiedNameAndThenReadItAgain() throws IOException {
        QualifiedName original = QualifiedNameBuilder.nameOf("one","two","three");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        original.write(new DataOutputStream(bout));
        QualifiedName read = QualifiedNameBuilder.read(new DataInputStream(new ByteArrayInputStream(bout.toByteArray())));

        assertThat(read, equalTo(original));
    }

}