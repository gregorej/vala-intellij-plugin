package org.intellij.vala.psi.impl;


import org.intellij.vala.psi.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.intellij.psi.util.PsiTreeUtil.getParentOfType;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.join;

public class QualifiedNameBuilder implements QualifiedName {

    private final List<String> parts;

    public static final QualifiedName ROOT = new QualifiedNameBuilder();

    private QualifiedNameBuilder(List<String> parts) {
        this.parts = parts;
    }

    public QualifiedNameBuilder() {
        this(new ArrayList<String>());
    }

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
        return nameOf(asList(parts));
    }

    public static QualifiedName nameOf(List<String> parts) {
        QualifiedNameBuilder builder = new QualifiedNameBuilder();
        builder.parts.addAll(parts);
        return builder;
    }

    @Override
    public void write(DataOutput stubOutputStream) throws IOException {
        stubOutputStream.writeInt(parts.size());
        for (String part : parts) {
            byte [] bytes = part.getBytes();
            stubOutputStream.writeInt(bytes.length);
            stubOutputStream.write(bytes);
        }
    }

    @Override
    public int length() {
        return parts.size();
    }

    @Override
    public QualifiedName getPrefix(int segmentCount) {
        return new QualifiedNameBuilder(newArrayList(parts.subList(0, segmentCount)));
    }

    @Override
    public QualifiedName append(QualifiedName name) {
        QualifiedNameBuilder qualifiedNameBuilder = new QualifiedNameBuilder();
        qualifiedNameBuilder.parts.addAll(parts);
        qualifiedNameBuilder.parts.addAll(extractParts(name));
        return qualifiedNameBuilder;
    }

    private static List<String> extractParts(QualifiedName name) {
        List<String> parts = new ArrayList<String>(name.length());
        for (int i = 1; i <= name.length(); i++) {
            parts.add(name.getPrefix(i).getTail());
        }
        return parts;
    }

    public static QualifiedNameBuilder from(ValaSymbol symbol) {
        QualifiedNameBuilder builder = new QualifiedNameBuilder();
        for (ValaSymbolPart symbolPart : symbol.getSymbolPartList()) {
            builder.parts.add(symbolPart.getName());
        }
        return builder;
    }

    public static QualifiedNameBuilder from(ValaMember member) {
        QualifiedNameBuilder builder = new QualifiedNameBuilder();
        for (ValaMemberPart memberPart : member.getMemberPartList()) {
            builder.parts.add(memberPart.getName());
        }
        return builder;
    }

    public static QualifiedName from(ValaCreationMethodDeclaration creationMethodDeclaration) {
        ValaClassDeclaration valaClass = getParentOfType(creationMethodDeclaration, ValaClassDeclaration.class, false);
        return append(valaClass.getQName(), creationMethodDeclaration.getSymbol());
    }

    public static QualifiedName read(DataInput inputStream) throws IOException {
        int length = inputStream.readInt();
        QualifiedNameBuilder qName = new QualifiedNameBuilder();
        while (length-- > 0) {
            int partLength = inputStream.readInt();
            byte [] content = new byte[partLength];
            inputStream.readFully(content);
            qName.parts.add(new String(content));
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

    public static QualifiedName append(QualifiedName qualifiedName, ValaSymbol valaSymbol) {
        QualifiedName result = qualifiedName;
        for (ValaSymbolPart symbolPart : valaSymbol.getSymbolPartList()) {
            result = result.append(symbolPart.getName());
        }
        return result;
    }

    public static QualifiedName append(QualifiedName qualifiedName, ValaMember member) {
        QualifiedName result = qualifiedName;
        for (ValaMemberPart memberPart : member.getMemberPartList()) {
            result = result.append(memberPart.getName());
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
