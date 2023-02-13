package ru.ancap.menu.api.pattern.self;

import com.focamacho.sealmenus.bukkit.ChestMenu;
import com.focamacho.sealmenus.bukkit.PageableChestMenu;
import com.focamacho.sealmenus.bukkit.SealMenus;
import com.focamacho.sealmenus.bukkit.item.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class MenuFactory {
    
    private static JavaPlugin holderPlugin;
    
    public static void initialize(JavaPlugin plugin) {
        holderPlugin = plugin;
    }

    public static class Pageable {

        public static final char PREVIOUS_BUTTON_SYMBOL = '<';
        public static final char NEXT_BUTTON_SYMBOL = '>';
        public static final char SLOT_FOR_PAGEABLE_ITEMS_SYMBOL = 'S';

        public static ChestMenu of(
                String name,
                MenuPattern pattern,
                Map<Character, MenuItem> buttons,
                List<MenuItem> pageableItems,
                ItemStack previousButton,
                ItemStack nextButton
        ) {
            PageableChestMenu chestMenu = SealMenus.createPageableChestMenu(
                    name,
                    pattern.rows(),
                    pattern.slotsOf(SLOT_FOR_PAGEABLE_ITEMS_SYMBOL),
                    holderPlugin
            );
            for (Character knownChar : buttons.keySet()) {
                for (int slot : pattern.slotsOf(knownChar)) {
                    chestMenu.addItem(buttons.get(knownChar), slot);
                }
            }
            chestMenu.setPageableItems(pageableItems);
            chestMenu.setPreviousPageItem(previousButton, pattern.slotOf(PREVIOUS_BUTTON_SYMBOL));
            chestMenu.setNextPageItem(nextButton, pattern.slotOf(NEXT_BUTTON_SYMBOL));
            return chestMenu;
        }

        public static ChestMenu of(String name, MenuPattern pattern, Map<Character, MenuItem> buttons, List<MenuItem> pageableItems) {
            ItemStack arrow = new ItemStack(Material.ARROW);
            return Pageable.of(
                    name,
                    pattern,
                    buttons,
                    pageableItems,
                    arrow,
                    arrow
            );
        }

    }

}
