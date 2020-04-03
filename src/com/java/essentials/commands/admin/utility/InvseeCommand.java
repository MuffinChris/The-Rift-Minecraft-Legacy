package com.java.essentials.commands.admin.utility;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.mod")) {
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        p.openInventory(t.getInventory());
                        Main.msg(p, "&8» &eOpened &6" + t.getName() + "'s &einventory.");
                    } else {
                        Main.msg(p, "&cInvalid Player.");
                    }
                } else {
                    Main.msg(p, "Usage: /invsee <player>");
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            Main.so("&cCannot be used from console.");
        }
        return false;
    }

}
