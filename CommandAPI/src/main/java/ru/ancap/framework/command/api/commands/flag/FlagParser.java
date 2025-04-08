package ru.ancap.framework.command.api.commands.flag;

import ru.ancap.framework.command.api.commands.flag.exception.NotAFlagException;
import ru.ancap.framework.command.api.commands.flag.object.Flag;
import ru.ancap.framework.command.api.commands.flag.object.FlagData;
import ru.ancap.framework.command.api.commands.flag.object.Flags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlagParser {
    
    public static final String PREFIX = "--";
    
    public static Flags parseFlags(List<String> flagStrings) {
        List<Flag> flags = new ArrayList<>(flagStrings.size());
        for (String flagString : flagStrings) {
            if (!flagString.startsWith(PREFIX)) throw new NotAFlagException(flagString);
            flagString = flagString.substring(PREFIX.length());
            String[] parts = flagString.split("=", 2);
            String key = parts[0];
            Optional<String> data = parts.length == 2 ? Optional.of(parts[1]) : Optional.empty();
            flags.add(new Flag(key, new FlagData(data)));
        }
        return new Flags(flags);
    }
    
}