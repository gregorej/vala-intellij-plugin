package org.intellij.vala.reference.method;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.*;
import org.intellij.vala.reference.ValaReferenceTestBase;

import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.intellij.vala.psi.PsiMatchers.hasParentOfType;
import static org.junit.Assert.assertThat;

public class ResolveMethodDeclaration extends ValaReferenceTestBase {

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

    public void testReferenceToMethodFromCallOnExplicitObject() {
        myFixture.configureByFiles("ReferenceToMethodFromCallOnExplicitObject.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName("open")));
    }

    public void testReferenceToMethodFromCallOnMethodCallChain() {
        myFixture.configureByFiles("ReferenceToMethodFromCallOnMethodCallChain.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName("getName")));
    }

    public void testReferenceToMethodInSuperClass() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName("getCount")));
    }

    public void testReferenceToDelegate() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaDelegateDeclaration.class), hasName("get_size")));
    }

    public void testReferenceToDelegateInInterface() {
        myFixture.configureByFile(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaDelegateDeclaration.class), hasName("method"),
                hasParentOfType(ValaInterfaceDeclaration.class)));
    }
}
