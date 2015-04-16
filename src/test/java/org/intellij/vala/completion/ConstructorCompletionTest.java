package org.intellij.vala.completion;

public class ConstructorCompletionTest extends CompletionTestBase {

    public void testSingleConstructorCompletion() {
        doTest();
    }

    public void testClassNameInVariableTypeCompletion() {
        doTest();
    }

    public void testSingleConstructorCompletionFromImportedNamespace() {
        doTest();
    }

    public void testSingleNamedConstructorCompletion() {
        doTest();
    }

    public void testSingleNamedConstructorCompletionWithNameBeginWithDot() {
        doTest();
    }

    public void testClassNameWithoutContextCompletion() {
        doTest();
    }

    public void testSingleNamedConstructorCompletionWithFullClassNameBeforeDot() {
        doTest();
    }

    public void testSingleNamedConstructorCompletionRightAfterDot() {
        expect(lookupElement("from_string"));
    }

    public void testMultipleNamedConstructorCompletion() {
        expect(lookupElement("MyClass.from_string"), lookupElement("MyClass.from_int"));
    }
}
