package org.intellij.vala.parser;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import static org.intellij.vala.psi.PsiMatchers.hasNoErrors;
import static org.junit.Assert.assertThat;

public class ValidFilesParserTest extends ParsingTestCase {

    public ValidFilesParserTest() {
        super("valid", "vala", new ValaParserDefinition());
    }

    @Parameterized.Parameters
    public static Collection<String> files() {
        return ImmutableList.<String>builder()
                .add("EmptyClassInNamespace")
                .add("GtkHelloWorld")
                .add("SingleObjectCreation")
                .add("SingleMethodCall")
                .add("SingleLambdaExpression")
                .add("SingleObjectPropertyAssignment")
                .build();
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

    public void testShouldHaveNoErrors() throws IOException {
        for (String fileName : files()) {
            String content = this.loadFile(fileName + ".vala");
            PsiFile file = this.createPsiFile(fileName, content);

            assertThat(fileName + " has parsing errors", file, hasNoErrors());
        }
    }
}
