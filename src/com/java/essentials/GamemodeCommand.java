package com.java.essentials;


import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("core.mod")) {
				if (cmd.getName().equalsIgnoreCase("gmc")) {
					if (args.length == 0) {
						p.setGameMode(GameMode.CREATIVE);
						Main.msg(p, "&aSet Gamemode to Creative!");
					} else {
						if (args.length == 1) {
							if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
								Player target = (Player) Bukkit.getPlayer(args[0]);
								target.setGameMode(GameMode.CREATIVE);
								Main.msg(p, "&aSet Gamemode of " + target.getName() + " to Creative!");
							} else {
								Main.msg(p, "&cInvalid Player.");
							}
						} else {
							p.sendMessage("Usage: /gmc <player>");
						}
					}
				}
				if (cmd.getName().equalsIgnoreCase("gms")) {
					if (args.length == 0) {
						p.setGameMode(GameMode.SURVIVAL);
						Main.msg(p, "&aSet Gamemode to Survival!");
					} else {
						if (args.length == 1) {
							if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
								Player target = (Player) Bukkit.getPlayer(args[0]);
								target.setGameMode(GameMode.SURVIVAL);
								Main.msg(p, "&aSet Gamemode of " + target.getName() + " to Survival!");
							} else {
								Main.msg(p, "&cInvalid Player.");
							}
						} else {
							p.sendMessage("Usage: /gms <player>");
						}
					}
				}
				if (cmd.getName().equalsIgnoreCase("gmss")) {
					if (args.length == 0) {
						p.setGameMode(GameMode.SPECTATOR);
						Main.msg(p, "&aSet Gamemode to Spectator!");
					} else {
						if (args.length == 1) {
							if (Bukkit.getPlayerExact(args[0]) instanceof Player) {
								Player target = (Player) Bukkit.getPlayer(args[0]);
								target.setGameMode(GameMode.SPECTATOR);
								Main.msg(p, "&aSet Gamemode of " + target.getName() + " to Spectator!");
							} else {
								Main.msg(p, "&cInvalid Player.");
							}
						} else {
							p.sendMessage("Usage: /gmss <player>");
						}
					}
				}
			} else {
				Main.msg(p, Main.getInstance().noperm);
			}
		}
		return false;
	}

}
