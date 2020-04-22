package com.java.rpg.classes.skills.Bishop.upgrades;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;

import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Genesis extends Skill {
	
    private double damage = 400;
    private int range = 16;
    
    private Main main = Main.getInstance();
    public Genesis() {
        super("Genesis", 200, 160, 30, 6, SkillType.CAST, null, null);
        DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSummon holy powers to smite enemies around you"));
        desc.add(Main.color("&fin a large range, dealing massive damage"));
        desc.add(Main.color("&fenemies hit will be inflicted with weakness"));
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
                if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
                ent.setKiller(p);
                ent.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(80,2));
                
                spellDamage(p, ent, new PhysicalStack(), new ElementalStack(0, 0, 0, 50, 0), damage, 0);
            }
            ent.setKiller(p);
            ent.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(80,2));
            spellDamage(p, ent, new PhysicalStack(), new ElementalStack(0, 0, 0, 50, 0), damage, 0);
    	}
    }
    
    public void warmup(Player p) {
    	super.warmup(p);
    	p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(30,2));
    	p.addPotionEffect(PotionEffectType.GLOWING.createEffect(30,2));
    }
    
    public void summonBeam(Location loc) {
    	Location location = loc;
    	for(int i = 0; i < 50; i++) {
    			location.add(new Vector(0,1,0));
    			location.getWorld().spawnParticle(Particle.END_ROD, location, 10, 2, .5, 2);
    	}
    }
}