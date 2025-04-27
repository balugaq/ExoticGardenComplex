package io.github.thebusybiscuit.exoticgarden;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExoticCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 3) {
            if (hasPermission(commandSender, "exoticgarden.admin") &&
                    strings[0].equalsIgnoreCase("alo") &&
                    strings[1].equalsIgnoreCase("info")) {
                if (Bukkit.getPlayer(strings[2]).isOnline()) {
                    commandSender.sendMessage("§8[§b异域森林§8] §7玩家§e" + strings[2] + "§7的酒精度为§e" + ExoticGarden.drunkPlayers.get(strings[2]).getAlcohol());
                } else {
                    commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
                }
            }


            return true;
        }
        if (strings.length == 4) {
            if (hasPermission(commandSender, "exoticgarden.admin")) {
                if (strings[1].equalsIgnoreCase("add")) {
                    if (Bukkit.getPlayer(strings[2]).isOnline()) {
                        ExoticGarden.drunkPlayers.get(strings[2]).addAlcohol(Integer.valueOf(strings[3]));
                        commandSender.sendMessage("§8[§b异域森林§8] §7为玩家§e" + strings[2] + "§7增加了§e" + strings[3] + "§酒精度");
                    } else {
                        commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
                    }
                } else if (strings[1].equalsIgnoreCase("set")) {
                    if (Bukkit.getPlayer(strings[2]).isOnline()) {
                        ExoticGarden.drunkPlayers.get(strings[2]).setAlcohol(Integer.valueOf(strings[3]));
                        commandSender.sendMessage("§8[§b异域森林§8] §7将玩家§e" + strings[2] + "§7的酒精度设置为§e" + strings[3]);
                    } else {
                        commandSender.sendMessage("§8[§b异域森林§8] §c指定的玩家不在线！");
                    }
                }
            }
            return true;
        }
        sendHelp(commandSender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        if (hasPermission(sender, "exoticgarden.admin")) {
            String[] help = {"        §7--------§8====§e[ §b异域森林 §e]§8====§7--------", "§b/exotic help                         §7显示帮助信息", "§b/exotic alo info <玩家名>            §7查看指定玩家酒精度", "§b/exotic alo add <玩家名> <值>        §增加/减少 酒精度", "§b/exotic alo set <玩家名> <值>        §7设定 酒精度"};


            sender.sendMessage(help);
        } else {
            sender.sendMessage("§c你没有权限这么做!");
        }
    }

    private boolean hasPermission(CommandSender sender, String perms) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        return (player.hasPermission(perms) || player.isOp());
    }
}


