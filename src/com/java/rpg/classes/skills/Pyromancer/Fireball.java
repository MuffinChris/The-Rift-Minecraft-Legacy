package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

//FIREBALLS ARE TREATED AS ATTACK - BECAUSE THE EVENTS! eSETDMG - fixed? (not rly doesnt make sense)
//ALSO SPELLS ARE KNOCKING BACK, DISABLE THAT (set knockback resist whenever hitting spell) - fixed?
//FLAMETORNADO TOO FAST.

public class Fireball extends Skill implements Listener {


    // FIREBALL ISSUE: Damage is inconsistent with event setup, perhaps lightEnts should do all damage;

    private Main main = Main.getInstance();

    private double damage = 120;

    private double apscale = 0.4;

    private int range = 4;

    public Fireball() {
        super("Fireball", 50, 3 * 20, 0, 0, "%player% has shot a fireball!", "CAST");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fShoot a flaming projectile that travels for &e" + range + "&f seconds."));
        desc.add(Main.color("&fIt deals &b" + getDmg(p) + " &fdamage to nearby foes"));
        desc.add(Main.color("&fand ignites them for 5 seconds."));
        //setDescription(desc);
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public void cast(Player p) {
        super.cast(p);
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/
        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = (Arrow) p.getWorld().spawn(loc, Arrow.class);
        arrow.setCustomName("Fireball:" + getDmg(p));
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
            final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        player.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04,null, true);
                        if (arrow.isOnGround() || arrow.isDead()) {
                            lightEntities(arrow, p, arrow.getLocation(), Double.valueOf(arrow.getCustomName().replace("Fireball:", "")));
                            arrow.remove();
                            arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04,null, true);
                        }
                    }
                }
            }, 1, 1);
            final int task2 = scheduler.scheduleSyncRepeatingTask(main, new Runnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        player.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04,null, true);
                        if (arrow.isOnGround() || arrow.isDead()) {
                            lightEntities(arrow, p, arrow.getLocation(), Double.valueOf(arrow.getCustomName().replace("Fireball:", "")));
                            arrow.remove();
                            arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 20, 0.04, 0.04, 0.04, 0.02,null, true);
                        }
                    }
                }
            }, 0, 1);

            scheduler.scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run(){
                    if (!(arrow.isOnGround() || arrow.isDead())) {
                        lightEntities(arrow, p, arrow.getLocation(), Double.valueOf(arrow.getCustomName().replace("Fireball:", "")));
                        arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 20, 0.04, 0.04, 0.04, 0.04,null, true);
                    }
                    scheduler.cancelTask(task);
                    scheduler.cancelTask(task2);
                    arrow.remove();
                }
            }, 20 * range);
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
    }

    @EventHandler
    public void projectileHe (ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow a = (Arrow) e.getEntity();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Fireball:") && a.getShooter() instanceof Player) {
                Player shooter = (Player) a.getShooter();
                if (e.getHitEntity() instanceof Entity) {
                    lightEntities(e.getHitEntity(), shooter, e.getHitEntity().getLocation(), Double.valueOf(a.getCustomName().replace("Fireball:", "")));
                    e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getHitEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08,null, true);
                } else {
                    lightEntities(e.getEntity(), shooter, e.getEntity().getLocation(), Double.valueOf(a.getCustomName().replace("Fireball:", "")));
                    e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation(), 15, 0.08, 0.08, 0.08, 0.08,null, true);

                }
                a.remove();
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onHit (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && !(e.getEntity() instanceof ArmorStand)) {
            Arrow a = (Arrow) e.getDamager();
            if (a.getCustomName() instanceof String && a.getCustomName().contains("Fireball:") && a.getShooter() instanceof Player) {
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

    public void lightEntities(Entity e, Player caster, Location loc, double damage) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 25, 0.12, 0.12, 0.12, 0.12,null, true);
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
                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

    /*public boolean damageEntities(Location loc, Player caster, Entity e) {
        Main.so("de");
        for (LivingEntity ent : loc.getNearbyLivingEntities(0.5)) {
            Main.so("pp");
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    return false;
                }
            }
            ent.setKiller(caster);
            ent.damage(Double.valueOf(e.getCustomName().replace("Fireball:", "")));
            Main.so("" + Double.valueOf(e.getCustomName().replace("Fireball:", "")));
            ent.setFireTicks(60);
            e.remove();
            ent.getWorld().spawnParticle(Particle.LAVA, e.getLocation(), 50, 0.04, 0.04, 0.04, 0.04);
            return true;
        }
        return false;
    }*/
}
