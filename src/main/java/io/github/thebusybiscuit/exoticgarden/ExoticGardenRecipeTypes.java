package io.github.thebusybiscuit.exoticgarden;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class ExoticGardenRecipeTypes {

    public static final RecipeType KITCHEN = new RecipeType(new NamespacedKey(ExoticGarden.instance, "kitchen"), new SlimefunItemStack("KITCHEN", Material.CAULDRON, "&e厨房"), "", "&r这个物品必须要在厨房里制作");
    public static final RecipeType BREAKING_GRASS = new RecipeType(new NamespacedKey(ExoticGarden.instance, "breaking_grass"), new CustomItemStack(Material.GRASS, "&7破坏草"));
    public static final RecipeType HARVEST_TREE = new RecipeType(new NamespacedKey(ExoticGarden.instance, "harvest_tree"), new CustomItemStack(Material.OAK_LEAVES, "&a从树木中获得", "", "&r通过种植特定树木获得"));
    public static final RecipeType HARVEST_BUSH = new RecipeType(new NamespacedKey(ExoticGarden.instance, "harvest_bush"), new CustomItemStack(Material.OAK_LEAVES, "&a从灌木丛中获得", "", "&r通过种植特定灌木丛获得"));
    public static final RecipeType HARVEST_PLANT = new RecipeType(new NamespacedKey(ExoticGarden.instance, "harvest_plant"), new CustomItemStack(Material.OAK_LEAVES, "&7从特定的植物上收获"));
    public static final RecipeType SEED_ANALYZER = new RecipeType(new NamespacedKey(ExoticGarden.instance, "seed_analyzer"), ExoticItems.SeedAnalyzer_1);
    public static final RecipeType YeastCulturer = new RecipeType(new NamespacedKey(ExoticGarden.instance, "yeast_culturer"), ExoticItems.YeastCulturer);
    public static final RecipeType BREAK_GRASS = new RecipeType(new NamespacedKey(ExoticGarden.instance, "break_grass"), new CustomItemStack(Material.TALL_GRASS, "&7破坏杂草获得"));
    public static final RecipeType BREWER = new RecipeType(new NamespacedKey(ExoticGarden.instance, "brewer"), new CustomItemStack(SkullUtil.getByBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmZiOTg1OTYyZjQ2ZTA1NWY1M2Q4ZWUzNWIxMWI4YTYyZjM5N2RhZDlkYjlmZWFlZmY0ODI5NjMwZDlkOSJ9fX0="), "&b电力酿造机", "", "&7用于制作美酒"));
    public static final RecipeType ElectricityBrewing_1 = new RecipeType(new NamespacedKey(ExoticGarden.instance, "electricity_brewing_1"), ExoticItems.ElectricityBrewing_1);
    public static final RecipeType ElectricityBrewing_2 = new RecipeType(new NamespacedKey(ExoticGarden.instance, "electricity_brewing_2"), ExoticItems.ElectricityBrewing_2);
    public static final RecipeType ElectricityBrewing_3 = new RecipeType(new NamespacedKey(ExoticGarden.instance, "electricity_brewing_3"), ExoticItems.ElectricityBrewing_3);


    private ExoticGardenRecipeTypes() {
    }
}
