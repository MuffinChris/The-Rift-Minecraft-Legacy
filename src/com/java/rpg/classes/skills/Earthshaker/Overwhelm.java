package com.java.rpg.classes.skills.Earthshaker;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
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

class Earthquake extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double damage = 200;
    private int range = 3;

    public Earthquake(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        super(name, manaCost, cooldown, warmup, level, flavor, type);
    }

    public void target(Player p, LivingEntity t) {
        super.target(p, t);

    }
}
	