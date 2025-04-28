package io.github.thebusybiscuit.exoticgarden;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;

public class FoodListener implements Listener {
    final ExoticGarden plugin;

    public FoodListener(ExoticGarden plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onUse(final PlayerRightClickEvent e) {
        SlimefunItem item;
        if (e.getPlayer().getFoodLevel() >= 20)
            return;
        EquipmentSlot hand = e.getHand();

        switch (hand) {
            case HAND:
                item = SlimefunItem.getByItem(new CustomItemStack(e.getPlayer().getInventory().getItemInMainHand(), 1));
                if (item instanceof EGPlant && (
                        (EGPlant) item).isEdible()) {
                    ((EGPlant) item).restoreHunger(e.getPlayer());
                    e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        var a = e.getPlayer().getInventory().getItemInMainHand();
                        a.setAmount(a.getAmount() - 1);
                        e.getPlayer().getInventory().setItemInMainHand(a);
                    }, 0L);
                }
                break;


            case OFF_HAND:
                item = SlimefunItem.getByItem(new CustomItemStack(e.getPlayer().getInventory().getItemInOffHand(), 1));
                if (item instanceof EGPlant && (
                        (EGPlant) item).isEdible()) {
                    ((EGPlant) item).restoreHunger(e.getPlayer());
                    e.getPlayer().getWorld().playSound(e.getPlayer().getEyeLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                        var a = e.getPlayer().getInventory().getItemInOffHand();
                        a.setAmount(a.getAmount() - 1);
                        e.getPlayer().getInventory().setItemInOffHand(a);
                    }, 0L);
                }
                break;
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        SlimefunItem item = SlimefunItem.getByItem(e.getItemInHand());
        if (item instanceof EGPlant && e.getItemInHand().getType() == Material.PLAYER_HEAD)
            e.setCancelled(true);
    }

    @EventHandler
    public void onEquip(InventoryClickEvent e) {
        if (e.getSlotType() != InventoryType.SlotType.ARMOR)
            return;
        SlimefunItem item = SlimefunItem.getByItem(e.getCursor());
        if (item instanceof EGPlant && e.getCursor().getType() == Material.PLAYER_HEAD)
            e.setCancelled(true);
    }
}


