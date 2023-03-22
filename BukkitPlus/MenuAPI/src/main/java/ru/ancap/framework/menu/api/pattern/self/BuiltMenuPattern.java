package ru.ancap.framework.menu.api.pattern.self;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class BuiltMenuPattern implements MenuPattern {

    private final Map<Character, int[]> pattern;
    private final int rows;

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int[] slotsOf(char ch) {
        return this.pattern.getOrDefault(ch, new int[] {});
    }
}
