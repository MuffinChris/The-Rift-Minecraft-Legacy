package com.java.rpg.classes.skills.Wanderer;

import com.java.Main;
import com.java.rpg.classes.Skill;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Leap extends Skill {


        // Allows access to global methods in the Main class
        private Main main = Main.getInstance();


        // Put variables here
        private int duration = 5;


        // Constructor, when the plugin loads the skills get their information set up
        public Leap() {
            super("Leap", 0, 0 * 20, 0, 0, "unimportant", "CAST");
            List<String> desc = new ArrayList<>();
            desc.add(Main.color("&bActive:"));
            desc.add(Main.color("&fJump forward!"));
            setDescription(desc);
        }

        public void cast(Player p) {
            super.cast(p);


            // do whatever you want here with the player

            // Try pushing the player forwards and upwards...
        }
}
