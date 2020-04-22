package com.java.rpg.classes.skills.Bishop;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Heal extends Skill {

	private Main main = Main.getInstance();

	private double damage = 200;
	private double healVal = 800;
	
	private double APscale = 1;
	
	private int range = 8;
	
	public double getDmg(Player p) {
        return damage + main.getRP(p).getAP() * APscale;
    }
    
    public double getHeal(Player p) {
        return healVal + main.getRP(p).getAP() * APscale;
    }

	// You should not be able to die with a healer spamming heal off cd on you,
	// unless you have some healing debuff, or you get oneshot
	public Heal() {
		super("Heal", 80, 20, 0, 3, SkillType.CAST, null, null);
		DecimalFormat df = new DecimalFormat("#");
		List<String> desc = new ArrayList<>();
		desc.add(Main.color("&bActive:"));
		desc.add(Main.color("&fHeal players in your party around you in a medium radius"));
		desc.add(Main.color("&fThe heal damages undead enemies."));
		setDescription(desc);
	}

	public void cast(Player p) {
		super.cast(p);
		for (LivingEntity ent : p.getLocation().getNearbyLivingEntities(range)) {
			if (ent instanceof ArmorStand) {
				continue;
			}
			if (ent instanceof Zombie || ent instanceof Skeleton || ent instanceof Stray
					|| ent instanceof WitherSkeleton || ent instanceof Wither || ent instanceof Drowned
					|| ent instanceof Husk || ent instanceof PigZombie || ent instanceof ZombieVillager
					|| ent instanceof Phantom) {
				spellDamage(p, ent, new PhysicalStack(), new ElementalStack(0, 0, 0, 50, 0), getDmg(p), 0 );
				p.spawnParticle(Particle.VILLAGER_ANGRY, p.getEyeLocation(), 5, 0.5, 0.5, 0.5);
			}
			if (ent instanceof Player) {
				Player pl = (Player) ent;
				if (main.getPM().getParty(pl) != null && !main.getPM().getParty(pl).getPvp()) {
					if (main.getPM().getParty(pl).getPlayers().contains(p)) {

						pl.spawnParticle(Particle.HEART, pl.getEyeLocation(), 5, 0.5, 0.5, 0.5);

						healTarget(pl, getHeal(p));
					}
				}
			}
		}
	}

}