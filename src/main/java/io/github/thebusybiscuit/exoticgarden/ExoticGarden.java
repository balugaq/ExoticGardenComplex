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
import org.bukkit.command.CommandExecutor;
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

    private static final String ALCOHOL_PATH = "Players.%p.Alcohol";
    private static final String DRUNK_PATH = "Players.%p.Drunk";
    public static ExoticGarden instance;
    public static ConcurrentHashMap<String, PlayerAlcohol> drunkPlayers = new ConcurrentHashMap<>();
    private static boolean skullitems;
    private static List<String> drunkMsg = new ArrayList<>();
    private final File schematicsFolder = new File(getDataFolder(), "schematics");
    private final List<Berry> berries = new ArrayList<>();
    private final List<Tree> trees = new ArrayList<>();
    private final Map<String, ItemStack> items = new HashMap<>();
    private final Set<String> treeFruits = new HashSet<>();
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
    private HashMap<String, String> traslateNames = new HashMap<>();

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
        player.chat(((String) drunkMsg.get(ramdom.nextInt(drunkMsg.size()))).replace("%player%", (
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
        if (getServer().getPluginManager().getPlugin("Residence") != null) {
            FlagPermissions.addFlag("exo-harvest");
            this.residence = true;
        }
        getCommand("exotic").setExecutor((CommandExecutor) new ExoticCommand());

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
        kitchenResearch.register();

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

        registerPlant("Corn", "玉米", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "9bd3802e5fac03afab742b0f3cca41bcd4723bee911d23be29cffd5b965f1");
        registerPlant("Pineapple", "菠萝", ChatColor.GOLD, PlantType.DOUBLE_PLANT, "d7eddd82e575dfd5b7579d89dcd2350c991f0483a7647cffd3d2c587f21");

        registerPlant("Red Bell Pepper", "红甜椒", ChatColor.RED, PlantType.DOUBLE_PLANT, "65f7810414a2cee2bc1de12ecef7a4c89fc9b38e9d0414a90991241a5863705f");

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

        FoodRegistry.register(this, miscItemGroup, drinksItemGroup, foodItemGroup);

        registerMagicalPlant("Dirt", "泥土", new ItemStack(Material.DIRT, 2), "1ab43b8c3d34f125e5a3f8b92cd43dfd14c62402c33298461d4d4d7ce2d3aea",
        new ItemStack[] {null, new ItemStack(Material.DIRT), null, new ItemStack(Material.DIRT), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.DIRT), null, new ItemStack(Material.DIRT), null});

        registerMagicalPlant("Coal", "煤炭", new ItemStack(Material.COAL, 2), "7788f5ddaf52c5842287b9427a74dac8f0919eb2fdb1b51365ab25eb392c47",
        new ItemStack[] {null, new ItemStack(Material.COAL_ORE), null, new ItemStack(Material.COAL_ORE), new ItemStack(Material.WHEAT_SEEDS), new ItemStack(Material.COAL_ORE), null, new ItemStack(Material.COAL_ORE), null});

        registerMagicalPlant("Iron", "铁锭", new ItemStack(Material.IRON_INGOT), "db97bdf92b61926e39f5cddf12f8f7132929dee541771e0b592c8b82c9ad52d",
        new ItemStack[] {null, new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), getItem("COAL_PLANT"), new ItemStack(Material.IRON_BLOCK), null, new ItemStack(Material.IRON_BLOCK), null});

        registerMagicalPlant("Gold", "金", SlimefunItems.GOLD_4K, "e4df892293a9236f73f48f9efe979fe07dbd91f7b5d239e4acfd394f6eca",
        new ItemStack[] {null, SlimefunItems.GOLD_16K, null, SlimefunItems.GOLD_16K, getItem("IRON_PLANT"), SlimefunItems.GOLD_16K, null, SlimefunItems.GOLD_16K, null});

        registerMagicalPlant("Copper", "铜", new CustomItemStack(SlimefunItems.COPPER_DUST, 8), "d4fc72f3d5ee66279a45ac9c63ac98969306227c3f4862e9c7c2a4583c097b8a",
        new ItemStack[] {null, SlimefunItems.COPPER_DUST, null, SlimefunItems.COPPER_DUST, getItem("GOLD_PLANT"), SlimefunItems.COPPER_DUST, null, SlimefunItems.COPPER_DUST, null});

        registerMagicalPlant("Aluminum", "铝", new CustomItemStack(SlimefunItems.ALUMINUM_DUST, 8), "f4455341eaff3cf8fe6e46bdfed8f501b461fb6f6d2fe536be7d2bd90d2088aa",
        new ItemStack[] {null, SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, getItem("IRON_PLANT"), SlimefunItems.ALUMINUM_DUST, null, SlimefunItems.ALUMINUM_DUST, null});

        registerMagicalPlant("Tin", "锡", new CustomItemStack(SlimefunItems.TIN_DUST, 8), "6efb43ba2fe6959180ee7307f3f054715a34c0a07079ab73712547ffd753dedd",
        new ItemStack[] {null, SlimefunItems.TIN_DUST, null, SlimefunItems.TIN_DUST, getItem("IRON_PLANT"), SlimefunItems.TIN_DUST, null, SlimefunItems.TIN_DUST, null});

        registerMagicalPlant("Silver", "银", new CustomItemStack(SlimefunItems.SILVER_DUST, 8), "1dd968b1851aa7160d1cd9db7516a8e1bf7b7405e5245c5338aa895fe585f26c",
        new ItemStack[] {null, SlimefunItems.SILVER_DUST, null, SlimefunItems.SILVER_DUST, getItem("IRON_PLANT"), SlimefunItems.SILVER_DUST, null, SlimefunItems.SILVER_DUST, null});

        registerMagicalPlant("Lead", "铅", new CustomItemStack(SlimefunItems.LEAD_DUST, 8), "93c3c418039c4b28b0da75a6d9b22712c7015432d4f4226d6cc0a77d54b64178",
        new ItemStack[] {null, SlimefunItems.LEAD_DUST, null, SlimefunItems.LEAD_DUST, getItem("IRON_PLANT"), SlimefunItems.LEAD_DUST, null, SlimefunItems.LEAD_DUST, null});

        registerMagicalPlant("Redstone", "红石", new ItemStack(Material.REDSTONE, 8), "e8deee5866ab199eda1bdd7707bdb9edd693444f1e3bd336bd2c767151cf2",
        new ItemStack[] {null, new ItemStack(Material.REDSTONE_BLOCK), null, new ItemStack(Material.REDSTONE_BLOCK), getItem("GOLD_PLANT"), new ItemStack(Material.REDSTONE_BLOCK), null, new ItemStack(Material.REDSTONE_BLOCK), null});

        registerMagicalPlant("Lapis", "青金石", new ItemStack(Material.LAPIS_LAZULI, 16), "2aa0d0fea1afaee334cab4d29d869652f5563c635253c0cbed797ed3cf57de0",
        new ItemStack[] {null, new ItemStack(Material.LAPIS_ORE), null, new ItemStack(Material.LAPIS_ORE), getItem("REDSTONE_PLANT"), new ItemStack(Material.LAPIS_ORE), null, new ItemStack(Material.LAPIS_ORE), null});

        registerMagicalPlant("Ender", "末影珍珠", new ItemStack(Material.ENDER_PEARL, 4), "4e35aade81292e6ff4cd33dc0ea6a1326d04597c0e529def4182b1d1548cfe1",
        new ItemStack[] {null, new ItemStack(Material.ENDER_PEARL), null, new ItemStack(Material.ENDER_PEARL), getItem("LAPIS_PLANT"), new ItemStack(Material.ENDER_PEARL), null, new ItemStack(Material.ENDER_PEARL), null});

        registerMagicalPlant("Quartz", "石英", new ItemStack(Material.QUARTZ, 8), "26de58d583c103c1cd34824380c8a477e898fde2eb9a74e71f1a985053b96",
        new ItemStack[] {null, new ItemStack(Material.NETHER_QUARTZ_ORE), null, new ItemStack(Material.NETHER_QUARTZ_ORE), getItem("ENDER_PLANT"), new ItemStack(Material.NETHER_QUARTZ_ORE), null, new ItemStack(Material.NETHER_QUARTZ_ORE), null});

        registerMagicalPlant("Diamond", "钻石", new ItemStack(Material.DIAMOND), "f88cd6dd50359c7d5898c7c7e3e260bfcd3dcb1493a89b9e88e9cbecbfe45949",
        new ItemStack[] {null, new ItemStack(Material.DIAMOND), null, new ItemStack(Material.DIAMOND), getItem("QUARTZ_PLANT"), new ItemStack(Material.DIAMOND), null, new ItemStack(Material.DIAMOND), null});

        registerMagicalPlant("Emerald", "绿宝石", new ItemStack(Material.EMERALD), "4fc495d1e6eb54a386068c6cb121c5875e031b7f61d7236d5f24b77db7da7f",
        new ItemStack[] {null, new ItemStack(Material.EMERALD), null, new ItemStack(Material.EMERALD), getItem("DIAMOND_PLANT"), new ItemStack(Material.EMERALD), null, new ItemStack(Material.EMERALD), null});

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            registerMagicalPlant("Netherite", "下界合金", new ItemStack(Material.NETHERITE_INGOT), "27957f895d7bc53423a35aac59d584b41cc30e040269c955e451fe680a1cc049",
            new ItemStack[] {null, new ItemStack(Material.NETHERITE_BLOCK), null, new ItemStack(Material.NETHERITE_BLOCK), getItem("EMERALD_PLANT"), new ItemStack(Material.NETHERITE_BLOCK), null, new ItemStack(Material.NETHERITE_BLOCK), null});
        }

        registerMagicalPlant("Glowstone", "萤石", new ItemStack(Material.GLOWSTONE_DUST, 8), "65d7bed8df714cea063e457ba5e87931141de293dd1d9b9146b0f5ab383866",
        new ItemStack[] { null, new ItemStack(Material.GLOWSTONE), null, new ItemStack(Material.GLOWSTONE), getItem("REDSTONE_PLANT"), new ItemStack(Material.GLOWSTONE), null, new ItemStack(Material.GLOWSTONE), null });

        registerMagicalPlant("Obsidian", "黑曜石", new ItemStack(Material.OBSIDIAN, 2), "7840b87d52271d2a755dedc82877e0ed3df67dcc42ea479ec146176b02779a5",
        new ItemStack[] {null, new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), getItem("LAPIS_PLANT"), new ItemStack(Material.OBSIDIAN), null, new ItemStack(Material.OBSIDIAN), null});

        registerMagicalPlant("Slime", "粘液球", new ItemStack(Material.SLIME_BALL, 8), "90e65e6e5113a5187dad46dfad3d3bf85e8ef807f82aac228a59c4a95d6f6a",
        new ItemStack[] {null, new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), getItem("ENDER_PLANT"), new ItemStack(Material.SLIME_BALL), null, new ItemStack(Material.SLIME_BALL), null});

        registerTechPlant("咖啡豆", "&c", Material.COCOA, PlantType.DOUBLE_PLANT, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA4M2VjMmIwMWRjMGZlZTc5YWEzMjE4OGQ5NDI5YWNjNjhlY2Y3MTQwOGRjYTA0YWFhYjUzYWQ4YmVhMCJ9fX0=");

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

        try {
            JarFile file = new JarFile(getFile());
            ZipEntry copy = file.getEntry("resources/" + defaultName);
            if (copy == null) {
                throw new FileNotFoundException();
            }
            input = file.getInputStream(copy);
        } catch (IOException iOException) {
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
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException iOException) {
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
                Map.Entry entry = o;
                String player = "Players." + entry.getKey();
                this.yamlStorge.set(player + ".Alcohol", Integer.valueOf(((PlayerAlcohol) entry.getValue()).getAlcohol()));
                this.yamlStorge.set(player + ".Drunk", Boolean.valueOf(((PlayerAlcohol) entry.getValue()).isDrunk()));
            }
            this.yamlStorge.save(new File(getDataFolder() + File.separator + "storge.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDatas(Player player) {
        try {
            String playerName = "Players." + player.getName();
            this.yamlStorge.set(playerName + ".Alcohol", Integer.valueOf(((PlayerAlcohol) drunkPlayers.get(player.getName())).getAlcohol()));
            this.yamlStorge.set(playerName + ".Drunk", Boolean.valueOf(((PlayerAlcohol) drunkPlayers.get(player.getName())).isDrunk()));
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
            Map.Entry entry = o;
            PlayerAlcohol pa = (PlayerAlcohol) entry.getValue();
            Player player = Bukkit.getPlayer(pa.getPlayer());
            if (player != null) {
                if (pa.getAlcohol() > 0) {
                    ((PlayerAlcohol) drunkPlayers.get(pa.getPlayer())).addAlcohol(-1);
                }
                if (pa.isDrunk) {
                    if (pa.getAlcohol() <= 0) {
                        ((PlayerAlcohol) drunkPlayers.get(pa.getPlayer())).setDrunk(false);
                        continue;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 1, false));
                    continue;
                }
                if (pa.getAlcohol() >= 100)
                    ((PlayerAlcohol) drunkPlayers.get(pa.getPlayer())).setDrunk(true);
            }
        }
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
