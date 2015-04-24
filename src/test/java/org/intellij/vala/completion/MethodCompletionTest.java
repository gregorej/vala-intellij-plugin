package org.intellij.vala.completion;

public class MethodCompletionTest extends CompletionTestBase {

    public void testSingleParameterlessMethodCompletion() {
        doTest();
    }

    public void testSingleParameterlessMethodFromSuperclassCompletion() {
        doTest();
    }

    public void testSingleMethodCompletionForObjectReferencedByField() {
        doTest();
    }

    public void testSingleMethodCompletionFromCurrentClass() {
        doTest();
    }
}
