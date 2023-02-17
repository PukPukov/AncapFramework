package ru.ancap.framework.resource.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.AncapDebug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor
public class BuiltTransferMap implements TransferMap {

    public static final BuiltTransferMap EMPTY = new BuiltTransferMap(Map.of());
    private final Map<String, String> map;
    
    public static BuiltTransferMap makeFor(ConfigurationSection section, int fromVersion) {
        if (section == null) return BuiltTransferMap.EMPTY;
        Map<Integer, Map<String, String>> fullMap = new HashMap<>();
        Set<String> keys = section.getKeys(false);
        keys.forEach(version -> {
            List<String> transfersList = section.getStringList(version);
            Map<String, String> transfers = new HashMap<>();
            transfersList.forEach(transfer -> {
                String[] split = transfer.split(":");
                try {
                    transfers.put(split[0], split[1]);
                } catch (ArrayIndexOutOfBoundsException exception) {
                    throw new RuntimeException("Transfer values should be in format \"old:new\", but inserted \""+transfer+"\".");
                }
            });
            try {
                fullMap.put(Integer.valueOf(version), transfers);
            } catch (NumberFormatException exception) {
                throw new RuntimeException("Version should be an integer, but inserted \""+version+"\".");
            }
        });
        fullMap.forEach((key, value) -> {if (key <= fromVersion) fullMap.remove(key);});
        if (fullMap.size() < 1) return new BuiltTransferMap(Map.of());
        AncapDebug.debug(fromVersion + 1);
        Map<String, String> first = fullMap.get(fromVersion + 1);
        fullMap.remove(fromVersion + 1);
        fullMap.forEach((version, next) -> first.forEach((old, target) -> {
            if (next.containsKey(target)) first.put(old, next.get(target));
        }));
        return new BuiltTransferMap(first);
    }
    
    @Override
    public @Nullable String getTarget(String old) {
        return this.map.get(old);
    }
}
