package org.intellij.vala.psi;

public class ReferenceTypeDescriptor implements ValaTypeDescriptor {



    private final ValaTypeBase type;
    private QualifiedName qualifiedName;

    private ReferenceTypeDescriptor(ValaTypeBase type) {
        this.type = type;
    }

    private ReferenceTypeDescriptor() {
        this((ValaTypeBase) null);
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

    public static ValaTypeDescriptor forType(ValaType type) {
        if (type.getArrayTypeList().isEmpty() && type.getTypeBase() != null && type.getTypeBase().getBuiltInType() != null) {
            return BasicTypeDescriptor.forType(type.getTypeBase().getBuiltInType());
        }
        return new ReferenceTypeDescriptor(type.getTypeBase());
    }

    public static ValaTypeDescriptor forType(ValaTypeWeak type) {
        if (type.getArrayTypeList().isEmpty() && type.getTypeBase() != null && type.getTypeBase().getBuiltInType() != null) {
            return BasicTypeDescriptor.forType(type.getTypeBase().getBuiltInType());
        }
        return new ReferenceTypeDescriptor(type.getTypeBase());
    }

    public static ValaTypeDescriptor forType(ValaBuiltInType type) {
        return BasicTypeDescriptor.forType(type);
    }

    public boolean sameAs(ValaTypeDescriptor otherTypeDescriptor) {
        if (otherTypeDescriptor == this) return true;
        if (otherTypeDescriptor.getQualifiedName().equals(this.qualifiedName)) return true;
        return false;
    }

    public static class ValaTypeTypeDescriptor extends ReferenceTypeDescriptor {
    }
}