package org.intellij.vala.reference;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;

public class ValaReferenceTestBase extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/reference/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    protected PsiElement getElementOfTypeAtCaret(Class<? extends PsiElement> elementType) {
        return getParentOfType(myFixture.getFile().findElementAt(myFixture.getCaretOffset()), elementType);
    }
}
