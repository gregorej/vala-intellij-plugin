package org.intellij.vala.project.template;


import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

public class ValaFilesChooserDescriptor extends FileChooserDescriptor {

    public ValaFilesChooserDescriptor() {
        super(false, true, false, false, false, false);
    }

    @Override
    public void validateSelectedFiles(VirtualFile[] files) throws Exception {

    }
}
