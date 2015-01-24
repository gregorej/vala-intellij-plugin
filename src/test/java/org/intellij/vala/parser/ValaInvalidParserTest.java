package org.intellij.vala.parser;

import com.intellij.testFramework.ParsingTestCase;

public class ValaInvalidParserTest extends AbstractValaParserTest {
    public ValaInvalidParserTest() {
        super("invalid");
    }

    public void testSyntaxErrorInClassDeclarationRecovery() {
        doTest(true);
    }

    public void testSyntaxErrorInMethodRecovery() {
        doTest(true);
    }
}
