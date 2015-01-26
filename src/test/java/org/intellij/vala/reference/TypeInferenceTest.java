package org.intellij.vala.reference;

import org.intellij.vala.parser.AbstractValaParserTest;
import org.intellij.vala.psi.*;
import org.intellij.vala.psi.inference.ExpressionTypeInference;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.intellij.vala.psi.impl.QualifiedNameBuilder.nameOf;
import static org.junit.Assert.assertThat;

public class TypeInferenceTest extends AbstractValaParserTest {

    public TypeInferenceTest() {
        super("inference");
    }

    public void testInferFromVariableReference() {
        final String code = "int main(string [] args) { int a = 0; var inferred = a} ";
        assertThat(inferredType(code), is(ValaTypeDescriptor.INTEGER));
    }

    public void testInferTypeFromMethodCall() throws IOException {
        String text = loadFile(getTestName(false) + "." + myFileExt);
        assertThat(inferredType(text), is(ValaTypeDescriptor.LONG));
    }

    public void testInferFromLiteral() {
        final String code = "int main(string [] args) { var inferred = \"some string \"} ";
        assertThat(inferredType(code), is(ValaTypeDescriptor.STRING));
    }

    public void testInferTypeFromConstructor() throws IOException {
        String text = loadFile(getTestName(false) + "." + myFileExt);
        assertThat(inferredType(text).getQualifiedName(), is(equalTo(nameOf("MyClass"))));
    }

    private ValaTypeDescriptor inferredType(String code) {
        ValaFile file = (ValaFile) this.createPsiFile(getTestName(false), code);
        ValaMethodDeclaration methodDeclaration = getMainMethod(file);
        ValaExpression expression = findVariableWithInferredType(methodDeclaration);
        file.getFileType();
        return ExpressionTypeInference.inferType(expression);
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
        for (ValaDeclaration declaration : file.getDeclarations()) {
            if (declaration instanceof ValaMethodDeclaration && ((ValaMethodDeclaration) declaration).getName().equals("main")) {
                return (ValaMethodDeclaration) declaration;
            }
        }
        return null;
    }
}
