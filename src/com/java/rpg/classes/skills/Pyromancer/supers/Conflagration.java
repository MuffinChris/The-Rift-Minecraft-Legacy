package com.java.rpg.classes.skills.Pyromancer.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

need to add to super skill list
public class Conflagration extends Skill {

    private Main main = Main.getInstance();

    private double damage = 150;

    private double apscale = 0.5;

    private int range = 4;

    private int tickrate = 2;

    public Conflagration() {
        super("Conflagration", 0, 30, 0, 0, "%player% has shot a fireball!", "TOGGLE-CAST");
        setToggleMana(5);
        setToggleTicks(tickrate);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSpew flame from your hand traveling &e" + range + "&f blocks."));
        desc.add(Main.color("&fIt deals &b" + getDmg(p) + " &fdamage per second"));
        desc.add(Main.color("&fand ignites them for 1 second."));
        return desc;
    }

    public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * apscale;
    }

    public void cast(Player p) {
        super.cast(p);
    }

}
