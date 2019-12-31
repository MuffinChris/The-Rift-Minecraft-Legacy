package com.java.communication;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor {

	private Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getLabel().equalsIgnoreCase("r") || cmd.getLabel().equalsIgnoreCase("reply")) {
				if (main.getMsg().getPinf(p).getReply() != null && main.getMsg().getPinf(p).getReply().isOnline()) {
					if (args.length == 0) {
						Main.msg(p, "Usage: /r <msg>");
					}
					if (args.length >= 1) {
						Player t = main.getMsg().getPinf(p).getReply();
						if (t != null && t.isOnline()) {
							String output = "";
							for (String s : args) {
								output += s;
								output += " ";
							}
							Main.msg(p, "&7[&eYou &8\u00BB &e" + t.getName() + "&7] &f" + output);
							Main.msg(t, "&7[&e" + p.getName() + " &8\u00BB &eYou&7] &f" + output);
							main.getMsg().getPinf(p).setReply(t);
							main.getMsg().getPinf(t).setReply(p);
						} else {
							Main.msg(p, "&cYou have no one to reply to.");
						}
					}
				} else {
					Main.msg(p, "&cYou have no one to reply to.");
				}
			} else {
				if (args.length <= 1) {
					Main.msg(p, "Usage: /msg <player> <msg>");
				}
				if (args.length >= 2) {
					if (Bukkit.getPlayer(args[0]) instanceof Player) {
						Player t = Bukkit.getPlayer(args[0]);
						String output = "";
						int index = 0;
						for (String s : args) {
							if (index != 0) {
								output+=s;
								output+=" ";
							}
							index++;
						}
						Main.msg(p, "&7[&eYou &8\u00BB &e" + t.getName() + "&7] &f" + output);
						Main.msg(t, "&7[&e" + p.getName() + " &8\u00BB &eYou&7] &f" + output);
						main.getMsg().getPinf(p).setReply(t);
						main.getMsg().getPinf(t).setReply(p);
					} else {
						Main.msg(p, "&cInvalid Player.");
					}
				}
			}
		} else {
			if (args.length <= 1) {
				Main.so("Usage: /msg <player> <msg>");
			}
			if (args.length >= 2) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					String output = "";
					int index = 0;
					for (String s : args) {
						if (index != 0) {
							output+=s;
							output+=" ";
						}
						index++;
					}
					Main.so("&7[&eCONSOLE (YOU) &8\u00BB &e" + t.getName() + "&7] &f" + output);
					Main.msg(t, "&7[&eCONSOLE" + " &8\u00BB &eYou&7] &f" + output);
				} else {
					Main.so("&cInvalid Player.");
				}
			}
		}
		return false;
	}
	
}
