package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.party.Party;
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

public class Combust extends Skill implements Listener {

    private Main main = Main.getInstance();
    private double damage = 50;
    private double damagePerEntity = 5;
    private ElementalStack eDmg = new ElementalStack(0, 0, 0, 100, 0);
    private ElementalStack eDmgPerEntity = new ElementalStack(0, 0, 0, 25, 0);

    private double apscale = 0.4;
    private double apscalePerEnt = 0.1;

    private int range = 10;

    private int travelRange = 8;

    public Combust() {
        super("Combust", 200, 60 * 20, 50, 35, SkillType.CAST, null, Material.DRAGON_BREATH);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Launch an explosive projectile."));
        desc.add(Main.color("&7It explodes at the nearest target hit for &b" + getMagicDmg(p) + " &7+ " + getEDmg(p).getFancyNumberFire() + " &7damage."));
        desc.add(Main.color("&7A second delayed explosion hits a huge radius of &e" + range + "&7."));
        desc.add(Main.color("&7It deals &b" + getMagicDmg(p) + " &7+ " + getEDmg(p).getFancyNumberFire() + " &7damage + &b" + getMagicDmgPerEntity(p) + " &7+ " + getEDmgPerEntity(p).getFancyNumberFire()));
        desc.add(Main.color("&7damage per nearby entity."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Launch an explosive projectile."));
        desc.add(Main.color("&7It explodes at the nearest target hit for &b" + damage + " &7+ " + eDmg.getFancyNumberFire() + " &7damage."));
        desc.add(Main.color("&7A second delayed explosion hits a huge radius of &e" + range + "&7."));
        desc.add(Main.color("&7It deals &b" + damage + " &7+ " + eDmg.getFancyNumberFire() + " &7damage + &b" + damagePerEntity + " &7+ " + eDmgPerEntity.getFancyNumberFire()));
        desc.add(Main.color("&7damage per nearby entity."));
        return desc;
    }

    public double getMagicDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }
    public double getMagicDmgPerEntity(Player p) {
        return damagePerEntity + main.getRP(p).getAP() * apscalePerEnt;
    }

    public ElementalStack getEDmg(Player p) {
        return eDmg;
    }

    public ElementalStack getEDmgPerEntity(Player p) {
        return eDmgPerEntity;
    }

    public void cast(Player p) {
        super.cast(p);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
        CombustProjectile proj = new CombustProjectile(p, range, 5, getMagicDmg(p), getMagicDmgPerEntity(p), getEDmg(p), getEDmgPerEntity(p), travelRange);
    }

    public void warmup(Player p) {
        super.warmup(p);
        p.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, p.getLocation().clone().subtract(new Vector(0, 0.1, 0)), 1, 0.1, 0.01, 0.1, 0.1, null, true);
    }

}
