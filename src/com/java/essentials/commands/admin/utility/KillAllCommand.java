package com.java.essentials.commands.admin.utility;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KillAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 0) {
                    Main.msg(p, "");
                    Main.msg(p, "&8» &c&lKilled all entities in your world.");
                    Main.msg(p, "");
                    new BukkitRunnable() {
                        public void run() {
                            for (Entity e : p.getWorld().getEntities()) {
                                if (e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ArmorStand)) {
                                    LivingEntity ent = (LivingEntity) e;
                                    ent.remove();
                                }
                            }
                        }
                    }.runTaskLaterAsynchronously(Main.getInstance(), 1L);
                } else {
                    Main.msg(p, "Usage: /killall (kills all entities in your world)");
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        } else {
            Main.so("&cCannot be used from console due to World Argument.");
        }
        return false;
    }

}
