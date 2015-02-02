package org.intellij.vala.parser;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import static org.intellij.vala.psi.PsiMatchers.hasNoErrors;
import static org.junit.Assert.assertThat;

public class ComplexValidFilesParserTest extends AbstractValaParserTest {

    public ComplexValidFilesParserTest() {
        super("complex");
    }

    @Parameterized.Parameters
    public static Collection<String> files() {
        return ImmutableList.<String>builder()
                .add("SearchDialog")
                .add("Mixins")
                .add("DemoService")
                .add("Device")
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
