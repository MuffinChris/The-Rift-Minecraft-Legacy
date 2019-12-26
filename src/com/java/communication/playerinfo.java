package com.java.communication;

import com.java.Main;
import net.minecraft.server.v1_15_R1.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class playerinfo {

	private UUID uuid;
	private Player reply;
	
	//private Objective o;
	//private Scoreboard board;
	
	public playerinfo(Player p) {
		uuid = p.getUniqueId();
		reply = null;
		//board = Bukkit.getScoreboardManager().getNewScoreboard();
		//o = this.board.registerNewObjective("health", "health", Main.color("&c❤"));
		//o.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}
	
	public Player getReply() {
		return reply;
	}

	public void scrub() {
		reply = null;
		//o.unregister();
		//board = null;
		uuid = null;
	}
	
	public void setReply(Player p) {
		reply = p;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
}
