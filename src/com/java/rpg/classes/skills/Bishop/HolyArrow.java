package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.classes.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
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

public class HolyArrow extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    private double damage = 800;
    private double duration = 40;
    
    private int arrowCount = 40;
    private int range = 10;
    
    private double APscale = .4;
    private double ADscale = 1;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale ) / arrowCount;
    }

    public HolyArrow() {
        super("HolyArrow", 200, 160, 30, 6, "%player% has shot a fireball!", "CAST");
        DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fChannel holy powers, spraying holy light"));
        desc.add(Main.color("&fat your enemies. Deal" + damage + "&f damage over"));
        desc.add(Main.color("&fover a duration of" + duration / 20 + "&f seconds"));
        setDescription(desc);
    }
	
    public void cast(Player p) {
    	super.cast(p);
    	new BukkitRunnable() {
            int count = 0;
            double xOffset;
            double yOffset;
            double zOffset;
            public void run() {
            	xOffset = 0.1 * Math.random();
            	yOffset = 0.1 * Math.random();
            	zOffset = 0.1 * Math.random();
                createArrow(p, xOffset, yOffset, zOffset);
                count++;
                if (count >= arrowCount) {
                    cancel();
                }
            }
        }.runTaskTimer(main, 0L, 1L);
    }
    
    public void createArrow(Player p, double xOffset, double yOffset, double zOffset) {
        super.cast(p);
        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = (Arrow) p.getWorld().spawn(loc, Arrow.class);
        arrow.setCustomName("HArrow:" + getDmg(p));
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            arrow.setShooter(p);
            arrow.setVelocity(p.getEyeLocation().getDirection().multiply(2).add(new Vector(xOffset, yOffset, zOffset)));
            arrow.setBounce(false);
            arrow.setGravity(false);
            arrow.setKnockbackStrength(0);
            arrow.setSilent(true);
            final BukkitScheduler scheduler = Bukkit.getScheduler();
            final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        player.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04);
                        if (arrow.isOnGround() || arrow.isDead()) {
                        	eatMyBean(arrow, p, arrow.getLocation(), getDmg(p));
                            arrow.remove();
                            arrow.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                        }
                    }
                }	
            }, 1, 1);
            final int task2 = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        player.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04);
                        if (arrow.isOnGround() || arrow.isDead()) {
                            eatMyBean(arrow, p, arrow.getLocation(), getDmg(p));
                            arrow.remove();
                            arrow.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                        }
                    }
                }
            }, 0, 1);

            scheduler.scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run(){
                    if (!(arrow.isOnGround() || arrow.isDead())) {
                    	eatMyBean(arrow, p, arrow.getLocation(), getDmg(p));
                        arrow.getWorld().spawnParticle(Particle.END_ROD, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
                    }
                    scheduler.cancelTask(task);
                    scheduler.cancelTask(task2);
                    arrow.remove();
                }
            }, 20 * range);
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1.0F, 1.0F);
    }

    @EventHandler
    public void projectileHe (ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Harrow:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                if (e.getHitEntity() instanceof Entity) {
                    e.getEntity().getWorld().spawnParticle(Particle.END_ROD, e.getHitEntity().getLocation(), 50, 0.04, 0.08, 0.08, 0.08);
                    eatMyBean(a, shooter, a.getLocation(), getDmg(shooter));
                } else {
                	eatMyBean(a, shooter, a.getLocation(), getDmg(shooter));
                }
                a.remove();
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && !(e.getEntity() instanceof ArmorStand)) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("HArrow:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                if (e.getEntity() instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
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
                    }                }
                a.remove();
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }
    
    public void eatMyBean(Entity e, Player caster, Location loc, double damage) {
        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 50, 0.04, 0.12, 0.12, 0.12);
        for (LivingEntity ent : loc.getNearbyLivingEntities(.25)) {
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
            spellDamage(caster, ent, damage, new ElementalStack(0, 0, 0, 50, 0, 0));
            ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
        }
    }
}