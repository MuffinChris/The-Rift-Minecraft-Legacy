package com.java.rpg.player;

import com.destroystokyo.paper.Title;
import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomDeath implements Listener {

    private Main main = Main.getInstance();

    public List<Player> recents = new ArrayList<>();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void cancelPD (PlayerDeathEvent e) {
        e.setCancelled(true);
        if (!recents.contains(e.getEntity())) {
            doDeath(e.getEntity());
            recents.add(e.getEntity());
            new BukkitRunnable() {
                public void run() {
                    recents.remove(e.getEntity());
                }
            }.runTaskLater(Main.getInstance(), 20);
        }
    }

    public void doDeath(Player p) {
        RPGPlayer rp = main.getRP(p);
        p.sendTitle(new Title(Main.color("&c&lYOU DIED"), Main.color(""), 5, 80, 5));
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
        rp.giveExpFromSource(p, p.getLocation(), rp.getMaxExp() * -0.3, "");
        main.setMana(p, 0);
        if (p.getBedSpawnLocation() == null) {
            p.teleport(p.getWorld().getSpawnLocation());
        } else {
            p.teleport(p.getBedSpawnLocation());
        }
    }


}
