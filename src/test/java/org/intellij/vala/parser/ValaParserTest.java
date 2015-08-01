package org.intellij.vala.parser;

public class ValaParserTest extends AbstractValaParserTest {
    public ValaParserTest() {
        super("valid");
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

    public void testOpenedBlockComment() {
        doTest(true);
    }

    public void testBlockComment() {
        doTest(true);
    }

    public void testBlockCommentInsideClass() {
        doTest(true);
    }

    public void testLineComment() {
        doTest(true);
    }

    public void testVerbatimString() {
        doTest(true);
    }

    public void testSwitch() {
        doTest(true);
    }

    public void testTryStatement() {
        doTest(true);
    }

    public void testClassWithMultipleEmptyMethodsReturningDifferentTypes() {
        doTest(true);
    }

    public void testIfStatementWithComplexCondition() {
        doTest(true);
    }

    public void testBasicTypesStaticMethodInvocation() {
        doTest(true);
    }

    public void testIfStatementWithMixedOperators() {
        doTest(true);
    }

    public void testArrays() {
        doTest(true);
    }

    public void testSimpleLibrary() {
        doTest(true);
    }

    public void testEnumWithMethodsWithAttributes() {
        doTest(true);
    }

    public void testConstantNameStartingWithDecimal() {
        doTest(true);
    }

    public void testPreprocessorDirectives() {
        doTest(true);
    }

    public void testFieldDeclarationWithMemberDeclarationModifier() {
        doTest(true);
    }

    public void testClassWithAttributesInSingleLine() {
        doTest(true);
    }

    public void testMethodsNamedWithPropertyContextKeywords() {
        doTest(true);
    }

    public void testStaticMembers() {
        doTest(true);
    }

    public void testSimplePreprocessor() {
        doTest(true);
    }

    public void testClassWithAttributeWithEmptyParametersParens() {
        doTest(true);
    }

    public void testEscapedCharacterLiteral() {
        doTest(true);
    }
}
