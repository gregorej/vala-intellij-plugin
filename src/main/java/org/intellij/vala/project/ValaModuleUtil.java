package org.intellij.vala.project;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.roots.impl.libraries.LibraryEx;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.util.Computable;
import com.intellij.util.Processor;

import java.util.function.Supplier;

public class ValaModuleUtil {

    public static ValaSdk getValaSdk(Module module) {
        return ValaProjectCache.instanceIn(module.getProject()).getOrUpdate(module, sdkSupplier(module));
    }

    private static Supplier<ValaSdk> sdkSupplier(Module module) {
        return () -> ApplicationManager.getApplication().runReadAction((Computable<ValaSdk>) () -> {
            OrderEnumerator enumerator = ModuleRootManager.getInstance(module).orderEntries().recursively().librariesOnly().exportedOnly();
            ValaLibraryFinder finder = new ValaLibraryFinder();
            enumerator.forEachLibrary(finder);
            if (finder.getValaLibrary() != null) {
                return new ValaSdk(finder.getValaLibrary());
            }
            return null;
        });
    }

    private static class ValaLibraryFinder implements Processor<Library> {

        public Library getValaLibrary() {
            return valaLibrary;
        }

        private Library valaLibrary = null;
        @Override
        public boolean process(Library library) {
            if (valaLibrary == null && ((LibraryEx) library).getKind() == ValaLibraryKind.INSTANCE) {
                valaLibrary = library;
                return false;
            }
            return true;
        }
    }

}
