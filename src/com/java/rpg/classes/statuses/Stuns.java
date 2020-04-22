package com.java.rpg.classes.statuses;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Stuns implements Listener {

    private Main main = Main.getInstance();

    public static boolean isStunned(Player p) {
        return (Main.getInstance().getRP(p) != null && Main.getInstance().isPlayer(p) && Main.getInstance().getRP(p).getStun().getValue() > 0);
    }

    @EventHandler
    public void move (PlayerMoveEvent e) {
        if (isStunned(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void cancelAttack (PlayerInteractEvent e) {
        if (isStunned(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

}
