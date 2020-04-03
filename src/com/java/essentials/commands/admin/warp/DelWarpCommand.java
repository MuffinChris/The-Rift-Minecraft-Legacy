package com.java.essentials.commands.admin.warp;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DelWarpCommand implements CommandExecutor {

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
                    if (pData.contains(warpname)) {
                        try {
                            pData.set(warpname, null);
                            Main.msg(p, "&aDeleted Warp &l" + warpname + "&a.");
                            pData.save(pFile);
                        } catch (IOException e) {
                            Main.msg(p, "&cFile Exception Error.");
                        }
                    } else {
                        Main.msg(p, "&cWarp does not exist.");
                    }
                } else {
                    Main.msg(p, "Usage: /delwarp <name>");
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
