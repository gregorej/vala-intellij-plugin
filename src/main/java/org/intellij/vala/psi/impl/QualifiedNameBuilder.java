package org.intellij.vala.psi.impl;


import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.intellij.vala.psi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static org.apache.commons.lang.StringUtils.join;

public class QualifiedNameBuilder implements QualifiedName {

    private final List<String> parts = new ArrayList<String>();

    @Override
    public String getTail() {
        return parts.get(parts.size() - 1);
    }

    @Override
    public QualifiedName append(String member) {
        QualifiedNameBuilder newName = new QualifiedNameBuilder();
        newName.parts.addAll(this.parts);
        newName.parts.add(member);
        return newName;
    }

    public static QualifiedName nameOf(String ... parts) {
        QualifiedNameBuilder builder = new QualifiedNameBuilder();
        builder.parts.addAll(Arrays.asList(parts));
        return builder;
    }

    @Override
    public void write(StubOutputStream stubOutputStream) throws IOException {
        stubOutputStream.writeVarInt(parts.size());
        for (String part : parts) {
            stubOutputStream.writeName(part);
        }
    }

    public static QualifiedName read(StubInputStream inputStream) throws IOException {
        int length = inputStream.readVarInt();
        QualifiedNameBuilder qName = new QualifiedNameBuilder();
        while (length-- > 0) {
            qName.parts.add(inputStream.readName().getString());
        }
        return qName;
    }

    public static QualifiedName forMethodDeclaration(ValaMethodDeclaration methodDeclaration) {
        final String methodName = methodDeclaration.getIdentifier().getText();
        ValaDeclaration container = getParentOfType(methodDeclaration, ValaDeclaration.class, false);
        if (container == null) {
            QualifiedNameBuilder qName = new QualifiedNameBuilder();
            qName.parts.add(methodName);
            return qName;
        } else {
            return container.getQName().append(methodName);
        }
    }

    private static QualifiedName append(QualifiedName qualifiedName, ValaSymbol valaSymbol) {
        QualifiedName result = qualifiedName;
        for (ValaSymbolPart symbolPart : valaSymbol.getSymbolPartList()) {
            result = result.append(symbolPart.getName());
        }
        return result;
    }

    public static QualifiedName forClassDeclaration(ValaClassDeclaration classDeclaration) {
        ValaDeclaration container = getParentOfType(classDeclaration.getParent(), ValaDeclaration.class, false);
        QualifiedName qName;
        if (container == null) {
            qName = new QualifiedNameBuilder();
        } else {
            qName = container.getQName();
        }
        return append(qName, classDeclaration.getSymbol());
    }

    public static QualifiedName forNamespaceDeclaration(ValaNamespaceDeclaration namespaceDeclaration) {
        ValaDeclaration container = getParentOfType(namespaceDeclaration.getParent(), ValaDeclaration.class, false);
        QualifiedName qName;
        if (container == null) {
            qName = new QualifiedNameBuilder();
        } else {
            qName = container.getQName();
        }
        return append(qName, namespaceDeclaration.getSymbol());
    }

    public int hashCode() {
        return parts.hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof QualifiedNameBuilder && ((QualifiedNameBuilder) o).parts.equals(this.parts);
    }

    public String toString() {
        return join(parts, ".");
    }
}
