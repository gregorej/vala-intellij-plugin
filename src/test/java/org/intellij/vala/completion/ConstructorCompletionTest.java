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
        expect(lookupItem("from_string"));
    }

    public void testMultipleNamedConstructorCompletion() {
        expect(lookupItem("MyClass.from_string"), lookupItem("MyClass.from_int"));
    }
}
