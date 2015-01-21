package org.intellij.vala.reference.method;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaMethodDeclaration;
import org.intellij.vala.psi.ValaSimpleName;
import org.intellij.vala.reference.ValaReferenceTestBase;

import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.hasName;
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

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName("open")));
    }

    public void testReferenceToMethodFromCallOnMethodCallChain() {
        myFixture.configureByFiles("ReferenceToMethodFromCallOnMethodCallChain.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaMethodDeclaration.class), hasName("getName")));
    }
}
