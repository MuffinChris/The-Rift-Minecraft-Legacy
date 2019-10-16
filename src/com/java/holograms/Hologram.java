package com.java.holograms;

import com.java.Main;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;

public class Hologram implements Listener {

    private Main main = Main.getInstance();

    private ArmorStand stand;
    private String text;
    private Entity entity;
    private int lifetime;
    private HologramType type;

    public ArmorStand getStand() {
        return stand;
    }

    public Hologram() {

    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e)
    {
        if(!e.getRightClicked().isVisible())
        {
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void damageStand(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand && !((ArmorStand) e.getEntity()).isVisible()) {
            e.setCancelled(true);
        }
    }

    public enum HologramType {
        DAMAGE, HOLOGRAM
    }

    public void resetLifetime() {
        lifetime = 0;
    }

    public Hologram(Entity e, Location loc, String text, HologramType type) {
        this.text = text;
        this.type = type;
        entity = e;
        lifetime = 0;
        stand = (ArmorStand) loc.getWorld().spawnEntity(new Location(loc.getWorld(), 0, 0, 0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setMarker(true);
        double neg = Math.random();
        double neg2 = Math.random();
        if (type == HologramType.DAMAGE) {
            if (neg < 0.5) {
                neg = -0.7;
            } else {
                neg = 0.7;
            }
            if (neg2 < 0.5) {
                neg2 = -0.7;
            } else {
                neg2 = 0.7;
            }
            stand.teleport(loc.add(new Vector(Math.random() * neg, e.getHeight(), Math.random() * neg2)));
        } else {
            stand.teleport(loc);
        }
        stand.setCustomNameVisible(true);
        stand.setCustomName(Main.color(this.text));
        stand.setAI(false);
        stand.setCollidable(false);
        stand.setInvulnerable(true);
        stand.setGravity(false);
        stand.setCanPickupItems(false);
        main.getHolos().add(this);
    }

    public boolean shouldRemove() {
        if (!(entity instanceof LivingEntity) || entity.isDead()) {
            return true;
        }
        return false;
    }

    public Entity getEntity() {
        return entity;
    }

    public void center() {
        if (entity != null && !entity.isDead()) {
            stand.teleport(entity.getLocation().add(new Vector(0, entity.getHeight() - 0.2, 0)));
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String s) {
        text = s;
        stand.setCustomName(Main.color(this.text));
    }

    public int getLifetime() {
        return lifetime;
    }

    public HologramType getType() {
        return type;
    }

    public void rise() {
        // say sike right now
    }

    public void incrementLifetime() {
        lifetime++;
    }

    public void destroy() {
        stand.remove();
        text = null;
        entity = null;
        type = null;
        lifetime = 0;
    }

}
