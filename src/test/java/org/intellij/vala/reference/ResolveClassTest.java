package org.intellij.vala.reference;


import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.*;

import static org.hamcrest.Matchers.*;
import static org.intellij.vala.psi.PsiMatchers.*;
import static org.junit.Assert.assertThat;

public class ResolveClassTest extends ValaReferenceTestBase {

    public static final boolean NOT_STRICT = false;

    public void testDetectingElementAtCaret() {
        myFixture.configureByFiles("ResolveClassDefinitionInSameFile.vala");

        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

        assertThat(myFixture.getFile(), hasNoErrors());
        assertThat(elementAtCaret, hasParentOfType(ValaFieldDeclaration.class));
    }

    public void testResolveClassDefinitionInSameFile() {
        myFixture.configureByFiles("ResolveClassDefinitionInSameFile.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSymbolPart.class).getReference().resolve();

        assertThat(referencedElement, instanceOf(ValaClassDeclaration.class));
    }

    public void testResolveClassDefinitionInAnotherFile() {
        myFixture.configureByFiles("FileContainingClassReference.vala", "FileContainingClassDefinition.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSymbolPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaClassDeclaration.class), isInFile(hasName(containsString("FileContainingClassDefinition")))));
    }

    public void testResolveClassDefaultConstructorInAnotherFile() {
        myFixture.configureByFiles("FileContainingDefaultClassContructorReference.vala", "FileContainingClassDefinition.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaClassDeclaration.class), isInFile(hasName(containsString("FileContainingClassDefinition")))));
    }

    public void testResolveNamedConstructorInAnotherFile() {
        myFixture.configureByFiles("FileContainingNamedConstructorReference.vala", "ClassWithMultipleConstructors.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaCreationMethodDeclaration.class), isInFile(hasName(containsString("ClassWithMultipleConstructors")))));
    }

    public void testResolveClassDefinitionWithFullyDeclaredNamespaceInSameFile() {
        myFixture.configureByFiles("ResolveClassDefinitionWithFullyDeclaredNamespaceInSameFile.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(aClassDeclarationThat(hasName("SomeClass")), isInFile(hasName(containsString("ResolveClassDefinitionWithFullyDeclaredNamespaceInSameFile")))));
    }

    public void testResolveClassDefinitionInSameFileButInNestedNamespace() {
        myFixture.configureByFiles("ResolveClassDefinitionInSameFileButInNestedNamespace.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSymbolPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(aClassDeclarationThat(hasName("SomeClass")), isInFile(hasName(containsString("ResolveClassDefinitionInSameFileButInNestedNamespace")))));
    }

    public void testResolveNamedConstructorForClassInImportedNamespace() {
        myFixture.configureByFiles("ResolveNamedConstructorForClassInImportedNamespace.vala", "ClassWithMultipleConstructorsInNamespace.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(instanceOf(ValaCreationMethodDeclaration.class), isInFile(hasName(containsString("ClassWithMultipleConstructorsInNamespace")))));
    }

    public void testDoNotResolveNamedConstructorForClassInImportedNamespaceIfNamespaceIsNotUsed() {
        myFixture.configureByFiles("FileContainingNamedConstructorReference.vala", "ClassWithMultipleConstructorsInNamespace.vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaMemberPart.class).getReference().resolve();

        assertThat(referencedElement, nullValue());
    }

    public void testReferenceToSuperType() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSymbolPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasName("Parent"), instanceOf(ValaClassDeclaration.class)));
    }

}
