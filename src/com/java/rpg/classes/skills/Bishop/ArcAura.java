package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ArcAura extends Skill {
	
	private Main main = Main.getInstance();
	private double range = 6;
	
	public ArcAura() {
		super("Arcane Aura", 0, 0, 0, 3, SkillType.PASSIVE, null, null);
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
                if (main.getPM().getParty(pl) instanceof Party && !main.getPM().getParty(pl).getPvp()) {
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