package io.github.thebusybiscuit.exoticgarden;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class YeastCulturer
        extends DefaultGUI {
    public YeastCulturer(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }


    public String getMachineIdentifier() {
        return "YEAST_CULTURER";
    }


    public void registerDefaultRecipes() {
        registerRecipe(120, new ItemStack[]{new ItemStack(Material.HAY_BLOCK)}, new ItemStack[]{ExoticItems.Yeast_1});
        registerRecipe(80, new ItemStack[]{getById("WINEFRUIT").getItem()}, new ItemStack[]{ExoticItems.Yeast_2});
        registerRecipe(40, new ItemStack[]{getById("DREAMFRUIT").getItem()}, new ItemStack[]{ExoticItems.Yeast_3});
    }


    public List<DefaultSubRecipe> getSubRecipes() {
        List<DefaultSubRecipe> subRecipes = new ArrayList<>();
        subRecipes.add(new DefaultSubRecipe(750 + getLevel() * 250, ExoticItems.Yeast_4));
        return subRecipes;
    }


    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }
}


