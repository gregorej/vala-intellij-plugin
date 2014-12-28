package org.intellij.vala.psi.index;

import com.intellij.util.io.KeyDescriptor;
import org.intellij.vala.psi.QualifiedName;
import org.intellij.vala.psi.impl.QualifiedNameBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

class QualifiedNameKeyDescriptor implements KeyDescriptor<QualifiedName> {

    public static final QualifiedNameKeyDescriptor INSTANCE = new QualifiedNameKeyDescriptor();

    private QualifiedNameKeyDescriptor() { }

    @Override
    public void save(@NotNull DataOutput dataOutput, QualifiedName qualifiedName) throws IOException {
        qualifiedName.write(dataOutput);
    }

    @Override
    public QualifiedName read(@NotNull DataInput dataInput) throws IOException {
        return QualifiedNameBuilder.read(dataInput);
    }

    @Override
    public int getHashCode(QualifiedName qualifiedName) {
        return qualifiedName.hashCode();
    }

    @Override
    public boolean isEqual(QualifiedName qualifiedName, QualifiedName t1) {
        return qualifiedName.equals(t1);
    }
}
