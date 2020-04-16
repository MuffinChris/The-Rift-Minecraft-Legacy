package com.java.rpg.classes.skills.Earthshaker.supers;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.*;
import com.java.rpg.party.Party;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ObSkin extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    
    public ObSkin() {
    	super("Obsidian Skin", 100, 20, 0, 5, "%player% has shot a fireball!", "CAST-TARGET");
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }
}