package com.java.rpg.classes.statuses;

import com.java.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Stuns implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void move (PlayerMoveEvent e) {
        if (main.getRP(e.getPlayer()) != null && main.getRP(e.getPlayer()).getStun().getValue() > 0) {
            e.setCancelled(true);
        }
    }

}
