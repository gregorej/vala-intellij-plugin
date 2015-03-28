package org.intellij.vala.completion;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupEx;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import javax.annotation.processing.Completion;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;


public class ConstructorCompletionTest extends LightPlatformCodeInsightFixtureTestCase {

    public ConstructorCompletionTest() {
        PlatformTestCase.initPlatformPrefix("not_existing_class", "PlatformLangXml");
    }

    private static final int SINGLE_INVOCATION = 1;

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/completion/test";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    public void testSingleConstructorCompletion() {
        doTest();
    }

    public void testClassNameInVariableTypeCompletion() {
        doTest();
    }

    public void testSingleConstructorCompletionFromImportedNamespace() {
        doTest();
    }

    public void testSingleNamedConstructorCompletion() {
        doTest();
    }

    public void testSingleNamedConstructorCompletionWithNameBeginWithDot() {
        doTest();
    }

    public void testClassNameWithoutContextCompletion() {
        doTest();
    }

    public void testSingleNamedConstructorCompletionRightAfterDot() {
        myFixture.configureByFiles(this.getTestName(false) + ".vala");

        List<LookupElement> elements = Arrays.asList(myFixture.completeBasic());

        assertThat(elements, contains(lookupElement("from_string")));
    }

    private static Matcher<? super LookupElement> lookupElement(String lookupString) {
        return new CustomTypeSafeMatcher<LookupElement>("Lookup element with lookup string " + lookupString) {
            @Override
            protected boolean matchesSafely(LookupElement lookupElement) {
                return lookupString.equals(lookupElement.getLookupString());
            }
        };
    }

    private void doTest() {
        myFixture.configureByFiles(this.getTestName(false) + "Before.vala");

        myFixture.complete(CompletionType.BASIC, SINGLE_INVOCATION);

        myFixture.checkResultByFile(this.getTestName(false) + "After.vala");
    }
}
