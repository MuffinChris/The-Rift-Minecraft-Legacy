package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.skills.Pyromancer.upgrades.Conflagration;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WorldOnFire extends Skill implements Listener {

    private Main main = Main.getInstance();

    private static double empowered = 1.25;

    public static double getEmp() {
        return empowered;
    }

    private int range = 6;
    private int ramp = 5;
    private int initRamp = 20;
    private int maxramp = 500;
    private double magicDamage = 50;
    private double apscale = 0.5;
    private ElementalStack eDmg = new ElementalStack(0, 0, 0, 200, 0);

    public double getMagicDmg(Player p) {
        return magicDamage + main.getRP(p).getAP() * apscale;
    }
    public ElementalStack getEDmg(Player p) {
        return eDmg;
    }

    public WorldOnFire() {
        super("WorldOnFire", 50, 10 * 20, 0, 5, SkillType.PASSIVE_TOGGLE, new Conflagration(), Material.FLINT_AND_STEEL);
        setPassiveTicks(20);
        setToggleTicks(10);
        setToggleMana(10);
        setToggleCooldown(10);
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
        desc.add(Main.color("&7On end, nearby enemies take &b" + getMagicDmg(p) + " &7+ " + getEDmg(p).getFancyNumberFire() + " &7damage!"));
        return desc;
    }

    public List<String> getDescription() {
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
        desc.add(Main.color("&7On end, nearby enemies take &b" + magicDamage + " &7+ " + eDmg.getFancyNumberFire() + " &7damage!"));
        return desc;
    }

    public void passive(Player p) {
        super.passive(p);
    }

    public int toggleInit(Player p) {
        main.getRP(p).getBonusAP().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), initRamp, 0, 0, true));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        return super.toggleInit(p);
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(p.getLocation(), p, range)) {
            if (!(Math.abs(ent.getLocation().getY() - p.getLocation().getY()) < 3.5)) {
                continue;
            }
            ent.setKiller(p);
            if (ent.getFireTicks() > 0) {
                if (!(main.getRP(p).getBonusAP().getValue() >= maxramp)) {
                    main.getRP(p).getBonusAP().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), ramp, 0, 0, true));
                }
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
            p.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0, 0.01, 0.01, 0.01 ,null, true);
            p.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0, 0.01, 0.01, 0.01 ,null, true);
        }
        return false;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        main.getRP(p).getBonusAP().clearBasedTitleDurationless(getName(), p);
        endDamage(p);
    }

    public void endDamage(Player caster) {
        makeCircle(caster, 8, 64, 0.2);
        makeCircle(caster, 7, 64, 0.2);
        makeCircle(caster, 6, 64, 0.2);
        makeCircle(caster, 5, 48, 0.2);
        makeCircle(caster, 4, 32, 0.2);
        makeCircle(caster, 2, 32, 0.2);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
        for (LivingEntity ent : main.getNearbyLivingEntitiesTargetValid(caster.getLocation(), caster, range)) {
            if (!(Math.abs(ent.getLocation().getY() - caster.getLocation().getY()) < 3.5)) {
                continue;
            }
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            spellDamage(caster, ent, new PhysicalStack(), getEDmg(caster), getMagicDmg(caster), 0);
            caster.getWorld().spawnParticle(Particle.LAVA, ent.getEyeLocation(), 15, 0.2, 0.2, 0.2, 0.2,null, true);
        }
    }

    public void makeCircle(Player caster, double radius, double cnt, double height) {
        for (double alpha = 0; alpha < Math.PI; alpha+= Math.PI/cnt) {
            Location loc = caster.getLocation();
            Location firstLocation = loc.clone().add( radius * Math.cos( alpha ), height, radius * Math.sin( alpha ) );
            Location secondLocation = loc.clone().add( radius * Math.cos( alpha + Math.PI ), height, radius * Math.sin( alpha + Math.PI ) );
            caster.getWorld().spawnParticle( Particle.FLAME, firstLocation, 1, 0.01, 0.01, 0.01, 0.01, null, true);
            caster.getWorld().spawnParticle( Particle.FLAME, secondLocation, 1, 0.01, 0.01, 0.01, 0.01,null, true);
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
