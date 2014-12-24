package org.intellij.vala.reference.method;


import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.intellij.vala.psi.ValaMethodDeclaration;
import org.intellij.vala.psi.ValaSimpleName;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.junit.Assert.assertThat;

public class ResolveMethodDeclaration extends LightPlatformCodeInsightFixtureTestCase {
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/reference/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    public void testReferenceMethodInSameClass() {
        myFixture.configureByFiles("FileContainingMethodCallFromSameClass.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName(equalTo("method1"))));
    }

    public void testReferenceMethodInSameFileSameNamespaceLevel() {
        myFixture.configureByFiles("FileContainingReferenceToMethodInSameFile.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName(equalTo("run"))));
    }


    private PsiElement getElementOfTypeAtCaret(Class<? extends PsiElement> elementType) {
        return getParentOfType(myFixture.getFile().findElementAt(myFixture.getCaretOffset()), elementType);
    }

}
