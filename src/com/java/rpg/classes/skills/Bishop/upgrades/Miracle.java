package com.java.rpg.classes.skills.Bishop.upgrades;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Miracle extends Skill {
	
    private Main main = Main.getInstance();

    private int range = 8;
    private int duration = 600;

    public Miracle() {
        super("Miracle", 400, 2400, 0, 3, SkillType.CAST, null, null);
        DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fBless an ally, protecting them from death for 10 seconds."));
        desc.add(Main.color("&fIf you ally would fatal damage in that time, the buff will be consumed,"));
        desc.add(Main.color("&fand the player will instead take 0 damage and be healed to full health."));
        setDescription(desc);
    }
	
    public void cast(Player p) {
    	Location bean  = p.getLocation();
    	Vector sightLine = p.getEyeLocation().getDirection();
    	for(int i = 0; i < 32; i++) {
    		bean.add(sightLine.multiply(.25));
    		if(AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH(p, bean))
    			break;
    	}
    }
    
    public boolean AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH(Player p, Location loc) {
    	for (LivingEntity ent : loc.getNearbyLivingEntities(0.5)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(p)) {
                    	main.getRP(p).getAutoLife().getStatuses().add(new StatusValue("AutoLife:" + p.getName(), 1, duration, System.currentTimeMillis(), false));
                    	p.getLocation().getWorld().playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                    	return true;
                    }
                }
                if (pl.equals(p)) {
                    continue;
                }
            }
        }
    	return false;
    }
   
}