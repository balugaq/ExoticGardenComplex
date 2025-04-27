
package io.github.thebusybiscuit.exoticgarden;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;

 import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
 import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
 import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
 import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
 import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
 import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
 import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
 import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
 import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
 import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
 import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
 import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
 import me.mrCookieSlime.ExoticGarden.recipe.DefaultSubRecipe;
 import me.mrCookieSlime.Slimefun.Lists.RecipeType;
 import me.mrCookieSlime.Slimefun.Misc.compatibles.ProtectionUtils;
 import me.mrCookieSlime.Slimefun.Objects.Category;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineHelper;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
 import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;
 import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
 import me.mrCookieSlime.Slimefun.api.BlockStorage;
 import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
 import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
 import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
 import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
 import org.bukkit.Bukkit;
 import org.bukkit.Material;
 import org.bukkit.block.Block;
 import org.bukkit.entity.Player;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import org.bukkit.inventory.Inventory;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.inventory.meta.ItemMeta;
 import org.bukkit.material.MaterialData;
 
 public abstract class DefaultGUI extends SlimefunItem {
   public static Map<Block, MachineRecipe> processing = new HashMap<>();
   public static Map<Block, Integer> progress = new HashMap<>();
   protected List<MachineRecipe> recipes = new ArrayList<>();
   private static final int[] border = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
   private static final int[] inputBorder = new int[] { 10, 11, 12, 14, 15, 16 };
   private static final int[] centerBorder = new int[] { 19, 20, 21, 22, 23, 24, 25 };
   private static final int[] outputBorder = new int[] { 30, 32, 39, 40, 41 };
   private static final int[] subSlotSign = new int[] { 28, 29 };
   private static final int[] mainSlotSign = new int[] { 33, 34 };
 
 
 
 
   
   public DefaultGUI(ItemGroup category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
     super(category, item, name, recipeType, recipe);
     
     new BlockMenuPreset(name, getInventoryTitle())
       {
         
         public void init()
         {
           DefaultGUI.this.constructMenu(this);
         }
 
 
         
         public void newInstance(BlockMenu menu, Block b) {}
 
         
         public boolean canOpen(Block b, Player p) {
           boolean perm = (p.hasPermission("slimefun.inventory.bypass"));
           return (perm && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
         }
 
 
         
         public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
           if (flow.equals(ItemTransportFlow.INSERT)) {
             return DefaultGUI.this.getInputSlots();
           }
           return DefaultGUI.this.getOutputMainSlots();
         }
       };
     registerBlockHandler(name, new SlimefunBlockHandler()
         {
           public void onPlace(Player p, Block b, SlimefunItem item) {}
 
 
 
           
           public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
             BlockMenu inv = BlockStorage.getInventory(b);
             if (inv != null) {
               
               for (int slot : DefaultGUI.this.getInputSlots()) {
                 if (inv.getItemInSlot(slot) != null) {
                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                 }
               } 
               for (int slot : DefaultGUI.this.getOutputMainSlots()) {
                 if (inv.getItemInSlot(slot) != null) {
                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                 }
               } 
               for (int slot : DefaultGUI.this.getOutputSubSlots()) {
                 if (inv.getItemInSlot(slot) != null) {
                   b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                 }
               } 
             } 
             DefaultGUI.progress.remove(b);
             DefaultGUI.processing.remove(b);
             return true;
           }
         });
     registerDefaultRecipes();
   }
   
   public DefaultGUI(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
     super(category, item, name, recipeType, recipe, recipeOutput);
     
     new BlockMenuPreset(name, getInventoryTitle())
       {
         
         public void init()
         {
           DefaultGUI.this.constructMenu(this);
         }
 
         
         public void newInstance(BlockMenu menu, Block b) {}
 
         
         public boolean canOpen(Block b, Player p) {
           boolean perm = (p.hasPermission("slimefun.inventory.bypass"));
           return (perm && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
         }
 
 
         
         public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
           if (flow.equals(ItemTransportFlow.INSERT)) {
             return DefaultGUI.this.getInputSlots();
           }
           return DefaultGUI.this.getOutputMainSlots();
         }
       };
     registerBlockHandler(name, new SlimefunBlockHandler()
         {
           public void onPlace(Player p, Block b, SlimefunItem item) {}
 
 
 
           
           public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
             for (int slot : DefaultGUI.this.getInputSlots()) {
               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
               }
             } 
             for (int slot : DefaultGUI.this.getOutputMainSlots()) {
               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
               }
             } 
             for (int slot : DefaultGUI.this.getOutputSubSlots()) {
               if (BlockStorage.getInventory(b).getItemInSlot(slot) != null) {
                 b.getWorld().dropItemNaturally(b.getLocation(), BlockStorage.getInventory(b).getItemInSlot(slot));
               }
             } 
             DefaultGUI.processing.remove(b);
             DefaultGUI.progress.remove(b);
             return true;
           }
         });
     registerDefaultRecipes();
   }
   
   private void constructMenu(BlockMenuPreset preset) {
     for (int i : border) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)3).toItemStack(), " ", new String[0]), (player, i1, itemStack, clickAction) -> false);
     } 
     for (int i : inputBorder) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)0).toItemStack(), " ", new String[0]), (player, i2, itemStack, clickAction) -> false);
     } 
     for (int i : centerBorder) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)4).toItemStack(), " ", new String[0]), (player, i4, itemStack, clickAction) -> false);
     } 
     for (int i : outputBorder) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)9).toItemStack(), " ", new String[0]), (player, i3, itemStack, clickAction) -> false);
     } 
     for (int i : subSlotSign) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)5).toItemStack(), "&e副输出槽", new String[] { "", "&7副输出槽通常会输出机器的副产物", "&7有些副产物极其有用甚至非常珍贵" }), new ChestMenu.MenuClickHandler()
           {
             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
             {
               return false;
             }
           });
     } 
     for (int i : mainSlotSign) {
       preset.addItem(i, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)5).toItemStack(), "&c主输出槽", new String[] { "", "&7主输出槽输出机器的常规产品" }), new ChestMenu.MenuClickHandler()
           {
             public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
             {
               return false;
             }
           });
     } 
     preset.addItem(31, (ItemStack)new CustomItemStack(new MaterialData(Material.LEGACY_STAINED_GLASS_PANE, (byte)15).toItemStack(), " ", new String[0]), new ChestMenu.MenuClickHandler()
         {
           public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction)
           {
             return false;
           }
         });
     
     preset.addItem(38, null, (ChestMenu.MenuClickHandler)new ChestMenu.AdvancedMenuClickHandler()
         {
           public boolean onClick(Player player, int i, ItemStack item, ClickAction action) {
             return false;
           }
 
           
           public boolean onClick(InventoryClickEvent event, Player player, int slot, ItemStack item, ClickAction action) {
             return (item == null || item.getType() == null || item.getType() == Material.AIR);
           }
         });
   }
   public int[] getInputSlots() {
     return new int[] { 13 };
   }
   public int[] getOutputSubSlots() {
     return new int[] { 37, 38 };
   }
   public int[] getOutputMainSlots() {
     return new int[] { 42, 43 };
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public MachineRecipe getProcessing(Block b) {
     return processing.get(b);
   }
 
   
   public boolean isProcessing(Block b) {
     return (getProcessing(b) != null);
   }
 
   
   public void registerRecipe(MachineRecipe recipe) {
     recipe.setTicks(recipe.getTicks());
     this.recipes.add(recipe);
   }
 
   
   public void registerRecipe(int seconds, ItemStack[] input, ItemStack[] output) {
     registerRecipe(new MachineRecipe(seconds, input, output));
   }
 
   
   private Inventory inject(Block b) {
     int size = BlockStorage.getInventory(b).toInventory().getSize();
     Inventory inv = Bukkit.createInventory(null, size);
     for (int i = 0; i < size; i++) {
       inv.setItem(i, (ItemStack)new CustomItemStack(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
     }
     for (int slot : getOutputMainSlots()) {
       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
     }
     return inv;
   }
 
   
   protected boolean fits(Block b, ItemStack[] items) {
     return inject(b).addItem(items).isEmpty();
   }
 
   
   protected void pushMainItems(Block b, ItemStack[] items) {
     Inventory inv = inject(b);
     inv.addItem(items);
     for (int slot : getOutputMainSlots()) {
       BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
     }
   }
 
   
   private Inventory injectSub(Block b) {
     int size = BlockStorage.getInventory(b).toInventory().getSize();
     Inventory inv = Bukkit.createInventory(null, size);
     for (int i = 0; i < size; i++) {
       inv.setItem(i, (ItemStack)new CustomItemStack(Material.COMMAND_BLOCK, " &4ALL YOUR PLACEHOLDERS ARE BELONG TO US", 0));
     }
     for (int slot : getOutputSubSlots()) {
       inv.setItem(slot, BlockStorage.getInventory(b).getItemInSlot(slot));
     }
     return inv;
   }
 
   
   protected void pushSubItems(Block b, DefaultSubRecipe recipe) {
     if (recipe != null && willOutput(recipe) && fits(b, new ItemStack[] { recipe.getItem() })) {
       Inventory inv = injectSub(b);
       inv.addItem(new ItemStack[] { recipe.getItem() });
       for (int slot : getOutputSubSlots()) {
         BlockStorage.getInventory(b).replaceExistingItem(slot, inv.getItem(slot));
       }
     } 
   }
   
   protected DefaultSubRecipe selectSubItem(List<DefaultSubRecipe> subRecipes) {
     int random = (int)(Math.random() * subRecipes.size());
     return subRecipes.get(random);
   }
   
   private boolean willOutput(DefaultSubRecipe recipe) {
     Random random = new Random();
     int point = random.nextInt(10000);
     return (point < recipe.getChance());
   }
 
 
   
   public void register(boolean slimefun) {
     addItemHandler(new ItemHandler[] { (ItemHandler)new me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker()
           {
             
             public void tick(Block b, SlimefunItem sf, Config data)
             {
               DefaultGUI.this.tick(b);
             }
 
 
             
             public void uniqueTick() {}
 
             
             public boolean isSynchronized() {
               return false;
             }
           } });
     super.register(ExoticGarden.instance);
   }
 
 
   
   protected void tick(Block b) {
     if (isProcessing(b)) {
       
       int timeleft = ((Integer)progress.get(b)).intValue();
       if (timeleft > 0) {
         
         ItemStack item = getProgressBar().clone();
         item.setDurability(MachineHelper.getDurability(item, timeleft, ((MachineRecipe)processing.get(b)).getTicks()));
         ItemMeta im = item.getItemMeta();
         im.setDisplayName(" ");
         List<String> lore = new ArrayList<>();
         lore.add(MachineHelper.getProgress(timeleft, ((MachineRecipe)processing.get(b)).getTicks()));
         lore.add("");
         lore.add(MachineHelper.getTimeLeft(timeleft / 2));
         im.setLore(lore);
         item.setItemMeta(im);
         
         BlockStorage.getInventory(b).replaceExistingItem(31, item);
         if (ChargableBlock.isChargable(b))
         {
           if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
             return;
           }
           ChargableBlock.addCharge(b, -getEnergyConsumption());
           progress.put(b, Integer.valueOf(timeleft - 1));
         }
         else
         {
           progress.put(b, Integer.valueOf(timeleft - 1));
         }
       
       } else {
         
         BlockStorage.getInventory(b).replaceExistingItem(31, (ItemStack)new CustomItemStack(new MaterialData(Material.STAINED_GLASS_PANE, (byte)15), " ", new String[0]));
         pushMainItems(b, ((MachineRecipe)processing.get(b)).getOutput());
         pushSubItems(b, selectSubItem(getSubRecipes()));
         progress.remove(b);
         processing.remove(b);
       }
     
     } else {

         MachineRecipe r = null;
       Map<Integer, Integer> found = new HashMap<>();
       for (MachineRecipe recipe : this.recipes) {
         
         for (ItemStack input : recipe.getInput()) {
           for (int slot : getInputSlots()) {
             if (SlimefunUtils.isItemSimilar(BlockStorage.getInventory(b).getItemInSlot(slot), input, true))
             {
               if (input != null) {
                 found.put(Integer.valueOf(slot), Integer.valueOf(input.getAmount()));
               }
             }
           } 
         } 
         
         if (found.size() == (recipe.getInput()).length) {
           
           r = recipe;
           break;
         } 
         found.clear();
       } 
       if (r != null) {
         
         if (!fits(b, r.getOutput())) {
           return;
         }
         for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
           BlockStorage.getInventory(b).replaceExistingItem(((Integer)entry.getKey()).intValue(), InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(((Integer)entry.getKey()).intValue()), ((Integer)entry.getValue()).intValue()));
         }
         processing.put(b, r);
         progress.put(b, Integer.valueOf(r.getTicks()));
       } 
     } 
   }
   
   public abstract String getInventoryTitle();
   
   public abstract ItemStack getProgressBar();
   
   public abstract List<DefaultSubRecipe> getSubRecipes();
   
   public abstract void registerDefaultRecipes();
   
   public abstract int getEnergyConsumption();
   
   public abstract int getLevel();
   
   public abstract String getMachineIdentifier();
 }


