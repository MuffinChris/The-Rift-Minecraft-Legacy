package com.java.towns;

import com.java.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class Town {
    /*
    Ranks go from 0 to 5 inclusive (default names subject to change)
    */

    private Main main = Main.getInstance();
    private List<String> ranks = new ArrayList<String> (){
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

    public Town(Player p, String _n){
        cl = new CitizenList();

        Citizen citizen = main.getUUIDCitizenMap().get(p.getUniqueId());
        citizen.setRank(ranks.size() - 1); // first player in creating class so set them to max rank
        citizen.setTown(_n);

        this.getCitizenList().citimap.put(p, citizen);
        this.getCitizenList().town = _n;
        this.name = _n;
    }
    public Town(CitizenList citilist, String _n){
        this.cl = citilist;
        this.name = _n;
    }
    public CitizenList getCitizenList() {
        return cl;
    }

    public int getRank(Player p){
        return cl.citimap.get(p).getRank();
    }
    public String getRankName(Player p) { return ranks.get(cl.getRank(p)); }
    public void setName(String s) { name = s; }


    public void changeRankName(int i, String newname){
        ranks.set(i, newname);
    }

    public void invite(Player inviter, Player recieve){
        if(cl.getRank(inviter) < 3){
            //recieve gets an invite
            Main.msg(inviter, "&4You are not high enough rank to invite " + recieve.getName());
        }
        else if(!main.getUUIDCitizenMap().get(recieve.getUniqueId()).getTown().equals("none")){
            Main.msg(inviter, "&4" + recieve.getName() + " is already in another town");
        }
        else{
            TextComponent invText1 = new TextComponent(inviter.getName() + " wants to invite you to " + name);
            TextComponent acceptText = new TextComponent("[ACCEPT]");
            TextComponent declineText = new TextComponent("[DECLINE]");
            acceptText.setBold(true); declineText.setBold(true);
            acceptText.setColor(ChatColor.GREEN); declineText.setColor(ChatColor.RED);
            acceptText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say accept"));
            declineText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/say decline"));
            TextComponent invText2 = new TextComponent();
            invText2.addExtra(acceptText); invText2.addExtra(" or "); invText2.addExtra(declineText);
            recieve.sendMessage(invText1);
            recieve.sendMessage(invText2);
        }
        //inviter is not high enough rank
        return;
    }
    public void kick(Player kicker, Player recieve){
        
    }
    public void promote(Player promoter, Player recieve){
        
    }


    public void pushFiles(){
        File tFile = new File("plugins/Rift/data/towns/" + name + ".yml");
        FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
        try{
             /*
            Name
             */
            tData.set("TownName", name);
            /*
            Citizens
             */
            String citizenData = "";
            List<String> uuidList = new ArrayList<String>();
            for(Map.Entry<Player, Citizen> entry : cl.getCitizenList().entrySet()){
                uuidList.add(entry.getKey().getUniqueId().toString());
            }
            citizenData = String.join(", ", uuidList);
            tData.set("CitizensList", citizenData);
            tData.save(tFile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void pullFiles(){
        File pFile = new File("plugins/Rift/data/towns/" + name + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if(!pFile.exists()){
            pushFiles(); pullFiles();
        }
        else{
            /*
            Town info
             */
            setName(pData.getString("TownName"));
        }
    }
}
