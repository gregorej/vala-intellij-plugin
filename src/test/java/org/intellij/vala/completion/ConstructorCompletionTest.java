package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;


public class ConstructorCompletionTest extends LightPlatformCodeInsightFixtureTestCase {

    public ConstructorCompletionTest() {
        PlatformTestCase.initPlatformPrefix("not_existing_class", "PlatformLangXml");
    }

    private static final int SINGLE_INVOCATION = 1;

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/completion/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

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

    private static Matcher<? super LookupElement> lookupElement(String lookupString) {
        return new CustomTypeSafeMatcher<LookupElement>("Lookup element with lookup string " + lookupString) {
            @Override
            protected boolean matchesSafely(LookupElement lookupElement) {
                return lookupString.equals(lookupElement.getLookupString());
            }
        };
    }

    private void doTest() {
        myFixture.configureByFiles(this.getTestName(false) + "Before.vala");

        myFixture.complete(CompletionType.BASIC, SINGLE_INVOCATION);

        myFixture.checkResultByFile(this.getTestName(false) + "After.vala");
    }
}
