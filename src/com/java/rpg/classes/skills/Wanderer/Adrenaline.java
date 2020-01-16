package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Adrenaline extends Skill {

    private Main main = Main.getInstance();

    private int duration = 5;

    public Adrenaline() {
        super("Adrenaline", 30, 13 * 20, 0, 0, "%player% has shot a fireball!", "CAST");
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSpeed yourself up for &a" + duration + " &fseconds!"));
        return desc;
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue(getName() + ":" + p.getName(), 12, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateWS();
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1.0F, 1.0F);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1.0F, 1.0F);
    }

}
