package com.java.essentials.commands.admin.utility;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FastRestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                Main.getInstance().restartTimer(5);
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            Main.getInstance().restartTimer(5);
        }
        return false;
    }

}
