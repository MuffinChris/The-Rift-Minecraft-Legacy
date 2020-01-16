package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class StoneSkin extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    final int maxStacks = 8;
    private int stacks = 0;
    private double damage = 120;
    private int range = 4;
    private int cooldown = 2 * 20;
	
    public StoneSkin() {
    	super("Stone Skin", 0, 0, 0, 5, " ", "PASSIVE");
    	setPassiveTicks(20);
    }
    
    public void passive(Player p) {
    	//every second or so you should get a stack
    	//when you hit or cast a spell it should eat up the stacks and deal extra damage (use bigdamage)
    	//then set stacks to 0 and put on the cooldown
    	
    	//2nd part of passive just reduce damage if it exceeds a threshold
    }
    
    public double bigDamage(int stacks) {
    	stacks = 0;
    	return damage + 40 * Math.pow(1.2, stacks);
    }
    
    public int damageReduction(int inp) {
    	if(inp > 0) {
    	    return 0;
    	    // reduce damage by like 60% against attacks that would deal more than like 200 hp idk should be damage reduction against big damage
        }
    	return 1;
    }
    
}