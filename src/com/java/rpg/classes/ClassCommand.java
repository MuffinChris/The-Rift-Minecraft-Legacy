package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (main.getCM().getPClassFromString(args[0]) instanceof PlayerClass) {
                    if (!main.getPC().get(p.getUniqueId()).changeClass(main.getCM().getPClassFromString(args[0]))) {
                        Main.msg(p, "&cYou are already that class!");
                        return false;
                    }
                    Main.msg(p, "&aYou have selected the " + args[0] + " class!");
                } else {
                    Main.msg(p, "&cClass does not exist.");
                }
            } else {
                String output="";

                for (String plc : ClassManager.classes.keySet()) {
                    output+=plc + ", ";
                }

                output = output.substring(0, output.length() - 2);

                Main.msg(p, "&fClass List: " + output);
                Main.msg(p, "&fUsage: /class <class>");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.color("&cInvalid command for Console"));
        }
        return false;
    }

}
