package com.java.rpg.classes.commands.admin;

import com.java.Main;
import com.java.rpg.classes.PlayerClass;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetClassCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 2) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        RPGPlayer pl = main.getPC().get(t.getUniqueId());
                        if (main.getCM().getPClassFromString(args[1]) != null) {
                            PlayerClass pclass = main.getCM().getPClassFromString(args[1]);
                            if (pclass.getName().equals(pl.getPClass().getName())) {
                                Main.msg(p, "&cPlayer is already this class.");
                            } else {
                                pl.changeClass(pclass);
                                Main.msg(p, "&aSuccessfully changed &f" + t.getName() + "&a's class to &f" + pclass.getName() + "&a.");
                            }
                        } else {
                            Main.msg(p, "&cInvalid Class.");
                        }
                    } else {
                        Main.msg(p, "&cInvalid Target.");
                    }
                } else {
                    Main.msg(p, "&fUsage: /setclass <player> <class>");
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayer(args[0]);
                    RPGPlayer pl = main.getPC().get(t.getUniqueId());
                    if (main.getCM().getPClassFromString(args[1]) != null) {
                        PlayerClass pclass = main.getCM().getPClassFromString(args[1]);
                        if (pclass.getName().equals(pl.getPClass().getName())) {
                            Main.so("&cPlayer is already this class.");
                        } else {
                            pl.changeClass(pclass);
                            Main.so("&aSuccessfully changed &f" + t.getName() + "&a's class to &f" + pclass.getName() + "&a.");
                        }
                    } else {
                        Main.so("&cInvalid Class.");
                    }
                } else {
                    Main.so("&cInvalid Target.");
                }
            } else {
                Main.so("&fUsage: /setclass <player> <class>");
            }
        }
        return false;
    }
}
