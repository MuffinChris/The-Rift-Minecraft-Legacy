package com.java.essentials;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.isOp() || p.hasPermission("core.mod")) {
				if (args.length <= 1) {
					Main.msg(p, "Usage: /speed <fly/walk> <1-10> <player>");
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("walk")) {
						if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
							p.setWalkSpeed(Float.parseFloat(args[1]) / 10);
							Main.msg(p, "&aSet your walk speed to " + args[1]);
						} else {
							Main.msg(p, "&fUsage: /speed walk <1-10>");
						}
					}
					if (args[0].equalsIgnoreCase("fly")) {
						if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
							p.setFlySpeed(Float.parseFloat(args[1]) / 10);
							Main.msg(p, "&aSet your fly speed to " + args[1]);
						} else {
							Main.msg(p, "&fUsage: /speed fly <1-10>");
						}
					}
				}
				if (args.length >= 3) {
					if (Bukkit.getPlayer(args[2]) instanceof Player) {
						Player t = Bukkit.getPlayer(args[2]);
						if (args[0].equalsIgnoreCase("walk")) {
							if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
								t.setWalkSpeed(Float.parseFloat(args[1]) / 10);
								Main.msg(p, "&aSet " + t.getName()  + "'s walk speed to " + args[1]);
							} else {
								Main.msg(p, "&fUsage: /speed walk <1-10> <player>");
							}
						}
						if (args[0].equalsIgnoreCase("fly")) {
							if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
								t.setFlySpeed(Float.parseFloat(args[1]) / 10);
								Main.msg(p, "&aSet " + t.getName()  + "'s fly speed to " + args[1]);
							} else {
								Main.msg(p, "&fUsage: /speed fly <1-10> <player>");
							}
						}
					} else {
						Main.msg(p, "&cInvalid Target");
					}
				}
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		} else {
			if (args.length <= 2) {
				Main.so("Usage: /speed <fly/walk> <1-10> <player>");
			}
			if (args.length >= 3) {
				if (Bukkit.getPlayer(args[2]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[2]);
					if (args[0].equalsIgnoreCase("walk")) {
						if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
							t.setWalkSpeed(Float.parseFloat(args[1]) / 10);
							Main.so("&aSet " + t.getName()  + "'s walk speed to " + args[1]);
						} else {
							Main.so("&fUsage: /speed walk <1-10> <player>");
						}
					}
					if (args[0].equalsIgnoreCase("fly")) {
						if (Float.parseFloat(args[1]) >= 1 && Float.parseFloat(args[1]) <= 10) {
							t.setFlySpeed(Float.parseFloat(args[1]) / 10);
							Main.so("&aSet " + t.getName()  + "'s fly speed to " + args[1]);
						} else {
							Main.so("&fUsage: /speed fly <1-10> <player>");
						}
					}
				} else {
					Main.so("&cInvalid Target");
				}
			}
		}
		return false;
	}
}
