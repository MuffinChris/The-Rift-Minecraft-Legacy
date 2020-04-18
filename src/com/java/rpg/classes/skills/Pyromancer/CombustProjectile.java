package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class CombustProjectile {

    private Main main = Main.getInstance();

    double range = 0;
    Player p = null;
    Player caster = null;
    double upDownRange = 0;
    double dmgB = 0;
    double dmgPerEnt = 0;
    int travelRange = 0;

    public CombustProjectile(Player p, double range, double upDownRange, double dmgB, double dmgPerEnt, int travelRange) {
        this.range = range;
        this.p = p;
        this.caster = p;
        this.upDownRange = upDownRange;
        this.dmgB = dmgB;
        this.dmgPerEnt = dmgPerEnt;
        this.travelRange = travelRange;

        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = (Arrow) p.getWorld().spawn(loc, Arrow.class);
        arrow.setCustomName("Combust:" + dmgB);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            arrow.setShooter(p);
            arrow.setVelocity(p.getEyeLocation().getDirection().multiply(1.0));
            arrow.setBounce(false);
            arrow.setGravity(false);
            arrow.setKnockbackStrength(0);
            arrow.setSilent(true);
        }
            final BukkitScheduler scheduler = Bukkit.getScheduler();
            final int task = scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
                @Override
                public void run() {
                    if (!arrow.isDead()) {
                        arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.5, 0.5, 0.5, 0.1, null, true);
                    }
                    if (!arrow.doesBounce() &&( arrow.isOnGround() || arrow.isDead() || arrow.isInBlock())) {
                        explodeSingle(p, arrow.getLocation(), Double.valueOf(arrow.getCustomName().replace("Combust:", "")));
                        arrow.remove();
                        arrow.setBounce(true);
                    }
                }
            }, 1, 1);

            scheduler.scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run(){
                    if (!p.isOnline() && scheduler.isCurrentlyRunning(task)) {
                        scheduler.cancelTask(task);
                    }
                    if (p.isOnline() && !(arrow.isOnGround() || arrow.isDead())) {
                        explodeSingle(p, arrow.getLocation(), Double.valueOf(arrow.getCustomName().replace("Combust:", "")));
                    }
                    scheduler.cancelTask(task);
                    arrow.remove();
                }
            }, (20 * travelRange));
    }

    public void explodeSingle(Player caster, Location loc, double damage) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 10, 0.1, 0.1, 0.1, 0.05,null, true);
        loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 1.0F);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0.12, 0.12, 0.12, 0.12,null, true);
        for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(loc, caster, 1.1)) {
            /*new BukkitRunnable() {
                public void run() {*/
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            Skill.spellDamageStatic(caster, ent, damage, new ElementalStack(0, 0, 0, 100, 0));
            ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
                /*}
            }.runTaskLater(Main.getInstance(), 1L);*/
        }

        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_AMBIENT, 1.0F, 1.0F);
        new BukkitRunnable() {
            public void run() {
                explodeMulti(caster, loc, damage);
            }
        }.runTaskLater(Main.getInstance(), 20L);
    }

    public void explodeMulti(Player caster, Location loc, double damage) {
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 3.0F, 1.0F);
        loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 3.0F, 1.0F);
        makeCircle(loc.clone().add(new Vector(0, 0, 0)), range, 32, 0);

        new BukkitRunnable() {
            public void run() {
                makeCircle(loc.clone().add(new Vector(0, 0, 0)), range - 1, 24, 0);

            }
        }.runTaskLater(Main.getInstance(), 5L);
        new BukkitRunnable() {
            public void run() {
                makeCircle(loc.clone().add(new Vector(0, 0, 0)), range - 2, 16, 0);
            }
        }.runTaskLater(Main.getInstance(), 8L);
        new BukkitRunnable() {
            public void run() {
                makeCircle(loc.clone().add(new Vector(0, 0, 0)), range - 4, 16, 0);
            }
        }.runTaskLater(Main.getInstance(), 11L);
        new BukkitRunnable() {
            public void run() {
                makeCircle(loc.clone().add(new Vector(0, 0, 0)), range - 6, 16, 0);
            }
        }.runTaskLater(Main.getInstance(), 14L);
        new BukkitRunnable() {
            public void run() {
                loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3.0F, 1.0F);
                loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 10, 3.0, 3.0, 3.0, 0.2,null, true);
                int numEnts = 0;
                for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(loc, caster, range)) {
                    if (Math.abs(ent.getLocation().getY() - loc.getY()) > upDownRange) {
                        continue;
                    }
                    numEnts++;
                }
                for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(loc, caster, range)) {
                    if (Math.abs(ent.getLocation().getY() - loc.getY()) > upDownRange) {
                        continue;
                    }
                    double dmg = dmgB + dmgPerEnt * numEnts;
                    /*new BukkitRunnable() {
                        public void run() {*/

                    makeCircle(loc.clone().add(new Vector(0, 0, 0)), range, 16, 0);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0.01, 0.01, 0.01, 0.01,null, true);
                    ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
                    Skill.spellDamageStatic(caster, ent, dmg, new ElementalStack(0, 0, 0, 300, 0));
                    ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
                        /*}
                    }.runTaskLater(Main.getInstance(), 1L);*/
                }
            }
        }.runTaskLater(Main.getInstance(), 16L);
    }

    public void makeCircle(Location loc, double radius, double cnt, double height) {
        for (double h = -upDownRange; h <= upDownRange; h+=0.5) {
            for (double alpha = 0; alpha < Math.PI; alpha += Math.PI / cnt) {
                Location firstLocation = loc.clone().add(radius * Math.cos(alpha), h, radius * Math.sin(alpha));
                Location secondLocation = loc.clone().add(radius * Math.cos(alpha + Math.PI), h, radius * Math.sin(alpha + Math.PI));
                //Location firstLocation = loc.clone().add( Math.cos( alpha ), Math.sin( alpha ) + 1, Math.sin( alpha ) );
                //Location secondLocation = loc.clone().add( Math.cos( alpha + Math.PI ), Math.sin( alpha ) + 1, Math.sin( alpha + Math.PI ) );
                loc.getWorld().spawnParticle(Particle.FLAME, firstLocation, 1, 0.01, 0.01, 0.01, 0.01, null, true);
                loc.getWorld().spawnParticle(Particle.FLAME, secondLocation, 1, 0.01, 0.01, 0.01, 0.01, null, true);
            }
        }
    }

}
