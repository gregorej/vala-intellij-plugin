package org.intellij.vala.psi;

import com.google.common.collect.Iterables;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.io.IOUtil;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.intellij.vala.reference.ResolveClassTest;

import static com.intellij.psi.impl.DebugUtil.psiToString;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.hamcrest.Matchers.equalTo;

public final class PsiMatchers {

    public static Matcher<PsiElement> hasParentOfType(final Class<? extends PsiElement> expectedPsiElement) {
        return new CustomTypeSafeMatcher<PsiElement>("has parent of type " + expectedPsiElement) {
            @Override
            protected boolean matchesSafely(PsiElement psiElement) {
                return PsiTreeUtil.getParentOfType(psiElement, expectedPsiElement, ResolveClassTest.NOT_STRICT) != null;
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

    public static Matcher<PsiElement> hasNoErrors() {
        return new CustomTypeSafeMatcher<PsiElement>("has no errors") {

            @Override
            protected boolean matchesSafely(PsiElement element) {
                return !PsiTreeUtil.hasErrorElements(element);
            }

            @Override
            protected void describeMismatchSafely(PsiElement item, Description mismatchDescription) {
                PsiErrorElement errorElement = Iterables.getFirst(PsiTreeUtil.findChildrenOfType(item, PsiErrorElement.class), null);
                String psiString = psiToString(item, true, true);
                mismatchDescription.appendText(psiString);
            }
        };
    }

    private static PsiElement getRoot(PsiElement element) {
        while (element.getParent() != null) {
            element = element.getParent();
        }
        return element;
    }

    public static Matcher<PsiElement> isInFile(final Matcher<? super PsiFile> psiFileMatcher) {
        return new CustomTypeSafeMatcher<PsiElement>("is contained in file that " + psiFileMatcher) {

            @Override
            protected boolean matchesSafely(PsiElement o) {
                PsiFile file = getParentOfType(o, PsiFile.class);
                return psiFileMatcher.matches(file);
            }
        };
    }

    public static Matcher<? super PsiElement> hasName(final Matcher<String> name) {
        return new CustomTypeSafeMatcher<PsiElement>("element with name " + name) {
            @Override
            protected boolean matchesSafely(PsiElement psiFile) {
                return psiFile instanceof PsiNamedElement && name.matches(((PsiNamedElement) psiFile).getName());
            }

            @Override
            protected void describeMismatchSafely(PsiElement item, Description mismatchDescription) {
                mismatchDescription.appendText("name was ").appendValue(((PsiNamedElement) item).getName());
            }
        };
    }

    public static Matcher<? super PsiElement> hasName(String name) {
        return hasName(equalTo(name));
    }


    public static Matcher<PsiElement> aClassDeclarationThat(final Matcher<? super ValaClassDeclaration> declarationMatcher) {
        return new CustomTypeSafeMatcher<PsiElement>("a class declaration that " + declarationMatcher) {
            @Override
            protected boolean matchesSafely(PsiElement element) {
                return element instanceof ValaClassDeclaration && declarationMatcher.matches(element);
            }

            @Override
            protected void describeMismatchSafely(PsiElement item, Description mismatchDescription) {
                declarationMatcher.describeMismatch(item, mismatchDescription);
            }
        };
    }
}
