package com.java.essentials;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SetWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 1) {
                    String warpname;
                    if (String.valueOf(args[0]) instanceof String) {
                        warpname = String.valueOf(args[0]);
                    } else {
                        Main.msg(p, "&cInvalid Name.");
                        return false;
                    }
                    File pFile = new File("plugins/Rift/warps.yml");
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                    try {
                        pData.set(warpname, p.getLocation());
                        Main.msg(p, "&aSet Warp &l" + warpname + " &ato your current location.");
                        pData.save(pFile);
                    } catch (IOException e) {
                        Main.msg(p, "&cFile Exception Error.");
                    }
                } else {
                    Main.msg(p, "Usage: /setwarp <name>");
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
