package ru.ancap.framework.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackFromConfiguration {
    
    public static ItemStack read(ConfigurationSection section) {
        ItemStack itemStack = new ItemStack(
            Material.valueOf(section.getString("material").toUpperCase()),
            section.getInt("amount")
        );
        @Nullable ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        @Nullable String name = section.getString("name");
        if (name != null) meta.setDisplayName(name);
        List<String> lore = section.getStringList("lore").stream()
            .map(lorum -> "&r"+lorum)
            .toList();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
}