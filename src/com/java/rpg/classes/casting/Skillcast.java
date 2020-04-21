package com.java.rpg.classes.casting;

import com.java.Main;
import com.java.rpg.classes.Skill;
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

    @EventHandler
    public void skillBar (PlayerSwapHandItemsEvent e) {
        if ((!main.getRP(e.getPlayer()).getToggleOffhand() || !e.getPlayer().isSneaking()) && main.getRP(e.getPlayer()).getSkillsCastable().size() > 0) {
            e.setCancelled(true);
            main.getRP(e.getPlayer()).getBoard().toggleSkillbar();
            main.getRP(e.getPlayer()).getBoard().updateSkillbar();
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

            if (slot > main.getRP(e.getPlayer()).getSkillsCastable().size()) {
                return;
            }
            e.setCancelled(true);

            List<Skill> pSkills = new ArrayList<>();
            for (Skill s : main.getRP(e.getPlayer()).getPClass().getSkills()) {
                if (main.getRP(e.getPlayer()).getSkillLevels().get(s.getName()) == 0) {
                    pSkills.add(s);
                } else {
                    pSkills.add(s.getUpgradedSkill());
                }
            }
            if (slot < idle) {
                e.getPlayer().performCommand("skill " + ((Skill) pSkills.toArray()[slot]).getName());
            } else {
                e.getPlayer().performCommand("skill " + ((Skill) pSkills.toArray()[slot - 1]).getName());
            }

        }
    }

}
