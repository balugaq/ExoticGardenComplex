package io.github.thebusybiscuit.exoticgarden;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public abstract class ElectricityBrewing
        extends ThreeInputGUI {
    public ElectricityBrewing(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }


    public void registerDefaultRecipes() {
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem0("NORMAL_BREW")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("APPLE_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("BREAD_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.POTATO), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("POTATO_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.NETHER_WART), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("NETHER_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("MILK_WINE")});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("ENDER_WINE")});
        if (getLevel() >= 2) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("NORMAL_BREW"), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem0("WHITE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("APPLE_WINE"), getItem0("APPLE")}, new ItemStack[]{getItem0("APPLE_VINEGAR")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("NETHER_WINE"), new ItemStack(Material.MAGMA_CREAM)}, new ItemStack[]{getItem0("FIRE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("GRAPE"), getItem0("GRAPE")}, new ItemStack[]{getItem0("GRAPE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("CORN"), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("CORN_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("SWEET_POTATO"), new ItemStack(Material.SUGAR)}, new ItemStack[]{getItem0("YELLOW_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("WINEFRUIT"), getItem0("WINEFRUIT")}, new ItemStack[]{getItem0("LIGHT_BEER")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("WINEFRUIT"), new ItemStack(Material.WHEAT)}, new ItemStack[]{getItem0("BEER")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("LIME"), new ItemStack(Material.VINE)}, new ItemStack[]{getItem0("RUM_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("BEER"), getItem0("PINEAPPLE")}, new ItemStack[]{getItem0("PINEAPPLE_BEER")});
        }
        if (getLevel() >= 3) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("DREAMFRUIT"), getItem0("LEMON")}, new ItemStack[]{getItem0("DREAMFRUIT_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), getItem0("NETHER_WINE")}, new ItemStack[]{getItem0("GLODAPPLE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("WHITE_WINE"), getItem0("YELLOW_WINE")}, new ItemStack[]{getItem0("HERO_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("DREAMFRUIT_WINE"), getItem0("YELLOW_WINE")}, new ItemStack[]{getItem0("SUPER_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("DREAMFRUIT_WINE"), getItem0("WHITE_WINE")}, new ItemStack[]{getItem0("DREAMER_WINE")});
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

    public ItemStack getItem0(String item) {
        return ExoticGarden.getItem(item);
    }
}


