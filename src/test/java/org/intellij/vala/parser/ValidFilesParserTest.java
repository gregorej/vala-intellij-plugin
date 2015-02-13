package org.intellij.vala.parser;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiFile;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import static org.intellij.vala.psi.PsiMatchers.hasNoErrors;
import static org.junit.Assert.assertThat;

public class ValidFilesParserTest extends AbstractValaParserTest {

    public ValidFilesParserTest() {
        super("valid");
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
                .add("BlockComment")
                .add("Switch")
                .add("SimpleLibrary")
                .build();
    }

    public void testShouldHaveNoErrors() throws IOException {
        for (String fileName : files()) {
            String content = this.loadFile(fileName + ".vala");
            PsiFile file = this.createPsiFile(fileName, content);

            assertThat(fileName + " has parsing errors", file, hasNoErrors());
        }
    }
}
