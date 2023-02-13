package ru.ancap.menu.api.pattern.self;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StringMenuPattern implements MenuPattern {

    @Delegate
    private final MenuPattern built;

    public StringMenuPattern(String pattern) {
        pattern = pattern.replace("\n", "");
        pattern = pattern.replace(" ", "");
        char[] splitString = pattern.toCharArray();
        Map<Character, int[]> map = new HashMap<>();
        for (int slot = 0; slot < splitString.length; slot++) {
            char ch = splitString[slot];
            int[] slots = map.get(ch);
            if (slots == null) {
                map.put(ch, new int[] {slot});
                continue;
            }
            map.put(ch, ArrayUtils.add(slots, slot));
        }
        this.built = new BuiltMenuPattern(map, splitString.length / 9);
    }

    public StringMenuPattern(String... patterns) {
        this(stringFromMassive(patterns));
    }

    private static String stringFromMassive(String[] patterns) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String pattern : patterns) {
            stringBuilder.append(pattern);
        }
        return stringBuilder.toString();
    }

    public static Builder builder() {
        StringBuilder pattern = new StringBuilder();
        return new Builder() {

            @Override
            public Builder line(String line) {
                pattern.append(line);
                return this;
            }

            @Override
            public StringMenuPattern build() {
                return new StringMenuPattern(
                        pattern.toString()
                );
            }

        };
    }

    public interface Builder {

        Builder line(String line);
        StringMenuPattern build();

    }
}
