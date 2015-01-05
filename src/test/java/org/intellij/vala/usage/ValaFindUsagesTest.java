package org.intellij.vala.usage;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.intellij.usageView.UsageInfo;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.intellij.vala.psi.ValaCreationMethodDeclaration;
import org.intellij.vala.psi.ValaMethodDeclaration;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.junit.Assert.assertThat;

public class ValaFindUsagesTest extends LightPlatformCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/usage/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    public void testSimpleMethodUsage() {
        Collection<UsageInfo> foundUsages = myFixture.testFindUsages("SimpleMethod.vala");

        assertThat(foundUsages, contains(resolvesTo(methodDeclaration("some_method"))));
    }

    public void testTwoMethodUsages() {
        Collection<UsageInfo> foundUsages = myFixture.testFindUsages("TwoMethodUsages.vala");

        assertThat(foundUsages, contains(
                resolvesTo(methodDeclaration("some_method")),
                resolvesTo(methodDeclaration("some_method"))
        ));
    }

    public void testTwoConstructorUsages() {
        Collection<UsageInfo> foundUsages = myFixture.testFindUsages("TwoConstructorUsages.vala");

        assertThat(foundUsages, contains(
                resolvesTo(constructor("FooClass")),
                resolvesTo(constructor("FooClass"))));
    }

    public void testNamedConstructorUsage() {
        Collection<UsageInfo> foundUsages = myFixture.testFindUsages("NamedConstructorUsage.vala");

        assertThat(foundUsages, hasSize(1));
        assertThat(foundUsages, contains(resolvesTo(constructor("with_beer"))));
    }

    private static Matcher<PsiElement> constructor(String name) {
        return allOf(instanceOf(ValaCreationMethodDeclaration.class), hasName(name));
    }

    private static Matcher<PsiElement> methodDeclaration(String name) {
        return allOf(instanceOf(ValaMethodDeclaration.class), hasName(name));
    }

    private static Matcher<UsageInfo> resolvesTo(final Matcher<? super PsiElement> resolutionTarget) {
        return new CustomTypeSafeMatcher<UsageInfo>("resolving to " + resolutionTarget) {
            @Override
            protected void describeMismatchSafely(UsageInfo item, Description mismatchDescription) {
                if (item.getReference() == null) {
                    mismatchDescription.appendText("reference was empty");
                } else {
                    resolutionTarget.describeMismatch(item.getReference().resolve(), mismatchDescription);
                }
            }

            @Override
            protected boolean matchesSafely(UsageInfo usageInfo) {
                final PsiReference reference = usageInfo.getReference();
                return reference != null && resolutionTarget.matches(reference.resolve());
            }
        };
    }
}
