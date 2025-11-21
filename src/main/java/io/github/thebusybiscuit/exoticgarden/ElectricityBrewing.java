package io.github.thebusybiscuit.exoticgarden;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;


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
        registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_1, getItem0("DIRT_ESSENCE"), new ItemStack(Material.MUDDY_MANGROVE_ROOTS)}, new ItemStack[]{getItem0("DIRT_WINE")});
        if (getLevel() >= 2) {
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("VITAMINS"), new ItemStack(Material.GLISTERING_MELON_SLICE)}, new ItemStack[]{getItem0("ENHANCE_VITAMIN")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("ICE_CUBE"), new ItemStack(Material.BLUE_ICE)}, new ItemStack[]{getItem0("ICEJADE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_2, getItem0("POMEGRANATE"), new ItemStack(Material.BEETROOT)}, new ItemStack[]{getItem0("POMEGRANATE_WINE")});
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
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.SUGAR_CANE), getItem0("COCONUT")}, new ItemStack[]{getItem0("SWEET_TRUFFLE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("GOLD_24K"), new ItemStack(Material.HONEY_BOTTLE)}, new ItemStack[]{getItem0("VINHO_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("DREAMFRUIT"), getItem0("GRAPE")}, new ItemStack[]{getItem0("SHIRAZ_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("ORGANIC_FOOD_KELP"), getItem0("LEEK")}, new ItemStack[]{getItem0("GRENACHE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("PLUM"), getItem0("COFFEEBEAN")}, new ItemStack[]{getItem0("MERLOT_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("SWEET_POTATO"), getItem0("WINEFRUIT")}, new ItemStack[]{getItem0("PINOT_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.GLOW_BERRIES), new ItemStack(Material.BIG_DRIPLEAF)}, new ItemStack[]{getItem0("GLOWBERRY_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("DREAMFRUIT"), getItem0("LEMON")}, new ItemStack[]{getItem0("DREAMFRUIT_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE), getItem0("NETHER_WINE")}, new ItemStack[]{getItem0("GLODAPPLE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("GINSENGBABY"), getItem0("NETHER_ICE")}, new ItemStack[]{getItem0("UNDYING_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_3, getItem0("WHITE_WINE"), getItem0("YELLOW_WINE")}, new ItemStack[]{getItem0("HERO_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("DREAMFRUIT_WINE"), getItem0("YELLOW_WINE")}, new ItemStack[]{getItem0("SUPER_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("DREAMFRUIT_WINE"), getItem0("WHITE_WINE")}, new ItemStack[]{getItem0("DREAMER_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("SHIRAZ_WINE"), getItem0("TEQUILA")}, new ItemStack[]{getItem0("CHAMPAGNE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("ENDER_WINE"), getItem0("ENDER_LUMP_3")}, new ItemStack[]{getItem0("ENDERDREAM_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("PINEAPPLE_BEER"), getItem0("RAINBOW_FRUITS")}, new ItemStack[]{getItem0("PARTY_BEER")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("GLODAPPLE_WINE"), getItem0("BUCKET_OF_FUEL")}, new ItemStack[]{getItem0("BURNING_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("WHITE_WINE"), getItem0("PEANUT")}, new ItemStack[]{getItem0("SMALL_MAOTAI")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("WHITE_WINE"), getItem0("ORGANIC_FOOD_POTATO")}, new ItemStack[]{getItem0("SKYBLUE_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("DREAMER_WINE"), getItem0("CORN")}, new ItemStack[]{getItem0("INDUSTRIAL_ALCOHOL_WINE")});
            registerRecipe(80 - getLevel() * 20, new ItemStack[]{ExoticItems.Yeast_4, getItem0("MEDICINE"), getItem0("GOOSEBERRY")}, new ItemStack[]{getItem0("SANITY_DRUG")});
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


