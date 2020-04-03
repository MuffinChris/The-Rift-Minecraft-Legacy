package com.java.essentials.commands;

import com.java.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class LagCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DecimalFormat dF = new DecimalFormat("#.##");
		String max = dF.format((Runtime.getRuntime().maxMemory() * 1.0D) / 1073741824D);
		String min = dF.format(((Runtime.getRuntime().freeMemory()) * 1.0D) / 1073741824D);
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Main.msg(p, "");
			Main.msg(p, "&8» &e&lRAM USAGE: &f" + min + " GB &8/ &f" + max + " GB");
			Main.msg(p, "");
		} else {
			Main.so("&8» &e&lRAM USAGE: &f" + min + " GB &8/ &f" + max + " GB");
		}
		return false;
		
	}
}
