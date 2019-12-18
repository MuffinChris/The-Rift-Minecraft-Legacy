package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Genesis extends Skill {
	
    private double damage = 400;
    private int range = 16;
    
    private Main main = Main.getInstance();

    // There needs to be some effect, like a very thick beam of light coming down.
    // Perimeter of beacon beams or some shit in a circle
    // Lightning bolt will work, but it will overlap with thunderlord
    public Genesis() {
        super("Genesis", 200, 160, 30, 6, "%player% has shot a fireball!", "CAST");
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
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
                    if (main.getPM().getParty(pl).getPlayers().contains(p)) {
                        continue;
                    }
                }
            }
            ent.setKiller(p);
            ent.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(80,2));
            spellDamage(p, ent, damage);
    	}
    }
    
    public void warmup(Player p) {
    	super.warmup(p);
    	p.addPotionEffect(PotionEffectType.LEVITATION.createEffect(30,2));
    	p.addPotionEffect(PotionEffectType.GLOWING.createEffect(30,2));
    }
}