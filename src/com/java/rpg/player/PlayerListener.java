package com.java.rpg.player;

import com.java.Main;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Main.sendHp((Player) e.getEntity());
        }
    }

}
