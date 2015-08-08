package org.intellij.vala.parser;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;

import java.io.File;
import java.io.IOException;

import static org.intellij.vala.psi.PsiMatchers.hasNoErrors;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNotNull;

public class ValaParseSdkVapisTest extends AbstractValaParserTest {

    public ValaParseSdkVapisTest() {
        super("does not matter");
    }

    public void testParseGlibWithNoErrors() throws IOException {
        doTest("glib-2.0.vapi");
    }

    public void testParseGtk20WithNoErrors() throws IOException {
        doTest("gtk+-2.0.vapi");
    }

    public void testParseGtk30WithNoErrors() throws IOException {
        doTest("gtk+-3.0.vapi");
    }

    protected void doTest(String vapiFileName) throws IOException {
        assumeNotNull(System.getenv("VALA_HOME"));

        PsiFile file = parseFile(vapiFileName);

        assertThat(vapiFileName + " had parse errors", file, hasNoErrors());
    }

    private PsiFile parseFile(String vapiFileName) throws IOException {
        String filePath = System.getenv("VALA_HOME") + "/vapi/" + vapiFileName;
        String content = FileUtil.loadFile(new File(filePath), "UTF-8", true).trim();
        PsiFile file = createFile(vapiFileName, content);
        ensureParsed(file);
        return file;
    }
}
