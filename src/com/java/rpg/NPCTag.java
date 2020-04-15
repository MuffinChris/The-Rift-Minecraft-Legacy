package com.java.rpg;

import com.java.Main;
import com.java.rpg.holograms.Hologram;
import com.java.rpg.classes.RPGPlayer;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class NPCTag implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onNPC(NPCSpawnEvent e) {
        if (!Main.getInstance().getPC().containsKey(e.getNPC().getUniqueId())) {
            Main.getInstance().getPC().put(e.getNPC().getUniqueId(), new RPGPlayer());
        }
        List<Player> players = new ArrayList<>(e.getNPC().getEntity().getWorld().getNearbyPlayers(e.getNPC().getEntity().getLocation(), 24));
        Main.getInstance().getNpcTags().put(e.getNPC().getEntity(), new Hologram(e.getNPC().getEntity(), e.getNPC().getEntity().getLocation().add(new Vector(0, e.getNPC().getEntity().getHeight() - 0.2, 0)), "&7NPC", Hologram.HologramType.HOLOGRAM, players));
        Main.getInstance().getHolos().add(Main.getInstance().getNpcTags().get(e.getNPC().getEntity()));
    }

    @EventHandler
    public void npcDeath (NPCDespawnEvent e) {
        if (Main.getInstance().getPC().containsKey(e.getNPC().getUniqueId())) {
            Main.getInstance().getPC().remove(e.getNPC().getUniqueId());
        }
        if (main.getNpcTags().containsKey(e.getNPC().getEntity())) {
            main.getHolos().remove(main.getNpcTags().get(e.getNPC().getEntity()));
            main.getNpcTags().get(e.getNPC().getEntity()).destroy();
            main.getNpcTags().remove(e.getNPC().getEntity());
        }
    }

    @EventHandler
    public void npcDeath (NPCDeathEvent e) {
        if (main.getNpcTags().containsKey(e.getNPC().getEntity())) {
            main.getHolos().remove( main.getNpcTags().get(e.getNPC().getEntity()));
            main.getNpcTags().get(e.getNPC().getEntity()).destroy();
            main.getNpcTags().remove(e.getNPC().getEntity());
        }
    }

}
