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
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT)}, new ItemStack[]{getById("NORMAL_BREW").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("APPLE_WINE").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("BREAD_WINE").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.POTATO), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("POTATO_WINE").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.NETHER_WART), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("NETHER_WINE").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("MILK_WINE").getItem()});
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("ENDER_WINE").getItem()});
        if (getLevel() >= 2) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("NORMAL_BREW").getItem(), new ItemStack(Material.WHEAT)}, new ItemStack[]{getById("WHITE_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("APPLE_WINE").getItem(), getById("APPLE").getItem()}, new ItemStack[]{getById("APPLE_VINEGAR").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("NETHER_WINE").getItem(), new ItemStack(Material.MAGMA_CREAM)}, new ItemStack[]{getById("FIRE_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("GRAPE").getItem(), getById("GRAPE").getItem()}, new ItemStack[]{getById("GRAPE_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("CORN").getItem(), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("CORN_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("SWEET_POTATO").getItem(), new ItemStack(Material.SUGAR)}, new ItemStack[]{getById("YELLOW_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("WINEFRUIT").getItem(), getById("WINEFRUIT").getItem()}, new ItemStack[]{getById("LIGHT_BEER").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("WINEFRUIT").getItem(), new ItemStack(Material.WHEAT)}, new ItemStack[]{getById("BEER").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("LIME").getItem(), new ItemStack(Material.VINE)}, new ItemStack[]{getById("RUM_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getById("BEER").getItem(), getById("PINEAPPLE").getItem()}, new ItemStack[]{getById("PINEAPPLE_BEER").getItem()});
        }
        if (getLevel() >= 3) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getById("DREAMFRUIT").getItem(), getById("LEMON").getItem()}, new ItemStack[]{getById("DREAMFRUIT_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), getById("NETHER_WINE").getItem()}, new ItemStack[]{getById("GLODAPPLE_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getById("WHITE_WINE").getItem(), getById("YELLOW_WINE").getItem()}, new ItemStack[]{getById("HERO_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getById("DREAMFRUIT_WINE").getItem(), getById("YELLOW_WINE").getItem()}, new ItemStack[]{getById("SUPER_WINE").getItem()});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getById("DREAMFRUIT_WINE").getItem(), getById("WHITE_WINE").getItem()}, new ItemStack[]{getById("DREAMER_WINE").getItem()});
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


