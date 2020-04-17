package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldOnFire extends Skill implements Listener {

    private Main main = Main.getInstance();

    private static double empowered = 1.25;

    public static double getEmp() {
        return empowered;
    }

    private int range = 6;
    private int hp = 5;
    private int mana = 5;
    private int ramp = 5;
    private int initRamp = 20;
    private int maxramp = 300;
    private Map<Player, Integer> amp;
    private double enddamage = 50;
    private double apscale = 0.5;

    public double getDmg(Player p) {
        return enddamage + main.getRP(p).getAP() * apscale;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        //main.getPC().get(p.getUniqueId()).setPStrength((main.getPC().get(p.getUniqueId()).getPStrength() - initRamp - amp.get(p)));
        //amp.remove(p);
        main.getRP(p).getBonusAP().clearBasedTitle(getName(), p);
        //main.getRP(p).getPStrength2().clearBasedTitle(getName(), p);
        endDamage(p);
    }

    public void makeCircle(Player caster, double radius, double cnt, double height) {
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/cnt) {
            Location loc = caster.getLocation();
            Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height, radius * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height, radius * Math.sin( alpha + Math.PI ) );
            //Location firstLocation = loc.clone().add( Math.cos( alpha ), Math.sin( alpha ) + 1, Math.sin( alpha ) );
            //Location secondLocation = loc.clone().add( Math.cos( alpha + Math.PI ), Math.sin( alpha ) + 1, Math.sin( alpha + Math.PI ) );
            caster.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0.01, 0.01, 0.01, 0.01, null, true);
            caster.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0.01, 0.01, 0.01, 0.01,null, true);
        }
    }

    public void endDamage(Player caster) {
        makeCircle(caster, 8, 64, 0.2);
        makeCircle(caster, 7, 64, 0.2);
        makeCircle(caster, 6, 64, 0.2);
        makeCircle(caster, 5, 48, 0.2);
        makeCircle(caster, 4, 32, 0.2);
        makeCircle(caster, 2, 32, 0.2);
        //caster.getWorld().spawnParticle(Particle.DRIP_LAVA, caster.getLocation(), 20, 0.25, 0, 0, 0,null, true);
        //caster.getWorld().spawnParticle(Particle.LAVA, caster.getEyeLocation(), 45, 0, 0.2, 0.2, 0.2);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
        for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(caster.getLocation(), caster, range)) {
            if (!(Math.abs(ent.getLocation().getY() - caster.getLocation().getY()) < 3.5)) {
                continue;
            }
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            spellDamage(caster, ent, getDmg(caster), new ElementalStack(0, 0, 25, 100, 0));
            caster.getWorld().spawnParticle(Particle.LAVA, ent.getEyeLocation(), 15, 0.2, 0.2, 0.2, 0.2,null, true);
        }
    }

    public boolean toggleCont(Player p) {
        DecimalFormat df = new DecimalFormat("#.##");
        if (!super.toggleCont(p)) {
            return false;
        }
        /*if (p.getFireTicks() > 0) {
            if (amp.get(p)+ramp <= maxramp) {
                amp.replace(p, amp.get(p) + ramp);
                main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + ramp);
            }
        }*/
        for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(p.getLocation(), p, range)) {
            if (!(Math.abs(ent.getLocation().getY() - p.getLocation().getY()) < 3.5)) {
                continue;
            }
            ent.setKiller(p);
            if (ent.getFireTicks() > 0) {
                if (!(main.getRP(p).getBonusAP().getValue() >= maxramp)) {
                    main.getRP(p).getBonusAP().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), ramp, 0, 0, true));
                }

                /*if (amp.get(p)+ramp <= maxramp) {
                    amp.replace(p, amp.get(p) + ramp);
                    main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + ramp);
                }*/
            } else {
                drawLine(p, ent);
                ent.setFireTicks(100);
                ent.getWorld().spawnParticle(Particle.FLAME, ent.getEyeLocation().subtract(new Vector(0, ent.getHeight() * 0.4, 0)), 35, 0.04, 0.04, 0.04, 0.04,null, true);
                ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.25F, 0.5F);
            }
        }
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/64) {
            Location loc = p.getLocation();
            Location firstLocation = loc.clone().add( range * Math.cos( alpha ), 0.1, range * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( range * Math.cos( alpha + Math.PI ), 0.1, range * Math.sin( alpha + Math.PI ) );
            //Location firstLocation = loc.clone().add( Math.cos( alpha ), Math.sin( alpha ) + 1, Math.sin( alpha ) );
            //Location secondLocation = loc.clone().add( Math.cos( alpha + Math.PI ), Math.sin( alpha ) + 1, Math.sin( alpha + Math.PI ) );
            p.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01 ,null, true);
            p.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01 ,null, true);
        }
        return false;
    }

    public int toggleInit(Player p) {
        //DecimalFormat df = new DecimalFormat("#.##");
        /*amp.put(p, 0);
        main.getPC().get(p.getUniqueId()).setPStrength(main.getPC().get(p.getUniqueId()).getPStrength() + initRamp);*/
        main.getRP(p).getBonusAP().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), initRamp, 0, 0, true));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        return super.toggleInit(p);
    }

    public WorldOnFire() {
        super("WorldOnFire", 50, 3 * 20, 0, 5, "%player% has conjured a burst of Flame!", "TOGGLE-PASSIVE-CAST", Material.FLINT_AND_STEEL);
        setPassiveTicks(20);
        setToggleTicks(10);
        setToggleMana(10);
        amp = new HashMap<>();
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&aPassive:"));
        desc.add(Main.color("&7Fire damage you deal"));
        desc.add(Main.color("&7is amplified by &b" + empowered + "x &7damage."));
        desc.add(Main.color(""));
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Actively ignite nearby enemies."));
        desc.add(Main.color("&7Grant yourself &a" + initRamp + " &7AP."));
        desc.add(Main.color("&7Gain &a" + ramp + " &7AP every tick for each nearby"));
        desc.add(Main.color("&7entity on fire. Max AP is &a" + maxramp + "&7."));
        desc.add(Main.color("&7When ended, nearby enemies take &b" + getDmg(p) + " &7damage!"));
        return desc;
    }

    public void passive(Player p) {
        super.passive(p);
        /*if (p.getFireTicks() > 0) {
            passiveIgnite(p);
        }
        if (p.getWorld().getTime() % 10 == 0) {
            sapEntities(p);
        }*/
    }

    public void passiveIgnite(Player caster) {
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
            if (!(Math.abs(ent.getLocation().getY() - caster.getLocation().getY()) < 3)) {
                continue;
            }
            ent.setKiller(caster);
            if (ent.getFireTicks() == 0) {
                drawLine(caster, ent);
            }
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
        }
    }

    // Event listen fire damage, ignite nearby
    // On server startup loop through RPGPlayer passives list. Get skill from name in list and run passiveCast.
    // Check if player still has that passive, changing classes CLEAR PASSIVE.

    public void cast(Player p) {
        super.cast(p);
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/

        /*p.getLineOfSight(null, range).forEach(block -> block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1));
        p.getLineOfSight(null, range).forEach(block -> damageEntities(block, p));*/
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_BURN, 1.0F, 1.0F);
    }

    public void igniteEntities(Player caster) {
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
            if (!(Math.abs(ent.getLocation().getY() - caster.getLocation().getY()) < 3)) {
                continue;
            }
            ent.setKiller(caster);
            if (ent.getFireTicks() == 0) {
                drawLine(caster, ent);
            }

            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
        }
    }

    public void sapEntities(Player caster) {
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
            if (!(Math.abs(ent.getLocation().getY() - caster.getLocation().getY()) < 3)) {
                continue;
            }
            if (ent.getFireTicks() > 0) {
                if (!caster.isDead() && !ent.isDead()) {
                    ent.getWorld().spawnParticle(Particle.FLAME, ent.getLocation(), 15, 0.04, 0.04, 0.04, 0.04,null, true);
                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.25F, 0.5F);

                    Skill.healTarget(caster, hp);
                    drawLine(caster, ent);
                    //caster.getLocation().toVector().subtract(ent.getLocation().toVector()).forEach(block -> igniteBlocks(block, caster));
                    main.getPC().get(caster.getUniqueId()).setMana(main.getPC().get(caster.getUniqueId()).getCMana() + mana);
                }
            }
        }
    }

    public void drawLine(Player caster, LivingEntity ent) {
        Location point1 = new Location(ent.getWorld(), ent.getEyeLocation().getX(), (ent.getEyeLocation().getY() - 0.7), ent.getEyeLocation().getZ());
        Location point2 = new Location(caster.getWorld(), caster.getEyeLocation().getX(), (caster.getEyeLocation().getY() - 0.7), caster.getEyeLocation().getZ());
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(0.3);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            caster.getWorld().spawnParticle(Particle.FLAME, p1.getX(), p1.getY(), p1.getZ(), 1, 0.01, 0.01, 0.01, 0.01,null, true);
            length += 0.3;
        }
    }

}
