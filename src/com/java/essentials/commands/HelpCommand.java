package com.java.essentials.commands;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.msg(p, "&8» &eUseful Commands");
            Main.msg(p, "");
            Main.msg(p, "&8» &e/class &7- &fPick a Class");
            Main.msg(p, "&8» &e/skills &7- &fView your Class's Skills");
            Main.msg(p, "&8» &e/info &7- &fView your Player Info");
            Main.msg(p, "&8» &ePress F &8(&eOffhand Key&8) &7- &fOpen Skill UI");
            Main.msg(p, "&8» &e/bind <skill> &7- &fBind skills to Items");
            Main.msg(p, "");
        } else {
            Bukkit.dispatchCommand(sender.getServer().getConsoleSender(), "?");
        }
        return false;
    }
}
