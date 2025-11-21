package io.github.thebusybiscuit.exoticgarden;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;


public abstract class SeedAnalyzer
        extends DefaultGUI {
    public SeedAnalyzer(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }


    public void registerDefaultRecipes() {
        registerRecipe(140 - getLevel() * 40, new ItemStack[]{getItem0("MYSTIC_SEED")}, new ItemStack[]{new ItemStack(Material.GOLD_NUGGET)});
    }

    public String getMachineIdentifier() {
        return "SEED_ANALYZER_" + getLevel();
    }


    public List<DefaultSubRecipe> getSubRecipes() {
        List<DefaultSubRecipe> subRecipes = new ArrayList<>();


        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("GRAPE_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("BLUEBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("ELDERBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("RASPBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("BLACKBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("CRANBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("COWBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("STRAWBERRY_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PEAR_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PEACH_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("ORANGE_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("LIME_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("PLUM_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("LEMON_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("POMEGRANATE_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("CHERRY_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2500 + 1000 * getLevel(), EGPlant.getByName("COCONUT_SAPLING").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("WINEFRUIT_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("DREAMFRUIT_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("COFFEEBEAN_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("LEEK_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("TEQUILA_BUSH").getItem()));
        subRecipes.add(new DefaultSubRecipe(2000 + 1000 * getLevel(), EGPlant.getByName("PEANUT_BUSH").getItem()));

        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("COAL_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("IRON_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("GOLD_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("REDSTONE_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("LAPIS_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("QUARTZ_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("OBSIDIAN_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("ENDER_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("GLOWSTONE_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("DIAMOND_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("EMERALD_PLANT").getItem()));
        subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("SLIME_PLANT").getItem()));
        //subRecipes.add(new DefaultSubRecipe(500 + 500 * getLevel(), EGPlant.getByName("SHULKER_SHELL_PLANT").getItem()));
        return subRecipes;
    }

    public ItemStack getItem0(String item) {
        return ExoticGarden.getItem(item);
    }
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }
}



