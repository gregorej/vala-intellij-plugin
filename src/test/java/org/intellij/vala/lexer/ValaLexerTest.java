package org.intellij.vala.lexer;

import org.intellij.vala.ValaTypes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
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
}
