package org.intellij.vala.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.tree.IElementType;

import java.util.LinkedList;
import java.util.List;

import static org.intellij.vala.lexer.ValaLexer.KEYWORDS;

public class KeywordCollector {
    public List<LookupElement> getProposedLookUpItems() {
        List<LookupElement> result = new LinkedList<LookupElement>();
        for (IElementType keywordTokenType : KEYWORDS.getTypes()) {
            result.add(LookupElementBuilder.create(keywordTokenType.toString()).bold());
        }
        return result;
    }
}
