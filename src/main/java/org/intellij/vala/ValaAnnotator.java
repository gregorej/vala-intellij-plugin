package org.intellij.vala;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiElement;
import org.intellij.vala.psi.ValaAttribute;
import org.intellij.vala.psi.ValaFieldDeclaration;
import org.intellij.vala.psi.ValaIdentifier;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.INSTANCE_FIELD;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.MARKUP_TAG;
import static com.intellij.openapi.editor.DefaultLanguageHighlighterColors.METADATA;

public class ValaAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        ProgressManager.checkCanceled();
        if (isClassFieldAccess(psiElement)) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(INSTANCE_FIELD);
        } else if (isAttribute(psiElement)) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(METADATA);
        } else if (psiElement.getNode().getElementType() == ValaTypes.PREPROCESSOR_DIRECTIVE) {
            annotationHolder.createInfoAnnotation(psiElement, null).setTextAttributes(MARKUP_TAG);
        }
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
