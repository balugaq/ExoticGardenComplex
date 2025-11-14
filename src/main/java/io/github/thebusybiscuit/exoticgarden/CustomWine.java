package io.github.thebusybiscuit.exoticgarden;

// import com.narcissu14.sanity.Sanity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;


public class CustomWine extends EGPlant implements NotPlaceable {
    final float food;
    final float sanity;
    final int alcohol;
    PotionEffect[] effects = null;

    public CustomWine(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, float sanity, int alcohol, PotionEffect[] effects) {
        super(category, item, name, recipeType, true, recipe);
        this.food = food;
        this.sanity = sanity;
        this.alcohol = alcohol;
        this.effects = effects;
    }

    public CustomWine(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, float sanity, int alcohol) {
        super(category, item, name, recipeType, true, recipe);
        this.food = food;
        this.sanity = sanity;
        this.alcohol = alcohol;
    }

    public CustomWine(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, int food, int alcohol) {
        super(category, item, name, recipeType, true, recipe);
        this.food = food;
        this.sanity = food;
        this.alcohol = alcohol;
    }


    public void restoreHunger(Player p) {
        int level = p.getFoodLevel() + (int) this.food;
        p.setFoodLevel(Math.min(level, 20));
        p.setSaturation(this.food);
        if (ExoticGarden.instance.isSanityEnabled() && this.sanity != 0) {

                float reduceExhaust = Math.max(0, this.sanity / 25.0f);
                float newExhaust = Math.max(0, p.getExhaustion() - reduceExhaust);
                p.setExhaustion(newExhaust);
        }
        if (!ExoticGarden.drunkPlayers.containsKey(p.getName())) {
            ExoticGarden.drunkPlayers.put(p.getName(), new PlayerAlcohol(p.getName(), 0));
        }
        ExoticGarden.drunkPlayers.get(p.getName()).addAlcohol(this.alcohol);
        if (this.effects != null) {
            for (PotionEffect potion : this.effects) {
                p.addPotionEffect(potion);
            }
        }
        int alcohol = ExoticGarden.drunkPlayers.get(p.getName()).getAlcohol();
        if (alcohol < 100 && alcohol > 50) {
            p.sendMessage("§8[§a异域花园§8] §e你已经半醉了，请适度饮酒！");
        } else if (alcohol >= 100) {
            p.sendMessage("§8[§a异域花园§8] §e你醉了！可以尝试食用一些可以§b解酒§e的消耗品");
            ExoticGarden.sendDrunkMessage(p);
        }
    }
}


