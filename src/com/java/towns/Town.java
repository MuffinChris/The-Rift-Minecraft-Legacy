package com.java.towns;

import com.java.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;

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
    //private CitizenList cl;
    private String name;
    private int level = 1;
    private List<UUID> cList;
    public Town(Player p, String _n) {
        //cl = new CitizenList();
        cList = new ArrayList<UUID>();
        Citizen citizen = main.getUUIDCitizenMap().get(p.getUniqueId());
        citizen.setRank(ranks.size() - 1); // first player in creating class so set them to max rank
        citizen.setTown(_n);
        citizen.pushFiles(); // this is kinda of weird place to do this -- but refactoring this part is for some other time

        cList.add(p.getUniqueId());
        this.name = _n;
        pushFiles();
    }
    public Town(String _n){
        cList = new ArrayList<UUID>();
        name = _n;
        pullFiles();
    }


    public List<UUID> getCitizenList() {
        return cList;
    }

    public List<String> getRanks() {
        return ranks;
    } // TODO: have some way for town members to create ranks

    public int getRank(Player p) {
        return main.getUUIDCitizenMap().get(p.getUniqueId()).getRank();
    }

    public String getRankName(Player p) {
        return ranks.get(getRank(p));
    }

    public String getRankName(int i) {
        return ranks.get(i);
    }
    public void setName(String s) {
        name = s; pushFiles();
    }

    public String getName() {
        return name;
    }

    public int getSize(){
        return cList.size();
    }
    public void setLevel(int l) { level = l; pushFiles(); }

    public int getLevel() { return level; }


    public void changeRankName(int i, String newName) {
        ranks.set(i, newName);
    }

    public void invite(Player inviter, Player receiver) {
        Main.msg(inviter, "&aInvite sent to " + receiver.getDisplayName() + "!");
        TextComponent invText1 = new TextComponent(inviter.getName() + " has invited you to " + name + "!");
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
        receiver.sendMessage(invText1);
        receiver.sendMessage(invText2);

        Citizen mc = main.getUUIDCitizenMap().get(receiver.getUniqueId());
        mc.setInviteStatus(this.name);
        new BukkitRunnable() {
            public void run() {
                if (!mc.getInviteStatus().equals("Normal")) {
                    mc.setInviteStatus("Normal");
                    Main.msg(inviter, Main.color("&4Invite Timed Out."));
                    Main.msg(receiver, Main.color("&4Invite Timed Out."));
                }
            }
        }.runTaskLater(Main.getInstance(), 20 * 60);
    }

    public void kick(Player kicker, Player receiver) {
        if(!main.getUUIDCitizenMap().get(receiver.getUniqueId()).getTown().equals(name)){
            Main.msg(kicker, receiver.getName() + " is not in your town!");
            return;
        }
        int krank = getRank(kicker);
        int rrank = getRank(receiver);
        if(krank <= 3){
            main.msg(kicker, "You are not high enough rank to kick.");
            return;
        }
        if(krank <= rrank){
            main.msg(kicker, "You are not high enough rank to kick " + receiver.getName() + ".");
            return;
        }
        Citizen c = main.getUUIDCitizenMap().get(receiver.getUniqueId());
        c.setRank(-1); c.setTown(Citizen.defaultTownName);
        this.getCitizenList().remove(receiver.getUniqueId());
        c.pushFiles();
        pushFiles();
    }

    public void kick(Player kicker, OfflinePlayer receiver) {
        OfflineCitizen cr = new OfflineCitizen(receiver);
        if(!cr.getTown().equals(name)){
            Main.msg(kicker, receiver.getName() + " is not in your town!");
            return;
        }
        int krank = getRank(kicker);
        int rrank = cr.getRank();
        if(krank <= 3){
            main.msg(kicker, "You are not high enough rank to kick.");
            return;
        }
        if(krank <= rrank){
            main.msg(kicker, "You are not high enough rank to kick " + receiver.getName() + ".");
            return;
        }
        cr.setRank(-1); cr.setTown(Citizen.defaultTownName);
        this.getCitizenList().remove(receiver.getUniqueId());
        pushFiles();
    }
    public void promote(Player promoter, Player receiver) {
        if(!main.getUUIDCitizenMap().get(receiver.getUniqueId()).getTown().equals(name)){
            Main.msg(promoter, receiver.getName() + " is not in your town!");
            return;
        }
        int prank = getRank(promoter); int rrank = getRank(receiver);
        //Check if promoter rank is high enough
        if(prank <= 2){
            Main.msg(promoter, "You are not high enough rank to promote.");
            return;
        }
        if(rrank == ranks.size() - 1){
            Main.msg(promoter, receiver.getName() + " is " + getRankName(receiver) +".");
            Main.msg(promoter, receiver.getName() + " cannot be promoted");
            return;
        }
        if(prank - rrank < 2 && prank != ranks.size() - 1){
            Main.msg(promoter, "You are not high enough rank to promote " + receiver.getName() + " to "
                    + ranks.get(rrank + 1));
            return;
        }
        //TODO: Promote sub-owner to owner
        Main.msg(promoter, "You have promoted " + receiver.getName() + " to " + ranks.get(rrank + 1));
        Main.msg(receiver, "You have been promoted to " + ranks.get(rrank + 1) + " by " + promoter.getName());
        main.getUUIDCitizenMap().get(receiver.getUniqueId()).setRank(getRank(receiver) + 1);
        main.getUUIDCitizenMap().get(receiver.getUniqueId()).pushFiles();
    }
    public void promote(Player promoter, OfflinePlayer receiver) {
        OfflineCitizen cr = new OfflineCitizen(receiver);
        if(!cr.getTown().equals(name)){
            Main.msg(promoter, receiver.getName() + " is not in your town!");
            return;
        }
        int prank = getRank(promoter); int rrank = cr.getRank();
        //Check if promoter rank is high enough
        if(prank <= 2){
            Main.msg(promoter, "You are not high enough rank to promote.");
            return;
        }
        if(rrank == ranks.size() - 1){
            Main.msg(promoter, receiver.getName() + " is " + getRankName(cr.getRank()) +".");
            Main.msg(promoter, receiver.getName() + " cannot be promoted");
            return;
        }
        if(prank - rrank < 2 && prank != ranks.size() - 1){
            Main.msg(promoter, "You are not high enough rank to promote " + receiver.getName() + " to "
                    + ranks.get(rrank + 1));
            return;
        }
        //TODO: Promote sub-owner to owner
        Main.msg(promoter, "You have promoted " + receiver.getName() + " to " + ranks.get(rrank + 1));
        cr.setRank(cr.getRank() + 1);
    }

    public void demote(Player demoter, Player receiver){
        if(!main.getUUIDCitizenMap().get(receiver.getUniqueId()).getTown().equals(name)){
            Main.msg(demoter, receiver.getName() + " is not in your town!");
            return;
        }
        int drank = getRank(demoter);
        int rrank = getRank(receiver);
        if(drank <= 2){
            Main.msg(demoter, "You are not high enough rank to demote someone");
            return;
        }
        if(drank == rrank){
            Main.msg(demoter, "You cannot demote someone that is the same rank as you");
            return;
        }
        if(drank < rrank){
            Main.msg(demoter, "You cannot demote someone that is a higher rank than you");
            return;
        }
        if(rrank <= 0){
            Main.msg(demoter, receiver.getName() + " cannot be demoted any lower");
            return;
        }
        Main.msg(demoter, "You have demoted " + receiver.getName() + " to " + ranks.get(rrank - 1));
        Main.msg(receiver, "You have been demoted to " + ranks.get(rrank - 1) + " by " + demoter.getName());
        main.getUUIDCitizenMap().get(receiver.getUniqueId()).setRank(getRank(receiver) - 1);
        main.getUUIDCitizenMap().get(receiver.getUniqueId()).pushFiles();
    }
    public void demote(Player demoter, OfflinePlayer receiver){
        OfflineCitizen cr = new OfflineCitizen(receiver);
        if(!cr.getTown().equals(name)){
            Main.msg(demoter, receiver.getName() + " is not in your town!");
            return;
        }
        int drank = getRank(demoter);
        int rrank = cr.getRank();
        if(drank <= 2){
            Main.msg(demoter, "You are not high enough rank to demote someone");
            return;
        }
        if(drank == rrank){
            Main.msg(demoter, "You cannot demote someone that is the same rank as you");
            return;
        }
        if(drank < rrank){
            Main.msg(demoter, "You cannot demote someone that is a higher rank than you");
            return;
        }
        if(rrank <= 0){
            Main.msg(demoter, receiver.getName() + " cannot be demoted any lower");
            return;
        }
        Main.msg(demoter, "You have demoted " + receiver.getName() + " to " + ranks.get(rrank - 1));
        cr.setRank(rrank - 1);
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
            for(int i = 0; i < cList.size(); i++){
                citizenData.add(cList.get(i).toString());
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
            List<String> pullUUIDString = tData.getStringList("CitizensList");
            for(int i = 0; i < pullUUIDString.size(); i++){
                cList.add(UUID.fromString(pullUUIDString.get(i)));
            }

        }
    }

    // this function is broken (?)
    //      - tFile.delete() returns true so it thinks it works except the file is not actually removed
    //      - low prio fix though because it will get overridden if someone creates a town of the same name
    public void deleteFiles(){
        File tFile = new File("plugins/Rift/data/towns/" + name + ".yml");
        tFile.delete();
    }
}
