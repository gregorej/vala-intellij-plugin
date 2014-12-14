package org.intellij.vala;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;


public class ValaFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(ValaLanguageFileType.INSTANCE, ValaLanguageFileType.ALL_EXTENSIONS);
    }
}
