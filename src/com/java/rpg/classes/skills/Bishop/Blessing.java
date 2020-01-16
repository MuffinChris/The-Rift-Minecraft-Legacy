package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.holograms.Hologram;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blessing extends Skill {
	
	private Main main = Main.getInstance();
	private double range = 6;
	
	public Blessing() {
		super("Blessing", 0, 0, 0, 3, "%player% has shot a fireball!", "PASSIVE");
		DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bPassive:"));
        desc.add(Main.color("&fEvery 5 seconds restore mana, split amongst your party"));
        desc.add(Main.color("&fThis mana is equal to 25% of your missing mana"));
        setDescription(desc);
		
	}
    public void passive(Player p) {
        super.passive(p);
        if (p.getWorld().getTime() % 100 == 0) {
        	mRestore(p);
        }
    }

    public void mRestore(Player p) {
    	ArrayList<Player> party = new ArrayList<Player>();
    	for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player pl = (Player) ent;
                if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        party.add(pl);
                    }
                }
            }
            //take 25% of the player's missing mana and divide it amongst all of the party, including the player
            //How do you get the players max mana????
    	}
    }
}