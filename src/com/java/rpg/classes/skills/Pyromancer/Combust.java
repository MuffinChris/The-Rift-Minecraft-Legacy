package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Combust extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double damage = 100;

    private double damagePerEntity = 20;

    private double apscale = 0.4;
    private double apscalePerEnt = 0.1;

    private int range = 10;

    private int travelRange = 8;

    public Combust() {
        super("Combust", 200, 60 * 20, 50, 25, "%player% has shot a fireball!", "CAST");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fLaunch an explosive projectile."));
        desc.add(Main.color("&fIt explodes at the nearest target hit for &b" + getDmg(p) + " &fdamage."));
        desc.add(Main.color("&fA second delayed explosion hits a huge radius of &e" + range + "&f."));
        desc.add(Main.color("&fIt deals &b" + getDmg(p) + " &fdamage + &b" + getDmgPerEntity(p)));
        desc.add(Main.color("&fdamage per nearby entity."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public double getDmgPerEntity(Player p) {
        return damagePerEntity + main.getRP(p).getAP() * apscalePerEnt;
    }

    public void cast(Player p) {
        super.cast(p);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        CombustProjectile proj = new CombustProjectile(p, range, 5, getDmg(p), getDmgPerEntity(p), travelRange);
    }

    public void warmup(Player p) {
        super.warmup(p);
        p.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, p.getLocation().clone().subtract(new Vector(0, 0.1, 0)), 1, 0.1, 0.01, 0.1, 0.1, null, true);
    }

    @EventHandler
    public void projectileHe (ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Combust:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                /*if (e.getHitEntity() instanceof Entity) {
                    explodeSingle(shooter, e.getHitEntity().getLocation(), Double.valueOf(a.getCustomName().replace("Combust:", "")));
                    //e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getHitEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08,null, true);
                } else {
                    explodeSingle(shooter, e.getEntity().getLocation(), Double.valueOf(a.getCustomName().replace("Combust:", "")));
                    //e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08,null, true);
                }*/
                a.remove();
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && !(e.getEntity() instanceof ArmorStand)) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Combust:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                        if (main.getPM().getParty(p).getPlayers().contains(a.getShooter())) {
                            a.remove();
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (p.equals(shooter)) {
                        a.remove();
                        e.setCancelled(true);
                        return;
                    }
                    //((CraftPlayer)p).getHandle().getDataWatcher().set(new DataWatcherObject<>(10, DataWatcherRegistry.b),0);
                }
                /*if (e.getEntity() instanceof LivingEntity) {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    lightEntities(e.getEntity(), shooter, e.getEntity().getLocation(), Double.valueOf(a.getCustomName().replace("Fireball:", "")));
                    ent.getWorld().spawnParticle(Particle.LAVA, ent.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                }*/
                a.remove();
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }

}
