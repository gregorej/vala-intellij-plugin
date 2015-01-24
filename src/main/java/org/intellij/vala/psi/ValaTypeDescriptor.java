package org.intellij.vala.psi;

import com.google.common.collect.ImmutableMap;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.lexer.ValaLexer;

import java.util.Map;

public class ValaTypeDescriptor {

    public static final ValaTypeDescriptor CHARACTER = BasicTypeDescriptor.forType(ValaTypes.TYPE_CHAR);
    public static final ValaTypeDescriptor LONG = BasicTypeDescriptor.forType(ValaTypes.TYPE_LONG);
    private final ValaType type;
    private QualifiedName qualifiedName;

    public static final ValaTypeDescriptor INTEGER = BasicTypeDescriptor.forType(ValaTypes.TYPE_INT);
    public static final ValaTypeDescriptor STRING = BasicTypeDescriptor.forType(ValaTypes.TYPE_STRING);

    private ValaTypeDescriptor(ValaType type) {
        this.type = type;
    }

    private ValaTypeDescriptor() {
        this(null);
    }

    public static ValaTypeDescriptor forType(ValaType type) {
        if (type.getArrayTypeList().isEmpty() && type.getTypeBase() != null && type.getTypeBase().getBuiltInType() != null) {
            return BasicTypeDescriptor.forType(type.getTypeBase().getBuiltInType());
        }
        return new ValaTypeDescriptor(type);
    }

    public boolean sameAs(ValaTypeDescriptor otherTypeDescriptor) {
        if (otherTypeDescriptor == this) return true;
        if (otherTypeDescriptor.qualifiedName.equals(this.qualifiedName)) return true;
        return false;
    }

    private static final class BasicTypeDescriptor extends ValaTypeDescriptor {

        private static final Map<String, BasicTypeDescriptor> BASIC_TYPE_ELEMENT_TO_DESCRIPTOR = constructMap();

        private static Map<String, BasicTypeDescriptor> constructMap() {
            ImmutableMap.Builder<String, BasicTypeDescriptor> builder = ImmutableMap.builder();
            for (IElementType builtInTypeToken : ValaLexer.BUILT_IN_TYPES.getTypes()) {
                builder.put(builtInTypeToken.toString(), new BasicTypeDescriptor(builtInTypeToken.toString()));
            }
            return builder.build();
        }

        public BasicTypeDescriptor(String basicTypeName) {
            this.basicTypeName = basicTypeName;
        }

        private String basicTypeName;

        public String toString() {
            return basicTypeName;
        }

        public static BasicTypeDescriptor forType(ValaBuiltInType builtInType) {
            return BASIC_TYPE_ELEMENT_TO_DESCRIPTOR.get(builtInType.getText());
        }

        public static BasicTypeDescriptor forType(IElementType basicTypeElement) {
            return BASIC_TYPE_ELEMENT_TO_DESCRIPTOR.get(basicTypeElement.toString());
        }

    }
}
