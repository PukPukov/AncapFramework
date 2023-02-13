package ru.ancap.framework.artifex.test;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class KyoriTester implements Runnable {
    
    @Override
    public void run() {
        try {
            Class<?> class_ = BukkitAudiences.class;
            GsonComponentSerializer component = GsonComponentSerializer.gson();
            PlainTextComponentSerializer component1 = PlainTextComponentSerializer.plainText();
            LegacyComponentSerializer component2 = LegacyComponentSerializer.legacyAmpersand();
            MiniMessage miniMessage = MiniMessage.miniMessage();
        } catch (NoClassDefFoundError error) {
            throw new RuntimeException("Your server has no native support for net.kyori. Install KYORI plugin.");
        }
    }
}
