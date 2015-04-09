package org.intellij.vala.completion;

import com.intellij.codeInsight.lookup.LookupElement;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;


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
        myFixture.configureByFiles(this.getTestName(false) + ".vala");

        List<LookupElement> elements = Arrays.asList(myFixture.completeBasic());

        assertThat(elements, contains(lookupElement("from_string")));
    }

    public void testMultipleNamedConstructorCompletion() {
        myFixture.configureByFiles(this.getTestName(false) + ".vala");

        List<LookupElement> elements = Arrays.asList(myFixture.completeBasic());

        assertThat(elements, containsInAnyOrder(lookupElement("MyClass.from_string"), lookupElement("MyClass.from_int")));
    }
}
