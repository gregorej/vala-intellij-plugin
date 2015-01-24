package org.intellij.vala.parser;

import com.intellij.testFramework.ParsingTestCase;


public abstract class AbstractValaParserTest extends ParsingTestCase {

    public AbstractValaParserTest(String scope) {
        super(scope, "vala", new ValaParserDefinition());
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/parser/test";
    }
}
