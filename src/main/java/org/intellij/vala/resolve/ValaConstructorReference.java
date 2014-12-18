package org.intellij.vala.resolve;


import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.intellij.vala.psi.ValaMemberPart;
import org.intellij.vala.psi.ValaObjectOrArrayCreationExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValaConstructorReference extends PsiReferenceBase<ValaObjectOrArrayCreationExpression> {

    private ClassDeclarationResolver resolver;

    public ValaConstructorReference(ValaObjectOrArrayCreationExpression element) {
        super(element, calculateRange(element));
        resolver = new ClassDeclarationResolver(element.getProject());
    }

    private static TextRange calculateRange(ValaObjectOrArrayCreationExpression expression) {
        ValaMemberPart memberPart = expression.getMember().getMemberPartList().get(0);
        return memberPart.getTextRange();
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        String name = myElement.getMember().getText();
        return resolver.resolve(name);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
