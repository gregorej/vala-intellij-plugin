package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.intellij.vala.psi.ValaLocalVariable;
import org.intellij.vala.psi.ValaSimpleName;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.junit.Assert.assertThat;

public class ResolveVariablesTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/reference/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    public void testReferenceToVariableInSameScope() {
        myFixture.configureByFiles("ReferenceToVariableInSameScope.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaLocalVariable.class), hasName("obj")));
    }

    private PsiElement getElementOfTypeAtCaret(Class<? extends PsiElement> elementType) {
        return getParentOfType(myFixture.getFile().findElementAt(myFixture.getCaretOffset()), elementType);
    }
}
