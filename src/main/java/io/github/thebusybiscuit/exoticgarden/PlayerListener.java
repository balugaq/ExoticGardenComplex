package io.github.thebusybiscuit.exoticgarden;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {
    final ExoticGarden plugin;

    public PlayerListener(ExoticGarden plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ExoticGarden.instance.initPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ExoticGarden.instance.saveDatas(event.getPlayer());
        ExoticGarden.drunkPlayers.remove(event.getPlayer().getName());
    }


    @EventHandler
    public void move(PlayerMoveEvent event) {
        PlayerAlcohol playerAlcohol = ExoticGarden.drunkPlayers.get(event.getPlayer().getName());
        if (playerAlcohol.getAlcohol() >= 30 && playerAlcohol.isDrunk()) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                Player player = event.getPlayer();

                if (player.isOnGround()) {


                    Vector push = new Vector(0, 0, 0);
                    push.setX((Math.random() - 0.5D) / 2.0D);
                    push.setZ((Math.random() - 0.5D) / 2.0D);
                    player.setVelocity(push);
                }
            }
        }
    }
}
