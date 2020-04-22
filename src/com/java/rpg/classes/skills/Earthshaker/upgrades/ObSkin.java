package com.java.rpg.classes.skills.Earthshaker.upgrades;

import com.java.Main;
import com.java.rpg.classes.Skill;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ObSkin extends Skill implements Listener {
	
    private Main main = Main.getInstance();
    
    public ObSkin() {
    	super("Obsidian Skin", 100, 20, 0, 5, SkillType.TARGET, null, Material.OBSIDIAN);
    	DecimalFormat df = new DecimalFormat("#");
        List<String> desc = new ArrayList<>();
        setDescription(desc);
    }

    public List<String> getDescription(Player p) {
        return new ArrayList<>();
    }
    public List<String> getDescription() {
        return new ArrayList<>();
    }
}