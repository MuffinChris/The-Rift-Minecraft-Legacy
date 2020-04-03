package com.java.essentials.commands;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SeenCommand implements CommandExecutor {


    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayerExact(args[0]);
                    if (t.isOnline()) {
                        Main.msg(p, "&8» &e" + t.getName() + " &fis currently Online.");
                        return false;
                    }
                }
                if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
                    File pFile = new File("plugins/Rift/data/classes/" + t.getUniqueId() + ".yml");
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                    if (pData.contains("LastSeen")) {
                        long millis = System.currentTimeMillis() - pData.getLong("LastSeen");
                        long days = TimeUnit.MILLISECONDS.toDays(millis);
                        millis -= TimeUnit.DAYS.toMillis(days);
                        long hours = TimeUnit.MILLISECONDS.toHours(millis);
                        millis -= TimeUnit.HOURS.toMillis(hours);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                        millis -= TimeUnit.MINUTES.toMillis(minutes);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
                        String result = "";
                        if (days > 0) {
                            result+=days + " days, ";
                        }
                        if (hours > 0) {
                            result+=hours + " hours, ";
                        }
                        if (minutes > 0) {
                            result+=minutes +" minutes, ";
                        }
                        if (seconds > 0) {
                            result+=seconds +" seconds, ";
                        }
                        if (!result.equals("")) {
                            result = result.substring(0, result.length() - 2);
                            Main.msg(p, "&8» &e" + t.getName() + " &fwas last seen &e" + result + " &fago.");
                        } else {
                            Main.msg(p, "&8» &e" + t.getName() + " &fliterally just left.");
                        }
                    } else {
                        Main.msg(p, "&cPlayer has not joined since command was added.");
                    }
                } else {
                    Main.msg(p, "&8» &e"+ args[0] + " &fhas never played before!");
                }
            } else {
                Main.msg(p, "Usage: /seen <player>");
            }
        } else {
            if (args.length == 1) {
                if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
                    Player t = Bukkit.getPlayerExact(args[0]);
                    if (t.isOnline()) {
                        Main.so( "&8» &e" + t.getName() + " &fis currently Online.");
                        return false;
                    }
                }
                if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
                    File pFile = new File("plugins/Rift/data/classes/" + t.getUniqueId() + ".yml");
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                    if (pData.contains("LastSeen")) {
                        long millis = System.currentTimeMillis() - pData.getLong("LastSeen");
                        long days = TimeUnit.MILLISECONDS.toDays(millis);
                        millis -= TimeUnit.DAYS.toMillis(days);
                        long hours = TimeUnit.MILLISECONDS.toHours(millis);
                        millis -= TimeUnit.HOURS.toMillis(hours);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                        millis -= TimeUnit.MINUTES.toMillis(minutes);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
                        String result = "";
                        if (days > 0) {
                            result+=days + " days, ";
                        }
                        if (hours > 0) {
                            result+=hours + " hours, ";
                        }
                        if (minutes > 0) {
                            result+=minutes +" minutes, ";
                        }
                        if (seconds > 0) {
                            result+=seconds +" seconds, ";
                        }
                        if (!result.equals("")) {
                            result = result.substring(0, result.length() - 2);
                            Main.so( "&8» &e" + t.getName() + " &fwas last seen &e" + result + " &fago.");
                        } else {
                            Main.so( "&8» &e" + t.getName() + " &fliterally just left.");
                        }
                    } else {
                        Main.so( "&cPlayer has not joined since command was added.");
                    }
                } else {
                    Main.so( "&8» &e"+ args[0] + " &fhas never played before!");
                }
            } else {
                Main.so( "Usage: /seen <player>");
            }
        }
        return false;
    }

}
