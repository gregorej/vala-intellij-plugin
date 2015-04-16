package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public abstract class CompletionTestBase extends LightPlatformCodeInsightFixtureTestCase {

    public CompletionTestBase() {
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

    protected static Matcher<? super LookupElement> lookupElement(String lookupString) {
        return new CustomTypeSafeMatcher<LookupElement>("Lookup element with lookup string " + lookupString) {
            @Override
            protected boolean matchesSafely(LookupElement lookupElement) {
                return lookupString.equals(lookupElement.getLookupString());
            }
        };
    }

    protected void doTest() {
        myFixture.configureByFiles(this.getTestName(false) + "Before.vala");

        myFixture.complete(CompletionType.BASIC, SINGLE_INVOCATION);

        myFixture.checkResultByFile(this.getTestName(false) + "After.vala");
    }

    public void expect(Matcher<? super LookupElement> ... lookupElements) {
        myFixture.configureByFiles(this.getTestName(false) + ".vala");

        List<LookupElement> elements = Arrays.asList(myFixture.completeBasic());

        assertThat(elements, containsInAnyOrder(lookupElements));
    }

}
