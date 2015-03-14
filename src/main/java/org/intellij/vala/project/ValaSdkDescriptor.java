package org.intellij.vala.project;

import com.google.common.collect.ImmutableList;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.NewLibraryConfiguration;
import com.intellij.openapi.roots.ui.configuration.libraryEditor.LibraryEditor;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.http.util.Asserts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;

public class ValaSdkDescriptor {

    private String version;
    private List<File> vapiFiles;

    private ValaSdkDescriptor(String version, List<File> vapiFiles) {
        this.version = version;
        this.vapiFiles = vapiFiles;
    }

    public String getVersion() {
        return version;
    }

    public List<File> getVapiFiles() {
        return vapiFiles;
    }

    public NewLibraryConfiguration createNewLibraryConfiguration() {
        ValaLibraryProperties properties = new ValaLibraryProperties();
        properties.valaVersion = version;
        String name = "vala-sdk-" + version;
        return new NewLibraryConfiguration(name, ValaLibraryType.instance(), properties) {

            @Override
            public void addRoots(@NotNull LibraryEditor libraryEditor) {
                for (File file : vapiFiles) {
                    libraryEditor.addRoot(VfsUtil.getUrlForLibraryRoot(file), OrderRootType.CLASSES);
                }
            }
        };
    }

    @Nullable
    public static ValaSdkDescriptor discoverFromEnvironmentVariable() throws IOException {
        String valaHomePath = System.getenv("VALA_HOME");
        if (valaHomePath == null) {
            return null;
        }
        File valaHome = new File(valaHomePath);
        String version = readVersion(valaHome);
        VirtualFile valaSdkRoot = VfsUtil.findFileByIoFile(valaHome, false);
        Asserts.notNull(valaSdkRoot, "Vala SDK root directory could not be found");
        VirtualFile vapiDirectory = valaSdkRoot.findChild("vapi");
        Asserts.notNull(vapiDirectory, "No .vapi directory in Vala SDK root");
        ImmutableList.Builder<File> vapiFiles = ImmutableList.builder();
        for (VirtualFile vfs : vapiDirectory.getChildren()) {
            if (vfs.getName().endsWith(".vapi")) {
                vapiFiles.add(VfsUtil.virtualToIoFile(vfs));
            }
        }
        return new ValaSdkDescriptor(version, vapiFiles.build());
    }

    private static String readVersion(File valaHome) throws IOException {
        BufferedReader reader = null;
        try {
            final FileInputStream versionFile = new FileInputStream(new File(valaHome, ".version"));
            reader = new BufferedReader(new InputStreamReader(versionFile));
            return reader.readLine();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
