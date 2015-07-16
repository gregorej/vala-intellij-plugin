package org.intellij.vala;

import com.google.common.collect.ImmutableSet;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.vala.lexer.ValaLexer;
import org.intellij.vala.psi.ValaAttribute;
import org.intellij.vala.psi.ValaFieldDeclaration;
import org.intellij.vala.psi.ValaIdentifier;
import org.intellij.vala.psi.ValaPropertyDeclaration;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.INSTANCE_FIELD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.MARKUP_TAG;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.METADATA;

public class ValaAnnotator implements Annotator {

    private static final Set<String> VALA_BUILT_IN_TYPES = ImmutableSet.<String>builder()
            .add("int")
            .add("float")
            .add("string")
            .add("bool")
            .add("char")
            .add("double")
            .add("long")
            .add("unichar")
            .build();

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        ProgressManager.checkCanceled();
        if (VALA_BUILT_IN_TYPES.contains(psiElement.getText())) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(KEYWORD);
        }
        if (isPropertyContextKeyword(psiElement)) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(KEYWORD);
        }
        if (isClassFieldAccess(psiElement)) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(INSTANCE_FIELD);
        } else if (isAttribute(psiElement)) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(METADATA);
        } else if (psiElement.getNode().getElementType() == ValaTypes.PREPROCESSOR_DIRECTIVE) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(MARKUP_TAG);
        }
    }

    private static boolean isPropertyContextKeyword(PsiElement psiElement) {
        return ValaLexer.PROPERTY_CONTEXT_KEYWORDS.contains(psiElement.getNode().getElementType())
                && PsiTreeUtil.getParentOfType(psiElement, ValaPropertyDeclaration.class) != null;
    }

    private static boolean isAttribute(PsiElement psiElement) {
        return psiElement instanceof ValaIdentifier
                && psiElement.getParent() instanceof ValaAttribute;
    }

    private static boolean isClassFieldAccess(PsiElement psiElement) {
        return psiElement instanceof ValaIdentifier
                && ((ValaIdentifier) psiElement).resolve().filter(resolved -> resolved instanceof ValaFieldDeclaration).isPresent();
    }
}
