package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WandererListeners implements Listener {

    private Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
            Player p = (Player) e.getEntity();
            if (main.getRP(p) != null && main.getRP(p).getStatuses().contains("Bulwark")) {
                e.setCancelled(true);
                e.getDamager().remove();
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
            }
        }
    }

}
