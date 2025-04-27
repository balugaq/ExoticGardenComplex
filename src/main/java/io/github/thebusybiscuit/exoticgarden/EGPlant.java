package io.github.thebusybiscuit.exoticgarden;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EGPlant extends HandledBlock {
    boolean edible;
    private static final int food = 2;

    public EGPlant(ItemGroup category, ItemStack item, String name, RecipeType recipeType, boolean edible, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        this.edible = edible;
    }

    public boolean isEdible() {
     return this.edible;
   }

    public void restoreHunger(Player p) {
        int level = p.getFoodLevel() + 2;
        p.setFoodLevel((level > 20) ? 20 : level);
        p.setSaturation(2.0F);
    }
}
