package io.github.thebusybiscuit.exoticgarden.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import io.github.thebusybiscuit.exoticgarden.CustomPotion;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;

/**
 * In plugin class we register all our items and recipes for the dishes.
 *
 * @author TheBusyBiscuit
 * @author SoSeDiK
 * @author yurinogueira
 * @author Hellcode48
 * @author CURVX
 * @author haiman233
 */
public final class FoodRegistry {

    private FoodRegistry() {
    }

    public static void register(@Nonnull ExoticGarden plugin, @Nonnull ItemGroup misc, @Nonnull ItemGroup drinks, @Nonnull ItemGroup food) {

        ItemStack waterbottle = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) waterbottle.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        waterbottle.setItemMeta(meta);

        new Juice(drinks, new SlimefunItemStack("WINE", new CustomPotion("&c葡萄酒", Color.RED, new PotionEffect(PotionEffectType.SATURATION, 10, 0), "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值")), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{getItem("GRAPE"), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null})
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("POTATO_SWEET_POTATO_PIE", "3418c6b0a29fc1fe791c89774d828ff63d2a9fa6c83373ef3aa47bf3eb79", "&r双薯派", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("SWEET_POTATO"), new ItemStack(Material.POTATO), SlimefunItems.HEAVY_CREAM, SlimefunItems.WHEAT_FLOUR, null, null, null, null, null},
                13)
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("VEGETABLE_OIL", "2acb28fb8a310443af02c7a1283ace95a9906b2e0e6f3636597edbe8cad4e", "&r植物油"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{new ItemStack(Material.BEETROOT_SEEDS), waterbottle, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("YEAST", "606be2df2122344bda479feece365ee0e9d5da276afa0e8ce8d848f373dd131", "&r酵母"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{new ItemStack(Material.SUGAR), waterbottle, null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("MOLASSES", "f21d7b155edf440cb87ec94487cba64e8d128171eb1187c26d5ffe58bd794c", "&8糖浆"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{new ItemStack(Material.BEETROOT), new ItemStack(Material.SUGAR_CANE), waterbottle, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("BROWN_SUGAR", "964d4247278e1498374aa6b0e47368fe4f138abc94e583e8839965fbe241be", "&r红糖"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{new ItemStack(Material.SUGAR), getItem("MOLASSES"), null, null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("COUNTRY_GRAVY", "f21fa9439bfd8384464146f9c67ebd4c5fbf4196924892627eadf3bce1ff", "&r乡村肉汁"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.SUGAR), getItem("BLACK_PEPPER"), null, null, null, null, null, null})
                .register(plugin);

        /*
        new SlimefunItem(misc, new SlimefunItemStack("URANIUM_SALT", "b5a475460b5f9a367790df9df5bbbfbc10c079d4548e3382db36f3364ec08845", "&r可食用铀盐"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{SlimefunItems.URANIUM, getItem("DEMON_MELON"), getItem("SALT"), null, null, null, null, null, null})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("CHILI_POWDER", "28e71da916c82de0c1228846458e2f7de3e5d7e5b953f4f0389a3b716c69b8c6", "&r辣椒粉"), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{getItem("RED_BELL_PEPPER"), getItem("BLACK_PEPPER"), getItem("JALAPENO_CHILI"), getItem("CHIPOTLE_CHILI"), getItem("HABANERO_CHILI"), getItem("CAROLINA_REAPER_CHILI"), getItem("SALT"), getItem("SALT"), getItem("SALT")})
                .register(plugin);

        new SlimefunItem(misc, new SlimefunItemStack("RICE_SACK", "cb70f2fb5ebf49f79ff3e873616863ae5d362fbbfc31aef2dfb93d6e17dbf2", "&f大米"), RecipeType.GRIND_STONE,
                new ItemStack[]{getItem("PADDY"), null, null, null, null, null, null, null, null})
                .register(plugin);
         */

        new CustomFood(food, new SlimefunItemStack("BAGEL", "502e92f13de3bee69228c384478e761230681e5fce9bda195daeaf8484139331", "&r面包圈", "", "&7&o恢复 &b&o" + "2.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("YEAST"), SlimefunItems.WHEAT_FLOUR, null, null, null, null, null, null, null},
                4)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("CHICKEN_CURRY", "d09e0dd5489f03efdc8083088f521b82946cdec98fc1c94c4e09792e4735184a", "&r咖喱鸡", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("CILANTRO"), new ItemStack(Material.COOKED_CHICKEN), getItem("BROWN_SUGAR"), getItem("CURRY_LEAF"), getItem("VEGETABLE_OIL"), getItem("CURRY_LEAF"), getItem("ONION"), new ItemStack(Material.BOWL), getItem("GARLIC")},
                16)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("COCONUT_CHICKEN_CURRY", "d09e0dd5489f03efdc8083088f521b82946cdec98fc1c94c4e09792e4735184a", "&r椰子咖喱鸡", "", "&7&o恢复 &b&o" + "9.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("COCONUT"), getItem("COCONUT"), getItem("CHICKEN_CURRY"), null, null, null, null, null, null},
                19)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("BISCUITS_GRAVY", "28bbb835e22d9ec62e22411b8e015138d5597283ad36e618fe44ba5f1a6b60fd", "&r乡村肉汁饼干", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("COUNTRY_GRAVY"), getItem("COUNTRY_GRAVY"), getItem("COUNTRY_GRAVY"), getItem("BISCUIT"), getItem("BISCUIT"), getItem("BISCUIT"), null, new ItemStack(Material.BOWL), null},
                13)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("CHEESE_PIZZA", "7c3cb8ae6f65a1f5ddc66ea2f86d18f1651aa5c2d845a73d44b9e3339c7e", "&r芝士披萨", "", "&7&o恢复 &b&o" + "7.5" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), null, null, null, null, null, null},
                15)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("BACON_PIZZA", "899554fb47ee5aa96187e81505331b8f3492fff7b49f59e500d6535296692382", "&r培根披萨", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), getItem("BACON"), null, null, null, null, null},
                16)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MUSHROOM_PIZZA", "b597dcad6acad3d35fa3b5e8af9aaf131d0feb095624f45b36453eae7dbaf14", "&r蘑菇披萨", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), new ItemStack(Material.BROWN_MUSHROOM), new ItemStack(Material.RED_MUSHROOM), null, null, null, null},
                16)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("FISH_PIZZA", "3752d75e8de1711178fbc028d88fe4ef908b893dc5c887b339d01fb888a24", "&r咸鱼披萨", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), new ItemStack(Material.COOKED_SALMON), new ItemStack(Material.COOKED_COD), null, null, null, null},
                16)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("VEGGIE_PIZZA", "d5c441cba4d6b57ded54c8369216a6c7156fb887d4b5639ee934f1bb6cbfdb", "&r蔬菜披萨", "", "&7&o恢复 &b&o" + "8.5" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), getItem("ONION"), getItem("CILANTRO"), getItem("BLACK_PEPPER"), null, null, null},
                17)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("GARLIC_BREAD", "a33fa7d3e63b280a5d7e2bb09332dff86b17decd2b09eccdd62da5265597f74d", "&r蒜蓉面包", "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("GARLIC"), new ItemStack(Material.BREAD), null, null, null, null, null, null, null},
                10)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("GARLIC_CHEESE_BREAD", "a33fa7d3e63b280a5d7e2bb09332dff86b17decd2b09eccdd62da5265597f74d", "&r芝士蒜蓉面包", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.CHEESE, getItem("GARLIC"), new ItemStack(Material.BREAD), null, null, null, null, null, null},
                13)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("STREET_TACO", "1ad7c0a04f1485c7a3ef261a48ee83b2f1aa701ab11f3fc911e0366a9b97e", "&r炸玉米卷", "", "&7&o恢复 &b&o" + "9.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("CORNMEAL"), new ItemStack(Material.COOKED_BEEF), getItem("CILANTRO"), getItem("ONION"), null, null, null, null, null},
                18)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("SWEET_BERRY_PANCAKES", "e44ca99e308a186b30281b2017c44189acafb591152f81feea96fecbe57", "&r甜浆果煎饼", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("PANCAKES"), new ItemStack(Material.SWEET_BERRIES), null, null, null, null, null, null, null},
                13)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("STUFFED_RED_BELL_PEPPER", "b6c98b410123b0944422303798fc2db8cea0feeb09d0da40f5361b59498f3e8b", "&c酿制红甜椒", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("RED_BELL_PEPPER"), getItem("ONION"), getItem("GARLIC"), getItem("TOMATO"), null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("SWEET_POTATO_SALAD", "68c03eebf7d4cef7648f3cd94b3b933485ddd6df84b4a9827927c15f33ad2641", "&r地瓜沙拉", "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("SWEET_POTATO"), getItem("MAYO"), getItem("ONION"), new ItemStack(Material.BOWL), null, null, null, null, null},
                10)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("FRIED_CHICKEN", Material.COOKED_CHICKEN, "&e炸鸡", "&7&o\"肯德基!\" - @Wencrow", "", "&7&o恢复 &b&o" + "3.5" + " &7&o饱食度"),
                new ItemStack[]{new ItemStack(Material.CHICKEN), new ItemStack(Material.EGG), SlimefunItems.WHEAT_FLOUR, SlimefunItems.SALT, getItem("BLACK_PEPPER"), getItem("VEGETABLE_OIL"), null, null, null},
                7)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("FRIED_CHICKEN_BUCKET", "b8fb0502a3aa5f8bd32a5ea5e519c3dd353234170dfef959ee8adb9487fea", "&e炸鸡桶", "", "&7&o恢复 &b&o" + "21.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), null, new ItemStack(Material.BUCKET), null},
                42)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("FRIED_CHICKEN_SANDWICH", "f7b9f08ada4e8ba586a04ed2e9e25fe8b9d568a665243f9c603799a7c896736", "&e炸鸡三明治", "", "&7&o恢复 &b&o" + "5.5" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.BREAD), getItem("FRIED_CHICKEN"), null, null, null, null, null, null, null},
                11)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("FRIED_CHICKEN_AND_CHEESE_SANDWICH", "48182b22adaa195504a411a71e853138fc21a498bcf0f971978f6991c7b817e0", "&r炸鸡奶酪三明治", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("FRIED_CHICKEN_SANDWICH"), SlimefunItems.CHEESE, null, null, null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("SWEET_POTATO_FRIES", "563b8aeaf1df11488efc9bd303c233a87ccba3b33f7fba9c2fecaee9567f053", "&r红薯条", "", "&7&o恢复 &b&o" + "6.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("SWEET_POTATO"), getItem("SALT"), null, null, null, null, null, null, null},
                12)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("HASHBROWN", "641b3cc9f5f45d6a74826b81a39db930fde855bc1417aa8ab59e73784913e362", "&r土豆饼", "", "&7&o恢复 &b&o" + "4.5" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.POTATO), getItem("SALT"), getItem("BLACK_PEPPER"), getItem("VEGETABLE_OIL"), null, null, null, null, null},
                9)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("SWEET_POTATO_HASHBROWN", "5e7b6cab31be13a92a310f7163050e36c5bad93b7a8c4252e814d4877c7e545b", "&r甜薯饼", "", "&7&o恢复 &b&o" + "4.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("SWEET_POTATO"), getItem("SALT"), getItem("BLACK_PEPPER"), getItem("VEGETABLE_OIL"), null, null, null, null, null},
                9)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("NOODLES", "69ec9ac43317a87f2f49e3c085c1e39a426d52252646362b646de221f1b271", "&r面条", "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.WHEAT_FLOUR, waterbottle, null, null, null, null, null, null, null},
                10)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("BUTTERED_NOODLES", "9174b34c549eed8bafe727618bab6821afcb1787b5decd1eecd6c213e7e7c6d", "&r黄油面条", "", "&7&o恢复 &b&o" + "6.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("NOODLES"), SlimefunItems.BUTTER, null, null, null, null, null, null, null},
                12)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MAC_N_CHEESE", "6bbecba5231805aaadda81d764b096eee62ed2e4cb447448544f5182b091f101", "&r通心粉奶酪", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("BUTTERED_NOODLES"), SlimefunItems.CHEESE, null, null, null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("POUTINE", "de068423d7b97f5db4d80951b61e380f24da41fcff7a33b2b264ba9e4b162ece", "&r肉汁奶酪薯条", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("FRIES"), SlimefunItems.CHEESE, getItem("COUNTRY_GRAVY"), null, null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_CURLY_FRIES", "19f99e6f4b65122f64a1e0f5d3b010cdd4c01a8ce55a5ef3e41650984b8b3ae3", "&r薯圈", "", "&7&o恢复 &b&o" + "4.0" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.POTATO), SlimefunItems.CHEESE, new ItemStack(Material.POTATO), null, new ItemStack(Material.POTATO), null, null, null, null},
                8)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_ONION_RINGS", "816a14f7132423bddd18159f76984f8814e61c08494784144d9ac35f0c9267c7", "&r洋葱圈", "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("ONION"), SlimefunItems.CHEESE, getItem("ONION"), null, getItem("ONION"), null, null, null, null},
                10)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_CHICKEN_NUGGETS", "86c31fd57f99ee32bc9dbdfdce0114ad1973f44fbfe8f5d194ad75a36867ac92", "&r炸鸡块", "", "&7&o恢复 &b&o" + "6.0" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.COOKED_CHICKEN), SlimefunItems.CHEESE, getItem("ONION"), getItem("ONION"), null, null, null, null, null},
                12)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_CHICKEN_TENDERS", "65639b618a4fd88afe7200fbf681ecbb51d827db3835867861dfaee06e373395", "&r炸鸡柳", "", "&7&o恢复 &b&o" + "6.0" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.COOKED_CHICKEN), getItem("BLACK_PEPPER"), getItem("BLACK_PEPPER"), getItem("ONION"), null, null, null, null, null},
                12)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_CHICKEN_FRIES", "c246c17bda956eafc139cbaada74d70113a2e40f6091cfb662c1dd7dd5fad1ec", "&r炸鸡条", "", "&7&o恢复 &b&o" + "6.0" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.COOKED_CHICKEN), getItem("CURRY_LEAF"), getItem("CURRY_LEAF"), getItem("ONION"), null, null, null, null, null},
                12)
                .register(plugin);
        
        new CustomFood(food, new SlimefunItemStack("MC_STEAK_FRIES", "6716b2fdd4a10576aa696858864777fb30512a9363a322dcb1b84954c03ec9b6", "&r炸牛柳", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.COOKED_BEEF), getItem("CURRY_LEAF"), getItem("CURRY_LEAF"), getItem("ONION"), null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_CHESS_FRIES", "2687bdbe4eea0717fb933b1e64a79af0c81cb358e9bff3db31a3ad8c5ef62955", "&r炸芝士条", "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值"),
                new ItemStack[]{SlimefunItems.CHEESE, getItem("CURRY_LEAF"), getItem("CURRY_LEAF"), getItem("ONION"), null, null, null, null, null},
                10)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("KFC_MEALS", "79afa1781e3a19277becc86758b4c04b85f7ec73361a75e891ff96bf11b4", "&r肯德基", "", "&7&o恢复 &b&o" + "8.5" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.COOKED_CHICKEN), getItem("RED_BELL_PEPPER"), new ItemStack(Material.COOKED_CHICKEN), getItem("ONION"), getItem("GARLIC"), getItem("BLACK_PEPPER"), null, null, null},
                17)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("SWEET_POTATO_POUTINE", "10b34069c9286efaca416fde8820b55975160c240f3e0d7d56f5bdb67b41bf85", "&r地瓜布丁", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("SWEET_POTATO_FRIES"), SlimefunItems.CHEESE, getItem("COUNTRY_GRAVY"), null, null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(drinks, new SlimefunItemStack("SODA", "27c9c71f3328ec3a7daaf122aaadd65d69c2afc7738bc09519397b39c71da9a3", "&a苏打", "", "&7&o恢复 &b&o" + "4.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("ORANGE"), getItem("LIME"), getItem("LEMON"), new ItemStack(Material.SUGAR), waterbottle, getItem("ICE_CUBE"), null, null, null},
                9)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_MUFFIN", "b8c34b9c7f568c229e5e2a7b914ddb6e710d1c095532d84019bf2de7d10cfd4e", "&r麦满分", "", "&7&o恢复 &b&o" + "4.5" + " &7&o点饥饿值"),
                new ItemStack[]{new ItemStack(Material.BREAD), new ItemStack(Material.COOKED_PORKCHOP), SlimefunItems.CHEESE, new ItemStack(Material.EGG), null, null, null, null, null},
                9)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("MC_NUGGETS", "11ec45cef70e846ff48ea174b60dc6ad2e2ab7a7ccf7eff32e914c53d5f3fc87", "&r麦乐鸡", "", "&7&o恢复 &b&o" + "7.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), getItem("FRIED_CHICKEN"), null, null, null, null, null},
                14)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("CHEESEBURGER_HAPPY_MEAL", "5f4984251a1b3d62dcbcd694b143e27d1850733f426087b860a45453a88b1481", "&r奶酪汉堡快乐儿童餐", "", "&7&o恢复 &b&o" + "8.5" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("CHEESEBURGER"), getItem("FRIES"), new ItemStack(Material.COOKIE), getItem("OAK_APPLE_JUICE"), null, null, null, null, null},
                17)
                .register(plugin);

        new CustomFood(food, new SlimefunItemStack("NUGGET_HAPPY_MEAL", "5f4984251a1b3d62dcbcd694b143e27d1850733f426087b860a45453a88b1481", "&r麦乐鸡儿童餐", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"),
                new ItemStack[]{getItem("MC_NUGGETS"), getItem("FRIES"), new ItemStack(Material.COOKIE), getItem("OAK_APPLE_JUICE"), null, null, null, null, null},
                16)
                .register(plugin);

        // Custom item
        new Juice(drinks, new SlimefunItemStack("ICE_COLA", new CustomPotion("&c冰可乐", Color.fromRGB(37, 30, 15), new PotionEffect(PotionEffectType.SPEED, 1200, 0), "", "&7+ 速度加成", "", "&7&o上面贴着&c&o可口可乐&7&o的字样", "&7&o撕开后发现是&9&o百事", "&7&o这到底是什么可乐呢...")), RecipeType.ENHANCED_CRAFTING_TABLE,
                new ItemStack[]{null, getItem("ICE_CUBE"), null, new ItemStack(Material.SUGAR), new ItemStack(Material.COCOA_BEANS), new ItemStack(Material.SUGAR), null, getItem("ICE_CUBE"), null
                }).register(plugin);
    }

    @Nullable
    private static ItemStack getItem(@Nonnull String id) {
        SlimefunItem item = SlimefunItem.getById(id);
        return item != null ? item.getItem() : null;
    }
}
