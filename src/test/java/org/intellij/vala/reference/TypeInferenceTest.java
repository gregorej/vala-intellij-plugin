package org.intellij.vala.reference;

import com.intellij.testFramework.ParsingTestCase;
import org.intellij.vala.parser.ValaParserDefinition;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.inference.ExpressionTypeInference;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TypeInferenceTest extends ParsingTestCase {

    public TypeInferenceTest() {
        super("inference", "vala", new ValaParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/org/intellij/vala/parser/test";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testInferFromVariableReference() {
        final String code = "int main(string [] args) { int a = 0; var inferred = a} ";
        assertThat(inferredType(code), is(ValaTypeDescriptor.INTEGER));
    }

    private ValaTypeDescriptor inferredType(String code) {
        ValaFile file = (ValaFile) this.createPsiFile(getTestName(false), code);
        ValaMethodDeclaration methodDeclaration = getMainMethod(file);
        ValaExpression expression = findVariableWithInferredType(methodDeclaration);
        file.getFileType();
        return ExpressionTypeInference.inferType((ValaPrimaryExpression) expression);
    }

    private static ValaExpression findVariableWithInferredType(ValaMethodDeclaration methodDeclaration) {
        for (ValaStatement statement : methodDeclaration.getBlock().getStatementList()) {
            if (statement instanceof ValaLocalVariableDeclarations) {
                ValaLocalVariableDeclarations declarations = (ValaLocalVariableDeclarations) statement;
                if (declarations.getType() == null) {
                    for (ValaLocalVariableDeclaration declaration : declarations.getLocalVariableDeclarationList()) {
                        final ValaLocalVariable localVariable = declaration.getLocalVariable();
                        if (localVariable != null && localVariable.getName().equals("inferred")) {
                            return localVariable.getExpression();
                        }
                    }
                }
            }
        }
        return null;
    }

    private static ValaMethodDeclaration getMainMethod(ValaFile file) {
        return (ValaMethodDeclaration) file.getDeclarations().get(0);

    }
}
