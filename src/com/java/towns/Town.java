package com.java.towns;

import com.java.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class Town {
    /*
    Ranks go from 0 to 5 inclusive (default names subject to change)
    */
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
        this.getCitizenList().citimap.put(p, new Citizen(p));
        this.getCitizenList().town = _n;
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
        if(cl.getRank(inviter) >= 3){
            //recieve gets an invite
            return;
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
