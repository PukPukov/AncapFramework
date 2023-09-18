package ru.ancap.framework.updater;

import ru.ancap.framework.plugin.api.AncapPlugin;

public interface Updater {
    
    void update(AncapPlugin plugin);
    
}
