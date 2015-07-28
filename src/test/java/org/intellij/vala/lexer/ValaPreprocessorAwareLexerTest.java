package org.intellij.vala.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.intellij.vala.lexer.ValaPreprocessorAwareLexer.INACTIVE_CODE;
import static org.intellij.vala.psi.ValaTypes.*;
import static org.junit.Assert.assertThat;

public class ValaPreprocessorAwareLexerTest {

    private ValaPreprocessorAwareLexer lexer = new ValaPreprocessorAwareLexer();

    @Test
    public void testReadSimplePreprocessorExpressions() throws Exception {
        String content = readToString("org/intellij/vala/lexer/SimplePreprocessor.vala");

        lexer.start(content);

        List<Token> tokens = consumeTokensIgnoringWhiteSpace();

        assertThat(tokens, contains(
                token(PREPROCESSOR_DIRECTIVE),
                token(KEY_STRUCT),
                token(LEFT_CURLY),
                token(PREPROCESSOR_DIRECTIVE, equalTo("#else\n")),
                token(INACTIVE_CODE, equalTo("class {\n")),
                token(PREPROCESSOR_DIRECTIVE, equalTo("#endif\n")),
                token(RIGHT_CURLY)));

    }

    private List<Token> consumeTokensIgnoringWhiteSpace() {
        IElementType type;
        List<Token> types = new ArrayList<>();
        while ((type = lexer.getTokenType()) != null) {
            if (type != TokenType.WHITE_SPACE) {
                types.add(new Token(type, lexer.getTokenText(), lexer.getTokenStart(), lexer.getTokenEnd()));
            }
            lexer.advance();
        }

        types = types.stream().filter(t -> t.getType() != TokenType.WHITE_SPACE).collect(Collectors.toList());
        return types;
    }

    private static String readToString(String file) throws Exception {
        final URL resource = ValaPreprocessorAwareLexerTest.class.getClassLoader().getResource(file);
        if (resource == null) {
            throw new IOException("Could not find " + file + " in classpath");
        }
        byte[] encoded = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(encoded);
    }

    private static class Token {
        private IElementType type;
        private String text;
        private int start;

        private int end;

        public Token(IElementType type, String text, int start, int end) {
            this.type = type;
            this.text = text;
            this.start = start;
            this.end = end;
        }

        public IElementType getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String toString() {
            return type + " [" + text + "] (" + start + ", " + end + ")";
        }
    }

    private static Matcher<Token> token(IElementType type) {
        return token(type, Matchers.any(String.class));
    }

    private static Matcher<Token> token(IElementType elementType, Matcher<String> text) {
        return new CustomTypeSafeMatcher<Token>("Token of type " + elementType + " with text " + text) {
            @Override
            protected boolean matchesSafely(Token token) {
                return token.getType() == elementType && text.matches(token.getText());
            }
        };
    }


}
