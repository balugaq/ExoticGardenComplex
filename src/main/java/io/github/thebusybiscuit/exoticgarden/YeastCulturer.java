package io.github.thebusybiscuit.exoticgarden;

import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
import me.mrCookieSlime.ExoticGarden.gui.DefaultGUI;
import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class YeastCulturer
        extends DefaultGUI {
    public YeastCulturer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }


    public String getMachineIdentifier() {
        return "YEAST_CULTURER";
    }


    public void registerDefaultRecipes() {
        registerRecipe(120, new ItemStack[]{new ItemStack(Material.HAY_BLOCK)}, new ItemStack[]{ExoticItems.Yeast_1});
        registerRecipe(80, new ItemStack[]{getItem("WINEFRUIT")}, new ItemStack[]{ExoticItems.Yeast_2});
        registerRecipe(40, new ItemStack[]{getItem("DREAMFRUIT")}, new ItemStack[]{ExoticItems.Yeast_3});
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


