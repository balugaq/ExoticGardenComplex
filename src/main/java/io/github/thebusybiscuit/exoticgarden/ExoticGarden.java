package io.github.thebusybiscuit.exoticgarden;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.exoticgarden.items.BonemealableItem;
import io.github.thebusybiscuit.exoticgarden.items.Crook;
import io.github.thebusybiscuit.exoticgarden.items.CustomFood;
import io.github.thebusybiscuit.exoticgarden.items.ExoticGardenFruit;
import io.github.thebusybiscuit.exoticgarden.items.FoodRegistry;
import io.github.thebusybiscuit.exoticgarden.items.GrassSeeds;
import io.github.thebusybiscuit.exoticgarden.items.Kitchen;
import io.github.thebusybiscuit.exoticgarden.items.MagicalEssence;
import io.github.thebusybiscuit.exoticgarden.listeners.AndroidListener;
import io.github.thebusybiscuit.exoticgarden.listeners.PlantsListener;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

public class ExoticGarden extends JavaPlugin implements SlimefunAddon {

    public static final ConcurrentHashMap<String, PlayerAlcohol> drunkPlayers = new ConcurrentHashMap<>();
    private static final String ALCOHOL_PATH = "Players.%p.Alcohol";
    private static final String DRUNK_PATH = "Players.%p.Drunk";
    private static final List<String> drunkMsg = new ArrayList<>();
    public static ExoticGarden instance;
    private static final boolean skullitems = true;
    private final File schematicsFolder = new File(getDataFolder(), "schematics");
    private final List<Berry> berries = new ArrayList<>();
    private final List<Tree> trees = new ArrayList<>();
    private final Map<String, ItemStack> items = new HashMap<>();
    private final Set<String> treeFruits = new HashSet<>();
    private final HashMap<String, String> traslateNames = new HashMap<>();
    public NestedItemGroup nestedItemGroup;
    public ItemGroup mainItemGroup;
    public ItemGroup miscItemGroup;
    public ItemGroup foodItemGroup;
    public ItemGroup drinksItemGroup;
    public ItemGroup magicalItemGroup;
    public ItemGroup techItemGroup;
    public Kitchen kitchen;
    protected Config cfg;
    private YamlConfiguration yamlStorge = null;
    private boolean sanity = false;
    private boolean residence = false;
    private boolean fluffy = false;

    public static ItemStack getSkull(Material material, String texture) {
        return getSkull(new MaterialData(material), texture);
    }
    
    public static ItemStack getSkull(MaterialData material, String texture) {
        try {
            if (texture.equals("NO_SKULL_SPECIFIED")) return material.toItemStack(1);
            return skullitems ? SkullUtil.getByBase64(texture) : material.toItemStack(1);
        } catch (Exception e) {
            e.printStackTrace();
            return material.toItemStack(1);
        }
    }

    public static void sendDrunkMessage(Player player) {
        Random ramdom = new Random();
        player.chat(drunkMsg.get(ramdom.nextInt(drunkMsg.size())).replace("%player%", (
                (Player) Bukkit.getOnlinePlayers().toArray()[ramdom.nextInt(Bukkit.getOnlinePlayers().size())]).getName()));
    }

    @Nullable
    static ItemStack getItem(@Nonnull String id) {
        SlimefunItem item = SlimefunItem.getById(id);
        return item != null ? item.getItem() : null;
    }

    @Nullable
    public static ItemStack harvestPlant(@Nonnull Block block) {
        SlimefunItem item = StorageCacheUtils.getSfItem(block.getLocation());

        if (item == null) {
            return null;
        }

        for (Berry berry : getBerries()) {
            if (item.getId().equalsIgnoreCase(berry.getID())) {
                var controller = Slimefun.getDatabaseManager().getBlockDataController();
                switch (berry.getType()) {
                    case ORE_PLANT, DOUBLE_PLANT -> {
                        Block plant;
                        Block head;
                        if (Tag.LEAVES.isTagged(block.getType())) {
                            // Player broke the leaf block
                            plant = block;
                            head = block.getRelative(BlockFace.UP);
                        } else {
                            // Player broke the head block
                            plant = block.getRelative(BlockFace.DOWN);
                            head = block;
                        }

                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.OAK_LEAVES);
                        head.setType(Material.AIR, false);
                        plant.setType(Material.OAK_SAPLING, false);
                        controller.removeBlock(head.getLocation());
                        controller.removeBlock(plant.getLocation());
                        BlockStorage.store(plant, getItem(berry.toBush()));
                        return berry.getItem().clone();
                    }
                    default -> {
                        block.setType(Material.OAK_SAPLING, false);
                        controller.removeBlock(block.getLocation());
                        BlockStorage.store(block, getItem(berry.toBush()));
                        return berry.getItem().clone();
                    }
                }
            }
        }

