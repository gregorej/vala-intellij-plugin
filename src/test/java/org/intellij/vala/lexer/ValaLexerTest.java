package org.intellij.vala.lexer;

import org.intellij.vala.ValaTypes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ValaLexerTest {

    private ValaLexer valaLexer;

    @Before
    public void before() {
        valaLexer = new ValaLexer();
    }

    @Test
    public void shouldDetectString() throws IOException {
        String content = "\"blah\"";
        valaLexer.start(content);

        assertThat(valaLexer.getTokenType(), is(ValaTypes.STRING_LITERAL));
    }

    @Test
    public void shouldDetectInteger() throws IOException {
        valaLexer.start("2");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INT));
    }

    @Test
    public void shouldHandleEmptyContent() throws IOException {
        valaLexer.start("");

        assertThat(valaLexer.getTokenType(), is(nullValue()));
    }

    @Test
    public void shouldHandleWhiteSpace() throws IOException {
        valaLexer.start(" \t\t\t  ");

        assertThat(valaLexer.getTokenType(), is(nullValue()));
    }

    @Test
    public void shouldAdvanceOnMultipleContents() throws IOException {
        valaLexer.start("\"string\" 3");
        valaLexer.advance();

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INT));
    }
}
