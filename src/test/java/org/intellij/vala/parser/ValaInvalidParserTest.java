package org.intellij.vala.parser;

import com.intellij.testFramework.ParsingTestCase;

public class ValaInvalidParserTest extends ParsingTestCase {
    public ValaInvalidParserTest() {
        super("invalid", "vala", new ValaParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/parser/test";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testSyntaxErrorInClassDeclarationRecovery() {
        doTest(true);
    }

    public void testSyntaxErrorInMethodRecovery() {
        doTest(true);
    }
}
