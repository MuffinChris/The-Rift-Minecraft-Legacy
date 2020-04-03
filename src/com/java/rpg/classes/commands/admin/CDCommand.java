package com.java.rpg.classes.commands.admin;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CDCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("core.admin")) {
                if (args.length == 0) {
                    RPGPlayer pl = main.getPC().get(p.getUniqueId());
                    pl.getCooldowns().clear();
                    p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 50, 1, 1, 1);
                } else if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) instanceof Player) {
                        Player t = Bukkit.getPlayer(args[0]);
                        RPGPlayer pl = main.getPC().get(t.getUniqueId());
                        pl.getCooldowns().clear();
                        t.getWorld().spawnParticle(Particle.PORTAL, t.getLocation(), 50, 1, 1, 1);
                    } else {
                        Main.msg(p, "&cInvalid Target");
                    }
                } else {
                    Main.msg(p, "Usage: /cd <player>");
                }
            } else {
                Main.msg(p, Main.getInstance().noperm);
            }
        }
        return false;
    }

}
