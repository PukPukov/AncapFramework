package ru.ancap.framework.util.inventory;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@AllArgsConstructor
public class InventoryStorage {
    
    private final PlayerInventory inventory;
    
    public PlayerInventory inventory() {
        return this.inventory;
    }
    
    public int amount(Material currency) {
        int amount = 0;
        for (ItemStack stack : inventory.getContents()) {
            if (stack.getType() != currency) continue;
            if (stack.hasItemMeta()) continue;
            if (stack.getDurability() > 0) continue;
            amount = amount + stack.getAmount();
        }
        return amount;
    }

    public boolean hasInventoryMoney(Material currency, int price) {
        return this.amount(currency) >= price;
    }

    public void removeInventoryMoney(Material currency, int amount) {
        int left = amount;
        
        ItemStack[] contents = inventory.getContents();
        ItemStack[] newContents = contents.clone();
        
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].getType() != currency) continue;
            if (contents[i].hasItemMeta()) continue;
            if (contents[i].getDurability() > 0) continue;
            if (contents[i].getAmount() <= left) {
                left = left - contents[i].getAmount();
                newContents[i] = new ItemStack(Material.AIR);
            } else {
                newContents[i] = new ItemStack(contents[i].getType(), contents[i].getAmount() - left);
                left = 0;
            }
        }

        if (amount != 0) throw new IllegalStateException(
                "Player don't have enough items, " +
                "you should check the ability of " +
                "removing items with hasInventoryMoney" +
                "(Material, int) before calling this method!"
        );
        
        this.inventory.setContents(newContents);
    }

    // -1 if no
    public int getFreeInventorySlot() {
        ItemStack[] inventory = this.inventory.getContents();
        for (int i = 0; i<inventory.length; i++) {
            if (inventory[i] == null || inventory[i].getType() == Material.AIR) return i;
        }
        return -1;
    }
}
