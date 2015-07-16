package org.intellij.vala.annotation;


import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ValaIdentifierAnnotationTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/annotation";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    public void testHighlightClassFieldAccess() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        assertThat(textAttributesAtCaret().get(), equalTo(DefaultLanguageHighlighterColors.INSTANCE_FIELD));
    }

    public void testHighlightAttribute() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        assertThat(textAttributesAtCaret().get(), equalTo(DefaultLanguageHighlighterColors.METADATA));
    }

    public void testHighlightPropertyContextKeywords() {
        myFixture.configureByFiles(getTestName(false) + ".vala");

        assertThat(textAttributesAtCaret().get(), equalTo(DefaultLanguageHighlighterColors.KEYWORD));
    }

    private Optional<TextAttributesKey> textAttributesAtCaret() {
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting();
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        for (HighlightInfo info : highlightInfos) {
            final TextRange textRange = elementAtCaret.getTextRange();
            if (info.getStartOffset() == textRange.getStartOffset() && info.getEndOffset() == textRange.getEndOffset()) {
                return Optional.ofNullable(info.forcedTextAttributesKey);
            }
        }
        return Optional.empty();
    }
}
