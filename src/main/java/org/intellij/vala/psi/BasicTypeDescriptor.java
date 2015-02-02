package org.intellij.vala.psi;

import com.google.common.collect.ImmutableMap;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.lexer.ValaLexer;

import java.util.Map;


public enum BasicTypeDescriptor implements ValaTypeDescriptor {
    CHARACTER(ValaTypes.TYPE_CHAR),
    LONG(ValaTypes.TYPE_LONG),
    DOUBLE(ValaTypes.TYPE_DOUBLE),
    BOOL(ValaTypes.TYPE_BOOL),
    INTEGER(ValaTypes.TYPE_INT),
    STRING(ValaTypes.TYPE_STRING);

    private static final Map<String, BasicTypeDescriptor> BASIC_TYPE_ELEMENT_TO_DESCRIPTOR = constructMap();

    private static Map<String, BasicTypeDescriptor> constructMap() {
        ImmutableMap.Builder<String, BasicTypeDescriptor> builder = ImmutableMap.builder();
        for (BasicTypeDescriptor descriptor : BasicTypeDescriptor.values()) {
            builder.put(descriptor.elementType.toString(), descriptor);
        }
        return builder.build();
    }

    private BasicTypeDescriptor(String basicTypeName) {
        this.basicTypeName = basicTypeName;
    }
    private BasicTypeDescriptor(IElementType basicTypeName) {
        this(basicTypeName.toString());
        this.elementType = basicTypeName;
    }

    private String basicTypeName;

    private IElementType elementType;

    public String toString() {
        return basicTypeName;
    }

    public static BasicTypeDescriptor forType(ValaBuiltInType builtInType) {
        return BASIC_TYPE_ELEMENT_TO_DESCRIPTOR.get(builtInType.getText());
    }

    public static BasicTypeDescriptor forType(IElementType basicTypeElement) {
        return BASIC_TYPE_ELEMENT_TO_DESCRIPTOR.get(basicTypeElement.toString());
    }

    @Override
    public QualifiedName getQualifiedName() {
        return null;
    }
}
