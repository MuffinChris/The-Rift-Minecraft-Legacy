package com.java.essentials;

import com.java.Main;
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
            Main.msg(p, "&8» &e/skill &7- &fCast a skill");
            Main.msg(p, "&8» &ePress F &8(&eOffhand Key&8) &7- &fOpen Skill UI");
            Main.msg(p, "");
        }
        return false;
    }
}
