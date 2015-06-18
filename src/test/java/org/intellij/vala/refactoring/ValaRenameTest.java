package org.intellij.vala.refactoring;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

public class ValaRenameTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/refactoring";
    }

    protected boolean isWriteActionRequired() {
        return false;
    }

    protected void checkRenameTo(String targetName) {
        myFixture.configureByFiles(this.getTestName(false) + "Before.vala");

        myFixture.renameElementAtCaret(targetName);

        myFixture.checkResultByFile(this.getTestName(false) + "After.vala");
    }

    public void testRenameLocalVariable() {
        checkRenameTo("localRenamed");
    }

    public void testRenameClass() {
        checkRenameTo("MyClassRenamed");
    }

    public void testRenameLocalVariableWithMethodCallOnIt() {
        checkRenameTo("goodName");
    }
}
