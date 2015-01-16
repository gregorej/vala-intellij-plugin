package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.*;

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

    public void testReferenceToFieldInCurrentClass() {
        myFixture.configureByFiles("ReferenceToFieldInCurrentClass.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaFieldDeclaration.class), hasName("status")));
    }

    public void testReferenceToMethodArgument() {
        myFixture.configureByFiles("ReferenceToMethodArgument.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaParameter.class), hasName("name")));
    }

    public void testReferenceToVariableFromCallParameter() {
        myFixture.configureByFiles("ReferenceToVariableFromCallParameter.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaParameter.class), hasName("name")));
    }

    public void testReferenceToFieldInExplicitObject() {
        myFixture.configureByFiles("ReferenceToFieldInExplicitObject.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaFieldDeclaration.class), hasName("field")));
    }
}
