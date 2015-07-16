package org.intellij.vala.psi;

import com.google.common.collect.ImmutableMap;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;

import java.util.Map;
import java.util.Optional;

public enum BasicTypeDescriptor implements ValaTypeDescriptor {
    CHARACTER("char"),
    LONG("long"),
    DOUBLE("double"),
    BOOL("bool"),
    INTEGER("int"),
    STRING("string"),
    VOID("void");

    private static final Map<String, BasicTypeDescriptor> BASIC_TYPE_ELEMENT_TO_DESCRIPTOR = constructMap();

    private static Map<String, BasicTypeDescriptor> constructMap() {
        ImmutableMap.Builder<String, BasicTypeDescriptor> builder = ImmutableMap.builder();
        for (BasicTypeDescriptor descriptor : BasicTypeDescriptor.values()) {
            builder.put(descriptor.basicTypeName, descriptor);
        }
        return builder.build();
    }

    private final QualifiedName qualifiedName;
    private String basicTypeName;

    private BasicTypeDescriptor(String basicTypeName) {
        this.basicTypeName = basicTypeName;
        this.qualifiedName = QualifiedNameBuilder.nameOf(basicTypeName);
    }

    public String toString() {
        return basicTypeName;
    }

    public static Optional<ValaTypeDescriptor> forName(String name) {
        return Optional.ofNullable(BASIC_TYPE_ELEMENT_TO_DESCRIPTOR.get(name));
    }

    @Override
    public QualifiedName getQualifiedName() {
        return qualifiedName;
    }
}
