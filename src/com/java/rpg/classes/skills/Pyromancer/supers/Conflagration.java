package com.java.rpg.classes.skills.Pyromancer.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Conflagration extends Skill {

    private Main main = Main.getInstance();

    private double damage = 150;

    private double apscale = 0.5;

    private int range = 4;

    private int tickrate = 1;

    public Conflagration() {
        super("Conflagration", 0, 20, 0, 5, "%player% has shot a fireball!", "TOGGLE-CAST");
        setToggleMana(0);
        setToggleTicks(tickrate);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fYou become immune to fire damage."));
        desc.add(Main.color("&fAll blocks in a &a" + range + " &fradius are lit on fire."));
        desc.add(Main.color("&fEvery time someone is damaged by the fire, gain AP..."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public void cast(Player p) {
        super.cast(p);
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 500000, 1));

        p.setFireTicks(0);
        return false;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        p.setFireTicks(0);
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    public int toggleInit(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 500000, 1));

        return super.toggleInit(p);
    }

}
