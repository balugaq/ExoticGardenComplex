package com.be.utils;

import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BEListener implements Listener {

    private static final BEListener INSTANCE = new BEListener();

    public static BEListener getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ExoticGarden.getInstance().reloadConfig();
        if (ExoticGarden.getInstance().getConfig().getString("spawn.world") != null && ExoticGarden.getInstance().getConfig().getString("spawn.x") != null && ExoticGarden.getInstance().getConfig().getString("spawn.y") != null && ExoticGarden.getInstance().getConfig().getString("spawn.z") != null && ExoticGarden.getInstance().getConfig().getString("spawn.yaw") != null && ExoticGarden.getInstance().getConfig().getString("spawn.pitch") != null) {
            World world = Bukkit.getWorld(ExoticGarden.getInstance().getConfig().getString("spawn.world"));
            double x = ExoticGarden.getInstance().getConfig().getDouble("spawn.x");
            double y = ExoticGarden.getInstance().getConfig().getDouble("spawn.y");
            double z = ExoticGarden.getInstance().getConfig().getDouble("spawn.z");
            float yaw = (float) ExoticGarden.getInstance().getConfig().getDouble("spawn.yaw");
            float pitch = (float) ExoticGarden.getInstance().getConfig().getDouble("spawn.pitch");

            Location loc = new Location(world, x, y, z, yaw, pitch);
            Bukkit.getScheduler().runTask(ExoticGarden.instance, () -> event.getPlayer().teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN));
        }
    }


    @EventHandler
    public void onPerLogin(PlayerLoginEvent login) {
        Player p = login.getPlayer();
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + p.getName() + "已登录");
        login.allow();
    }
}