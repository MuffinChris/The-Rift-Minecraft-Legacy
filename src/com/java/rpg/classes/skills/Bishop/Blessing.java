package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import com.java.rpg.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.entity.*;

public class Blessing extends Skill {
	
    private int duration = 600;
    private int range = 8;
    
    private Main main = Main.getInstance();
    
    public Blessing() {
        super("Blessing", 400, 160, 0, 0, "%player% has shot a fireball!", "CAST");
        DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fBless your allies, increasing their"));
        desc.add(Main.color("&fresistances and damage"));
        setDescription(desc);
    }
	
    public void cast(Player p) {
    	super.cast(p);
    	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                    	pl.getWorld().spawnParticle(Particle.END_ROD, pl.getEyeLocation(), 10, .1, .1, .1);
                        main.getRP(p).getBlessing().getStatuses().add(new StatusValue("Blessing:" + p.getName(), 1, duration, System.currentTimeMillis(), false));
                    }
                }
            }
    	}
    }
}