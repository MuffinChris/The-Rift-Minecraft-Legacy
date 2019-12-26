package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import com.java.rpg.party.Party;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Rest extends Skill {

    private Main main = Main.getInstance();

    private double hps = 50;
    private double apscale = 0.5;
    private int fps = 1;

    public Rest() {
        super("Rest", 10, 3 * 20, 0, 2, "%player% has shot a fireball!", "TOGGLE-CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bToggle:"));
        desc.add(Main.color("&fWhile resting, gain &c" + hps + " &fhealth per second"));
        desc.add(Main.color("&fand &a" + fps + " &fhunger points per second."));
        setDescription(desc);
        setToggleTicks(20);
        setToggleMana(20);
    }

    public void cast(Player p) {
        super.cast(p);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_DOLPHIN_SWIM, 1.0F, 1.0F);
    }

    public int toggleInit(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999, 255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 255));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 255));
        return super.toggleInit(p);
    }

    public void toggleEnd(Player p) {
        super.toggleEnd(p);
        p.removePotionEffect(PotionEffectType.WEAKNESS);
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        p.removePotionEffect(PotionEffectType.SLOW);
        p.removePotionEffect(PotionEffectType.JUMP);
    }

    public boolean toggleCont(Player p) {
        if (!super.toggleCont(p)) {
            return false;
        }
        Skill.healTarget(p, hps + apscale * main.getRP(p).getAP());
        p.setFoodLevel(Math.min(20, p.getFoodLevel() + fps));
        p.setSaturation(p.getSaturation() + 0.1F);
        return false;
    }
}
