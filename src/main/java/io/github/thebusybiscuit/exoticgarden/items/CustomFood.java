package io.github.thebusybiscuit.exoticgarden.items;

import io.github.thebusybiscuit.exoticgarden.ExoticGardenRecipeTypes;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class CustomFood extends ExoticGardenFruit {

    private final int food;
    private final float sanity;

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, ItemStack[] recipe, int food, float sanity) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipe, food, sanity);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, ItemStack[] recipe, int food) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipe, food);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, int amount, ItemStack[] recipe, int food) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipe, food);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, int food, float sanity) {
        super(itemGroup, item, ExoticGardenRecipeTypes.KITCHEN, true, recipe);
        this.food = food;
        this.sanity = sanity;
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, int food) {
        super(itemGroup, item, ExoticGardenRecipeTypes.KITCHEN, true, recipe);
        this.food = food;
        this.sanity = food;
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, int amount, ItemStack[] recipe, int food) {
        super(itemGroup, item, ExoticGardenRecipeTypes.KITCHEN, true, recipe, new SlimefunItemStack(item, amount));
        this.food = food;
        this.sanity = food;
    }


    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, RecipeType recipeType, ItemStack[] recipe, int food, float sanity) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipeType, recipe, food, sanity);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, RecipeType recipeType, ItemStack[] recipe, int food) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipeType, recipe, food);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, ItemStack itemStack, String id, RecipeType recipeType, int amount, ItemStack[] recipe, int food) {
        this(itemGroup, new SlimefunItemStack(id, itemStack), recipeType, recipe, food);
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int food, float sanity) {
        super(itemGroup, item, recipeType, true, recipe);
        this.food = food;
        this.sanity = sanity;
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int food) {
        super(itemGroup, item, recipeType, true, recipe);
        this.food = food;
        this.sanity = food;
    }

    @ParametersAreNonnullByDefault
    public CustomFood(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, int amount, ItemStack[] recipe, int food) {
        super(itemGroup, item, recipeType, true, recipe, new SlimefunItemStack(item, amount));
        this.food = food;
        this.sanity = food;
    }

    @Override
    public int getFoodValue() {
        return food;
    }

    public float getSanity() {
        return sanity;
    }

    public void restoreHunger(Player p) {
        int level = p.getFoodLevel() + this.food;
        p.setFoodLevel(Math.min(level, 20));
        p.setSaturation(this.food);
        /*
        if (ExoticGarden.instance.isSanityEnabled()) {
            Sanity.getInstance().addSanity(p, this.sanity);
        }

         */
    }
}
