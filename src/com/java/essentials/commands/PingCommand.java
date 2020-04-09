package com.java.essentials.commands;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.msg(p, "");
            Main.msg(p, "&8» &e&lPing: &f" + ((CraftPlayer)p).getHandle().ping);
            Main.msg(p, "");
        } else {
            Main.so("&cNot a console command");
        }
        return false;

    }

}
