package org.intellij.vala.lexer;

import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.psi.ValaTokenType;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

import static org.intellij.vala.psi.ValaTypes.PREPROCESSOR_DIRECTIVE;

public class ValaPreprocessorAwareLexer extends DelegateLexer {

    private final PreprocessorExpressionEvaluator evaluator= new PreprocessorExpressionEvaluator();

    private static enum PreprocessorState {
        OUTSIDE,
        ACTIVE,
        INACTIVE
    }

    private String inactiveTokenText;
    private Stack<PreprocessorState> states = new Stack<>();
    private int inactiveTokenStart;
    private int inactiveTokenEnd;

    private PreprocessorState preprocessorState = PreprocessorState.OUTSIDE;

    public static final IElementType INACTIVE_CODE = new ValaTokenType("INACTIVE_CODE");
    public ValaPreprocessorAwareLexer() {
        super(new ValaLexer());
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        super.start(buffer, startOffset, endOffset, initialState);
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        if (preprocessorState == PreprocessorState.INACTIVE) {
            return INACTIVE_CODE;
        } else {
            return super.getTokenType();
        }
    }

    @Override
    public int getTokenStart() {
        if (preprocessorState == PreprocessorState.INACTIVE) {
            return inactiveTokenStart;
        } else {
            return super.getTokenStart();
        }
    }

    @Override
    public int getTokenEnd() {
        if (preprocessorState == PreprocessorState.INACTIVE) {
            return inactiveTokenEnd;
        } else {
            return super.getTokenEnd();
        }
    }

    @NotNull
    @Override
    public String getTokenText() {
        if (preprocessorState == PreprocessorState.INACTIVE) {
            return inactiveTokenText;
        } else {
            return super.getTokenText();
        }
    }

    @Override
    public void advance() {
        handlePreprocessorDirectives();
        super.advance();
        if (preprocessorState == PreprocessorState.INACTIVE) {
            consumeInactiveTokens();
        }
    }

    private void handlePreprocessorDirectives() {
        final Lexer delegate = getDelegate();
        final IElementType currentTokenType = delegate.getTokenType();
        if (currentTokenType == PREPROCESSOR_DIRECTIVE) {
            final String tokenText = delegate.getTokenText();
            if (tokenText.startsWith("#if")) {
                handleIf(tokenText);
            } else if (tokenText.startsWith("#else")) {
                handleElse();
            } else if (tokenText.startsWith("#endif")) {
                handleEndif();
            }
        } else if (currentTokenType == null || preprocessorState == PreprocessorState.INACTIVE) {
            preprocessorState = PreprocessorState.OUTSIDE;
        }
    }

    private void handleEndif() {
        if (states.empty()) {
            preprocessorState = PreprocessorState.OUTSIDE;
        } else {
            preprocessorState = states.pop();
        }
    }

    private void consumeInactiveTokens() {
        StringBuilder builder = new StringBuilder();
        final Lexer delegate = getDelegate();
        LexerPosition lastPosition = delegate.getCurrentPosition();
        this.inactiveTokenStart = delegate.getTokenStart();
        while (delegate.getTokenType() != null && delegate.getTokenType() != PREPROCESSOR_DIRECTIVE) {
            builder.append(delegate.getTokenText());
            this.inactiveTokenEnd = delegate.getTokenEnd();
            lastPosition = delegate.getCurrentPosition();
            delegate.advance();
        }
        delegate.restore(lastPosition);
        this.inactiveTokenText = builder.toString();
    }

    private void handleElse() {
        if (preprocessorState == PreprocessorState.ACTIVE) {
            preprocessorState = PreprocessorState.INACTIVE;
        } else {
            preprocessorState = PreprocessorState.ACTIVE;
        }
    }

    private void handleIf(String tokenText) {
        states.push(preprocessorState);
        if (evaluator.evaluate(tokenText)) {
            preprocessorState = PreprocessorState.ACTIVE;
        } else {
            preprocessorState = PreprocessorState.INACTIVE;
        }
    }
}
