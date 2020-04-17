package com.java.rpg.classes.commands.player;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                RPGPlayer pl = main.getPC().get(p.getUniqueId());
                Main.msg(p, "");
                Main.msg(p, "&8» &eLevel: &f" + pl.getLevel() + " &8/ &f" + pl.getMaxLevel() + " &8(&6" + pl.getPClass().getName() + "&8)");
                Main.msg(p, "&8» &eEXP: &f" + pl.getPrettyExp() + " &8/ &f" + pl.getPrettyMaxExp() + " &8(&6" + pl.getPrettyPercent() + "%&8)");
                Main.msg(p, "");
            } else if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayer(args[0]);
                    RPGPlayer pl = main.getPC().get(t.getUniqueId());
                    Main.msg(p, "");
                    Main.msg(p, "&8» &e" + t.getName() + "'s Level: &f" + pl.getLevel() + " &8/ &f" + pl.getMaxLevel() + " &8(&6" + pl.getPClass().getName() + "&8)");
                    Main.msg(p, "&8» &e" + t.getName() + "'s EXP: &f" + pl.getPrettyExp() + " &8/ &f" + pl.getPrettyMaxExp() + " &8(&6" + pl.getPrettyPercent() + "%&8)");
                    Main.msg(p, "");
                } else {
                    Main.msg(p, "&cInvalid Target");
                }
            } else {
                Main.msg(p, "Usage: /level <player>");
            }
        } else if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) instanceof Player) {
                Player t = Bukkit.getPlayer(args[0]);
                RPGPlayer pl = main.getPC().get(t.getUniqueId());
                Main.so( "");
                Main.so( "&8» &e" + t.getName() + "'s Level: &f" + pl.getLevel() + " &8/ &f" + pl.getMaxLevel() + " &8(&6" + pl.getPClass().getName() + "&8)");
                Main.so( "&8» &e" + t.getName() + "'s EXP: &f" + pl.getPrettyExp() + " &8/ &f" + pl.getPrettyMaxExp() + " &8(&6" + pl.getPrettyPercent() + "%&8)");
                Main.so( "");
            } else {
                Main.so( "&cInvalid Target");
            }
        } else {
            Main.so( "Usage: /level <player>");
        }
        return false;
    }

}
