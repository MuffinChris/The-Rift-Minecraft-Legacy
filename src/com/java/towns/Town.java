package com.java.towns;

import com.java.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scheduler.BukkitRunnable;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;


public class Town {
    /*
    Ranks go from 0 to 5 inclusive (default names subject to change)
    */

    private Main main = Main.getInstance();
    private List<String> ranks = new ArrayList<String>() {
        {
            add("Newbie");
            add("Recruit");
            add("Veteran");
            add("IDFK");
            add("Sub-owner");
            add("Owner");
        }
    };
    private CitizenList cl;
    private String name;

    public Town(Player p, String _n) {
        cl = new CitizenList();

        Citizen citizen = main.getUUIDCitizenMap().get(p.getUniqueId());
        citizen.setRank(ranks.size() - 1); // first player in creating class so set them to max rank
        citizen.setTown(_n);

        this.getCitizenList().citimap.put(p, citizen);
        this.getCitizenList().town = _n;
        this.name = _n;
    }
    public Town(String _n){
        name = _n;
        pullFiles();
    }
    public Town(CitizenList citilist, String _n) {
        this.cl = citilist;
        this.name = _n;
    }

    public CitizenList getCitizenList() {
        return cl;
    }

    public List<String> getRanks() {
        return ranks;
    } // TODO: have some way for town members to create ranks

    public int getRank(Player p) {
        return cl.citimap.get(p).getRank();
    }

    public String getRankName(Player p) {
        return ranks.get(cl.getRank(p));
    }

    public void setName(String s) {
        name = s;
    }

    public String getName() {
        return name;
    }


    public void changeRankName(int i, String newname) {
        ranks.set(i, newname);
    }

    public void invite(Player inviter, Player reciever) {
        Main.msg(inviter, "&aInvite sent!");
        TextComponent invText1 = new TextComponent(inviter.getName() + " has invited you to " + name + "!       Click to choose:");
        TextComponent acceptText = new TextComponent("[ACCEPT]");
        TextComponent declineText = new TextComponent("[DECLINE]");
        acceptText.setBold(true);
        declineText.setBold(true);
        acceptText.setColor(ChatColor.GREEN);
        declineText.setColor(ChatColor.RED);
        acceptText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town accept"));
        declineText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town decline"));
        // TODO: maybe do some hover events to make it more obvious what's going on
        TextComponent invText2 = new TextComponent();
        invText2.addExtra(acceptText);
        invText2.addExtra(" or ");
        invText2.addExtra(declineText);
        reciever.sendMessage(invText1);
        reciever.sendMessage(invText2);

        Citizen mc = main.getUUIDCitizenMap().get(reciever.getUniqueId());
        mc.setInviteStatus(this.name);
        new BukkitRunnable() {
            public void run() {
                if (!mc.getInviteStatus().equals("Normal")) {
                    mc.setInviteStatus("Normal");
                    Main.msg(inviter, Main.color("&4Invite Timed Out."));
                    Main.msg(reciever, Main.color("&4Invite Timed Out."));
                }
            }
        }.runTaskLater(Main.getInstance(), 20 * 60);
    }

    public void kick(Player kicker, Player reciever) {

    }

    public void promote(Player promoter, Player reciever) {
        if(cl.getRank(reciever) == ranks.size() - 1){
            //TODO: Reciever is owner
            return;
        }
        //Check if promoter rank is high enough
        if(cl.getRank(promoter) <= 3){
            //TODO: You are not high enough rank
            return;
        }
        if(cl.getRank(promoter) - cl.getRank(reciever) < 2){
            //TODO: You are not high enough rank to promote reciever
            return;
        }
        //TODO: Promoter has successfully promoted reciever
        //TODO: Reciever has successfully been promoted by Promoter
        main.getUUIDCitizenMap().get(reciever).setRank(getRank(reciever) + 1);
    }


    public void pushFiles() {
        File tFile = new File("plugins/Rift/data/towns/" + name + ".yml");
        FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
        try {
             /*
            Name
             */
            tData.set("TownName", name);
            /*
            Citizens
             */
            List<String> citizenData = new ArrayList<String>();
            for (Map.Entry<Player, Citizen> entry : cl.getCitizenList().entrySet()) {
                citizenData.add(entry.getKey().getUniqueId().toString());
            }

            tData.set("CitizensList", citizenData);
            tData.save(tFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullFiles() {
        File tFile = new File("plugins/Rift/data/towns/" + name + ".yml");
        FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
        if (!tFile.exists()) {
            pushFiles();
            pullFiles();
        } else {
            /*
            Town info
             */
            setName(tData.getString("TownName"));
            List<String> pullUUIDString = new ArrayList<String>();
            List<Citizen> pullcitizens = new ArrayList<Citizen>();
            pullUUIDString = tData.getStringList("CitizensList");
            for(int i = 0; i < pullUUIDString.size(); i++){
                pullcitizens.add(main.getUUIDCitizenMap().get(UUID.fromString(pullUUIDString.get(i))));
            }
            cl = new CitizenList(pullcitizens, name);
        }
    }
}
