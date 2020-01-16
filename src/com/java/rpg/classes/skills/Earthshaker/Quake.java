package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;

public class Quake extends Skill {
	
    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 8;
    
    private double APscale = .7;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
	
    public Quake() {
    	super("Quake", 200, 200, 0, 3, "player has shot a fireball", "CAST");
    }
    
    public void cast(Player p) {
    	super.cast(p);
    	p.getWorld().spawnParticle(Particle.BLOCK_DUST, p.getLocation(), 40, 4, 1, 4);
    	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.0F);
    	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
            }
            ent.setKiller(p);
            spellDamage(p, ent, getDmg(p));
    	}
    }
}