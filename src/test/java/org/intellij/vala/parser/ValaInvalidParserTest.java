package org.intellij.vala.parser;

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

    public void testIncompleteMethodReference() {
        doTest(true);
    }
}
