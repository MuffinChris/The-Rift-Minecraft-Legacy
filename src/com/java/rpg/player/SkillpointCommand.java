package com.java.rpg.player;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillpointCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player) {
            Player p = (Player) sender;
            RPGPlayer rp = main.getPC().get(p.getUniqueId());
            int total = rp.calculateSP();
            Main.msg(p, "");
            Main.msg(p, ("&8» &eSkillpoints: &f" + (total) + " &8(&e" + (2 - total) + " Used&8)"));
            Main.msg(p, "");
        } else {
            Main.so("&cNot a console command.");
        }
        return false;
    }

}
