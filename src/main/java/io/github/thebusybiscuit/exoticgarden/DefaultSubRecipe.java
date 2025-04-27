package io.github.thebusybiscuit.exoticgarden;

import org.bukkit.inventory.ItemStack;

public class DefaultSubRecipe {
    final int chance;
    final ItemStack item;

    public DefaultSubRecipe(int chance, ItemStack item) {
        this.chance = chance;
        this.item = item;
    }

    public int getChance() {
        return this.chance;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
