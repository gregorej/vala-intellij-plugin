package org.intellij.vala.lexer;

import com.intellij.psi.TokenType;
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

        assertThat(valaLexer.getTokenType(), is(TokenType.WHITE_SPACE));
    }

    @Test
    public void shouldAdvanceOnMultipleContents() throws IOException {
        valaLexer.start("\"string\" 3");
        valaLexer.advance();
        valaLexer.advance();

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INT));
    }

    @Test
    public void shouldHandleInvalidCharacters() throws IOException {
        valaLexer.start("not_string");

        assertThat(valaLexer.getTokenType(), is(TokenType.BAD_CHARACTER));
    }

    @Test
    public void shouldProceedAfterSeeingBadCharacter() throws IOException {
        valaLexer.start("na 3");
        valaLexer.advance();
        valaLexer.advance();
        valaLexer.advance();

        assertThat(valaLexer.getTokenType(), is(ValaTypes.INT));
    }

    @Test
    public void shouldRecognizeClassKeyword() throws IOException {
         valaLexer.start("class");

        assertThat(valaLexer.getTokenType(), is(ValaTypes.KEY_CLASS));
    }

}
