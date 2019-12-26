package com.java.essentials;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BetterRestart implements Listener {

    @EventHandler
    public void restart (PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("restart")) {
            if (e.getPlayer().hasPermission("core.admin")) {
                e.setCancelled(true);
                Main.getInstance().restartTimer(3);
            }
        }
    }

}
