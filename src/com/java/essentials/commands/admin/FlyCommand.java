package com.java.essentials.commands.admin;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.isOp() || p.hasPermission("core.mod")) {
				if (args.length == 0) {
					if (p.getAllowFlight()) {
						p.setAllowFlight(false);
						Main.msg(p, "&cFlying Disabled.");
					} else {
						p.setAllowFlight(true);
						Main.msg(p, "&aFlying Enabled.");
					}
				}
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("speed")) {
						if (args.length == 1) {
							Main.msg(p, "&fUsage: /fly speed <1-10>");
						} else {
							if (Integer.parseInt(args[1]) >= 1 && Integer.parseInt(args[1]) <= 10) {
								p.setFlySpeed(Float.parseFloat(args[1]) / 10);
								Main.msg(p, "&aSet your fly speed to " + args[1]);
							} else {
								Main.msg(p, "&fUsage: /fly speed <1-10>");
							}
						}
					} else {
						if (Bukkit.getPlayer(args[0]) instanceof Player) {
							Player target = Bukkit.getPlayer(args[0]);
							if (target.getAllowFlight()) {
								target.setAllowFlight(false);
								Main.msg(p, "&cFlying Disabled for " + target.getName() + ".");
							} else {
								target.setAllowFlight(true);
								Main.msg(p, "&aFlying Enabled for " + target.getName() + ".");
							}
						} else {
							Main.msg(p, "&cInvalid Player.");
						}
					}
				}
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target.getAllowFlight()) {
						target.setAllowFlight(false);
						Main.so("&cFlying Disabled for " + target.getName() + ".");
					} else {
						target.setAllowFlight(true);
						Main.so("&aFlying Enabled for " + target.getName() + ".");
					}
				}
			} else {
				Main.so("&fUsage: /fly <player>");
			}
		}
		return false;
	}

}
