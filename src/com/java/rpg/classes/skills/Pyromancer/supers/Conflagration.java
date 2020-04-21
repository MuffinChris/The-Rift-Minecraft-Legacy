package com.java.rpg.classes.skills.Pyromancer.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Conflagration extends Skill {

    private Main main = Main.getInstance();

    private double damage = 150;

    private double apscale = 0.5;

    private int range = 4;

    private int tickrate = 1;

    public Conflagration() {
        super("Conflagration", 0, 20, 0, 5, SkillType.TOGGLE, null, Material.FLINT_AND_STEEL);
        setToggleMana(0);
        setToggleTicks(tickrate);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7You become immune to fire damage."));
        desc.add(Main.color("&7All blocks in a &a" + range + " &7radius are lit on fire."));
        desc.add(Main.color("&7Every time someone is damaged by the fire, gain AP..."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7You become immune to fire damage."));
        desc.add(Main.color("&7All blocks in a &a" + range + " &7radius are lit on fire."));
        desc.add(Main.color("&7Every time someone is damaged by the fire, gain AP..."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 500000, 1, false, false));

        p.setFireTicks(0);
        return false;
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        p.setFireTicks(0);
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    public int toggleInit(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 500000, 1, false, false));

        return super.toggleInit(p);
    }

}
