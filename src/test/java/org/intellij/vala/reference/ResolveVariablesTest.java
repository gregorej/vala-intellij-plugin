package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.ValaFieldDeclaration;
import org.intellij.vala.psi.ValaLocalVariable;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaSimpleName;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.junit.Assert.assertThat;

public class ResolveVariablesTest extends ValaReferenceTestBase {

    public void testReferenceToVariableInSameScope() {
        myFixture.configureByFiles("ReferenceToVariableInSameScope.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaLocalVariable.class), hasName("obj")));
    }

    public void testReferenceToVariableFromInsideExpression() {
        myFixture.configureByFiles("ReferenceToVariableFromInsideExpression.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaLocalVariable.class), hasName("factor")));
    }

    public void testReferenceToFieldInCurrentClassWithThisAccessor() {
        myFixture.configureByFiles("ReferenceToFieldInCurrentClassWithThisAccessor.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaFieldDeclaration.class), hasName("status")));
    }
}
