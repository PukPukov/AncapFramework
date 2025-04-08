package ru.ancap.framework.communicate.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.bossbar.BossBar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class BossBarPool {

    public static final Map<String, BossBar> pool = new ConcurrentHashMap<>();

}