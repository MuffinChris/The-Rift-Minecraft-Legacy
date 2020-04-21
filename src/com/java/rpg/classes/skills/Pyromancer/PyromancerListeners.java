package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.entity.Mobs;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import static com.java.rpg.entity.EntityUtils.*;

public class PyromancerListeners implements Listener {

    private Main main = Main.getInstance();

    // FIREBALL STUFF

    @EventHandler
    public void projectileHe(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity();
            if (a.getShooter() instanceof Player && main.isPlayer((Player) a.getShooter()) && getCustomName(a) != null && getCustomName(a).equalsIgnoreCase("Fireball")) {
                Player shooter = (Player) a.getShooter();
                if (e.getHitEntity() != null) {
                    Fireball.lightEntities(e.getHitEntity(), shooter, e.getHitEntity().getLocation(), getMagicDamage(a), getElementalDamage(a));
                    e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getHitEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08, null, true);
                } else {
                    Fireball.lightEntities(e.getHitEntity(), shooter, e.getEntity().getLocation(), getMagicDamage(a), getElementalDamage(a));
                    e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08, null, true);
                }
                a.remove();
            }
            if (a.getShooter() instanceof Player && main.isPlayer((Player) a.getShooter()) && getCustomName(a) != null && getCustomName(a).equalsIgnoreCase("Fireball")) {
                a.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && !(e.getEntity() instanceof ArmorStand)) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getShooter() instanceof Player && main.isPlayer((Player) a.getShooter()) && getCustomName(a) != null && (getCustomName(a).equalsIgnoreCase("Fireball") || getCustomName(a).equalsIgnoreCase("Combust"))) {
                Player shooter = (Player) a.getShooter();
                if (!main.isValidTarget(e.getEntity(), shooter)) {
                    a.remove();
                    e.setCancelled(true);
                    return;
                }
                a.remove();
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }

    // PYROCLASM

    @EventHandler
    public void noFireSpread(BlockSpreadEvent e) {
        Location fireLoc = e.getSource().getLocation();
        if (fireLoc.getBlock().hasMetadata("noFire")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noFireIgnite(BlockIgniteEvent e) {
        if (e.getIgnitingBlock() != null) {
            Location fireLoc = e.getIgnitingBlock().getLocation();
            if (fireLoc.getBlock().hasMetadata("noFire")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noFireBurn(BlockBurnEvent e) {
        if (e.getIgnitingBlock() != null) {
            Location fireLoc = e.getIgnitingBlock().getLocation();
            if (fireLoc.getBlock().hasMetadata("noFire")) {
                e.setCancelled(true);
            }
        }
    }
}
