package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.classes.StatusValue;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Adrenaline extends Skill {

    private Main main = Main.getInstance();

    private int duration = 5;

    public Adrenaline() {
        super("Adrenaline", 30, 13 * 20, 0, 0, "%player% has shot a fireball!", "CAST");
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&fSpeed yourself up for &a" + duration + " &fseconds!"));
        setDescription(desc);
    }

    public void cast(Player p) {
        super.cast(p);
        main.getRP(p).getWalkspeed().getStatuses().add(new StatusValue("Adrenaline", 5, duration * 20, System.currentTimeMillis(), false));
        main.getRP(p).updateWS();
    }

}
