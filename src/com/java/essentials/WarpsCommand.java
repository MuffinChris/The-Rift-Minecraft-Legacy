package com.java.essentials;

import com.java.Main;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                File pFile = new File("plugins/Rift/warps.yml");
                FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                String warps = "&8» &eWarps: &f";

                for (String s : pData.getKeys(false)) {
                    warps += Main.color("&f" + s + ", ");
                }

                warps = warps.substring(0, warps.length() - 2);

                Main.msg(p, warps);
            } else {
                if (String.valueOf(args[0]) instanceof String) {
                    File pFile = new File("plugins/Rift/warps.yml");
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
                    if (pData.contains(String.valueOf(args[0]))) {
                        p.teleport((Location) pData.get(String.valueOf(args[0])));
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    } else {
                        Main.msg(p, "&cWarp does not exist.");
                    }
                } else {
                    Main.msg(p, "&cInvalid warp name.");
                }
            }
        } else {
            Main.so("&cCannot be used from console.");
        }
        return false;
    }
}
