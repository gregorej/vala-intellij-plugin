package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.intellij.vala.ValaLanguageFileType;
import org.intellij.vala.psi.ValaFieldDeclaration;
import org.intellij.vala.psi.ValaFile;
import org.intellij.vala.psi.ValaSymbol;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ResolveClassTest extends LightPlatformCodeInsightFixtureTestCase {

    public static final boolean NOT_STRICT = false;

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/reference/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    protected String getDefaultFileName() {
        return getTestName(false) + ValaLanguageFileType.DEFAULT_EXTENSION_WITH_DOT;
    }

    public void testResolveClassDefinitionInSameFile() {
        myFixture.configureByFiles("ResolveClassDefinitionInSameFile.vala");

        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

        assertThat(myFixture.getFile(), hasNoErrors());
        assertThat(elementAtCaret, hasParentOfType(ValaFieldDeclaration.class));
    }

    private static Matcher<PsiElement> hasParentOfType(final Class<? extends PsiElement> expectedPsiElement) {
        return new CustomTypeSafeMatcher<PsiElement>("has parent of type " + expectedPsiElement) {
            @Override
            protected boolean matchesSafely(PsiElement psiElement) {
                return PsiTreeUtil.getParentOfType(psiElement, expectedPsiElement, NOT_STRICT) != null;
            }
        };
    }

    private static Matcher<PsiElement> hasRootThat(final Matcher<? extends PsiElement> rootMatcher) {
        return new CustomTypeSafeMatcher<PsiElement>("has root that " + rootMatcher.toString()) {
            @Override
            protected boolean matchesSafely(PsiElement psiElement) {
                return rootMatcher.matches(getRoot(psiElement));
            }
        };
    }

    private static Matcher<PsiElement> hasNoErrors() {
        return new CustomTypeSafeMatcher<PsiElement>("has no errors") {

            @Override
            protected boolean matchesSafely(PsiElement element) {
                return !PsiTreeUtil.hasErrorElements(element);
            }
        };
    }

    private static PsiElement getRoot(PsiElement element) {
        while (element.getParent() != null) {
            element = element.getParent();
        }
        return element;
    }
}
