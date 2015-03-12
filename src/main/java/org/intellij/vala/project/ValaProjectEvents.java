package org.intellij.vala.project;

import com.intellij.ProjectTopics;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.util.messages.MessageBusConnection;

import java.util.ArrayList;
import java.util.List;

public class ValaProjectEvents extends AbstractProjectComponent {

    private final MessageBusConnection connection;
    private List<Listener> listeners = new ArrayList<>();

    public ValaProjectEvents(Project project) {
        super(project);
        connection = project.getMessageBus().connect();
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void projectOpened() {
        connection.subscribe(ProjectTopics.PROJECT_ROOTS, new ModuleRootAdapter() {
            @Override
            public void rootsChanged(ModuleRootEvent event) {
                for (Listener listener : listeners) {
                    listener.onValaProjectChanged();
                }
            }
        });
    }

    @Override
    public void projectClosed() {
        connection.disconnect();
    }

    public static interface Listener {
        public void onValaProjectChanged();
    }
}
