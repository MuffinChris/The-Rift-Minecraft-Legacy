package com.java.essentials.commands.admin;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.mod")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        if (main.getMuted().containsKey(t.getUniqueId())) {
                            main.getMuted().remove(t.getUniqueId());
                            Main.msg(p, "&aYou have unmuted &f" + t.getName() + "&a!");
                        } else {
                            Main.msg(p, "&cPlayer was never muted.");
                        }
                    } else {
                        Main.msg(p, "&cInvalid Target");
                    }
                } else {
                    Main.msg(p, "Usage: /unmute <player>");
                }
            } else {
                Main.msg(p, main.noperm);
            }
        } else if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) instanceof Player) {
                Player t = Bukkit.getPlayer(args[0]);
                if (main.getMuted().containsKey(t.getUniqueId())) {
                    main.getMuted().remove(t.getUniqueId());
                    Main.so( "&aYou have unmuted &f" + t.getName() + "&a!");
                } else {
                    Main.so( "&cPlayer was never muted.");
                }
            } else {
                Main.so( "&cInvalid Target");
            }
        } else {
            Main.so( "Usage: /unmute <player>");
        }
        return false;
    }

}
