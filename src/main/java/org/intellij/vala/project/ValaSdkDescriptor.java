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
import java.util.Optional;

public class ValaSdkDescriptor {

    public static enum GtkVersion {
        GTK_3("gtk+-3.0"),
        GTK_2("gtk+-2.0");

        private final String vapiFileName;

        GtkVersion(String vapiFileName) {
            this.vapiFileName = vapiFileName;
        }

        String getVapiFileName() {
            return vapiFileName;
        }
    }

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
    public static ValaSdkDescriptor fromDirectory(File valaHome, Optional<GtkVersion> gtkVersion) throws IOException {
        String version = readVersion(valaHome);
        VirtualFile valaSdkRoot = VfsUtil.findFileByIoFile(valaHome, false);
        Asserts.notNull(valaSdkRoot, "Vala SDK root directory could not be found");
        VirtualFile vapiDirectory = valaSdkRoot.findChild("vapi");
        Asserts.notNull(vapiDirectory, "No .vapi directory in Vala SDK root");
        ImmutableList.Builder<File> vapiFiles = ImmutableList.builder();
        for (VirtualFile vfs : vapiDirectory.getChildren()) {
            final String name = vfs.getName();
            if (name.endsWith(".vapi") && (!name.startsWith("gtk+-") || gtkVersion.map(gtk -> name.startsWith(gtk.getVapiFileName())).orElse(false))) {
                vapiFiles.add(VfsUtil.virtualToIoFile(vfs));
            }
        }
        return new ValaSdkDescriptor(version, vapiFiles.build());
    }

    @Nullable
    public static ValaSdkDescriptor discoverFromEnvironmentVariable() throws IOException {
        String valaHomePath = System.getenv("VALA_HOME");
        if (valaHomePath == null) {
            return null;
        }
        File valaHome = new File(valaHomePath);
        return fromDirectory(valaHome, Optional.of(GtkVersion.GTK_3));
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
