package org.intellij.vala.reference.method;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.*;
import org.intellij.vala.reference.ValaReferenceTestBase;

import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.intellij.vala.psi.PsiMatchers.hasParentOfType;
import static org.intellij.vala.psi.PsiMatchers.identifier;
import static org.intellij.vala.psi.PsiMatchers.nameOfMethodDeclaration;
import static org.junit.Assert.assertThat;

public class ResolveMethodDeclaration extends ValaReferenceTestBase {

    public void testReferenceMethodInSameClass() {
        myFixture.configureByFiles("FileContainingMethodCallFromSameClass.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, nameOfMethodDeclaration("method1"));
    }

    public void testReferenceMethodInSameFileSameNamespaceLevel() {
        myFixture.configureByFiles("FileContainingReferenceToMethodInSameFile.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSimpleName.class).getReference().resolve();

        assertThat(referencedElement, nameOfMethodDeclaration("run"));
    }

    public void testReferenceToMethodFromCallOnExplicitObject() {
        myFixture.configureByFiles("ReferenceToMethodFromCallOnExplicitObject.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, nameOfMethodDeclaration("open"));
    }

    public void testReferenceToMethodFromCallOnMethodCallChain() {
        myFixture.configureByFiles("ReferenceToMethodFromCallOnMethodCallChain.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, nameOfMethodDeclaration("getName"));
    }

    public void testReferenceToMethodInSuperClass() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, nameOfMethodDeclaration("getCount"));
    }

    public void testReferenceToDelegate() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaDelegateDeclaration.class), identifier("get_size")));
    }

    public void testReferenceToDelegateInInterface() {
        myFixture.configureByFile(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaIdentifier.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaDelegateDeclaration.class), identifier("method"),
                hasParentOfType(ValaInterfaceDeclaration.class)));
    }
}
