package com.be.command;

import com.be.BEPlugin;
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
                    BEPlugin.getInstance().reloadConfig();
                    if (BEPlugin.getInstance().getConfig().getString("spawn.world") != null && BEPlugin.getInstance().getConfig().getString("spawn.x") != null && BEPlugin.getInstance().getConfig().getString("spawn.y") != null && BEPlugin.getInstance().getConfig().getString("spawn.z") != null && BEPlugin.getInstance().getConfig().getString("spawn.yaw") != null && BEPlugin.getInstance().getConfig().getString("spawn.pitch") != null) {
                        World world = Bukkit.getWorld(BEPlugin.getInstance().getConfig().getString("spawn.world"));
                        double x = BEPlugin.getInstance().getConfig().getDouble("spawn.x");
                        double y = BEPlugin.getInstance().getConfig().getDouble("spawn.y");
                        double z = BEPlugin.getInstance().getConfig().getDouble("spawn.z");
                        float yaw = (float) BEPlugin.getInstance().getConfig().getDouble("spawn.yaw");
                        float pitch = (float) BEPlugin.getInstance().getConfig().getDouble("spawn.pitch");

                        Location loc = new Location(world, x, y, z, yaw, pitch);
                        p.teleport(loc);

                        if (Boolean.parseBoolean(BEPlugin.getInstance().getConfig().getString("settings.tpmessage-enable"))) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', BEPlugin.getInstance().getConfig().getString("messages.tpmessage")));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', BEPlugin.getInstance().getConfig().getString("messages.error-nospawnpoint")));
                    }
                } else {
                    p.sendMessage("§cYou do not have permission to execute this command!");
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', BEPlugin.getInstance().getConfig().getString("messages.cmd-spawn-usage")));
            }
        } else {
            sender.sendMessage("This Command can only executed by a player, sorry!");
        }

        return false;
    }
}