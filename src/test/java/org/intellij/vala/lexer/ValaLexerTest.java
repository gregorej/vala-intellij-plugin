package org.intellij.vala.lexer;

import com.intellij.psi.TokenType;
import org.intellij.vala.psi.ValaTypes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.*;
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

        assertThat(valaLexer.getTokenType(), is(ValaTypes.STRING));
    }

    @Test
    public void shouldDetectInteger() throws IOException {
        valaLexer.start("2");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INTEGER_LITERAL));
    }

    @Test
    public void shouldDetectNegativeInteger() throws IOException {
        valaLexer.start("-16");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INTEGER_LITERAL));
        assertThat(valaLexer.getTokenText(), is(equalTo("-16")));
    }

    @Test
    public void shouldHandleEmptyContent() throws IOException {
        valaLexer.start("");

        assertThat(valaLexer.getTokenType(), is(nullValue()));
    }

    @Test
    public void shouldHandleWhiteSpace() throws IOException {
        valaLexer.start(" \t\t\t  ");

        assertThat(valaLexer.getTokenType(), is(TokenType.WHITE_SPACE));
    }

    @Test
    public void shouldAdvanceOnMultipleContents() throws IOException {
        valaLexer.start("\"string\" 3");
        valaLexer.advance();
        valaLexer.advance();

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INTEGER_LITERAL));
    }

    @Test
    public void shouldHandleInvalidCharacters() throws IOException {
        valaLexer.start("$%");

        assertThat(valaLexer.getTokenType(), is(TokenType.BAD_CHARACTER));
    }

    @Test
    public void shouldProceedAfterSeeingBadCharacter() throws IOException {
        valaLexer.start("%^ 3");
        valaLexer.advance();
        valaLexer.advance();
        valaLexer.advance();

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INTEGER_LITERAL));
    }

    @Test
    public void shouldRecognizeClassKeyword() throws IOException {
        valaLexer.start("class");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.KEY_CLASS));
    }

    @Test
    public void shouldRecognizeIdentifiers() throws IOException {
        valaLexer.start("some_identifier");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.IDENTIFIER));
        assertThat(valaLexer.getTokenText(), is(equalTo("some_identifier")));
    }

    @Test
    public void shouldParseFloatNumbers() throws IOException {
        valaLexer.start("0.2234");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.REAL_LITERAL));
        assertThat(valaLexer.getTokenText(), is(equalTo("0.2234")));
    }

    @Test
    public void shouldParseNegativeFloatNumbers() throws IOException {
        valaLexer.start("-31.4");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.REAL_LITERAL));
        assertThat(valaLexer.getTokenText(), is(equalTo("-31.4")));
    }

    @Test
    public void shouldDetectVerbatimString() throws IOException {
        final String verbatimString = "\"\"\"some\n\"text\"\"\"\"";
        valaLexer.start(verbatimString);

        assertThat(valaLexer.getTokenText(), is(equalTo(verbatimString)));
        assertThat(valaLexer.getTokenType(), is(ValaTypes.VERBATIM_STRING_LITERAL));
    }

}
