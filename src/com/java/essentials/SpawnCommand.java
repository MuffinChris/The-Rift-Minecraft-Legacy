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

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            File pFile = new File("plugins/Rift/warps.yml");
            FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
            if (pData.contains("Spawn")) {
                p.teleport((Location) pData.get("Spawn"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            } else {
                Main.msg(p, "&cSpawn has not been set!");
            }
        } else {
            Main.so("&cCannot be used from console.");
        }
        return false;
    }

}
