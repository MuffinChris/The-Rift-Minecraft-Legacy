package com.java.essentials;

import com.java.Main;
import com.java.rpg.classes.MobEXP;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DummyCommand implements CommandExecutor, Listener {

    @EventHandler
    public void dummy(EntityDamageEvent e) {
        /*if (e.getEntity() instanceof Villager && e.getEntity() instanceof LivingEntity) {
            if (e.getEntity() instanceof Entity && e.getEntity().isCustomNameVisible() && e.getEntity().getCustomName().contains("HP:")) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                new BukkitRunnable() {
                    public void run() {
                        if (!ent.isDead()) {
                            ent.setCustomName(Main.color("&cHP: &f" + ent.getHealth()));
                        }
                    }
                }.runTaskLater(Main.getInstance(), 5L);
            }
        }*/
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                Entity e = p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
                LivingEntity ent = (LivingEntity) e;
                ent.setAI(false);
                ent.setCanPickupItems(false);
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000000);
                ent.setHealth(100000);
                MobEXP.setLevel(ent, 50);
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        }
        return false;
    }

}
