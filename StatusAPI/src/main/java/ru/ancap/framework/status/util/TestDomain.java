package ru.ancap.framework.status.util;

import lombok.experimental.UtilityClass;
import ru.ancap.framework.language.additional.LAPIDomain;

@UtilityClass
public class TestDomain {
    
    public static String of(Class<?> base, String moduleName) {
        return LAPIDomain.of(base, "modules", moduleName, "name");
    }
    
}
