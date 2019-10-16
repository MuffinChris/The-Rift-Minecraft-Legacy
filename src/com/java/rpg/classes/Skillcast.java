package com.java.rpg.classes;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Skillcast implements Listener {

    private Main main = Main.getInstance();

    //private Map<UUID, List<Integer>> lastSlot = new HashMap<>();

    /*public Skillcast() {
        new BukkitRunnable() {
            public void run() {

            }
        }.runTaskTimer(Main.getInstance(), 10, 2);
    }*/

    @EventHandler
    public void skillBar (PlayerSwapHandItemsEvent e) {
        if (!e.getPlayer().isSneaking() && main.getRP(e.getPlayer()).getPClass().getSkills().size() > 0) {
            e.setCancelled(true);
            main.getRP(e.getPlayer()).getBoard().toggleSkillbar();
            main.getRP(e.getPlayer()).getBoard().updateSkillbar();
            //main.getRP(e.getPlayer()).setIdleSlot(e.getPlayer().getInventory().getHeldItemSlot());
            e.getPlayer().getInventory().setHeldItemSlot(main.getRP(e.getPlayer()).getIdleSlot());
        }
    }

    @EventHandler
    public void skillBarCast (PlayerItemHeldEvent e) {
        if (main.getRP(e.getPlayer()).getBoard().getSkillbar()) {
            int idle = main.getRP(e.getPlayer()).getIdleSlot();
            if (e.getNewSlot() == idle) {
                return;
            }
            int slot = e.getNewSlot();
            /*if (lastSlot.containsKey(e.getPlayer().getUniqueId()) && slot == (int) (lastSlot.get(e.getPlayer().getUniqueId()).toArray()[0])) {
                return;
            }*/
            if (slot > main.getRP(e.getPlayer()).getPClass().getSkills().size()) {
                return;
            }
            e.setCancelled(true);
            /*List<Integer> list = new ArrayList<>();
            list.add(e.getPreviousSlot());
            list.add(slot);
            lastSlot.put(e.getPlayer().getUniqueId(), e.getPreviousSlot());*/
            if (slot < idle) {
                e.getPlayer().performCommand("skill " + ((Skill) main.getRP(e.getPlayer()).getPClass().getSkills().toArray()[slot]).getName());
            } else {
                e.getPlayer().performCommand("skill " + ((Skill) main.getRP(e.getPlayer()).getPClass().getSkills().toArray()[slot - 1]).getName());
            }

        }/* else {
            ItemStack i = e.getPlayer().getInventory().getItemInMainHand();
            if (i.getLore().contains("Skill Item")) {
                e.getPlayer().getInventory().remove(i);
            }
        }*/
    }

}
