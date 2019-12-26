package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusObject;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Miracle extends Skill {
	
    private Main main = Main.getInstance();

    private int range = 8;
    private int duration = 600;

    public Miracle() {
        super("Miracle", 400, 2400, 0, 3, "%player% has shot a fireball!", "CAST", 0, 0, 0, 0);
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
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
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