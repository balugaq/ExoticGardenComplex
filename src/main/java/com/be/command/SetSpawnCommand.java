package com.be.command;

import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                if (sender.isOp() || sender.hasPermission("spawn.admin")) {
                    ExoticGarden.getInstance().reloadConfig();
                    ExoticGarden.getInstance().getConfig().set("spawn.world", p.getWorld().getName());
                    ExoticGarden.getInstance().getConfig().set("spawn.x", p.getLocation().getX());
                    ExoticGarden.getInstance().getConfig().set("spawn.y", p.getLocation().getY());
                    ExoticGarden.getInstance().getConfig().set("spawn.z", p.getLocation().getZ());
                    ExoticGarden.getInstance().getConfig().set("spawn.yaw", p.getLocation().getYaw());
                    ExoticGarden.getInstance().getConfig().set("spawn.pitch", p.getLocation().getPitch());
                    ExoticGarden.getInstance().saveConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ExoticGarden.getInstance().getConfig().getString("messages.success-setspawn")));
                } else {
                    p.sendMessage("§cYou do not have permission to execute this command!");
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ExoticGarden.getInstance().getConfig().getString("messages.cmd-setspawn-usage")));
            }
        } else {
            sender.sendMessage("This Command can only executed by a player, sorry!");
        }

        return false;
    }
}