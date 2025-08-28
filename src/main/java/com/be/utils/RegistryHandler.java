package com.be.utils;

import com.be.BETree;
import io.github.thebusybiscuit.exoticgarden.Berry;
import io.github.thebusybiscuit.exoticgarden.CustomPotion;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.exoticgarden.ExoticGardenRecipeTypes;
import io.github.thebusybiscuit.exoticgarden.PlantType;
import io.github.thebusybiscuit.exoticgarden.items.BonemealableItem;
import io.github.thebusybiscuit.exoticgarden.items.CustomFood;
import io.github.thebusybiscuit.exoticgarden.items.ExoticGardenFruit;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;

public class RegistryHandler {

    private static final File schematicsFolder = new File(ExoticGarden.getInstance().getDataFolder(), "schematics");

    public static void initPlant(String rawName, String name, ChatColor color, PlantType type, String texture) {
        String upperCase = rawName.toUpperCase(Locale.ROOT);
        String enumStyle = upperCase.replace(' ', '_');
        Berry berry = new Berry(enumStyle, type, texture);
        ExoticGarden.getBerries().add(berry);
        SlimefunItemStack bush = new SlimefunItemStack(enumStyle + "_BUSH", Material.OAK_SAPLING, color + name + "植物");
        ExoticGarden.getGrassDrops().put(upperCase + "_BUSH", bush);
        (new BonemealableItem(ExoticGarden.instance.mainItemGroup, bush, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[]{null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null})).register(ExoticGarden.getInstance());
        (new ExoticGardenFruit(ExoticGarden.instance.mainItemGroup, new SlimefunItemStack(enumStyle, texture, color + name), ExoticGardenRecipeTypes.HARVEST_BUSH, true, new ItemStack[]{null, null, null, null, getItem(enumStyle + "_BUSH"), null, null, null, null})).register(ExoticGarden.getInstance());
    }

    public static void initTree(String rawName, String name, String texture, String color, Color pcolor, String juice, boolean pie, Material... soil) {
        String id = rawName.toUpperCase(Locale.ROOT).replace(' ', '_');
        BETree tree = new BETree(id, texture, soil);
        ExoticGarden.getTrees().add(tree);
        SlimefunItemStack sapling = new SlimefunItemStack(id + "_SAPLING", Material.OAK_SAPLING, color + name + "树苗");
        ExoticGarden.getGrassDrops().put(id + "_SAPLING", sapling);
        (new BonemealableItem(ExoticGarden.instance.mainItemGroup, sapling, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[]{null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null})).register(ExoticGarden.getInstance());
        (new ExoticGardenFruit(ExoticGarden.instance.mainItemGroup, new SlimefunItemStack(id, texture, color + name), ExoticGardenRecipeTypes.HARVEST_TREE, true, new ItemStack[]{null, null, null, null, getItem(id + "_SAPLING"), null, null, null, null})).register(ExoticGarden.getInstance());
        if (pcolor != null) {
            (new Juice(ExoticGarden.instance.drinksItemGroup, new SlimefunItemStack(juice.toUpperCase().replace(" ", "_"), new CustomPotion(color + juice, pcolor, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o3.0 &7&o点饥饿值")), RecipeType.JUICER, new ItemStack[]{getItem(id), null, null, null, null, null, null, null, null})).register(ExoticGarden.getInstance());
        }

        if (pie) {
            (new CustomFood(ExoticGarden.instance.foodItemGroup, new SlimefunItemStack(id + "_PIE", "3418c6b0a29fc1fe791c89774d828ff63d2a9fa6c83373ef3aa47bf3eb79", color + name + "派", "", "&7&o恢复 &b&o6.5 &7&o点饥饿值"), new ItemStack[]{getItem(id), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), new ItemStack(Material.MILK_BUCKET), SlimefunItems.WHEAT_FLOUR, null, null, null, null}, 13)).register(ExoticGarden.getInstance());
        }

        if (!(new File(getSchematicsFolder(), id + "_TREE.schematic")).exists()) {
            saveSchematic(id + "_TREE");
        }

    }

    public static File getSchematicsFolder() {
        return schematicsFolder;
    }

    private static void saveSchematic(@Nonnull String id) {
        try {
            InputStream input = ExoticGarden.getInstance().getClass().getResourceAsStream("/schematics/" + id + ".schematic");

            try {
                FileOutputStream output = new FileOutputStream(new File(getSchematicsFolder(), id + ".schematic"));

                try {
                    byte[] buffer = new byte[1024];

                    int len;
                    while ((len = input.read(buffer)) > 0) {
                        output.write(buffer, 0, len);
                    }
                } catch (Throwable var8) {
                    try {
                        output.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }

                    throw var8;
                }

                output.close();
            } catch (Throwable var9) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }

                throw var9;
            }

            if (input != null) {
                input.close();
            }
        } catch (IOException var10) {
            ExoticGarden.getInstance().getLogger().log(Level.SEVERE, var10, () -> "Failed to load file: \"" + id + ".schematic\"");
        }

    }

    @Nullable
    private static ItemStack getItem(@Nonnull String id) {
        SlimefunItem item = SlimefunItem.getById(id);
        return item != null ? item.getItem() : null;
    }

}