package org.intellij.vala.folding;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

public class ValaFoldingTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/folding/test";
    }

    private void doTest() {
        myFixture.testFoldingWithCollapseStatus(getTestDataPath() + "/" + getTestName(false) + ".vala");
    }

    public void testSimpleCLI() {
        doTest();
    }

    public void testClassAndMethods() {
        doTest();
    }

    public void testNamespace() {
        doTest();
    }
}
