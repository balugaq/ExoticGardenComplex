package com.be.utils;

import com.be.BEPlugin;
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
        BEPlugin.getInstance().reloadConfig();
        if (BEPlugin.getInstance().getConfig().getString("spawn.world") != null && BEPlugin.getInstance().getConfig().getString("spawn.x") != null && BEPlugin.getInstance().getConfig().getString("spawn.y") != null && BEPlugin.getInstance().getConfig().getString("spawn.z") != null && BEPlugin.getInstance().getConfig().getString("spawn.yaw") != null && BEPlugin.getInstance().getConfig().getString("spawn.pitch") != null) {
            World world = Bukkit.getWorld(BEPlugin.getInstance().getConfig().getString("spawn.world"));
            double x = BEPlugin.getInstance().getConfig().getDouble("spawn.x");
            double y = BEPlugin.getInstance().getConfig().getDouble("spawn.y");
            double z = BEPlugin.getInstance().getConfig().getDouble("spawn.z");
            float yaw = (float) BEPlugin.getInstance().getConfig().getDouble("spawn.yaw");
            float pitch = (float) BEPlugin.getInstance().getConfig().getDouble("spawn.pitch");

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