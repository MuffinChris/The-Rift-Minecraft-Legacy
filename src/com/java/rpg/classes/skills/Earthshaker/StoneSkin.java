package com.java.rpg.classes.skills.Earthshaker;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.utility.StatusValue;
import java.util.List;
import java.util.ArrayList;

public class StoneSkin extends Skill implements Listener {
	
    private Main main = Main.getInstance();

    final int maxStacks = 8;
    private double damage = 120;
    private int cooldown = 2 * 20;
    
    private double APscale = .7;
    private double ADscale = 2;
    
    public double getDmg(Player p) {
        return ( damage + main.getRP(p).getAP() * APscale + main.getRP(p).getAD() * ADscale );
    }
    
    public StoneSkin() {
        super("Stone Skin", 0, 0, 0, 5, " ", "PASSIVE");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&7Hotdog"));
        return desc;
    }
    
    public void passive(Player p) {
    	super.passive(p);
        main.getRP(p).getStoneSkinDR().getStatuses().add(new StatusValue("StoneSkinDR" + p.getName(), 1, 1, System.currentTimeMillis(), true));
        main.getRP(p).getStoneSkinS().getStatuses().add(new StatusValue("StoneSkinS" + p.getName(), 0, 1, System.currentTimeMillis(), true));
        if (p.getWorld().getTime() % 100 == 0 && getStacks(p) < maxStacks) {
            stack(p);
        }
    }
    
    public void stack(Player p) {
    	StatusValue stackcurrent = main.getRP(p).getStoneSkinS().getStatuses().get(0);
    	main.getRP(p).getStoneSkinS().scrub();
        main.getRP(p).getStoneSkinS().getStatuses().add(new StatusValue("StoneSkinS" + p.getName(), stackcurrent.getValue() + 1, 0, System.currentTimeMillis(), true));
    }
    
    public int getStacks(Player p) {
        return main.getRP(p).getStoneSkinS().getStatuses().get(0).getValue();
    }
    
    public void stacksCD(Player p) {
    	main.getRP(p).getStoneSkinS().scrub();
    	if(main.getRP(p).getStoneSkinCD().getStatuses().size() == 0) {
    		main.getRP(p).getStoneSkinCD().getStatuses().add(new StatusValue("StoneSkinCd" + p.getName(), 0, cooldown, System.currentTimeMillis(), false));
    	}
    	main.getRP(p).getStoneSkinS().getStatuses().add(new StatusValue("StoneSkinS" + p.getName(), 0, 1, System.currentTimeMillis(), true));
    }
}