        return null;
    }

    public static ExoticGarden getInstance() {
        return instance;
    }

    public static Kitchen getKitchen() {
        return instance.kitchen;
    }

    public static List<Tree> getTrees() {
        return instance.trees;
    }

    public static List<Berry> getBerries() {
        return instance.berries;
    }

    public static Map<String, ItemStack> getGrassDrops() {
        return instance.items;
    }

    public boolean isFluffyEnabled() {
        return fluffy;
    }

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);

        if (!getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            getLogger().log(Level.SEVERE, "本插件需要 鬼斩前置库插件(GuizhanLibPlugin) 才能运行!");
            getLogger().log(Level.SEVERE, "从此处下载: https://50L.cc/gzlib");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!schematicsFolder.exists()) {
            schematicsFolder.mkdirs();
        }

        instance = this;
        cfg = new Config(this);

        // Setting up bStats
        new Metrics(this, 4575);

        // Auto Updater
        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("Build")) {
            GuizhanUpdater.start(this, getFile(), "ybw0014", "ExoticGarden", "master");
        }

        initTransNames();

        registerItems();

        new AndroidListener(this);
        new PlantsListener(this);
        new FoodListener(this);

        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Sanity")) {
            this.sanity = true;
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Residence")) {
            // FlagPermissions.addFlag("exo-harvest");
            this.residence = true;
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FluffyMachines")) {
            this.fluffy = true;
        }
        getCommand("exotic").setExecutor(new ExoticCommand());

    }

    private void registerDishes() {
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ1ZThjNDg2YjUyNmRkYTgxMmI0MjQ0YzJmMjE5NDE4OWZiZWJjY2JlYmZiYTVhOTM3YTU2NTMzNWRhNDEyIn19fQ=="), "§3咖啡", new String[] { "", "&7提神醒脑的咖啡", "&7恢复&e2&7点饥饿", "&7恢复&e6&7点精神" }), "COFFEE", RecipeType.JUICER, new ItemStack[] {

           getItem("COFFEEBEAN"), null, null, null, null, null, null, null, null }, 2, 6.0F)).register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&a酸橙冰沙", 8203, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 10, 0)), "LIME_SMOOTHIE", RecipeType.JUICER, new ItemStack[] {
           getItem("LIME_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null }, 5))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&4番茄汁", 8193, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "TOMATO_JUICE", RecipeType.JUICER, new ItemStack[] {
           getItem("TOMATO"), null, null, null, null, null, null, null, null }, 3))
       .register(ExoticGarden.instance);
        
        
        
        
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&e柠檬冰茶", 8227, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "LEMON_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("LEMON"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&d覆盆子冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "RASPBERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("RASPBERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&d香桃冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "PEACH_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("PEACH"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&c草莓冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "STRAWBERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("STRAWBERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&c樱桃冰茶", 8193, new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 13, 0)), "CHERRY_ICED_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("CHERRY"), getItem("ICE_CUBE"), getItem("TEA_LEAF"), null, null, null, null, null, null }, 6))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&6泰式甜茶", 8201, new String[] { "", "§7恢复 §b7.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 14, 0)), "THAI_TEA", RecipeType.JUICER, new ItemStack[] {
           getItem("TEA_LEAF"), new ItemStack(Material.SUGAR), SlimefunItems.HEAVY_CREAM, getItem("COCONUT_MILK"), null, null, null, null, null }, 7))
       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ODdkNDU3ZjkwNjJkNzg3YTNlNmNlMWM0NjY0YmY3NDAyZWM2N2RkMTExMjU2ZjE5YjM4Y2U0ZjY3MCJ9fX0="), "&e南瓜面包", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "PUMPKIN_BREAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.SUGAR), new ItemStack(Material.PUMPKIN), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR }, 8))



       .register(ExoticGarden.instance);
        
             (new EGPlant(Categories.MISC, (ItemStack)new CustomItemStack(getSkull(Material.MILK_BUCKET, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4ZDUzNmM4YzJjMjU5NmJjYzE3MDk1OTBhOWQ3ZTMzMDYxYzU2ZTY1ODk3NGNkODFiYjgzMmVhNGQ4ODQyIn19fQ=="), "&e蛋黄酱"), "MAYO", RecipeType.GRIND_STONE, false, new ItemStack[] { new ItemStack(Material.EGG), new ItemStack(Material.EGG), null, null, null, null, null, null, null


         })).register(ExoticGarden.instance);
        
             (new EGPlant(Categories.MISC, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI5ZTk5NjIxYjk3NzNiMjllMzc1ZTYyYzY0OTVmZjFhYzg0N2Y4NWIyOTgxNmMyZWI3N2I1ODc4NzRiYTYyIn19fQ=="), "&e芥末"), "MUSTARD", RecipeType.GRIND_STONE, false, new ItemStack[] {

           getItem("MUSTARD_SEED"), getItem("MUSTARD_SEED"), null, null, null, null, null, null, null
         })).register(ExoticGarden.instance);
        
             (new EGPlant(Categories.MISC, (ItemStack)new CustomItemStack(getSkull(Material.MILK_BUCKET, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg2ZjE5YmYyM2QyNDhlNjYyYzljOGI3ZmExNWVmYjhhMWYxZDViZGFjZDNiODYyNWE5YjU5ZTkzYWM4YSJ9fX0="), "&c烤肉酱"), "BBQ_SAUCE", RecipeType.ENHANCED_CRAFTING_TABLE, false, new ItemStack[] {

           getItem("TOMATO"), getItem("MUSTARD"), getItem("SALT"), new ItemStack(Material.SUGAR), null, null, null, null, null
         })).register(ExoticGarden.instance);
        
             (new SlimefunItem(Categories.MISC, (ItemStack)new CustomItemStack(new MaterialData(Material.SUGAR), "&r玉米粉", new String[0]), "CORNMEAL", RecipeType.GRIND_STONE, new ItemStack[] {
           getItem("CORN"), null, null, null, null, null, null, null, null
         })).register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(new MaterialData(Material.INK_SACK, (byte)3), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE5Zjk0OGQxNzcxOGFkYWNlNWRkNmUwNTBjNTg2MjI5NjUzZmVmNjQ1ZDcxMTNhYjk0ZDE3YjYzOWNjNDY2In19fQ=="), "&3巧克力棒", new String[] { "", "§7恢复 §b1.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_BAR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { (new MaterialData(Material.INK_SACK, (byte)3))
                
                           .toItemStack(1), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null, null }3))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&e土豆沙拉", 0, new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "POTATO_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BAKED_POTATO),
           getItem("MAYO"), new ItemStack(Material.BAKED_POTATO), null, new ItemStack(Material.BOWL), null, null, null, null }, 6))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&e鸡肉三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "CHICKEN_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_CHICKEN),

           getItem("MAYO"), new ItemStack(Material.BREAD), null, null, null, null, null, null }, 11))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3鱼肉三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "FISH_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_FISH),

           getItem("MAYO"), new ItemStack(Material.BREAD), null, null, null, null, null, null }, 11))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&e鸡蛋沙拉", 0, new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "EGG_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.EGG),
           getItem("MAYO"), new ItemStack(Material.EGG), null, new ItemStack(Material.BOWL), null, null, null, null }, 6))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&4番茄浓汤", 0, new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "TOMATO_SOUP", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HEAVY_CREAM,
           getItem("TOMATO"), SlimefunItems.SALT, null, new ItemStack(Material.BOWL), null, null, null, null }, 5))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&c草莓沙拉", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "STRAWBERRY_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BOWL),
           getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 4))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&c葡萄沙拉", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "GRAPE_SALAD", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BOWL),
           getItem("GRAPE"), null, null, null, null, null, null, null }, 4))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&r芝士蛋糕", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, new ItemStack(Material.SUGAR), null, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR, SlimefunItems.WHEAT_FLOUR }, 16))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&c樱桃芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHERRY_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESECAKE"), getItem("CHERRY"), null, null, null, null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&9蓝莓芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESECAKE"), getItem("BLUEBERRY"), null, null, null, null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&6南瓜芝士蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "PUMPKIN_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESECAKE"), new ItemStack(Material.PUMPKIN), null, null, null, null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&6甜梨芝士蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "SWEETENED_PEAR_CHEESECAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESECAKE"), new ItemStack(Material.SUGAR), getItem("PEAR"), null, null, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.COOKIE, "&6松饼", 0, new String[] { "", "§7恢复 §b2.0 §7饥饿值§8和§7精神值" }), "BISCUIT", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, null, null, null, null, null, null, null }, 2))


       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjMzY1MjNjMmQxMWI4YzhlYTJlOTkyMjkxYzUyYTY1NDc2MGVjNzJkY2MzMmRhMmNiNjM2MTY0ODFlZSJ9fX0="), "&8黑莓脆皮饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "BLACKBERRY_COBBLER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.SUGAR),

           getItem("BLACKBERRY"), SlimefunItems.WHEAT_FLOUR, null, null, null, null, null, null }, 4))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjM2NWI2MWU3OWZjYjkxM2JjODYwZjRlYzYzNWQ0YTZhYjFiNzRiZmFiNjJmYjZlYTZkODlhMTZhYTg0MSJ9fX0="), "&e帕芙洛娃", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "PAVLOVA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("LEMON"), getItem("STRAWBERRY"), new ItemStack(Material.SUGAR), new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.GOLDEN_CARROT, "&6香甜玉米棒", 0, new String[] { "", "§7恢复 §b4.5 §7饥饿值§8和§7精神值" }), "CORN_ON_THE_COB", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.BUTTER,
           getItem("CORN"), null, null, null, null, null, null, null }, 3))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.MUSHROOM_SOUP, "&r奶油玉米粒", 0, new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "CREAMED_CORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { SlimefunItems.HEAVY_CREAM,
           getItem("CORN"), new ItemStack(Material.BOWL), null, null, null, null, null, null }, 2))

       .register(ExoticGarden.instance);
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.GRILLED_PORK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdiYTIyZDVkZjIxZTgyMWE2ZGU0YjhjOWQzNzNhM2FhMTg3ZDhhZTc0ZjI4OGE4MmQyYjYxZjI3MmU1In19fQ=="), "&3培根", new String[] { "", "§7恢复 §b1.5 §7饥饿值§8和§7精神值" }), "BACON", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.GRILLED_PORK), null, null, null, null, null, null, null, null }, 3))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD),

           getItem("MAYO"), new ItemStack(Material.COOKED_BEEF), getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null }, 19))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3BLT三明治", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "BLT", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.GRILLED_PORK),

           getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3鲜蔬鸡肉三明治", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "LEAFY_CHICKEN_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHICKEN_SANDWICH"), getItem("LETTUCE"), null, null, null, null, null, null, null }, 1))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&3时蔬鲜鱼三明治", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "LEAFY_FISH_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("FISH_SANDWICH"), getItem("LETTUCE"), null, null, null, null, null, null, null }, 11))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&3汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "HAMBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.COOKED_BEEF), null, null, null, null, null, null, null }, 10))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e芝士汉堡", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HAMBURGER"), SlimefunItems.CHEESE, null, null, null, null, null, null, null }, 13))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e培根芝士汉堡", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BACON_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESEBURGER"), getItem("BACON"), null, null, null, null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e豪华芝士汉堡", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "DELUXE_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHEESEBURGER"), getItem("LETTUCE"), getItem("TOMATO"), null, null, null, null, null, null }, 16))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkxMzY1MTRmMzQyZTdjNTIwOGExNDIyNTA2YTg2NjE1OGVmODRkMmIyNDkyMjAxMzllOGJmNjAzMmUxOTMifX19"), "&c胡萝卜蛋糕", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "CARROT_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.SUGAR), new ItemStack(Material.EGG), null, null, null, null, null }, 12))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&3鸡肉汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "CHICKEN_BURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.COOKED_CHICKEN), null, null, null, null, null, null, null }, 10))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&e鸡肉芝士汉堡", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHICKEN_CHEESEBURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHICKEN_BURGER"), SlimefunItems.CHEESE, null, null, null, null, null, null, null }, 13))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0="), "&c培根汉堡", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "BACON_BURGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD),

           getItem("BACON"), null, null, null, null, null, null, null }, 10))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&c培根三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "BACON_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD),

           getItem("BACON"), getItem("MAYO"), getItem("TOMATO"), getItem("LETTUCE"), null, null, null, null }, 19))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThjZWQ3NGEyMjAyMWE1MzVmNmJjZTIxYzhjNjMyYjI3M2RjMmQ5NTUyYjcxYTM4ZDU3MjY5YjM1MzhjZiJ9fX0="), "&e墨西哥玉米卷", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TACO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORNMEAL"), new ItemStack(Material.COOKED_BEEF), getItem("LETTUCE"), getItem("TOMATO"), getItem("CHEESE"), null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThjZWQ3NGEyMjAyMWE1MzVmNmJjZTIxYzhjNjMyYjI3M2RjMmQ5NTUyYjcxYTM4ZDU3MjY5YjM1MzhjZiJ9fX0="), "&3鲜鱼玉米卷", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "FISH_TACO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORNMEAL"), new ItemStack(Material.COOKED_FISH), getItem("LETTUCE"), getItem("TOMATO"), getItem("CHEESE"), null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(Material.COOKIE, "&c果酱夹心饼", 0, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "JAMMY_DODGER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null,
           getItem("BISCUIT"), null, null, getItem("RASPBERRY_JUICE"), null, null, getItem("BISCUIT"), null }, 8))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&e薄煎饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "PANCAKES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("WHEAT_FLOUR"), new ItemStack(Material.SUGAR), getItem("BUTTER"), new ItemStack(Material.EGG), new ItemStack(Material.EGG), null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&b蓝莓煎饼", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_PANCAKES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("PANCAKES"), getItem("BLUEBERRY"), null, null, null, null, null, null, null }, 13))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTYzYjhhZWFmMWRmMTE0ODhlZmM5YmQzMDNjMjMzYTg3Y2NiYTNiMzNmN2ZiYTljMmZlY2FlZTk1NjdmMDUzIn19fQ=="), "&e炸薯条", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "FRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.POTATO_ITEM),

           getItem("SALT"), null, null, null, null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORN"), getItem("BUTTER"), null, null, null, null, null, null, null }, 8))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花 &7(甜)", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "SWEET_POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORN"), getItem("BUTTER"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTATO_ITEM, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ5N2IxNDdjZmFlNTIyMDU1OTdmNzJlM2M0ZWY1MjUxMmU5Njc3MDIwZTRiNGZhNzUxMmMzYzZhY2RkOGMxIn19fQ=="), "&e爆米花 &7(咸)", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "SALTY_POPCORN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORN"), getItem("BUTTER"), getItem("SALT"), null, null, null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&e牧羊人派", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "SHEPARDS_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CABBAGE"), new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.COOKED_BEEF), getItem("TOMATO"), null, null, null, null }, 16))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&e鸡肉派", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHICKEN_POT_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.COOKED_CHICKEN), new ItemStack(Material.CARROT_ITEM), SlimefunItems.WHEAT_FLOUR, new ItemStack(Material.POTATO_ITEM), null, null, null, null, null }, 17))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&c巧克力蛋糕", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, new ItemStack(Material.EGG), null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.COOKIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZkNzFlMjBmYzUwYWJmMGRlMmVmN2RlY2ZjMDFjZTI3YWQ1MTk1NTc1OWUwNzJjZWFhYjk2MzU1ZjU5NGYwIn19fQ=="), "&r奶油曲奇", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "CREAM_COOKIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&b蓝莓玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "BLUEBERRY_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("BLUEBERRY"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&e南瓜玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "PUMPKIN_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.PUMPKIN), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM3OTRjNzM2ZmM3NmU0NTcwNjgzMDMyNWI5NTk2OTQ2NmQ4NmY4ZDdiMjhmY2U4ZWRiMmM3NWUyYWIyNWMifX19"), "&c巧克力薄片玛芬", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_CHIP_MUFFIN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, SlimefunItems.HEAVY_CREAM, new ItemStack(Material.EGG), null, null, null }, 13))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGZkNzFlMjBmYzUwYWJmMGRlMmVmN2RlY2ZjMDFjZTI3YWQ1MTk1NTc1OWUwNzJjZWFhYjk2MzU1ZjU5NGYwIn19fQ=="), "&r波士顿奶油派", new String[] { "", "§7恢复 §b4.5 §7饥饿值§8和§7精神值" }), "BOSTON_CREAM_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null,

           getItem("CHOCOLATE_BAR"), null, null, SlimefunItems.HEAVY_CREAM, null, null, getItem("BISCUIT"), null }, 9))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c香肠", new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }), "HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { null, null, null, null, new ItemStack(Material.GRILLED_PORK), null, null, new ItemStack(Material.BREAD), null }, 10))



       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c培根芝士香肠", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BACON_WRAPPED_CHEESE_FILLED_HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("BACON"), getItem("HOT_DOG"), getItem("BACON"), null, getItem("CHEESE"), null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c烤肉培根香肠", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "BBQ_BACON_WRAPPED_HOT_DOG", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("BACON"), getItem("HOT_DOG"), getItem("BACON"), null, getItem("BBQ_SAUCE"), null, null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzNmMmQ3ZDdhOGIxYjk2OTE0Mjg4MWViNWE4N2U3MzdiNWY3NWZiODA4YjlhMTU3YWRkZGIyYzZhZWMzODIifX19"), "&c双重烤肉培根香肠", new String[] { "", "§7恢复 §b10.0 §7饥饿值§8和§7精神值" }), "BBQ_DOUBLE_BACON_WRAPPED_HOT_DOG_IN_A_TORTILLA_WITH_CHEESE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("BACON"), getItem("BBQ_SAUCE"), getItem("BACON"), getItem("BACON"), new ItemStack(Material.GRILLED_PORK), getItem("BACON"), getItem("CORNMEAL"), getItem("CHEESE"), getItem("CORNMEAL") }, 20))

       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhlOTRkZGQ3NjlhNWJlYTc0ODM3NmI0ZWM3MzgzZmQzNmQyNjc4OTRkN2MzYmVlMDExZThlNGY1ZmNkNyJ9fX0="), "&a甜茶", new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }), "SWEETENED_TEA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("TEA_LEAF"), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null }, 6))

       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDExNTExYmRkNTViY2I4MjgwM2M4MDM5ZjFjMTU1ZmQ0MzA2MjYzNmUyM2Q0ZDQ2YzRkNzYxYzA0ZDIyYzIifX19"), "&6热巧克力", new String[] { "", "§7恢复 §b4.0 §7饥饿值§8和§7精神值" }), "HOT_CHOCOLATE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), SlimefunItems.HEAVY_CREAM, null, null, null, null, null, null, null }, 8))

       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE4ZjFmNzBlODU4MjU2MDdkMjhlZGNlMWEyYWQ0NTA2ZTczMmI0YTUzNDVhNWVhNmU4MDdjNGIzMTNlODgifX19"), "&6椰林飘香", new String[] { "", "§7恢复 §b7.0 §7饥饿值§8和§7精神值" }), "PINACOLADA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("PINEAPPLE"), getItem("ICE_CUBE"), getItem("COCONUT_MILK"), null, null, null, null, null, null }, 14))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.NETHER_BRICK, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ0ZWQ3YzczYWMyODUzZGZjYWE5Y2E3ODlmYjE4ZGExZDQ3YjE3YWQ2OGIyZGE3NDhkYmQxMWRlMWE0OWVmIn19fQ=="), "&c巧克力脆皮草莓", new String[] { "", "§7恢复 §b2.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_STRAWBERRY", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 5))

       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("&e柠檬水", 8227, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "LEMONADE", RecipeType.JUICER, new ItemStack[] {
           getItem("LEMON_JUICE"), new ItemStack(Material.SUGAR), null, null, null, null, null, null, null }, 3))
       .register(ExoticGarden.instance);
        
             new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&c番薯派", new String[] { "", "§7恢复 §b6.5 §7饥饿值§8和§7精神值" }), "SWEET_POTATO_PIE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("SWEET_POTATO"), new ItemStack(Material.EGG), SlimefunItems.HEAVY_CREAM, SlimefunItems.WHEAT_FLOUR, null, null, null, null, null }, 13);
        
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&r巧克力椰丝蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "LAMINGTON", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("COCONUT"), null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ3ZjRmNWE3NGM2NjkxMjgwY2Q4MGU3MTQ4YjQ5YjJjZTE3ZGNmNjRmZDU1MzY4NjI3ZjVkOTJhOTc2YTZhOCJ9fX0="), "&e华夫饼", new String[] { "", "§7恢复 §b6.0 §7饥饿值§8和§7精神值" }), "WAFFLES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("WHEAT_FLOUR"), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), getItem("BUTTER"), null, null, null, null, null }, 12))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0MjE2ZDEwNzE0MDgyYmJlM2Y0MTI0MjNlNmIxOTIzMjM1MmY0ZDY0ZjlhY2EzOTEzY2I0NjMxOGQzZWQifX19"), "&e俱乐部三明治", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "CLUB_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD),

           getItem("MAYO"), getItem("BACON"), getItem("TOMATO"), getItem("LETTUCE"), getItem("MUSTARD"), null, null, null }, 19))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4N2E2MjFlMjY2MTg2ZTYwNjgzMzkyZWIyNzRlYmIyMjViMDQ4NjhhYjk1OTE3N2Q5ZGMxODFkOGYyODYifX19"), "&e卷饼", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "BURRITO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORNMEAL"), new ItemStack(Material.COOKED_BEEF), getItem("LETTUCE"), getItem("TOMATO"), getItem("HEAVY_CREAM"), getItem("CHEESE"), null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4N2E2MjFlMjY2MTg2ZTYwNjgzMzkyZWIyNzRlYmIyMjViMDQ4NjhhYjk1OTE3N2Q5ZGMxODFkOGYyODYifX19"), "&e鸡肉卷饼", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "CHICKEN_BURRITO", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CORNMEAL"), new ItemStack(Material.COOKED_CHICKEN), getItem("LETTUCE"), getItem("TOMATO"), getItem("HEAVY_CREAM"), getItem("CHEESE"), null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFlZTg0ZDE5Yzg1YWZmNzk2Yzg4YWJkYTIxZWM0YzkyYzY1NWUyZDY3YjcyZTVlNzdiNWFhNWU5OWVkIn19fQ=="), "&c烧烤三明治", new String[] { "", "§7恢复 §b5.5 §7饥饿值§8和§7精神值" }), "GRILLED_SANDWICH", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] { new ItemStack(Material.BREAD), new ItemStack(Material.GRILLED_PORK),

           getItem("CHEESE"), null, null, null, null, null, null }, 11))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.BREAD, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDNhMzU3NGE4NDhmMzZhZTM3MTIxZTkwNThhYTYxYzEyYTI2MWVlNWEzNzE2ZjZkODI2OWUxMWUxOWUzNyJ9fX0="), "&c千层面", new String[] { "", "§7恢复 §b8.5 §7饥饿值§8和§7精神值" }), "LASAGNA", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("TOMATO"), getItem("CHEESE"), SlimefunItems.WHEAT_FLOUR, getItem("TOMATO"), getItem("CHEESE"), new ItemStack(Material.COOKED_BEEF), null, null, null }, 17))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.SNOWBALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzNjZjYTE3OTc0ODkyZTRmZDRjN2I5YjE4ZmViMTFmMDViYTJlYzQ3YWE1MDM1YzgxYTk1MzNiMjgifX19"), "&r冰激凌", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "ICE_CREAM", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HEAVY_CREAM"), getItem("ICE_CUBE"), new ItemStack(Material.SUGAR), (new MaterialData(Material.INK_SACK, (byte)3)).toItemStack(1), getItem("STRAWBERRY"), null, null, null, null }16))

       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("§6菠萝汁", 8195, new String[] { "", "§7恢复 §b3.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 6, 0)), "PINEAPPLE_JUICE", RecipeType.JUICER, new ItemStack[] {
           getItem("PINEAPPLE"), null, null, null, null, null, null, null, null }, 3))
       .register(ExoticGarden.instance);
        
             (new CustomFood(drinksItemGroup, (ItemStack)new CustomPotion("§6菠萝冰沙", 8195, new String[] { "", "§7恢复 §b5.0 §7饥饿值§8和§7精神值" }, new PotionEffect(PotionEffectType.SATURATION, 10, 0)), "PINEAPPLE_SMOOTHIE", RecipeType.JUICER, new ItemStack[] {
           getItem("PINEAPPLE_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null }, 5))
       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.SNOWBALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&r提拉米苏", new String[] { "", "§7恢复 §b8.0 §7饥饿值§8和§7精神值" }), "TIRAMISU", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HEAVY_CREAM"), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), (new MaterialData(Material.INK_SAC, (byte)3)).toItemStack(1), new ItemStack(Material.EGG), null, null, null, null }, 16))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.SNOWBALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&c草莓提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_STRAWBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HEAVY_CREAM"), getItem("STRAWBERRY"), null, null, null, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.SNOWBALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&c覆盆子提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_RASPBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HEAVY_CREAM"), getItem("RASPBERRY"), null, null, null, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.SNOWBALL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY5MDkxZDI4ODAyMmM3YjBlYjZkM2UzZjQ0YjBmZWE3ZjJjMDY5ZjQ5NzQ5MWExZGNhYjU4N2ViMWQ1NmQ0In19fQ=="), "&7黑莓提拉米苏", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "TIRAMISU_WITH_BLACKBERRIES", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("HEAVY_CREAM"), getItem("BLACKBERRY"), null, null, null, null, null, null, null }, 18))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTExOWZjYTRmMjhhNzU1ZDM3ZmJlNWRjZjZkOGMzZWY1MGZlMzk0YzFhNzg1MGJjN2UyYjcxZWU3ODMwM2M0YyJ9fX0="), "&e巧克力香梨蛋糕", new String[] { "", "§7恢复 §b9.5 §7饥饿值§8和§7精神值" }), "CHOCOLATE_PEAR_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("CHOCOLATE_BAR"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("PEAR"), new ItemStack(Material.EGG), null, null, null }, 19))

       .register(ExoticGarden.instance);
        
             (new CustomFood(foodItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.PUMPKIN_PIE, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQxOGM2YjBhMjlmYzFmZTc5MWM4OTc3NGQ4MjhmZjYzZDJhOWZhNmM4MzM3M2VmM2FhNDdiZjNlYjc5In19fQ=="), "&c苹果香梨蛋糕", new String[] { "", "§7恢复 §b9.0 §7饥饿值§8和§7精神值" }), "APPLE_PEAR_CAKE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

           getItem("APPLE"), new ItemStack(Material.SUGAR), SlimefunItems.WHEAT_FLOUR, SlimefunItems.BUTTER, getItem("PEAR"), new ItemStack(Material.EGG), null, null, null }, 18))

       .register(ExoticGarden.instance);
        
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3土烧", new String[] { "§8初级酒", "§7传统的土法制酒", "§7味道一般但胜在天然", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e2" }), "NORMAL_BREW", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.WHEAT), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 2, 3.0F, 20))

       .register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§3苹果酒", new String[] { "§8初级酒", "§7甜酸的苹果发酵而来", "§7是一种传统的果酒", "", "§7▷▷ §b酒精度: §e15", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "APPLE_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.APPLE), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 15))

       .register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3格瓦斯", new String[] { "§8初级酒", "§7一种传统的东欧饮料", "§7由面包发酵而来", "", "§7▷▷ §b酒精度: §e10", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e6" }), "BREAD_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.BREAD), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 6, 3.0F, 10))

       .register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc2MmRmYjJmMjQ0YjU2NWVhZjY5Yzg1ZTkyNDY4M2E5ODU0MWVhODg2ZDkzZDFhMzA0NTEyYWEzZDM2NzY2MyJ9fX0="), "§3土豆酒", new String[] { "§8初级酒", "§7廉价易制的酒", "§7有一种特殊的香气", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "POTATO_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.POTATO_ITEM), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 20))

       .register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§3下界酒", new String[] { "§8初级酒", "§7来自下界的酿品", "§7饮用后有些其妙的感觉...", "", "§7▷▷ §b酒精度: §e25", "§7▷▷ §d精神值: §e3", "§7▷▷ §a饱食度: §e4" }), "NETHER_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.NETHER_STALK), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 25, new PotionEffect[] { new PotionEffect(PotionEffectType.SPEED, 200, 1)


         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ=="), "§3酸奶酒", new String[] { "§8初级酒", "§7特殊的牛奶酿造品", "§7饮用后有些许的解酒功效", "", "§7▷▷ §b酒精度: §e-10", "§7▷▷ §d精神值: §e1", "§7▷▷ §a饱食度: §e8" }), "MILK_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.MILK_BUCKET), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 8, 1.0F, -10, new PotionEffect[] { new PotionEffect(PotionEffectType.SPEED, 200, 1)


         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFmODE4ZjNmNGNjMmI3YzhlNzBmOGJlYWY4MGY3OWU5MjY1YjMzZmJmZDcxNzZjN2MzMzQ5ZDRiNzZiIn19fQ=="), "§3紫影酒", new String[] { "§8初级酒", "§7来自终末之地的酿品", "§7会让人失去几秒的疼痛感", "", "§7▷▷ §b酒精度: §e25", "§7▷▷ §d精神值: §e1", "§7▷▷ §a饱食度: §e4" }), "ENDER_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_1, new ItemStack[] { ExoticItems.Yeast_1, new ItemStack(Material.CHORUS_FRUIT), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 3.0F, 25, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 160, 1)


         })).register(ExoticGarden.instance);
        
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFmODE4ZjNmNGNjMmI3YzhlNzBmOGJlYWY4MGY3OWU5MjY1YjMzZmJmZDcxNzZjN2MzMzQ5ZDRiNzZiIn19fQ=="), "§b二锅头", new String[] { "§8中级酒", "§7经典的蒸馏型白酒", "§7酒精度数很高", "", "§7▷▷ §b酒精度: §e65", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "WHITE_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("NORMAL_BREW"), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 4, 15.0F, 65, new PotionEffect[] { new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 240, 1)
         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc2MmRmYjJmMjQ0YjU2NWVhZjY5Yzg1ZTkyNDY4M2E5ODU0MWVhODg2ZDkzZDFhMzA0NTEyYWEzZDM2NzY2MyJ9fX0="), "§b苹果醋", new String[] { "§8中级酒", "§7香甜的苹果酒继续发酵制成", "§7酸甜可口，几乎没有酒精度", "", "§7▷▷ §b酒精度: §e5", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e10" }), "APPLE_VINEGAR", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("APPLE_WINE"), getItem("APPLE"), null, null, null, null, null, null }, 10, 10.0F, 5, new PotionEffect[] { new PotionEffect(PotionEffectType.LUCK, 200, 1)
         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§b赤炎酒", new String[] { "§8中级酒", "§7如火焰般的酿品", "§7非常辣口但却令人精神舒畅无惧烈焰", "", "§7▷▷ §b酒精度: §e60", "§7▷▷ §d精神值: §e20", "§7▷▷ §a饱食度: §e4" }), "FIRE_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("NETHER_WINE"), new ItemStack(Material.MAGMA_CREAM), null, null, null, null, null, null }, 4, 20.0F, 60, new PotionEffect[] { new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 1)
         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZlMWExZjNjNzcxNTNhYmZlMzhjZjgyZWVmMzZhOGJmN2VjMzJjM2M0MTc1NzZiMDU5YTVmMmU2ZGI0YmY3In19fQ=="), "§b葡萄酒", new String[] { "§8中级酒", "§7传统的葡萄酒", "§7酸甜中略微有一股特殊的苦涩味", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "GRAPE_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("GRAPE"), getItem("GRAPE"), null, null, null, null, null, null }, 4, 15.0F, 20)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b玉米酒", new String[] { "§8中级酒", "§7富含淀粉的玉米发酵而来", "§7兼有玉米清香与酒的醇香", "", "§7▷▷ §b酒精度: §e20", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "CORN_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("CORN"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 15.0F, 25)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b黄酒", new String[] { "§8中级酒", "§7拥有特殊酱香味的酒", "§7既可以饮用也可以用于烹饪", "", "§7▷▷ §b酒精度: §e30", "§7▷▷ §d精神值: §e15", "§7▷▷ §a饱食度: §e4" }), "YELLOW_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("SWEET_POTATO"), new ItemStack(Material.SUGAR), null, null, null, null, null, null }, 4, 15.0F, 30)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0="), "§b淡啤酒", new String[] { "§8中级酒", "§7清淡的啤酒", "§7冰镇后风味更佳", "", "§7▷▷ §b酒精度: §e10", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e2" }), "LIGHT_BEER", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("WINEFRUIT"), getItem("WINEFRUIT"), null, null, null, null, null, null }, 2, 10.0F, 10)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBjOGFhMTNlMTJhZjYxNWNiMzYyZjhhZjk0ZGQ1ZWEyNzgxODM5MDdmZTBhYmQ4NGQ2NWEwNzk5OTJkYTQifX19"), "§b啤酒", new String[] { "§8中级酒", "§7苦涩味略重的啤酒", "§7冰镇后风味更佳", "", "§7▷▷ §b酒精度: §e15", "§7▷▷ §d精神值: §e10", "§7▷▷ §a饱食度: §e4" }), "BEER", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("WINEFRUIT"), new ItemStack(Material.WHEAT), null, null, null, null, null, null }, 4, 10.0F, 15)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§b朗姆酒", new String[] { "§8中级酒", "§7来自古巴的传统佳酿", "§7由海盗和商贩们传向世界各地", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e20", "§7▷▷ §a饱食度: §e6" }), "RUM_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("LIME"), new ItemStack(Material.VINE), null, null, null, null, null, null }, 6, 20.0F, 40)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0="), "§b菠萝啤", new String[] { "§8中级酒", "§7使用菠萝特别调制的啤酒", "§7酸甜可口而又有啤酒淡淡的苦涩味", "", "§7▷▷ §b酒精度: §e18", "§7▷▷ §d精神值: §e16", "§7▷▷ §a饱食度: §e12" }), "PINEAPPLE_BEER", ExoticGardenRecipeTypes.ElectricityBrewing_2, new ItemStack[] { ExoticItems.Yeast_2,

           getItem("BEER"), getItem("PINEAPPLE"), null, null, null, null, null, null }, 12, 16.0F, 18)).register(ExoticGarden.instance);
        
        
