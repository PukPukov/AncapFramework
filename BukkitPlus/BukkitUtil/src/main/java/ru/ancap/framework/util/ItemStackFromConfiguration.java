package ru.ancap.framework.util;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackFromConfiguration {
    
    public static ItemStack read(@NonNull ConfigurationSection section) {
        String material = section.getString("material"); assert material != null;
        ItemStack itemStack = new ItemStack(
            Material.valueOf(material.toUpperCase()),
            section.getInt("amount", 1)
        );
        @Nullable ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        @Nullable String name = section.getString("name");
        if (name != null) meta.setDisplayName(name);
        List<String> lore = section.getStringList("lore").stream()
            .map(lorum -> "§r§f"+lorum)
            .toList();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
}