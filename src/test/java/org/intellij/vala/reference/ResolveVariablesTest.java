package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import org.hamcrest.Matcher;
import org.intellij.vala.psi.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.intellij.vala.psi.PsiMatchers.hasParentOfType;
import static org.intellij.vala.psi.PsiMatchers.identifier;
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

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaFieldDeclaration.class), identifier("status")));
    }

    public void testReferenceToFieldInCurrentClass() {
        myFixture.configureByFiles("ReferenceToFieldInCurrentClass.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaFieldDeclaration.class), identifier("status")));
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

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaFieldDeclaration.class), identifier("field")));
    }

    public void testReferenceToFieldInExplicitObjectIndirect() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(identifier("next"), hasParentOfType(ValaFieldDeclaration.class)));
    }

    public void testReferenceToNonExistentVariable() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, nullValue());
    }

    public void testReferenceToFieldInSuperClass() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaFieldDeclaration.class), identifier("count")));
    }

    public void testReferenceToFieldInInferredObject() {
        expect(ValaIdentifier.class).referencesTo(allOf(hasParentOfType(ValaFieldDeclaration.class), identifier("field")));
    }

    public void testReferenceToEnumValue() {
        expect(ValaIdentifier.class).referencesTo(allOf(instanceOf(ValaEnumvalue.class), hasName("MY_VALUE_1")));
    }

    private ReferenceExpectation expect(Class<? extends PsiElement> psiElementClass) {
        return new ReferenceExpectation(psiElementClass);
    }

    private class ReferenceExpectation {

        private final Class<? extends PsiElement> psiElementClass;

        public ReferenceExpectation(Class<? extends PsiElement> psiElementClass) {
            this.psiElementClass = psiElementClass;
        }

        public void referencesTo(Matcher<? super PsiElement> referenceMatcher) {
            myFixture.configureByFiles(getTestName(false) + ".vala");

            PsiElement referencedElement = getElementOfTypeAtCaret(psiElementClass).getReference().resolve();

            assertThat(referencedElement, referenceMatcher);
        }
    }
}
