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
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Combust extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double damage = 100;

    private double damagePerEntity = 20;

    private double apscale = 0.4;
    private double apscalePerEnt = 0.1;

    private int range = 12;

    public Combust() {
        super("Combust", 200, 60 * 20, 30, 25, "%player% has shot a fireball!", "CAST");
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
        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = (Arrow) p.getWorld().spawn(loc, Arrow.class);
        arrow.setCustomName("Combust:" + getDmg(p));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            arrow.setShooter(p);
            arrow.setVelocity(p.getEyeLocation().getDirection().multiply(2));
            arrow.setBounce(false);
            arrow.setGravity(false);
            arrow.setKnockbackStrength(0);
            arrow.setSilent(true);
            final BukkitScheduler scheduler = Bukkit.getScheduler();
            final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        player.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04, null, true);
                        if (arrow.isOnGround() || arrow.isDead()) {
                            lightEntities(arrow, p, arrow.getLocation(), getDmg(p));
                            arrow.remove();
                            arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04, null, true);
                        }
                    }
                }
            }, 1, 1);
        }
    }

    public void explode(Player caster, Location loc) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 50, 0.04, 0.12, 0.12, 0.12,null, true);
        for (LivingEntity ent : loc.getNearbyLivingEntities(1.1)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            new BukkitRunnable() {
                public void run() {
                    if (ent.getHealth() < damage && !(ent instanceof Player)) {
                        ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
                    }
                    spellDamage(caster, ent, damage);
                    ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
                    ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
                    ent.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, ent.getLocation(), 1, 0.12, 0.12, 0.12, 0.12,null, true);
                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

}
