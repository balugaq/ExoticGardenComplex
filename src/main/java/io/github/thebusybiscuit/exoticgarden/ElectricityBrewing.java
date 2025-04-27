package io.github.thebusybiscuit.exoticgarden;

import me.mrCookieSlime.ExoticGarden.Items.ExoticItems;
import me.mrCookieSlime.ExoticGarden.gui.ThreeInputGUI;
import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public abstract class ElectricityBrewing
        extends ThreeInputGUI {
    public ElectricityBrewing(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }


    public void registerDefaultRecipes() {
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem("NORMAL_BREW")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("APPLE_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("BREAD_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.POTATO_ITEM), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("POTATO_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.NETHER_WARTS), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("NETHER_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("MILK_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("ENDER_WINE")});
        if (getLevel() >= 2) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("NORMAL_BREW"), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem("WHITE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("APPLE_WINE"), getItem("APPLE")}, new ItemStack[]{getItem("APPLE_VINEGAR")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("NETHER_WINE"), new ItemStack(Material.MAGMA_CREAM)}, new ItemStack[]{getItem("FIRE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("GRAPE"), getItem("GRAPE")}, new ItemStack[]{getItem("GRAPE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("CORN"), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("CORN_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("SWEET_POTATO"), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem("YELLOW_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("WINEFRUIT"), getItem("WINEFRUIT")}, new ItemStack[]{getItem("LIGHT_BEER")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("WINEFRUIT"), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem("BEER")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("LIME"), new ItemStack(Material.VINE)}, new ItemStack[]{getItem("RUM_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem("BEER"), getItem("PINEAPPLE")}, new ItemStack[]{getItem("PINEAPPLE_BEER")});
        }
        if (getLevel() >= 3) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem("DREAMFRUIT"), getItem("LEMON")}, new ItemStack[]{getItem("DREAMFRUIT_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), getItem("NETHER_WINE")}, new ItemStack[]{getItem("GLODAPPLE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem("WHITE_WINE"), getItem("YELLOW_WINE")}, new ItemStack[]{getItem("HERO_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem("DREAMFRUIT_WINE"), getItem("YELLOW_WINE")}, new ItemStack[]{getItem("SUPER_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem("DREAMFRUIT_WINE"), getItem("WHITE_WINE")}, new ItemStack[]{getItem("DREAMER_WINE")});
        }
    }


    public String getMachineIdentifier() {
        return "ELECTRICITY_BREWING_" + getLevel();
    }


    public List<DefaultSubRecipe> getSubRecipes() {
        List<DefaultSubRecipe> subRecipes = new ArrayList<>();
        subRecipes.add(new DefaultSubRecipe(1000 * getLevel(), ExoticItems.Alcohol));
        return subRecipes;
    }


    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }
}


