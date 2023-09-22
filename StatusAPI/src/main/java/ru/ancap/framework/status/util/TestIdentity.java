package ru.ancap.framework.status.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@UtilityClass
public class TestIdentity {

    private static final int DO_NOT_MATCH_WITH_LAST = 10;
    private static final byte MAX_VALUE = 100;
    private static final Set<Byte> LAST_VALUES = new HashSet<>();

    private static final Random random = new Random();


    /**
     * Возвращает случайное число от 0 до 100, которое не возвращалось до этого в последние 10 вызовов
     */
    public static byte get() {
        byte value;
        do { value = (byte) random.nextInt(MAX_VALUE + 1); }
        while (LAST_VALUES.contains(value) || value <= 0);
        LAST_VALUES.add(value);
        if (LAST_VALUES.size() > DO_NOT_MATCH_WITH_LAST) LAST_VALUES.remove((byte) (value - DO_NOT_MATCH_WITH_LAST));
        return value;
    }
    
}
