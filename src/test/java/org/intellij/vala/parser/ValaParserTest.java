package org.intellij.vala.parser;

import com.intellij.testFramework.ParsingTestCase;

public class ValaParserTest extends ParsingTestCase {
    public ValaParserTest() {
        super("valid", "vala", new ValaParserDefinition());
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

    public void testEmptyClassInNamespace() {
        doTest(true);
    }

    public void testSingleMethodCall() {
        doTest(true);
    }

    public void testSingleObjectCreation() {
        doTest(true);
    }

    public void testGtkHelloWorld() {
        doTest(true);
    }

    public void testSingleObjectPropertyAssignment() {
        doTest(true);
    }

    public void testSingleLambdaExpression() {
        doTest(true);
    }

    public void testSimpleClassDefinition() {
        doTest(true);
    }

    public void testSimpleClassDefinitionWithNamedConstructor() {
        doTest(true);
    }
}
