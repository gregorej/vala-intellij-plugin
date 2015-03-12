package org.intellij.vala.project;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ValaProjectCache extends AbstractProjectComponent {

    private final Map<Object, Object> cache = new HashMap<>();
    private final Object lock = new Object();

    public ValaProjectCache(Project project, ValaProjectEvents events) {
        super(project);
        events.registerListener(new ValaProjectEvents.Listener() {
            @Override
            public void onValaProjectChanged() {
                synchronized (lock) {
                    cache.clear();
                }
            }
        });
    }

    public <K, V> V getOrUpdate(K key, Supplier<V> supplier) {
        synchronized (lock) {
            V value = (V) cache.get(key);
            if (value == null) {
                value = supplier.get();
                cache.put(key, value);
            }
            return value;
        }
    }

    public static ValaProjectCache instanceIn(Project project) {
        return project.getComponent(ValaProjectCache.class);
    }
}
