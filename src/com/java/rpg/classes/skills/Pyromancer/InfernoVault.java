package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfernoVault extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double vaultdamage = 50;
    private double landdamage = 100;
    private int range = 3;

    public InfernoVault() {
        super("InfernoVault", 100, 14 * 20, 0, 4, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fCombust the location at your feet, dealing &b" + vaultdamage + " &fdamage."));
        desc.add(Main.color("&fThe explosion launches yourself into the air."));
        desc.add(Main.color("&fLanding creates another explosion, dealing &b" + landdamage + " &fdamage."));
        setDescription(desc);
    }

    public void cast(Player p) {
        super.cast(p);
        vaultDamage(p);

        new BukkitRunnable() {
            double rem = -0.1;
            public void run() {
                Vector v = new Vector(p.getLocation().getDirection().getX() * 0.45, 0.2 + rem, p.getLocation().getDirection().getZ() * 0.45);
                p.setVelocity(v);
                rem+=0.1;
                if (rem > 0.4) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 2L);

        p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1);
        p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation(), 45, 0.2, 0.2, 0.2, 0.2);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);

        BukkitScheduler sched = Bukkit.getScheduler();
        if (!main.getCM().getFallMap().containsKey(p.getUniqueId())) {
            main.getCM().getFallMap().put(p.getUniqueId(), sched.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                public void run() {
                    p.getWorld().spawnParticle(Particle.LAVA, p.getLocation(), 15, 0, 0.1, 0.1, 0.1);
                    if (p.isOnGround()) {
                        if (main.getCM().getFall().contains(p.getUniqueId())) {
                            p.setFallDistance(0);
                            main.getCM().getFall().remove(p.getUniqueId());
                            if (main.getCM().getFallMap().containsKey(p.getUniqueId())) {
                                Bukkit.getScheduler().cancelTask(main.getCM().getFallMap().get(p.getUniqueId()));
                                main.getCM().getFallMap().remove(p.getUniqueId());
                            }
                            landDamage(p);
                            p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1);
                            p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation(), 45, 0, 0.2, 0.2, 0.2);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        }
                    }
                }
            }, 0L, 1L));
        }
        new BukkitRunnable() {
            public void run() {
                if (!main.getCM().getFall().contains(p.getUniqueId())) {
                    main.getCM().getFall().add(p.getUniqueId());
                }
            }
        }.runTaskLater(Main.getInstance(), 5L);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void fallDamageSafe (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player p = (Player) e.getEntity();
            if (main.getCM().getFall().contains(p.getUniqueId())) {
                e.setCancelled(true);
                main.getCM().getFall().remove(p.getUniqueId());
                if (main.getCM().getFallMap().containsKey(p.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(main.getCM().getFallMap().get(p.getUniqueId()));
                    main.getCM().getFallMap().remove(p.getUniqueId());
                }
                landDamage(p);
                p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 1);
                p.getWorld().spawnParticle(Particle.LAVA, p.getEyeLocation(), 45, 0, 0.2, 0.2, 0.2);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
            }
        }
    }

    public void landDamage(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            if (ent.getHealth() < landdamage && !(ent instanceof Player)) {
                ent.setFireTicks(Math.min(60 + ent.getFireTicks(), 200));
            }
            spellDamage(caster, ent, landdamage);
            ent.setFireTicks(Math.min(60 + ent.getFireTicks(), 200));
        }
    }

    public void vaultDamage(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            if (ent.getHealth() < vaultdamage && !(ent instanceof Player)) {
                ent.setFireTicks(Math.min(60 + ent.getFireTicks(), 200));
            }
            spellDamage(caster, ent, vaultdamage);
            ent.setFireTicks(Math.min(60 + ent.getFireTicks(), 200));
        }
    }
}
