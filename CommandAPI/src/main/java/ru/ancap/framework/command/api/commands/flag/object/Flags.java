package ru.ancap.framework.command.api.commands.flag.object;

import ru.ancap.commons.map.GuaranteedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Flags(List<Flag> list) {
    
    public Map<String, List<FlagData>> destroy() {
        Map<String, List<FlagData>> map = new GuaranteedMap<>(ArrayList::new);
        for (Flag flag : this.list) map.get(flag.key()).add(flag.data());
        return map;
    } 
    
}