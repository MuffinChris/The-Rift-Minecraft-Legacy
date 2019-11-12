package com.java.rpg.classes.skills.Earthshaker;

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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class Earthquake extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 3;
    
    public void target(Player p, LivingEntity t) {
    	super.target(p,t);
    	
    }
	