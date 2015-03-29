package org.intellij.vala.completion;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;

public class DefaultConstructorInsertHandler extends ParenthesesInsertHandler<LookupElement> {

    @Override
    protected boolean placeCaretInsideParentheses(InsertionContext insertionContext, LookupElement lookupElement) {
        return false;
    }
}
