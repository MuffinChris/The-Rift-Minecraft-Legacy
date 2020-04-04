package com.java.essentials.commands.admin;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.mod")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        main.getMuted().put(t.getUniqueId(), true);
                        Main.msg(p, "&aYou have muted &f" + t.getName() + "&a!");
                    } else {
                        Main.msg(p, "&cInvalid Target");
                    }
                } else {
                    Main.msg(p, "Usage: /mute <player>");
                }
            } else {
                Main.msg(p, main.noperm);
            }
        } else if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) instanceof Player) {
                Player t = Bukkit.getPlayer(args[0]);
                main.getMuted().put(t.getUniqueId(), true);
                Main.so( "&aYou have muted &f" + t.getName() + "&a!");
            } else {
                Main.so( "&cInvalid Target");
            }
        } else {
            Main.so( "Usage: /mute <player>");
        }
        return false;
    }

}
