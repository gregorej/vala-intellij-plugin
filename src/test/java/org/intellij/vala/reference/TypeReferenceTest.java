package org.intellij.vala.reference;

import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.ValaClassDeclaration;
import org.intellij.vala.psi.ValaSymbolPart;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.intellij.vala.psi.PsiMatchers.hasName;
import static org.intellij.vala.psi.PsiMatchers.hasParentOfType;
import static org.junit.Assert.assertThat;

public class TypeReferenceTest extends ValaReferenceTestBase {

    public void testReferenceToTypeDeclarationFromTypeInFieldDeclaration() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        PsiElement referencedElement = getElementOfTypeAtCaret(ValaSymbolPart.class).getReference().resolve();

        assertThat(referencedElement, allOf(hasParentOfType(ValaClassDeclaration.class), instanceOf(ValaSymbolPart.class), hasName("SomeType")));
    }
}
