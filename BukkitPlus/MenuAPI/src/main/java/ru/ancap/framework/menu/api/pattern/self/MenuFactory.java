package ru.ancap.framework.menu.api.pattern.self;

import com.focamacho.sealmenus.bukkit.ChestMenu;
import com.focamacho.sealmenus.bukkit.PageableChestMenu;
import com.focamacho.sealmenus.bukkit.SealMenus;
import com.focamacho.sealmenus.bukkit.item.MenuItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@UtilityClass
public class MenuFactory {
    
    private static JavaPlugin holderPlugin;
    
    public static void initialize(JavaPlugin plugin) {
        holderPlugin = plugin;
    }
    
    @UtilityClass
    public static class Pageable {
        
        public static final char PREVIOUS_BUTTON_SYMBOL = '<';
        public static final char NEXT_BUTTON_SYMBOL = '>';
        public static final char SLOT_FOR_PAGEABLE_ITEMS_SYMBOL = 'S';
        
        public static PageableBuilder builder() {
            return new PageableBuilder();
        }
        
        public static ChestMenu of(String name, MenuPattern pattern, Map<Character, MenuItem> buttons, List<MenuItem> pageableItems, ItemStack previousButton, ItemStack nextButton) {
            return Pageable.builder()
                .title(name)
                .pattern(pattern)
                .buttons(buttons)
                .pageableItems(pageableItems)
                .previousButton(previousButton)
                .nextButton(nextButton).build();
        }
        
        public static ChestMenu of(String name, MenuPattern pattern, Map<Character, MenuItem> buttons, List<MenuItem> pageableItems) {
            return Pageable.builder()
                .title(name)
                .pattern(pattern)
                .buttons(buttons)
                .pageableItems(pageableItems).build();
        }
        
        public interface ButtonProvider extends Iterable<ButtonProvider.Entry> {
            
            interface Entry {
                char key();
                MenuItem item();
            }
            
        }
        
        @RequiredArgsConstructor
        public static class MapButtonProvider implements ButtonProvider {
            
            private final Map<Character, MenuItem> map;
            
            @NotNull
            @Override
            public Iterator<Entry> iterator() {
                return this.map.entrySet().stream().map(mapEntry -> (Entry) new Entry() {
                    @Override public char     key()  { return mapEntry.getKey();   }
                    @Override public MenuItem item() { return mapEntry.getValue(); }
                }).iterator();
            }
            
        }
        
        @RequiredArgsConstructor
        public static class LazyButtonProvider implements ButtonProvider {
            
            private final Map<Character, Supplier<MenuItem>> map;
            
            @NotNull
            @Override
            public Iterator<Entry> iterator() {
                return this.map.entrySet().stream().map(mapEntry -> (Entry) new Entry() {
                    @Override public char     key()  { return mapEntry.getKey();   }
                    @Override public MenuItem item() { return mapEntry.getValue().get(); }
                }).iterator();
            }
            
        }
        
        
    }
    
    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    public static class PageableBuilder {
        
        private String title;
        private MenuPattern pattern;
        private Pageable.ButtonProvider buttonsProvider;
        private List<MenuItem> pageableItems;
        private static ItemStack arrow = new ItemStack(Material.ARROW);
        private ItemStack nextButton;     private static ItemStack nextButtonDefault = arrow;
        private ItemStack previousButton; private static ItemStack previousButtonDefault = arrow;
        
        public PageableBuilder buttons(Map<Character, MenuItem> buttons) {
            return this.buttonsProvider(new Pageable.MapButtonProvider(buttons));
        }
        
        public ChestMenu build() {
            Validate.notNull(this.title, "You should provide title to your menu");
            Validate.notNull(this.pattern, "You should provide pattern to your menu");
            Validate.notNull(this.buttonsProvider, "You should provide buttons provider to your menu");
            Validate.notNull(this.pageableItems, "You should provide pageable items to your menu");
            if (this.nextButton == null) this.nextButton = nextButtonDefault;
            if (this.previousButton == null) this.previousButton = previousButtonDefault;
            PageableChestMenu chestMenu = SealMenus.createPageableChestMenu(
                this.title,
                this.pattern.rows(),
                this.pattern.slotsOf(Pageable.SLOT_FOR_PAGEABLE_ITEMS_SYMBOL),
                holderPlugin
            );
            for (Pageable.ButtonProvider.Entry buttonProviderEntry : this.buttonsProvider) for (int slot : this.pattern.slotsOf(buttonProviderEntry.key())) {
                chestMenu.addItem(buttonProviderEntry.item(), slot);
            }
            chestMenu.setPageableItems(this.pageableItems);
            chestMenu.setPreviousPageItem(this.previousButton, this.pattern.slotOf(Pageable.PREVIOUS_BUTTON_SYMBOL));
            chestMenu.setNextPageItem(this.nextButton, this.pattern.slotOf(Pageable.NEXT_BUTTON_SYMBOL));
            return chestMenu;
        }
        
    }
    
}