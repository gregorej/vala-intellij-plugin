package org.intellij.vala.psi;

public class ReferenceTypeDescriptor implements ValaTypeDescriptor {

    private final ValaTypeBase type;
    private QualifiedName qualifiedName;

    private ReferenceTypeDescriptor(ValaTypeBase type) {
        this.type = type;
    }

    private ReferenceTypeDescriptor(QualifiedName qualifiedName) {
        this((ValaTypeBase) null);
        this.qualifiedName = qualifiedName;
    }

    public QualifiedName getQualifiedName() {
        return qualifiedName;
    }

    public static ValaTypeDescriptor forQualifiedName(QualifiedName qualifiedName) {
        return new ReferenceTypeDescriptor(qualifiedName);
    }

    public String toString() {
        return qualifiedName.toString();
    }
}