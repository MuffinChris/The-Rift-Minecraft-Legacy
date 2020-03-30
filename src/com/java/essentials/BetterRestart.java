package com.java.essentials;

import com.destroystokyo.paper.Title;
import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BetterRestart implements Listener {

    /*@EventHandler
    public void restart (PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("restart")) {
            if (e.getPlayer().hasPermission("core.admin")) {
                e.setCancelled(true);
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    Main.getInstance().getRP(p).updateStats();
                    Main.getInstance().getRP(p).pushFiles();
                }
                Bukkit.getServer().shutdown();
            }
        }
    }*/

}
