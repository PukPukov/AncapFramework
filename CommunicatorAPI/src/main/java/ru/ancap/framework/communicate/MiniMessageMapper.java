package ru.ancap.framework.communicate;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class MiniMessageMapper {
    
    private static final Map<Character, String> fromLegacyMap = Map.ofEntries(
        Map.entry('0', "<reset><black>"),
        Map.entry('1', "<reset><dark_blue>"),
        Map.entry('2', "<reset><dark_green>"),
        Map.entry('3', "<reset><dark_aqua>"),
        Map.entry('4', "<reset><dark_red>"),
        Map.entry('5', "<reset><dark_purple>"),
        Map.entry('6', "<reset><gold>"),
        Map.entry('7', "<reset><gray>"),
        Map.entry('8', "<reset><dark_gray>"),
        Map.entry('9', "<reset><blue>"),
        Map.entry('a', "<reset><green>"),
        Map.entry('A', "<reset><green>"),
        Map.entry('b', "<reset><aqua>"),
        Map.entry('B', "<reset><aqua>"),
        Map.entry('c', "<reset><red>"),
        Map.entry('C', "<reset><red>"),
        Map.entry('d', "<reset><light_purple>"),
        Map.entry('D', "<reset><light_purple>"),
        Map.entry('e', "<reset><yellow>"),
        Map.entry('E', "<reset><yellow>"),
        Map.entry('f', "<reset><white>"),
        Map.entry('F', "<reset><white>"),
        Map.entry('k', "<obfuscated>"),
        Map.entry('K', "<obfuscated>"),
        Map.entry('l', "<bold>"),
        Map.entry('L', "<bold>"),
        Map.entry('m', "<strikethrough>"),
        Map.entry('M', "<strikethrough>"),
        Map.entry('n', "<underlined>"),
        Map.entry('N', "<underlined>"),
        Map.entry('o', "<italic>"),
        Map.entry('O', "<italic>"),
        Map.entry('r', "<reset>"),
        Map.entry('R', "<reset>")
    );

    public static String mapLegacy(String untranslated) {
        return mapLegacy3C(untranslated, '&', '§');
    }

    /**
     * mapLegacyCustomColorCodes
     */
    public static String mapLegacy3C(String untranslated, char... colorCodes) {
        StringBuilder result = new StringBuilder((int) (untranslated.length() * 1.5));
        char[] b = untranslated.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (equalsOne(b[i], colorCodes)) {
                String code = fromLegacyMap.get(b[i+1]);
                if (code == null) { result.append(b[i]); continue; }
                result.append(code);
            } else {
                result.append(b[i]);
            }
        }
        return result.toString();
    }

    private static boolean equalsOne(char char_, char[] colorCodes) {
        for (char compared : colorCodes) if (compared == char_) return true;
        return false;
    }

}