//             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBhY2RlZWQ2MDcyNWQ5NWI2OTExNDM3MmQ3MDI0ZjlkNjY4ZjlmZTc0NjkzN2UwNTkzMjhiYmZiZmY2In19fQ=="), "§6仙馐酒", new String[] { "§8高级酒", "§7由神秘的仙馐果酿制", "§7拥有梦幻般的味道", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e10" }), "DREAMFRUIT_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_3, new ItemStack[] { ExoticItems.Yeast_3,

           getItem("DREAMFRUIT"), getItem("LEMON"), null, null, null, null, null, null }, 10, 30.0F, 40)).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§6金果酒", new String[] { "§8高级酒", "§7金苹果酿制的酒", "§7拥有梦幻般的味道", "", "§7▷▷ §b酒精度: §e40", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e16"}), "GLODAPPLE_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_3, new ItemStack[] { ExoticItems.Yeast_3, new ItemStack(Material.GOLDEN_APPLE),

           getItem("NETHER_WINE"), null, null, null, null, null, null }, 16, 30.0F, 40, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 600, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1400, 1)

         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFiMzVmNzA3N2VjZjk4ZWYzZWJhMGYzNWQ5M2E5ODEzMDMwZjliOGI4ZTQyNmFlYjY4ZGFiMzhmMTExNiJ9fX0="), "§6英雄酒", new String[] { "§8高级酒", "§7远古时期的祭祀用酒", "§7通常用于纪念名垂青史的英雄", "", "§7英雄已然逝去", "§7历史仍将继续", "", "§7▷▷ §b酒精度: §e60", "§7▷▷ §d精神值: §e30", "§7▷▷ §a饱食度: §e10" }), "HERO_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_3, new ItemStack[] { ExoticItems.Yeast_3,

           getItem("WHITE_WINE"), getItem("YELLOW_WINE"), null, null, null, null, null, null }, 10, 30.0F, 60, new PotionEffect[] { new PotionEffect(PotionEffectType.ABSORPTION, 600, 1), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1400, 1)

         })).register(ExoticGarden.instance);
        
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVkOGE5ODVkYTdiMzRiZjkyODdiYWQyMWY2YmZlY2FiMWQ5MGZiOGEyZjlmMTMwNWJmMzI4ZWE4ZGNmIn19fQ=="), "§d琼浆玉液", new String[] { "§8特级酒", "§7此物只应天上有", "§7人间能得几回闻", "", "§7▷▷ §b酒精度: §e70", "§7▷▷ §d精神值: §e60", "§7▷▷ §a饱食度: §e10" }), "SUPER_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_3, new ItemStack[] { ExoticItems.Yeast_4,

           getItem("DREAMFRUIT_WINE"), getItem("YELLOW_WINE"), null, null, null, null, null, null }, 10, 60.0F, 70, new PotionEffect[] { new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 1), new PotionEffect(PotionEffectType.HEAL, 20, 1), new PotionEffect(PotionEffectType.SPEED, 1400, 2)

         })).register(ExoticGarden.instance);
        
             (new CustomWine(drinksItemGroup, (ItemStack)new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc1NTg0ZTZmZDU0Y2EwMWRmNGVmZmQ1Zjc0NmIyZDgzYTU4OWRlNjc3NzU1NzU2YmI1OGQ5ZWEyODQ1MTYifX19"), "§d醉生梦死", new String[] { "§8特级酒", "§7醉入癫狂无谓死", "§7梦醒味逝不知生", "", "§7▷▷ §b酒精度: §e70", "§7▷▷ §d精神值: §e60", "§7▷▷ §a饱食度: §e10" }), "DREAMER_WINE", ExoticGardenRecipeTypes.ElectricityBrewing_3, new ItemStack[] { ExoticItems.Yeast_4,

           getItem("DREAMFRUIT_WINE"), getItem("WHITE_WINE"), null, null, null, null, null, null }, 6, 100.0F, 90, new PotionEffect[] { new PotionEffect(PotionEffectType.CONFUSION, 600, 1)
         })).register(ExoticGarden.instance);
    }

    private void registerItems() {
        nestedItemGroup = new NestedItemGroup(new NamespacedKey(this, "parent_category"), new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("847d73a91b52393f2c27e453fb89ab3d784054d414e390d58abd22512edd2b")), "&a异域花园"));
        mainItemGroup = new SubItemGroup(new NamespacedKey(this, "plants_and_fruits"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("a5a5c4a0a16dabc9b1ec72fc83e23ac15d0197de61b138babca7c8a29c820")), "&a异域花园 - 植物与水果"));
        miscItemGroup = new SubItemGroup(new NamespacedKey(this, "misc"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("606be2df2122344bda479feece365ee0e9d5da276afa0e8ce8d848f373dd131")), "&a异域花园 - 配料与工具"));
        foodItemGroup = new SubItemGroup(new NamespacedKey(this, "food"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("a14216d10714082bbe3f412423e6b19232352f4d64f9aca3913cb46318d3ed")), "&a异域花园 - 食物"));
        drinksItemGroup = new SubItemGroup(new NamespacedKey(this, "drinks"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("2a8f1f70e85825607d28edce1a2ad4506e732b4a5345a5ea6e807c4b313e88")), "&a异域花园 - 饮料"));
        magicalItemGroup = new SubItemGroup(new NamespacedKey(this, "magical_crops"), nestedItemGroup, new CustomItemStack(Material.BLAZE_POWDER, "&5异域花园 - 魔法植物"));
        techItemGroup = new SubItemGroup(new NamespacedKey(this, "tech"), nestedItemGroup, new CustomItemStack(getSkull(Material.POTION, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1NmY3ZmY1MmU3YmZkODE4N2I4M2RkMzRkZjM0NTAyOTUyYjhkYjlmYWZiNzI4OGViZWJiNmU3OGVmMTVmIn19fQ=="), "&a异域森林 &8- &b科技"));

        kitchen = new Kitchen(this, miscItemGroup);
        kitchen.register(this);
        Research kitchenResearch = new Research(new NamespacedKey(this, "kitchen"), 600, "厨房", 30);
        kitchenResearch.addItems(kitchen);
        kitchenResearch.register(ExoticGarden.instance);

        // @formatter:off
        SlimefunItemStack iceCube = new SlimefunItemStack("ICE_CUBE", "9340bef2c2c33d113bac4e6a1a84d5ffcecbbfab6b32fa7a7f76195442bd1a2", "&b冰块");
        new SlimefunItem(miscItemGroup, iceCube, RecipeType.GRIND_STONE, new ItemStack[] {new ItemStack(Material.ICE), null, null, null, null, null, null, null, null}, new SlimefunItemStack(iceCube, 4))
        .register(this);

        registerBerry("Grape", "葡萄", ChatColor.RED, Color.RED, PlantType.BUSH, "6ee97649bd999955413fcbf0b269c91be4342b10d0755bad7a17e95fcefdab0");
        registerBerry("Blueberry", "蓝莓", ChatColor.BLUE, Color.BLUE, PlantType.BUSH, "a5a5c4a0a16dabc9b1ec72fc83e23ac15d0197de61b138babca7c8a29c820");
        registerBerry("Elderberry", "接骨木果", ChatColor.RED, Color.FUCHSIA, PlantType.BUSH, "1e4883a1e22c324e753151e2ac424c74f1cc646eec8ea0db3420f1dd1d8b");
        registerBerry("Raspberry", "树莓", ChatColor.LIGHT_PURPLE, Color.FUCHSIA, PlantType.BUSH, "8262c445bc2dd1c5bbc8b93f2482f9fdbef48a7245e1bdb361d4a568190d9b5");
        registerBerry("Blackberry", "黑莓", ChatColor.DARK_GRAY, Color.GRAY, PlantType.BUSH, "2769f8b78c42e272a669d6e6d19ba8651b710ab76f6b46d909d6a3d482754");
        registerBerry("Cranberry", "蔓越莓", ChatColor.RED, Color.FUCHSIA, PlantType.BUSH, "d5fe6c718fba719ff622237ed9ea6827d093effab814be2192e9643e3e3d7");
        registerBerry("Cowberry", "越橘", ChatColor.RED, Color.FUCHSIA, PlantType.BUSH, "a04e54bf255ab0b1c498ca3a0ceae5c7c45f18623a5a02f78a7912701a3249");
        registerBerry("Strawberry", "草莓", ChatColor.DARK_RED, Color.FUCHSIA, PlantType.FRUIT, "cbc826aaafb8dbf67881e68944414f13985064a3f8f044d8edfb4443e76ba");

        registerPlant("Tomato", "番茄", ChatColor.DARK_RED, PlantType.FRUIT, "99172226d276070dc21b75ba25cc2aa5649da5cac745ba977695b59aebd");
        registerPlant("Lettuce", "生菜", ChatColor.DARK_GREEN, PlantType.FRUIT, "477dd842c975d8fb03b1add66db8377a18ba987052161f22591e6a4ede7f5");
        registerPlant("Tea Leaf", "茶叶", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "1514c8b461247ab17fe3606e6e2f4d363dccae9ed5bedd012b498d7ae8eb3");
        registerPlant("Cabbage", "卷心菜", ChatColor.DARK_GREEN, PlantType.FRUIT, "fcd6d67320c9131be85a164cd7c5fcf288f28c2816547db30a3187416bdc45b");
        registerPlant("Sweet Potato", "地瓜", ChatColor.GOLD, PlantType.FRUIT, "3ff48578b6684e179944ab1bc75fec75f8fd592dfb456f6def76577101a66");
        registerPlant("Mustard Seed", "芥菜籽", ChatColor.YELLOW, PlantType.FRUIT, "ed53a42495fa27fb925699bc3e5f2953cc2dc31d027d14fcf7b8c24b467121f");
        registerPlant("Curry Leaf", "咖喱叶", ChatColor.DARK_GREEN, PlantType.DOUBLE_PLANT, "32af7fa8bdf3252f69863b204559d23bfc2b93d41437103437ab1935f323a31f");
        registerPlant("Onion", "洋葱", ChatColor.RED, PlantType.FRUIT, "6ce036e327cb9d4d8fef36897a89624b5d9b18f705384ce0d7ed1e1fc7f56");
        registerPlant("Garlic", "大蒜", ChatColor.RESET, PlantType.FRUIT, "3052d9c11848ebcc9f8340332577bf1d22b643c34c6aa91fe4c16d5a73f6d8");
        registerPlant("Cilantro", "香菜", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "16149196f3a8d6d6f24e51b27e4cb71c6bab663449daffb7aa211bbe577242");
        registerPlant("Black Pepper", "黑胡椒", ChatColor.DARK_GRAY, PlantType.DOUBLE_PLANT, "2342b9bf9f1f6295842b0efb591697b14451f803a165ae58d0dcebd98eacc");

        registerPlant("Green Durian", "青榴莲", ChatColor.GREEN, PlantType.FRUIT, "aaa139ecc894c4e455825e313b542e2068601f2f31ab26d30cf276d51345bf3b");
        registerPlant("Durian", "榴莲", ChatColor.GOLD, PlantType.FRUIT, "44ba890fa8d8684c5119cf1b4b9d5460f5eff392e26ce68b3434e52d18fc666");

        registerPlant("Honeydew Melon", "哈密瓜", ChatColor.GREEN, PlantType.FRUIT, "fb14cba0f42a2d138ed243b3bff99cb1ea8cbdcd94fb5fb1e3a307f8e21ab1c");
        registerPlant("Demon Melon", "恶魔瓜", ChatColor.DARK_GRAY, PlantType.FRUIT, "24c66af64948fd84493dacd1a9dc40736a30931707d838948949bd8e9488d575");

        registerPlant("Papaya", "木瓜", ChatColor.YELLOW, PlantType.FRUIT, "631233362962e34f70de66c26ee6fcd2bbd5bc345c744f2dc42a73d779e0647e");


        registerPlant("Leek", "葱", ChatColor.GREEN, PlantType.FRUIT, "c2dd5433db4fddebc4a77166735699400cb18d43672ab31326a83f0b7c2586cc");

        registerPlant("Ginger", "生姜", ChatColor.YELLOW, PlantType.FRUIT, "693c3512fc5885fccbb25d2daf7fdcfae82641ed7e5e3597cddf73e41159f24");
        registerPlant("Paddy", "水稻", ChatColor.GOLD, PlantType.FRUIT, "3b3c84e4bdaf5cc5f85632ac928d059fc2f1ff0cc9e5998f1fe8b227881ada85");
        registerPlant("Ginseng Baby", "人参果娃", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "36aae6717f49917e043080241264b43a8f387b2df3f61f8f70c2836cd7c3d95c");

        registerPlant("Aubergine", "茄子", ChatColor.BLUE, PlantType.DOUBLE_PLANT, "8825536a44f1861633484753835e5873ed5667ec5b60ef41757a16a768aa76");

        registerPlant("Radish", "红萝卜", ChatColor.RED, PlantType.FRUIT, "c60339f116115c5d8466f9ce17607410fdafc288ed313850712c78b66b93c0ce");

        registerPlant("White Radish", "白萝卜", ChatColor.RESET, PlantType.FRUIT, "374f5302e94be7a27c8ba654d97a658716ca7dbefc8e11484ff683a4164f2d");

        registerPlant("Kohlrabi", "大头菜", ChatColor.GREEN, PlantType.FRUIT, "2969d3149333e1e658f5da69dc6131a87fa6817cda2ba6387d5f5f31e0ef73");

        registerPlant("Red Cabbage", "紫甘蓝", ChatColor.RED, PlantType.FRUIT, "95c27e9e07446825fa7ecbac1925109e2c16253564a4628202d894492d2c36f8");

        registerPlant("Tree Mushroom", "树菇", ChatColor.RESET, PlantType.FRUIT, "80f886503d25fadcbea9ee7779890257de0e3e94a4caf7a67c688631cf2b669");

        registerPlant("Olive", "橄榄", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "92bc8fd736d64a83bda5c161625b49de5c13494fb2f1b2c8ebbfca199651ff");

        registerPlant("Passionfruit", "百香果", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "61609954bdf7d4715e15af2d28c718e91f25ca39fcb8343951bf14706e9966");

        registerPlant("Tumbleweed", "风滚草", ChatColor.YELLOW, PlantType.FRUIT, "c2ef3ad5f653a72d936f0c255ced1b0d03688d8c489fcf044eb55d16bc11c8b8");
        registerPlant("Japanese Pumpkin", "日本南瓜", ChatColor.GREEN, PlantType.FRUIT, "5a625495ea6891673014fb65b63e4d817d5bf80d1fae8d5811b1b1179f1f0e4b");
        registerPlant("Blue Pumpkin", "蓝南瓜", ChatColor.BLUE, PlantType.FRUIT, "dd3384c4d34a8f986e26802ba3587a2aab1f4d2346dd8eb318ce8b7bd194cad2");
        registerPlant("Persimmon", "柿子", ChatColor.RED, PlantType.DOUBLE_PLANT, "2562a9e019b07f3b60b24f46eb29349d1d6d2695b6dc619ed6cfcaeaf21c0f2b");
        registerPlant("Rainbow Fruits", "彩虹果", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "6221fac3c17d189d9c5eced6ff23caa0f73e35b7452d918acb8b7900d14b8950");
        registerPlant("Fig", "无花果", ChatColor.DARK_GRAY, PlantType.DOUBLE_PLANT, "90b0537c0c0e8928bb7c85a425ece777494d508e55de59f8e8f462eecbc07835");

        registerPlant("Wine Fruit", "酒香果", ChatColor.DARK_GREEN, PlantType.DOUBLE_PLANT, "c4c05dd5d7a92889d8d22d4df0f1a1fe2bee3eddf192f78fc44e02e14dbf629");

        registerPlant("Yummy Fruit", "仙馐果", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "8cdcf38a8438ed3a547f8d5b47e0801559c595f0e26c45656a76b5bf8a56f");
        registerPlant("Peanut", "花生", ChatColor.GOLD, PlantType.FRUIT, "608043c5788050ce7ee54edddd48239bce491a9949d1410ad79e165436153ea4");


        registerPlant("Hazelnut", "榛子", ChatColor.GOLD, PlantType.FRUIT, "89e521885f3a20f6769b484f069a41d1105b285829cc78f7b6df79c5916e0b10");

        registerPlant("Walnut", "核桃", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "9b878a91ee4278d16ef15175ed8e2861541de797475cf4a4732915876c6e9a");

        registerPlant("Almond", "杏仁", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "89ce6a02c3d45fb6d5a8648ee430ac4e39e3e2a7503749f2369437d4deeb93bf");

        registerPlant("Pistachio", "开心果", ChatColor.GOLD, PlantType.FRUIT, "52a90a34d8740818b0bab2a687ebd2bfd956e08949d930d6ace666f470b3d9c8");

        registerPlant("Gooseberry", "红醋栗", ChatColor.RED, PlantType.FRUIT, "7e57cc56fb21d50af4890a59a18cf919bea1c2b13171e104d32ae67eda49aa16");
        registerPlant("Cauliflower", "花椰菜", ChatColor.RESET, PlantType.FRUIT, "14a6dedd99bb9af3f1b2f338d509a926606cddfdc351e018aad1c07015ad566d");

        registerPlant("Cotton", "棉花", ChatColor.RESET, PlantType.FRUIT, "d1392c68be8dc9eb62b3161b8062c294c4cb7f662330fbec2d31488bff605d90");

        registerPlant("Tequila", "龙舌兰", ChatColor.WHITE, PlantType.FRUIT, "3525db972cefca7d71976c1287fc7da3e1951323563dc342a6c4e0f702e8ffb");
        registerPlant("Peashooter", "豌豆射手", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "dbcbcf932296090ac687db4074ca9e4c9980ce5ed21e96564035a7f52dcc678b");

        registerPlant("Sunflower", "向日葵", ChatColor.YELLOW, PlantType.DOUBLE_PLANT, "49392a2bfa1c4a795bad101797cd54077910c55c1fa8ae55b679e95d2c6e860f");
        registerPlant("Chomper", "大嘴花", ChatColor.BLUE, PlantType.DOUBLE_PLANT, "798e90575e7d9a0f49587ffd784e2861357e2be83b7c591da3d1bc2d9c482d32");

        registerPlant("Corn", "玉米", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "9bd3802e5fac03afab742b0f3cca41bcd4723bee911d23be29cffd5b965f1");
        registerPlant("Red Corn", "红玉米", ChatColor.RED, PlantType.DOUBLE_PLANT, "b920b5226b625bc0649c447dda0e268f1c486bd536c220e22992a328c5c27ac6");
        registerPlant("Blue Corn", "蓝玉米", ChatColor.BLUE, PlantType.DOUBLE_PLANT, "fd541581b0d24b1b5ab1dad4f51e383d03b9b0bcb4cf86f1345145468efd1c5a");

        registerPlant("Pineapple", "菠萝", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "d7eddd82e575dfd5b7579d89dcd2350c991f0483a7647cffd3d2c587f21");

        registerPlant("Red Bell Pepper", "红甜椒", ChatColor.RED, PlantType.DOUBLE_PLANT, "65f7810414a2cee2bc1de12ecef7a4c89fc9b38e9d0414a90991241a5863705f");
        registerPlant("Jalapeno Chili", "墨西哥辣椒", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "5c8e453e84f663f2f6f4af8ed58e65a47aa8c5bffc2a4f67fad318a523b7a75c");
        registerPlant("Chipotle Chili", "熏辣椒", ChatColor.RED, PlantType.DOUBLE_PLANT, "a1406d5e25189fc57e10ee5e97ecb24143b47c1190047f21b63169f2fe6dad7a");
        registerPlant("Habanero Chili", "哈瓦那辣椒", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "1243cc88ef2ff200a512dc898f0c10349eb509ebe360d60f90e5c8630f8ede74");
        registerPlant("Carolina Reaper Chili", "卡罗莱纳死神辣椒", ChatColor.DARK_RED, PlantType.DOUBLE_PLANT, "1bc39557facf985c4f6592d055155102b464f2a4651dbbbeb835b90ed57a98f3");

        registerPlant("Lychee", "荔枝", ChatColor.RED, PlantType.DOUBLE_PLANT, "7b18a885844c9f1dfe8d2db18b3992e3022b68acc9d19f5fe9747208c202df7");
        registerPlant("Banana", "香蕉", ChatColor.YELLOW, PlantType.DOUBLE_PLANT, "20aaa1425d2b99383697d57193f27d872442bcb995508f42d19de4af1f8612");
        registerPlant("Kiwi", "猕猴桃", ChatColor.GREEN, PlantType.DOUBLE_PLANT, "4cc18ec4649f07d5a38a583d9271fd83a6f37318758e46ea87fc2b2d1afc2d9");
        registerPlant("Avocado", "鳄梨", ChatColor.DARK_GRAY, PlantType.DOUBLE_PLANT, "5bd752b141daea14b6b7f8793364538d85517136433893274069b1a90889f1cb");
        registerTree("Oak Apple", "橡树苹果",  "cbb311f3ba1c07c3d1147cd210d81fe11fd8ae9e3db212a0fa748946c3633", "&c", Color.FUCHSIA, "Oak Apple Juice", "橡树苹果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Coconut", "椰子", "6d27ded57b94cf715b048ef517ab3f85bef5a7be69f14b1573e14e7e42e2e8", "&6", Color.MAROON, "Coconut Milk", "椰奶", false, Material.SAND);
        registerTree("Cherry", "樱桃", "c520766b87d2463c34173ffcd578b0e67d163d37a2d7c2e77915cd91144d40d1", "&c", Color.FUCHSIA, "Cherry Juice", "樱桃汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Pomegranate", "石榴", "cbb311f3ba1c07c3d1147cd210d81fe11fd8ae9e3db212a0fa748946c3633", "&4", Color.RED, "Pomegranate Juice", "石榴汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Lemon", "柠檬", "957fd56ca15978779324df519354b6639a8d9bc1192c7c3de925a329baef6c", "&e", Color.YELLOW, "Lemon Juice", "柠檬汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Plum", "梅子", "69d664319ff381b4ee69a697715b7642b32d54d726c87f6440bf017a4bcd7", "&5", Color.RED, "Plum Juice", "酸梅汤", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Lime", "酸橙", "5a5153479d9f146a5ee3c9e218f5e7e84c4fa375e4f86d31772ba71f6468", "&a", Color.LIME, "Lime Juice", "酸橙汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Orange", "橙子", "65b1db547d1b7956d4511accb1533e21756d7cbc38eb64355a2626412212", "&6", Color.ORANGE, "Orange Juice", "橙汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Peach", "桃子", "d3ba41fe82757871e8cbec9ded9acbfd19930d93341cf8139d1dfbfaa3ec2a5", "&5", Color.RED, "Peach Juice", "桃汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Pear", "梨子", "2de28df844961a8eca8efb79ebb4ae10b834c64a66815e8b645aeff75889664b", "&a", Color.LIME, "Pear Juice", "梨汁", true, Material.DIRT, Material.GRASS_BLOCK);
        registerTree("Dragon Fruit", "火龙果", "847d73a91b52393f2c27e453fb89ab3d784054d414e390d58abd22512edd2b", "&d", Color.FUCHSIA, "Dragon Fruit Juice", "火龙果汁", true, Material.DIRT, Material.GRASS_BLOCK);

        registerDishes();
        FoodRegistry.register(this, miscItemGroup, drinksItemGroup, foodItemGroup);

        registerMagicalPlant("Dirt", "泥土", new ItemStack(Material.DIRT, 16), "1ab43b8c3d34f125e5a3f8b92cd43dfd14c62402c33298461d4d4d7ce2d3aea",
        new ItemStack[] {null, new ItemStack(Material.DIRT), null, new ItemStack(Material.DIRT), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.DIRT), null, new ItemStack(Material.DIRT), null});

        registerMagicalPlant("Coal", "煤炭", new ItemStack(Material.COAL, 8), "7788f5ddaf52c5842287b9427a74dac8f0919eb2fdb1b51365ab25eb392c47",
        new ItemStack[] {null, new ItemStack(Material.COAL_ORE), null, new ItemStack(Material.COAL_ORE), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.COAL_ORE), null, new ItemStack(Material.COAL_ORE), null});

        registerMagicalPlant("Iron", "铁锭", new ItemStack(Material.IRON_INGOT), "db97bdf92b61926e39f5cddf12f8f7132929dee541771e0b592c8b82c9ad52d",
        new ItemStack[] {null, new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), getItem("COAL_PLANT"), new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), null});

        registerMagicalPlant("IronDust", "铁粉", new CustomItemStack(SlimefunItems.IRON_DUST, 8), "8385aaedd784faef8e8f6f782fa48d07c2fc2bbcf6fea1fbc9b9862d05d228c1",
                new ItemStack[] {null, new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), getItem("IRON_PLANT"), new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), null});

        registerMagicalPlant("Gold", "金", SlimefunItems.GOLD_4K, "e4df892293a9236f73f48f9efe979fe07dbd91f7b5d239e4acfd394f6eca",
        new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.GOLD_16K, getItem("IRON_PLANT"), SlimefunItems.GOLD_16K, null, SlimefunItems.GOLD_16K, null});

        registerMagicalPlant("Copper", "铜", new CustomItemStack(SlimefunItems.COPPER_DUST, 8), "d4fc72f3d5ee66279a45ac9c63ac98969306227c3f4862e9c7c2a4583c097b8a",
        new ItemStack[] {null, SlimefunItems.COPPER_DUST, null, SlimefunItems.COPPER_DUST, getItem("COAL_PLANT"), SlimefunItems.COPPER_DUST, null, SlimefunItems.COPPER_DUST, null});

        registerMagicalPlant("Magnesium", "镁", new CustomItemStack(SlimefunItems.MAGNESIUM_DUST, 4), "e8c99d857a5b34331699ce6b5449d8d75f6c50b294ea1a29108f66ca086528bb",
                new ItemStack[] {null, SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, getItem("IRON_PLANT"), SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, null});

        registerMagicalPlant("Aluminum", "铝", new CustomItemStack(SlimefunItems.ALUMINUM_DUST, 4), "f4455341eaff3cf8fe6e46bdfed8f501b461fb6f6d2fe536be7d2bd90d2088aa",
        new ItemStack[] {null, SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, getItem("IRON_PLANT"), SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, null});

        registerMagicalPlant("Tin", "锡", new CustomItemStack(SlimefunItems.TIN_DUST, 4), "6efb43ba2fe6959180ee7307f3f054715a34c0a07079ab73712547ffd753dedd",
        new ItemStack[] {null, SlimefunItems.TIN_DUST, null, SlimefunItems.TIN_DUST, getItem("IRON_PLANT"), SlimefunItems.TIN_DUST, null, SlimefunItems.TIN_DUST, null});

        registerMagicalPlant("Silver", "银", new CustomItemStack(SlimefunItems.SILVER_DUST, 8), "1dd968b1851aa7160d1cd9db7516a8e1bf7b7405e5245c5338aa895fe585f26c",
        new ItemStack[] {null, SlimefunItems.SILVER_DUST, null, SlimefunItems.SILVER_DUST, getItem("IRON_PLANT"), SlimefunItems.SILVER_DUST, null, SlimefunItems.SILVER_DUST, null});

        registerMagicalPlant("Lead", "铅", new CustomItemStack(SlimefunItems.LEAD_DUST, 4), "93c3c418039c4b28b0da75a6d9b22712c7015432d4f4226d6cc0a77d54b64178",
        new ItemStack[] {null, SlimefunItems.LEAD_DUST, null, SlimefunItems.LEAD_DUST, getItem("IRON_PLANT"), SlimefunItems.LEAD_DUST, null, SlimefunItems.LEAD_DUST, null});

        registerMagicalPlant("Zinc", "锌", new CustomItemStack(SlimefunItems.ZINC_DUST, 4), "26ec74b9c9ed876ec9ae466a79c4c10f0a0fe7cd8dd49492cc103f2eaa7aa932",
                new ItemStack[] {null, SlimefunItems.ZINC_DUST, null, SlimefunItems.ZINC_DUST, getItem("IRON_PLANT"), SlimefunItems.ZINC_DUST, null, SlimefunItems.ZINC_DUST, null});

        registerMagicalPlant("Redstone", "红石", new ItemStack(Material.REDSTONE, 8), "e8deee5866ab199eda1bdd7707bdb9edd693444f1e3bd336bd2c767151cf2",
        new ItemStack[] {null, new ItemStack(Material.REDSTONE_BLOCK), null, new ItemStack(Material.REDSTONE_BLOCK), getItem("GOLD_PLANT"), new ItemStack(Material.REDSTONE_BLOCK), null, new ItemStack(Material.REDSTONE_BLOCK), null});

        registerMagicalPlant("Lapis", "青金石", new ItemStack(Material.LAPIS_LAZULI, 16), "2aa0d0fea1afaee334cab4d29d869652f5563c635253c0cbed797ed3cf57de0",
        new ItemStack[] {null, new ItemStack(Material.LAPIS_ORE), null, new ItemStack(Material.LAPIS_ORE), getItem("REDSTONE_PLANT"), new ItemStack(Material.LAPIS_ORE), null, new ItemStack(Material.LAPIS_ORE), null});

        registerMagicalPlant("Ender", "末影珍珠", new ItemStack(Material.ENDER_PEARL, 2), "4e35aade81292e6ff4cd33dc0ea6a1326d04597c0e529def4182b1d1548cfe1",
        new ItemStack[] {null, new ItemStack(Material.ENDER_PEARL), null, new ItemStack(Material.ENDER_PEARL), getItem("LAPIS_PLANT"), new ItemStack(Material.ENDER_PEARL), null, new ItemStack(Material.ENDER_PEARL), null});

        registerMagicalPlant("Quartz", "石英", new ItemStack(Material.QUARTZ, 4), "26de58d583c103c1cd34824380c8a477e898fde2eb9a74e71f1a985053b96",
        new ItemStack[] {null, new ItemStack(Material.NETHER_QUARTZ_ORE), null, new ItemStack(Material.NETHER_QUARTZ_ORE), getItem("ENDER_PLANT"), new ItemStack(Material.NETHER_QUARTZ_ORE), null, new ItemStack(Material.NETHER_QUARTZ_ORE), null});

        registerMagicalPlant("Diamond", "钻石", new ItemStack(Material.DIAMOND), "f88cd6dd50359c7d5898c7c7e3e260bfcd3dcb1493a89b9e88e9cbecbfe45949",
        new ItemStack[] {null, new ItemStack(Material.DIAMOND), null, new ItemStack(Material.DIAMOND), getItem("QUARTZ_PLANT"), new ItemStack(Material.DIAMOND), null, new ItemStack(Material.DIAMOND), null});

        registerMagicalPlant("Emerald", "绿宝石", new ItemStack(Material.EMERALD), "4fc495d1e6eb54a386068c6cb121c5875e031b7f61d7236d5f24b77db7da7f",
        new ItemStack[] {null, new ItemStack(Material.EMERALD), null, new ItemStack(Material.EMERALD), getItem("DIAMOND_PLANT"), new ItemStack(Material.EMERALD), null, new ItemStack(Material.EMERALD), null});

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            registerMagicalPlant("Netherite", "下界合金", new ItemStack(Material.NETHERITE_SCRAP), "27957f895d7bc53423a35aac59d584b41cc30e040269c955e451fe680a1cc049",
            new ItemStack[] {null, new ItemStack(Material.NETHER_STAR), null, new ItemStack(Material.NETHERITE_BLOCK), getItem("EMERALD_PLANT"), new ItemStack(Material.NETHERITE_BLOCK), null, new ItemStack(Material.NETHERITE_BLOCK), null});
        }

        registerMagicalPlant("Blaze", "烈焰棒", new ItemStack(Material.BLAZE_ROD, 2), "7717933c40fbf936aa9288513efe19bda4601efc0e4ecad2e023b0c1d28444b",
                new ItemStack[] { null, new ItemStack(Material.BLAZE_ROD), null, new ItemStack(Material.BLAZE_ROD), getItem("GOLD_PLANT"), new ItemStack(Material.BLAZE_ROD), null, new ItemStack(Material.BLAZE_ROD), null });

        registerMagicalPlant("Glowstone", "萤石", new ItemStack(Material.GLOWSTONE_DUST, 8), "65d7bed8df714cea063e457ba5e87931141de293dd1d9b9146b0f5ab383866",
        new ItemStack[] { null, new ItemStack(Material.GLOWSTONE), null, new ItemStack(Material.GLOWSTONE), getItem("REDSTONE_PLANT"), new ItemStack(Material.GLOWSTONE), null, new ItemStack(Material.GLOWSTONE), null });

        registerMagicalPlant("Sulfate", "硫酸盐", new CustomItemStack(SlimefunItems.SULFATE, 2), "20d9cb52a09f8f4a75b9bffe7ac20c0c85ac1ef57cf93fc2040436d660ba98ba",
                new ItemStack[] { null, SlimefunItems.SULFATE, null, SlimefunItems.SULFATE, getItem("GLOWSTONE_PLANT"), SlimefunItems.SULFATE, null, SlimefunItems.SULFATE, null });

        registerMagicalPlant("Uranium", "铀", new CustomItemStack(SlimefunItems.TINY_URANIUM,1), "90614e3abf64d53496794cd8ae68597fc7266c61794bd1e48d4519868ae3cad0",
                new ItemStack[] { null, SlimefunItems.BOOSTED_URANIUM, null, SlimefunItems.BLISTERING_INGOT_3, getItem("SULFATE_PLANT"), SlimefunItems.BLISTERING_INGOT_3, null, SlimefunItems.BOOSTED_URANIUM, null });

        registerMagicalPlant("Obsidian", "黑曜石", new ItemStack(Material.OBSIDIAN, 1), "7840b87d52271d2a755dedc82877e0ed3df67dcc42ea479ec146176b02779a5",
        new ItemStack[] {null, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), getItem("LAPIS_PLANT"), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null});

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            registerMagicalPlant("Amethyst", "紫水晶", new ItemStack(Material.AMETHYST_CLUSTER, 1), "3f4876b6a5d6dd785e091fd134a21c91d0a9cac5a622e448b5ffcb65ef45278",
                    new ItemStack[] {null, new ItemStack(Material.AMETHYST_SHARD), null, new ItemStack(Material.AMETHYST_SHARD), getItem("OBSIDIAN_PLANT"), new ItemStack(Material.AMETHYST_SHARD), null, new ItemStack(Material.AMETHYST_SHARD), null});
        }

        registerMagicalPlant("Slime", "粘液球", new ItemStack(Material.SLIME_BALL, 8), "90e65e6e5113a5187dad46dfad3d3bf85e8ef807f82aac228a59c4a95d6f6a",
        new ItemStack[] {null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), getItem("ENDER_PLANT"), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null});

        registerTechPlant("咖啡豆", "&c", Material.COCOA_BEANS, PlantType.DOUBLE_PLANT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA4M2VjMmIwMWRjMGZlZTc5YWEzMjE4OGQ5NDI5YWNjNjhlY2Y3MTQwOGRjYTA0YWFhYjUzYWQ4YmVhMCJ9fX0=");

        registerTechPlant("仙馐果", "&b", Material.APPLE, PlantType.DOUBLE_PLANT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNkY2YzOGE4NDM4ZWQzYTU0N2Y4ZDViNDdlMDgwMTU1OWM1OTVmMGUyNmM0NTY1NmE3NmI1YmY4YTU2ZiJ9fX0=");

        registerTechPlant("酒香果", "&b", Material.OAK_LEAVES, PlantType.DOUBLE_PLANT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRjMDVkZDVkN2E5Mjg4OWQ4ZDIyZDRkZjBmMWExZmUyYmVlM2VkZGYxOTJmNzhmYzQ0ZTAyZTE0ZGJmNjI5In19fQ==");

        new Crook(miscItemGroup, new SlimefunItemStack("CROOK", new CustomItemStack(Material.WOODEN_HOE, "&r钩子", "", "&7+ &b25% &7树苗掉落概率")), RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {new ItemStack(Material.STICK), new ItemStack(Material.STICK), null, null, new ItemStack(Material.STICK), null, null, new ItemStack(Material.STICK), null})
        .register(this);

        SlimefunItemStack grassSeeds = new SlimefunItemStack("GRASS_SEEDS", Material.PUMPKIN_SEEDS, "&r草种子", "", "&7&o可以种在泥土上", "&7&o让泥土变成草方块");
        new GrassSeeds(mainItemGroup, grassSeeds, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[] {null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null})
        .register(this);
        // @formatter:on

        items.put("WHEAT_SEEDS", new ItemStack(Material.WHEAT_SEEDS));
        items.put("PUMPKIN_SEEDS", new ItemStack(Material.PUMPKIN_SEEDS));
        items.put("MELON_SEEDS", new ItemStack(Material.MELON_SEEDS));

        for (Material sapling : Tag.SAPLINGS.getValues()) {
            items.put(sapling.name(), new ItemStack(sapling));
        }

        items.put("GRASS_SEEDS", grassSeeds);
        ExoticItems.registerItems();

        Iterator<String> iterator = items.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            cfg.setDefaultValue("grass-drops." + key, true);

            if (!cfg.getBoolean("grass-drops." + key)) {
                iterator.remove();
            }
        }

        cfg.save();

        for (Tree tree : ExoticGarden.getTrees()) {
            treeFruits.add(tree.getFruitID());
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void registerTree(String id, String name, String texture, String color, Color pcolor, String juice_id, String juice, boolean pie, Material... soil) {
        id = id.toUpperCase(Locale.ROOT).replace(' ', '_');
        Tree tree = new Tree(id, texture, soil);
        trees.add(tree);

        SlimefunItemStack sapling = new SlimefunItemStack(id + "_SAPLING", Material.OAK_SAPLING, color + name + "树苗");

        items.put(id + "_SAPLING", sapling);

        new BonemealableItem(mainItemGroup, sapling, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[]{null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null}).register(this);

        new ExoticGardenFruit(mainItemGroup, new SlimefunItemStack(id, texture, color + name), ExoticGardenRecipeTypes.HARVEST_TREE, true, new ItemStack[]{null, null, null, null, getItem(id + "_SAPLING"), null, null, null, null}).register(this);

        if (pcolor != null) {
            new Juice(drinksItemGroup, new SlimefunItemStack(juice_id.toUpperCase().replace(" ", "_"), new CustomPotion(color + juice, pcolor, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o点饥饿值")), RecipeType.JUICER, new ItemStack[]{getItem(id), null, null, null, null, null, null, null, null}).register(this);
        }

        if (pie) {
            new CustomFood(foodItemGroup, new SlimefunItemStack(id + "_PIE", "3418c6b0a29fc1fe791c89774d828ff63d2a9fa6c83373ef3aa47bf3eb79", color + name + "派", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"), new ItemStack[]{getItem(id), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), new ItemStack(Material.MILK_BUCKET), SlimefunItems.WHEAT_FLOUR, null, null, null, null}, 13).register(this);
        }

        if (!new File(schematicsFolder, id + "_TREE.schematic").exists()) {
            saveSchematic(id + "_TREE");
        }
    }

    private void saveSchematic(@Nonnull String id) {
        try (InputStream input = getClass().getResourceAsStream("/schematics/" + id + ".schematic")) {
            try (FileOutputStream output = new FileOutputStream(new File(schematicsFolder, id + ".schematic"))) {
                byte[] buffer = new byte[1024];
                int len;

                while ((len = input.read(buffer)) > 0) {
                    output.write(buffer, 0, len);
                }
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, e, () -> "Failed to load file: \"" + id + ".schematic\"");
        }
    }

    public void registerTechPlant(String rawName, String color, Material material, PlantType type, String data) {
        String name = getTranlateName(rawName);
        Berry berry = new Berry(name.toUpperCase().replace(" ", "_"), type, data);
        berries.add(berry);

        (new SlimefunItem(mainItemGroup, new SlimefunItemStack(name.toUpperCase().replace(" ", "_") + "_BUSH", Material.OAK_SAPLING, color + rawName + "苗"), ExoticGardenRecipeTypes.SEED_ANALYZER, new ItemStack[]{null, null, null, null, ExoticItems.MysticSeed, null, null, null, null})).register(instance);

        (new EGPlant(mainItemGroup, new CustomItemStack(getSkull(material, data), color + rawName), name.toUpperCase().replace(" ", "_"), ExoticGardenRecipeTypes.HARVEST_PLANT, true, new ItemStack[]{null, null, null, null,
                getItem(name.toUpperCase().replace(" ", "_") + "_BUSH"), null, null, null, null
        })).register(instance);
    }

    private ItemStack getSkull(Material material, String texture) {
        return getSkull(new MaterialData(material), texture);
    }

    private String getTranlateName(String name) {
        if (this.traslateNames.get(name) != null) {
            return this.traslateNames.get(name);
        }
        return name;
    }

    private void initTransNames() {
        this.traslateNames.put("葡萄", "Grape");
        this.traslateNames.put("蓝莓", "Blueberry");
        this.traslateNames.put("接骨木果", "Elderberry");
        this.traslateNames.put("覆盆子", "Raspberry");
        this.traslateNames.put("黑莓", "Blackberry");
        this.traslateNames.put("蔓越莓", "Cranberry");
        this.traslateNames.put("越桔", "Cowberry");
        this.traslateNames.put("草莓", "Strawberry");
        this.traslateNames.put("番茄", "Tomato");
        this.traslateNames.put("生菜", "Lettuce");
        this.traslateNames.put("茶叶", "Tea Leaf");
        this.traslateNames.put("卷心菜", "Cabbage");
        this.traslateNames.put("番薯", "Sweet Potato");
        this.traslateNames.put("芥菜籽", "Mustard Seed");
        this.traslateNames.put("玉米", "Corn");
        this.traslateNames.put("菠萝", "Pineapple");
        this.traslateNames.put("苹果", "Apple Oak");
        this.traslateNames.put("椰子", "Coconut");
        this.traslateNames.put("樱桃", "Cherry");
        this.traslateNames.put("石榴", "Pomegranate");
        this.traslateNames.put("柠檬", "Lemon");
        this.traslateNames.put("李子", "Plum");
        this.traslateNames.put("酸橙", "Lime");
        this.traslateNames.put("橙子", "Orange");
        this.traslateNames.put("桃子", "Peach");
        this.traslateNames.put("香梨", "Pear");

        this.traslateNames.put("煤炭", "Coal");
        this.traslateNames.put("铁", "Iron");
        this.traslateNames.put("黄金", "Gold");
        this.traslateNames.put("红石", "RedStone");
        this.traslateNames.put("青金石", "Lapis");
        this.traslateNames.put("末影", "Ender");
        this.traslateNames.put("石英", "Quartz");
        this.traslateNames.put("钻石", "Diamond");
        this.traslateNames.put("绿宝石", "Emerald");
        this.traslateNames.put("萤石", "Glowstone");
        this.traslateNames.put("黑曜石", "Obsidian");
        this.traslateNames.put("史莱姆", "Slime");
        this.traslateNames.put("潜影壳", "Shulker_Shell");
        this.traslateNames.put("咖啡豆", "Coffeebean");
        this.traslateNames.put("仙馐果", "DreamFruit");
        this.traslateNames.put("酒香果", "WineFruit");
    }

    private void createDefaultConfiguration(File actual, String defaultName) {
        File parent = actual.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (actual.exists()) {
            return;
        }
        InputStream input = null;

        try (JarFile file = new JarFile(getFile())) {
            ZipEntry copy = file.getEntry("resources/" + defaultName);
            if (copy == null) {
                throw new FileNotFoundException();
            }
            input = file.getInputStream(copy);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (input != null) {

            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[32];
                int length = 0;
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {


                try {

                    if (input != null) {
                        input.close();
                    }
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
    }

    private void initDataFromYAML(File storge) {
        this.yamlStorge = YamlConfiguration.loadConfiguration(storge);
        ConfigurationSection section = this.yamlStorge.getConfigurationSection("Players");
        if (section == null) {
            this.yamlStorge.set("Players", null);
            try {
                this.yamlStorge.save("storge.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (String s : this.yamlStorge.getConfigurationSection("Players").getKeys(false)) {
                drunkPlayers.put(s, new PlayerAlcohol(s, this.yamlStorge
                        .getInt("Players.%p.Alcohol".replace("%p", s)), this.yamlStorge.getBoolean("Players.%p.Drunk".replace("%p", s))));
            }
        }
    }

    public void initPlayerData(Player player) {
        String name = player.getName();
        ConfigurationSection section = this.yamlStorge.getConfigurationSection("Players");
        if (section != null && section.contains(name)) {
            drunkPlayers.put(name, new PlayerAlcohol(name, this.yamlStorge
                    .getInt("Players.%p.Alcohol".replace("%p", name)), this.yamlStorge.getBoolean("Players.%p.Drunk".replace("%p", name))));
        } else {
            drunkPlayers.put(name, new PlayerAlcohol(name, 0));
            saveDatas(player);
        }
    }

    private void saveDatas() {
        try {
            for (Map.Entry<String, PlayerAlcohol> o : drunkPlayers.entrySet()) {
                String player = "Players." + o.getKey();
                this.yamlStorge.set(player + ".Alcohol", o.getValue().getAlcohol());
                this.yamlStorge.set(player + ".Drunk", o.getValue().isDrunk());
            }
            this.yamlStorge.save(new File(getDataFolder() + File.separator + "storge.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDatas(Player player) {
        try {
            String playerName = "Players." + player.getName();
            this.yamlStorge.set(playerName + ".Alcohol", drunkPlayers.get(player.getName()).getAlcohol());
            this.yamlStorge.set(playerName + ".Drunk", drunkPlayers.get(player.getName()).isDrunk());
            this.yamlStorge.save(new File(getDataFolder() + File.separator + "storge.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSanityEnabled() {
        return this.sanity;
    }

    public boolean isResidenceEnabled() {
        return this.residence;
    }

    public YamlConfiguration getYamlStorge() {
        return this.yamlStorge;
    }

    private void registerDrunkMessage() {
        drunkMsg.add("§7我还能喝! §8(§c胡言乱语§8)");
        drunkMsg.add("§7我控计不住我计己啊! §8(§c胡言乱语§8)");
        drunkMsg.add("§7我...嗝! §8(§c胡言乱语§8)");
        drunkMsg.add("§7嗝!我...要摸摸我家的苦力怕 §8(§c胡言乱语§8)");
        drunkMsg.add("§7我要打十个...末影龙! §8(§c胡言乱语§8)");
        drunkMsg.add("§7@%player%...你怎么扭来扭去的! §8(§c胡言乱语§8)");
        drunkMsg.add("§7一...一起蛤皮! §8(§c胡言乱语§8)");
        drunkMsg.add("@%player% §7我给你讲个故事...从前...嗝!蛤蛤蛤蛤蛤蛤蛤! §8(§c胡言乱语§8)");
        drunkMsg.add("§7%player%...我超喜欢你的!让我揉揉你的肥脸... §8(§c胡言乱语§8)");
        drunkMsg.add("§7老子...最强! §8(§c胡言乱语§8)");
        drunkMsg.add("§7看到..这个酒瓶没有!看到了是吧!...怕什么我又不打你!蛤蛤蛤蛤蛤嗝 §8(§c胡言乱语§8)");
        drunkMsg.add("§7每天吃肉长不胖, 天天喝酒身体棒! §8(§c胡言乱语§8)");
        drunkMsg.add("§7这个服务器里的玩家超有钱的, 天天氪金, 还送我钱...我超喜欢这里的! §8(§c胡言乱语§8)");
    }

    private void checkDrunkers() {
        for (Map.Entry<String, PlayerAlcohol> o : drunkPlayers.entrySet()) {
            PlayerAlcohol pa = o.getValue();
            Player player = Bukkit.getPlayer(pa.getPlayer());
            if (player != null) {
                if (pa.getAlcohol() > 0) {
                    drunkPlayers.get(pa.getPlayer()).addAlcohol(-1);
                }
                if (pa.isDrunk) {
                    if (pa.getAlcohol() <= 0) {
                        drunkPlayers.get(pa.getPlayer()).setDrunk(false);
                        continue;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1, false));
                    continue;
                }
                if (pa.getAlcohol() >= 100)
                    drunkPlayers.get(pa.getPlayer()).setDrunk(true);
            }
        }
    }

    public void registerBerry(String id, String name, ChatColor color, PlantType type, String texture) {
        registerBerry(id, name, color, Color.fromRGB(color.asBungee().getColor().getRGB()), type, texture);
    }

    public void registerBerry(String id, String name, ChatColor color, Color potionColor, PlantType type, String texture) {
        String upperCase = id.toUpperCase(Locale.ROOT);
        Berry berry = new Berry(upperCase, type, texture);
        berries.add(berry);

        SlimefunItemStack sfi = new SlimefunItemStack(upperCase + "_BUSH", Material.OAK_SAPLING, color + name + "灌木丛");

        items.put(upperCase + "_BUSH", sfi);

        new BonemealableItem(mainItemGroup, sfi, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[]{null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null}).register(this);

        new ExoticGardenFruit(mainItemGroup, new SlimefunItemStack(upperCase, texture, color + name), ExoticGardenRecipeTypes.HARVEST_BUSH, true, new ItemStack[]{null, null, null, null, getItem(upperCase + "_BUSH"), null, null, null, null}).register(this);

        new Juice(drinksItemGroup, new SlimefunItemStack(upperCase + "_JUICE", new CustomPotion(color + name + "汁", potionColor, new PotionEffect(PotionEffectType.SATURATION, 6, 0), "", "&7&o恢复 &b&o" + "3.0" + " &7&o点饥饿值")), RecipeType.JUICER, new ItemStack[]{getItem(upperCase), null, null, null, null, null, null, null, null}).register(this);

        new Juice(drinksItemGroup, new SlimefunItemStack(upperCase + "_SMOOTHIE", new CustomPotion(color + name + "冰沙", potionColor, new PotionEffect(PotionEffectType.SATURATION, 10, 0), "", "&7&o恢复 &b&o" + "5.0" + " &7&o点饥饿值")), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{getItem(upperCase + "_JUICE"), getItem("ICE_CUBE"), null, null, null, null, null, null, null}).register(this);

        new CustomFood(foodItemGroup, new SlimefunItemStack(upperCase + "_JELLY_SANDWICH", "8c8a939093ab1cde6677faf7481f311e5f17f63d58825f0e0c174631fb0439", color + name + "果酱三明治", "", "&7&o恢复 &b&o" + "8.0" + " &7&o点饥饿值"), new ItemStack[]{null, new ItemStack(Material.BREAD), null, null, getItem(upperCase + "_JUICE"), null, null, new ItemStack(Material.BREAD), null}, 16).register(this);

        new CustomFood(foodItemGroup, new SlimefunItemStack(upperCase + "_PIE", "3418c6b0a29fc1fe791c89774d828ff63d2a9fa6c83373ef3aa47bf3eb79", color + name + "派", "", "&7&o恢复 &b&o" + "6.5" + " &7&o点饥饿值"), new ItemStack[]{getItem(upperCase), new ItemStack(Material.EGG), new ItemStack(Material.SUGAR), new ItemStack(Material.MILK_BUCKET), SlimefunItems.WHEAT_FLOUR, null, null, null, null}, 13).register(this);
    }

    public void registerPlant(String id, String name, ChatColor color, PlantType type, String texture) {
        String upperCase = id.toUpperCase(Locale.ROOT);
        String enumStyle = upperCase.replace(' ', '_');

        Berry berry = new Berry(enumStyle, type, texture);
        berries.add(berry);

        SlimefunItemStack bush = new SlimefunItemStack(enumStyle + "_BUSH", Material.OAK_SAPLING, color + name + "植物");
        items.put(upperCase + "_BUSH", bush);

        new BonemealableItem(mainItemGroup, bush, ExoticGardenRecipeTypes.BREAKING_GRASS, new ItemStack[]{null, null, null, null, new ItemStack(Material.GRASS), null, null, null, null})
                .register(this);

        new ExoticGardenFruit(mainItemGroup, new SlimefunItemStack(enumStyle, texture, color + name), ExoticGardenRecipeTypes.HARVEST_BUSH, true, new ItemStack[]{null, null, null, null, getItem(enumStyle + "_BUSH"), null, null, null, null}).register(this);
    }

    private void registerMagicalPlant(String id, String name, ItemStack item, String texture, ItemStack[] recipe) {
        String upperCase = id.toUpperCase(Locale.ROOT);
        String enumStyle = upperCase.replace(' ', '_');

        SlimefunItemStack essence = new SlimefunItemStack(enumStyle + "_ESSENCE", Material.BLAZE_POWDER, "&r魔法精华", "", "&7" + name);

        Berry berry = new Berry(essence, upperCase + "_ESSENCE", PlantType.ORE_PLANT, texture);
        berries.add(berry);

        new BonemealableItem(magicalItemGroup, new SlimefunItemStack(enumStyle + "_PLANT", Material.OAK_SAPLING, "&f" + name + "植物"), RecipeType.ENHANCED_CRAFTING_TABLE, recipe)
                .register(this);

        MagicalEssence magicalEssence = new MagicalEssence(magicalItemGroup, essence);

        magicalEssence.setRecipeOutput(item.clone());
        magicalEssence.register(this);
    }

    public void harvestFruit(Block fruit) {
        Location loc = fruit.getLocation();
        SlimefunItem check = StorageCacheUtils.getSfItem(loc);

        if (check == null) {
            return;
        }

        if (treeFruits.contains(check.getId())) {
            Slimefun.getDatabaseManager().getBlockDataController().removeBlock(loc);
            ItemStack fruits = check.getItem().clone();
            fruit.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.OAK_LEAVES);
            fruit.getWorld().dropItemNaturally(loc, fruits);
            fruit.setType(Material.AIR, false);
        }
    }

    public File getSchematicsFolder() {
        return schematicsFolder;
    }

    public Config getCfg() {
        return cfg;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/SlimefunGuguProject/ExoticGarden/issues";
    }

}
