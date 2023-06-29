package ru.ancap.framework.communicate.communicator.util;

import net.kyori.adventure.bossbar.BossBar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarPool {

    public static final Map<String, BossBar> pool = new ConcurrentHashMap<>();

}