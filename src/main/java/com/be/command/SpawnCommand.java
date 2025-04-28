package com.be.command;

import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                if (sender.isOp() || sender.hasPermission("spawn.admin") || sender.hasPermission("spawn.spawn")) {
                    ExoticGarden.getInstance().reloadConfig();
                    if (ExoticGarden.getInstance().getConfig().getString("spawn.world") != null && ExoticGarden.getInstance().getConfig().getString("spawn.x") != null && ExoticGarden.getInstance().getConfig().getString("spawn.y") != null && ExoticGarden.getInstance().getConfig().getString("spawn.z") != null && ExoticGarden.getInstance().getConfig().getString("spawn.yaw") != null && ExoticGarden.getInstance().getConfig().getString("spawn.pitch") != null) {
                        World world = Bukkit.getWorld(ExoticGarden.getInstance().getConfig().getString("spawn.world"));
                        double x = ExoticGarden.getInstance().getConfig().getDouble("spawn.x");
                        double y = ExoticGarden.getInstance().getConfig().getDouble("spawn.y");
                        double z = ExoticGarden.getInstance().getConfig().getDouble("spawn.z");
                        float yaw = (float) ExoticGarden.getInstance().getConfig().getDouble("spawn.yaw");
                        float pitch = (float) ExoticGarden.getInstance().getConfig().getDouble("spawn.pitch");

                        Location loc = new Location(world, x, y, z, yaw, pitch);
                        p.teleport(loc);

                        if (Boolean.parseBoolean(ExoticGarden.getInstance().getConfig().getString("settings.tpmessage-enable"))) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ExoticGarden.getInstance().getConfig().getString("messages.tpmessage")));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ExoticGarden.getInstance().getConfig().getString("messages.error-nospawnpoint")));
                    }
                } else {
                    p.sendMessage("§cYou do not have permission to execute this command!");
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ExoticGarden.getInstance().getConfig().getString("messages.cmd-spawn-usage")));
            }
        } else {
            sender.sendMessage("This Command can only executed by a player, sorry!");
        }

        return false;
    }
}