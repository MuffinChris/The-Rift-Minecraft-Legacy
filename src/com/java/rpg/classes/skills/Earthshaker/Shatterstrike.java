package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import org.bukkit.*;
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

public class Shatterstrike extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    private double damage = 120;
    private int range = 4;

    public Shatterstrike(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        super(name, manaCost, cooldown, warmup, level, flavor, type);
    }

    public void cast(Player p)
    {
    	Location loc = p.getLocation();
    	//Location c1 = loc.
    }
}