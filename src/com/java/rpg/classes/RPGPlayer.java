package com.java.rpg.classes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.java.Main;
import com.java.rpg.classes.utility.*;
import com.java.rpg.damage.utility.Damage;
import com.java.rpg.classes.casting.Skillboard;
import com.java.rpg.player.Items;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RPGPlayer extends Leveleable {

    private Main main = Main.getInstance();

    private Player player;
    private PlayerClass pclass;

    public enum ChatChannel {
        Global, Town, Party
    }

    private ChatChannel chatChannel;
    public ChatChannel getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(ChatChannel c) {
        chatChannel = c;
    }

    private double currentMana;

    private Skillboard board;

    private List<Damage> damages;

    public double getArmor() {
        double armor = 0;
        if (player == null) {
            return armor;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                armor += Items.getArmor(i);
            }
        }
        if (pclass == null) {
            return armor;
        }
        return getPClass().getCalcArmor(getLevel()) + armor;
    }

    public double getMR() {
        double mr = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                mr += Items.getMR(i);
            }
        }
        return getPClass().getCalcMR(getLevel()) + mr;
    }

    public double getAirDefense() {
        double val = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                val += Items.getAirDefense(i);
            }
        }
        return val + getPClass().getEDefense().getAir() + getPClass().getEDefenseScaling().getAir() * getLevel();
    }

    public double getEarthDefense() {
        double val = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                val += Items.getEarthDefense(i);
            }
        }
        return val + getPClass().getEDefense().getEarth() + getPClass().getEDefenseScaling().getEarth() * getLevel();
    }

    public double getElectricDefense() {
        double val = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                val += Items.getElectricDefense(i);
            }
        }
        return val + getPClass().getEDefense().getElectric() + getPClass().getEDefenseScaling().getElectric() * getLevel();
    }

    public double getFireDefense() {
        double val = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                val += Items.getFireDefense(i);
            }
        }
        return val + getPClass().getEDefense().getFire() + getPClass().getEDefenseScaling().getFire() * getLevel();
    }

    public double getIceDefense() {
        double val = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                val += Items.getIceDefense(i);
            }
        }
        return val + getPClass().getEDefense().getIce() + getPClass().getEDefenseScaling().getIce() * getLevel();
    }



    public int calculateSP() {
        int total;
        if (getLevel() >= RPGConstants.superSkillOne && getLevel() < RPGConstants.superSkillTwo) {
            total = 1;
        } else if (getLevel() >= RPGConstants.superSkillTwo) {
            total = 2;
        } else {
            total = 0;
        }
        for (int i : getSkillLevels().values()) {
            total -= i;
        }
        return total;
    }

    public List<Skill> getSkillsAll() {
        Player p = player;
        List<Skill> pSkills = new ArrayList<>();
        for (Skill s : main.getRP(p).getPClass().getSkills()) {
            if (main.getRP(p).getSkillLevels() != null && main.getRP(p).getSkillLevels().get(s.getName()) == 0) {
                pSkills.add(s);
            } else {
                pSkills.add(s.getUpgradedSkill());
            }
        }
        return pSkills;
    }

    public List<Skill> getSkillsCastable() {
        Player p = player;
        List<Skill> pSkills = new ArrayList<>();
        for (Skill s : main.getRP(p).getPClass().getSkills()) {
            if (main.getRP(p).getSkillLevels() != null && main.getRP(p).getSkillLevels().get(s.getName()) == 0) {
                if (s.getSkillType() == Skill.SkillType.PASSIVE) {
                    continue;
                }
                pSkills.add(s);
            } else {
                if (s.getUpgradedSkill().getSkillType() == Skill.SkillType.PASSIVE) {
                    continue;
                }
                pSkills.add(s.getUpgradedSkill());
            }
        }
        return pSkills;
    }

    public double getAD() {
        if (player != null && pclass != null) {
            return bonusad.getValue() + pclass.getCalcAD(getLevel());
        } else {
            return 0;
        }
    }

    public double getAP() {
        if (player != null && pclass != null) {
            return bonusap.getValue() + pclass.getCalcAP(getLevel());
        } else {
            return 0;
        }
    }



    public void setMana(double m) {
        currentMana = Math.min(m, pclass.getCalcMana(getLevel()));
    }

    public void setManaOverflow(double m) {
        currentMana = m;
    }

    public String getPrettyCMana() {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(currentMana);
    }


    private int idleslot = 0;
    public int getIdleSlot() {
        return idleslot;
    }

    private boolean sendExp = true;
    public boolean getSendExp() {
        return sendExp;
    }

    public void setSendExp(boolean b) {
        sendExp = b;
        pushFiles();
    }

    private boolean toggleOffhand = true;
    public boolean getToggleOffhand() {
        return toggleOffhand;
    }

    public void setToggleOffhand(boolean b) {
        toggleOffhand = b;
        pushFiles();
    }

    public void setIdleSlot(int i) {
        idleslot = i;
        pushFiles();
    }


    Map<String, Integer> skillLevels;
    List<String> statuses;
    List<String> passives;
    List<String> toggles;
    List<Map<Integer, String>> toggleTasks;
    List<Map<Integer, String>> passiveTasks;
    private Map<String, Long> cooldowns;

    private LivingEntity target;

    private List<StatusObject> so;

    public List<StatusObject> getSo() {
        return so;
    }

    private StatusObject stun;
    private StatusObject root;
    private StatusObject silence;
    private StatusObject manafreeze;
    private StatusObject hpfreeze;
    private StatusObject pstrength2;
    private StatusObject walkspeed;
    private StatusObject walkspeedS;
    private StatusObject bonusap;
    private StatusObject bonusad;
    private StatusObject bonusapS;
    private StatusObject bonusadS;

    private StatusObject autoLife;
    private StatusObject blessing;
    private StatusObject stoneskindr;
    private StatusObject stoneskins;
    private StatusObject stoneskincd;


    public StatusObject getWalkspeed() {
        return walkspeed;
    }

    public StatusObject getWalkSpeedS() {
        return walkspeedS;
    }

    public StatusObject getStun() {
        return stun;
    }

    public StatusObject getRoot() {
        return root;
    }
    public StatusObject getSilence() {
        return silence;
    }
    public StatusObject getManaFreeze() {
        return manafreeze;
    }
    public StatusObject getHpFreeze() {
        return hpfreeze;
    }

    public StatusObject getPStrength2() {
        return pstrength2;
    }

    public StatusObject getBonusAP() {
        return bonusap;
    }

    public StatusObject getBonusAD() {
        return bonusad;
    }

    public StatusObject getBonusAPS() {
        return bonusapS;
    }

    public StatusObject getBonusADS() {
        return bonusadS;
    }
    
    
    public StatusObject getAutoLife() {
    	return autoLife;
    }
    
    public StatusObject getBlessing() {
    	return blessing;
    }
    
    public StatusObject getStoneSkinDR() {
    	return stoneskindr;
    }
    
    public StatusObject getStoneSkinS() {
    	return stoneskins;
    }
    
    public StatusObject getStoneSkinCD() {
    	return stoneskincd;
    }

    private int baseWS = 20;
    public void setBaseWS(int i) {
        baseWS = i;
    }

    public int getBaseWS() {
        return baseWS;
    }

    public RPGPlayer() {
        pclass = Main.getInstance().getCM().getPClassFromString("Wanderer");
    }

    public RPGPlayer(Player p) {
        super (0, RPGConstants.maxLevel, p);

        stun = new StatusObject("Stun", "Stunned", false);
        root = new StatusObject("Root", "Rooted", false);
        silence = new StatusObject("Silence", "Silenced", false);
        manafreeze = new StatusObject("ManaFreeze", "Mana Frozen", false);
        hpfreeze = new StatusObject("HPFreeze", "HP Frozen", false);
        pstrength2 = new StatusObject("PStrength", "Power Strength", false);
        walkspeed = new StatusObject("Walkspeed", "Movespeed", false);
        walkspeedS = new StatusObject("Walkspeed", "Movespeed", true);
        bonusap = new StatusObject("AP", "AP", false);
        bonusapS = new StatusObject("AP", "AP", true);
        bonusad = new StatusObject("AD", "AD", false);
        bonusadS = new StatusObject("AD", "AD", true);
        
        autoLife = new StatusObject("AutoLife", "Protected", false);
        blessing = new StatusObject("Blessing", "Blessed", false);
        stoneskindr = new StatusObject("Stone Skin", "", false);
        stoneskins = new StatusObject("Stone Skin Stacks", "", false);
        stoneskincd = new StatusObject("Stone Skin on Cooldown", "", false);
        
        so = new ArrayList<>();
        so.add(stun);
        so.add(root);
        so.add(silence);
        so.add(manafreeze);
        so.add(hpfreeze);
        so.add(pstrength2);
        so.add(walkspeed);
        so.add(walkspeedS);
        so.add(bonusap);
        so.add(bonusad);
        so.add(bonusapS);
        so.add(bonusapS);
        
        so.add(autoLife);
        so.add(blessing);
        so.add(stoneskindr);
        so.add(stoneskins);
        so.add(stoneskincd);

        player = p;
        currentMana = 0;
        statuses = new ArrayList<>();
        passives = new ArrayList<>();
        cooldowns = new HashMap<>();
        passiveTasks = new ArrayList<>();
        toggleTasks = new ArrayList<>();
        toggles = new ArrayList<>();
        skillLevels = new LinkedHashMap<>();

        chatChannel = ChatChannel.Global;

        target = null;
        pullFiles();
        board = new Skillboard(p);

        damages = new ArrayList<>();

        walkspeedS.getStatuses().add(new StatusValue("Init:Walkspeed", baseWS, 0, 0, true));

        tabSetup();
    }

    public List<Damage> getDamages() {
        return damages;
    }

    public Map<String, Integer> getSkillLevels() {
        if (skillLevels.isEmpty() && pclass != null) {
            for (Skill s : pclass.getSkills()) {
                skillLevels.put(s.getName(), 0);
            }
        }
        return skillLevels;
    }

    public Skillboard getBoard() {
        return board;
    }

    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }

    public PlayerClass getPClass() {
        return pclass;
    }

    public Player getPlayer() {
        return player;
    }

    public double getCMana() {
        return currentMana;
    }

    public void setUsernameFile(FileConfiguration pData, String username) {
        if (pData.contains("Username")) {
            if (pData.getString("Username").equalsIgnoreCase(player.getName())) {
                pData.set("Username", username);
            } else {
                pData.set("PreviousUsername", pData.getString("Username"));
                pData.set("Username", username);
            }
        } else {
            pData.set("Username", username);
        }
    }

    public void setLastSeenFile(FileConfiguration pData, Long millis) {
        pData.set("LastSeen", millis);
    }

    public void pushFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            /*
            Utility
             */
            setUsernameFile(pData, player.getName());
            setLastSeenFile(pData, System.currentTimeMillis());
            /*
            Class Info
             */
            String name = RPGConstants.defaultClassName;
            if (pclass != null) {
                name = pclass.getName();
                pData.set("Current Class", name);
            } else {
                pData.set("Current Class", RPGConstants.defaultClassName);
                pclass = main.getCM().getPClassFromString(RPGConstants.defaultClassName);
            }
            pData.set(name + "Level", getLevel());
            pData.set(name + "Exp", getExp());
            pData.set(name + "CMana", currentMana);
            /*
            Skill Levels
             */
            String output = "";
            for (String s : skillLevels.keySet()) {
                output+=s + "-" + skillLevels.get(s) + ",";
            }
            if (output.contains(",")) {
                output = output.substring(0, output.length() - 1);
            }
            boolean none = false;
            if (pData.contains(name + "Skills") && pData.get(name + "Skills").equals("")) { //&& !pclass.getName().equals(RPGConstants.defaultClassName)) {
                none = true;
            }
            if (pData.contains(name + "Skills") && !none) {
                pData.set(name + "Skills", output);
            } else {
                output = "";
                for (Skill s : pclass.getSkills()) {
                    output+=s.getName() + "-0,";
                }
                if (output.contains(",")) {
                    output = output.substring(0, output.length() - 1);
                }
                pData.set(name + "Skills", output);
            }
            /*
            Cooldowns
             */
            String cd = "";
            for (String skill : cooldowns.keySet()) {
                cd+=skill + ":" + cooldowns.get(skill) + ",";
            }
            if (cd.contains(",")) {
                cd = cd.substring(0, cd.length() - 1);
            }
            pData.set("Cooldowns", cd);
            /*
            Settings
             */
            if (pData.contains("IdleSlot")) {
                pData.set("IdleSlot", getIdleSlot());
            } else {
                pData.set("IdleSlot", 0);
            }
            if (pData.contains("SendExp")) {
                pData.set("SendExp", getSendExp());
            } else {
                pData.set("SendExp", true);
            }
            if (pData.contains("ToggleOffhand")) {
                pData.set("ToggleOffhand", getSendExp());
            } else {
                pData.set("ToggleOffhand", true);
            }

            pData.save(pFile);
            updateStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pFile.exists()) {
            setUsernameFile(pData, player.getName());
            setLastSeenFile(pData, System.currentTimeMillis());
            /*
            Class Info
             */
            String name = RPGConstants.defaultClassName;
            pclass = main.getCM().getPClassFromString(pData.getString("Current Class"));
            if (pclass != null) {
                name = pclass.getName();
            }
            setLevel(pData.getInt(name + "Level"));
            setExp(pData.getDouble(name + "Exp"));
            currentMana = (pData.getInt(name + "CMana"));
            /*
            Skills
             */
            if(pData.contains(name + "Skills")) {
                if (pData.get(name + "Skills").equals("")) {
                    pushFiles();
                }
                String skstr = pData.getString(name + "Skills");
                List<Skill> skills = new ArrayList(pclass.getSkills());
                if (skstr.length() > 0) {
                    String[] sks = skstr.split(",");
                    for (String s : sks) {
                        String[] skobj = s.split("-");
                        skillLevels.put(skobj[0], Integer.valueOf(skobj[1]));
                        skills.remove(getSkillFromName(skobj[0]));
                    }
                    if (!skills.isEmpty()) {
                        for (Skill s : skills) {
                            skillLevels.put(s.getName(), 0);
                        }
                        skills.clear();
                    }
                }
            }
            /*
            Cooldowns
             */
            if(pData.contains("Cooldowns")) {
                String cdstr = pData.getString("Cooldowns");
                if (cdstr.length() > 0) {
                    String[] cds = cdstr.split(",");
                    for (String s : cds) {
                        String[] cdobj = s.split(":");
                        cooldowns.put(cdobj[0], Long.valueOf(cdobj[1]));
                    }
                }
            }

            /*
            Settings
             */
            if (pData.contains("IdleSlot")) {
                setIdleSlot(pData.getInt("IdleSlot"));
            } else {
                pData.set("IdleSlot", 0);
                setIdleSlot(pData.getInt("IdleSlot"));
            }

            if (pData.contains("SendExp")) {
                setSendExp(pData.getBoolean("SendExp"));
            } else {
                pData.set("SendExp", true);
                setSendExp(pData.getBoolean("SendExp"));
            }

            if (pData.contains("ToggleOffhand")) {
                setSendExp(pData.getBoolean("ToggleOffhand"));
            } else {
                pData.set("ToggleOffhand", true);
                setSendExp(pData.getBoolean("ToggleOffhand"));
            }

            updateStats();
        } else {
            /*
            SkillLevels Empty Bug
             */
            for (Skill s : main.getCM().getPClassFromString(RPGConstants.defaultClassName).getSkills()) {
                skillLevels.put(s.getName(), 0);
            }
            pushFiles();
            pullFiles();
        }
    }

    public boolean changeClass(PlayerClass pc) {
        if (pclass != null && pc.getName().equalsIgnoreCase(pclass.getName())) {
            return false;
        }

        /*
        Clear Statuses
         */
        if (pclass != null) {
            for (StatusObject sz : so) {
                for (Skill s : pclass.getSkills()) {
                    sz.clearBasedTitle(s.getName(), player);
                }
                for (Skill s : pclass.getUpgradedSkills()) {
                    sz.clearBasedTitle(s.getName(), player);
                }
            }
        }
        double hp = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        pushFiles();
        setExp(0);
        pclass = pc;
        skillLevels.clear();
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (pclass != null) {
                pData.set("Current Class", pclass.getName());
            } else {
                pData.set("Current Class", RPGConstants.defaultClassName);
            }
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullFiles();
        currentMana = 0;
        player.setHealth(hp * pclass.getCalcHP(getLevel()));
        updateStats();
        return true;
    }

    private DecimalFormat df = new DecimalFormat("#.##");

    public void updateStats() {
        player.setHealthScale(20);
        if (pclass != null && !player.isDead()) {
            double hpprev = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            double hp = getPClass().getCalcHP(getLevel());
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
            player.setHealth(Math.min(hp, hp * hpprev));
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(getAD());
            player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
            updateWS();
        }
    }

    public void updateWS() {
        for (StatusValue sv : walkspeedS.getStatuses()) {
            if (sv.getDurationless() && sv.getSource().equals("Init:Walkspeed")) {
                sv.setValue(baseWS);
            }
        }
        float currentWs = Math.max(0, player.getWalkSpeed());
        float actualWs = Math.max(0, Float.parseFloat(String.valueOf(df.format((getWalkspeed().getValue() * 1.0 + getWalkSpeedS().getValue() * 1.0) / 100.0))));
        if (currentWs != actualWs) {
            player.setWalkSpeed(Math.min(actualWs, 1.0F));
        }
    }

    public LivingEntity getNearestAnyEntityInSight(Player player, int range) {
        List<LivingEntity> entities = main.getNearbyLivingEntitiesTargetValid(player.getEyeLocation(), player, range);
        List<LivingEntity> sortedEntities = new ArrayList<>();
        while(!entities.isEmpty()) {
            double dist = 999;
            LivingEntity add = null;
            for (LivingEntity ent : entities) {
                if (ent.getEyeLocation().distance(player.getEyeLocation()) < dist) {
                    dist = ent.getEyeLocation().distance(player.getEyeLocation());
                    add = ent;
                }
            }
            entities.remove(add);
            sortedEntities.add(add);
        }
        for (LivingEntity ent : sortedEntities) {
            Vector toEntity = ent.getLocation().toVector().subtract(player.getEyeLocation().toVector());
            Vector toEntity2 = ent.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector());
            Vector direction = player.getEyeLocation().getDirection();
            double dot = toEntity.normalize().dot(direction);
            double dot2 = toEntity2.normalize().dot(direction);
            if (dot > 0.98 || dot2 > 0.98) {
                if (player.hasLineOfSight(ent)) {
                    return ent;
                }
            }
        }
        return null;
    }

    public LivingEntity getNearestTargetInSight(Player player, int range) {
        List<LivingEntity> entities = main.getNearbyLivingEntitiesTargetValid(player.getEyeLocation(), player, range);
        List<LivingEntity> sortedEntities = new ArrayList<>();
        while(!entities.isEmpty()) {
            double dist = 999;
            LivingEntity add = null;
            for (LivingEntity ent : entities) {
                if (ent.getEyeLocation().distance(player.getEyeLocation()) < dist) {
                    dist = ent.getEyeLocation().distance(player.getEyeLocation());
                    add = ent;
                }
            }
            entities.remove(add);
            sortedEntities.add(add);
        }
        for (LivingEntity ent : sortedEntities) {
            Vector toEntity = ent.getLocation().toVector().subtract(player.getEyeLocation().toVector());
            Vector toEntity2 = ent.getEyeLocation().toVector().subtract(player.getEyeLocation().toVector());
            Vector direction = player.getEyeLocation().getDirection();
            double dot = toEntity.normalize().dot(direction);
            double dot2 = toEntity2.normalize().dot(direction);
            if (dot > 0.98 || dot2 > 0.98) {
                if (player.hasLineOfSight(ent)) {
                    return ent;
                }
            }
        }
        return null;
    }

    public boolean isTarget(Skill s) {
        return (s.getSkillType() == Skill.SkillType.PASSIVE_TARGET || s.getSkillType() == Skill.SkillType.TARGET);
    }

    public boolean isToggle(Skill s) {
        return (s.getSkillType() == Skill.SkillType.PASSIVE_TOGGLE || s.getSkillType() == Skill.SkillType.TOGGLE);
    }

    public boolean isCast(Skill s) {
        return (s.getSkillType() == Skill.SkillType.PASSIVE_CAST || s.getSkillType() == Skill.SkillType.CAST);
    }

    public boolean isPassive(Skill s) {
        return (s.getSkillType() == Skill.SkillType.PASSIVE_CAST || s.getSkillType() == Skill.SkillType.PASSIVE_TARGET || s.getSkillType() == Skill.SkillType.PASSIVE_TOGGLE || s.getSkillType() == Skill.SkillType.PASSIVE);
    }

    public String isTargetValid(Skill s) {
        target = getNearestTargetInSight(player, s.getTargetRange() * 2);
        if (target != null) {
            if (target.getLocation().distance(player.getLocation()) > s.getTargetRange()) {
                target = null;
                getStatuses().remove("Warmup" + s.getName());
                return "OutOfRangeTarget";
            }
            s.targetParticles(player, target);
        } else {
            getStatuses().remove("Warmup" + s.getName());
            target = null;
            return "BadTarget";
        }
        return null;
    }

    public List<Integer> removeWarmupStatuses(Skill s) {
        List<Integer> indexesToRemove = new ArrayList<>();
        int index = 0;
        for (String status : statuses) {
            if (status.contains("Warmup")) {
                if (!status.contains(s.getName())) {
                    indexesToRemove.add(index);
                }
                index++;
            }
        }
        for (int ind : indexesToRemove) {
            statuses.remove(ind);
        }
        return indexesToRemove;
    }

    public String removeWarmupStatusesIntCheck(Skill s) {
        if (removeWarmupStatuses(s).size() > 0) {
            return "Interrupted";
        }
        return null;
    }

    public String castSkill(String name) {
        if (pclass != null) {
            Skill s = null;
            for (Skill st : pclass.getSkills()) {
                if (name.equalsIgnoreCase(st.getName())) {
                    if (skillLevels.get(st.getName()) != 0) {
                        return "AlreadyUpgraded";
                    }
                    s = st;
                }
            }
            for (Skill st : pclass.getUpgradedSkills()) {
                if (name.equalsIgnoreCase(st.getName())) {
                    if (skillLevels.get(getSkillFromUpgradedSkill(st.getName()).getName()) == 0) {
                        return "NotUpgraded";
                    }
                    s = st;
                }
            }
            if (s != null) {
                if (s.getLevel() <= getLevel()) {
                    if (s.getManaCost() <= currentMana || (isToggle(s) && getToggles().contains(s.getName()))) {
                        if (stun.getValue() > 0) {
                            return "Stunned";
                        }
                        String cd = getCooldown(s);

                        if (cd.equalsIgnoreCase("Invalid")) {
                            return "Failure";
                        }

                        if (cd.contains("CD:")) {
                            return cd;
                        }

                        final BukkitScheduler scheduler = Bukkit.getScheduler();
                        if (cd.equalsIgnoreCase("Warmup") && !(isToggle(s) && getToggles().contains(s.getName()))) {

                            if (!statuses.contains("Warmup" + s.getName())) {
                                getStatuses().add("Warmup" + s.getName());
                            } else {
                                return "AlreadyCasting";
                            }

                            if (isTarget(s)) {
                                String itv = isTargetValid(s);
                                if (itv != null) {
                                    return itv;
                                }
                            }

                            removeWarmupStatuses(s);

                            String nameS = s.getName();
                            int warmup = s.getWarmup();
                            int manaCost = s.getManaCost();
                            final Skill sk = s;
                            Player p = player;

                            final int task = scheduler.scheduleSyncDelayedTask(main, () -> {
                                if (!p.isOnline() || player.isDead()) {
                                    if (p.isOnline() && p.isDead()) {
                                        statuses.remove("Warmup" + nameS);
                                        target = null;
                                    }
                                } else {
                                    cooldowns.put(nameS, System.currentTimeMillis());

                                    if (currentMana >= manaCost) {
                                        if (isTarget(sk)) {
                                            if (target == null || target.isDead()) {
                                                statuses.remove("Warmup" + nameS);
                                                return;
                                            }
                                            sk.target(player, target);
                                            target = null;
                                        } else if (isToggle(sk)) {
                                            getToggles().add(sk.getName());
                                            Map<Integer, String> map = new HashMap<>();
                                            map.put(sk.toggleInit(player), sk.getName());
                                            getToggleTasks().add(map);
                                            currentMana -= sk.getManaCost();
                                        } else {
                                            sk.cast(player);
                                        }
                                        currentMana -= manaCost;
                                    } else {
                                        Main.msg(player, "&cOut of mana to cast " + nameS + "!");
                                    }
                                    statuses.remove("Warmup" + nameS);
                                }
                            }, s.getWarmup());

                            new BukkitRunnable() {
                                Player p = player;
                                int time = 0;
                                public void run() {
                                    if (!p.isOnline()) {
                                        cancel();
                                        return;
                                    }
                                    if (player.isDead()) {
                                        cancel();
                                        target = null;
                                        statuses.remove("Warmup" + name);
                                        return;
                                    }
                                    time++;
                                    if (time >= warmup) {
                                        cancel();
                                        return;
                                    }
                                    if (!getStatuses().contains("Warmup" + nameS) || isTarget(sk) && target != null && target.isDead() || (target == null && isTarget(sk))) {
                                        scheduler.cancelTask(task);
                                        if (target == null && isTarget(sk)) {
                                            Main.msg(player, "&cYour target died!");
                                            statuses.remove("Warmup" + name);
                                        }
                                        if (target != null && target.isDead() && isTarget(sk)) {
                                            Main.msg(player, "&cYour target died!");
                                            statuses.remove("Warmup" + name);
                                            target = null;
                                        }
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Main.getInstance(),  0, 1);
                            return "Warmup:" + s.getWarmup();
                        }


                        if (isToggle(s)) {
                            if (getToggles().contains(s.getName())) {
                                s.toggleEnd(player);
                                cooldowns.put(s.getName(), System.currentTimeMillis());
                            } else {
                                getToggles().add(s.getName());
                                Map<Integer, String> map = new HashMap<>();
                                map.put(s.toggleInit(player), s.getName());
                                getToggleTasks().add(map);
                                currentMana -= s.getManaCost();
                                cooldowns.put(s.getName(), System.currentTimeMillis());

                                if (removeWarmupStatusesIntCheck(s) != null) {
                                    return "Interrupted";
                                }
                            }
                            return "CastedSkill";
                        }

                        if (isTarget(s)) {
                            if (isTarget(s)) {
                                String itv = isTargetValid(s);
                                if (itv != null) {
                                    return itv;
                                }
                            }

                            cooldowns.put(s.getName(), System.currentTimeMillis());
                            currentMana -= s.getManaCost();
                            s.target(player, target);

                            if (removeWarmupStatusesIntCheck(s) != null) {
                                return "Interrupted";
                            }

                            target = null;
                            return "CastedSkill";
                        }
                        if (cd.equalsIgnoreCase("Casted")) {
                            if (!isCast(s)) {
                                return "CannotCast";
                            }

                            cooldowns.put(s.getName(), System.currentTimeMillis());
                            currentMana -= s.getManaCost();
                            s.cast(player);

                            if (removeWarmupStatusesIntCheck(s) != null) {
                                return "Interrupted";
                            }

                            return "CastedSkill";
                        }
                    }
                    return "NoMana";
                }
                return "Level" + s.getLevel();
            }
        }
        return "Invalid";
    }

    public String getCooldown(Skill s) {
        if (pclass != null) {
            for (Skill sk : getSkillsAll()) {
                if (sk.equals(s)) {
                    if (cooldowns.containsKey(s.getName())) {
                        long timeLeft = System.currentTimeMillis() - cooldowns.get(s.getName());

                        long cooldown;
                        if (getToggles().contains(sk.getName())) {
                            cooldown = s.getToggleCooldown();
                        } else {
                            cooldown = s.getCooldown();
                        }
                        if (timeLeft * 0.001 * 20 >= cooldown) {
                            if (s.getWarmup() != 0) {
                                return "Warmup";
                            }
                            return "Casted";
                        } else {
                            DecimalFormat dF = new DecimalFormat("#.#");
                            String cd = dF.format((cooldown / 20.0) - (timeLeft/1000.0));
                            if (!cd.contains(".")) {
                                cd+=".0";
                            }
                            return "CD:" + cd;
                        }
                    } else {
                        if (s.getWarmup() != 0) {
                            return "Warmup";
                        }
                        return "Casted";
                    }
                }
            }
        }
        return "Invalid";
    }

    public void refreshCooldowns() {
        if (pclass != null) {
            for (int i = cooldowns.keySet().size() - 1; i >= 0; i--) {
                String name = cooldowns.keySet().toArray()[i].toString();
                long timeLeft = System.currentTimeMillis() - cooldowns.get(name);
                if (getSkillFromName(name) != null) {
                    long cooldown;
                    if (getToggles().contains(getSkillFromName(name).getName())) {
                        cooldown = getSkillFromName(name).getToggleCooldown();
                    } else {
                        cooldown = getSkillFromName(name).getCooldown();
                    }
                    if (timeLeft * 0.001 * 20 >= cooldown) {
                        cooldowns.remove(name);
                    }
                }
            }
        }
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public List<String> getToggles() {
        return toggles;
    }

    public List<Map<Integer, String>> getToggleTasks() {
        return toggleTasks;
    }

    public List<String> getPassives() {
        return passives;
    }

    public List<Map<Integer, String>> getPassiveTasks() {
        return passiveTasks;
    }

    public Skill getSkillFromUpgradedSkill(String name) {
        if (pclass != null) {
            for (Skill s : pclass.getSkills()) {
                if (s.isUpgradeable() && name.equalsIgnoreCase(s.getUpgradedSkill().getName())) {
                    return s;
                }
            }
        }
        return null;
    }

    public Skill getSkillFromName(String name) {
        if (pclass != null) {
            for (Skill s : pclass.getSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    return s;
                }
                if (s.isUpgradeable() && name.equalsIgnoreCase(s.getUpgradedSkill().getName())) {
                    return s.getUpgradedSkill();
                }
            }
        }
        return null;
    }

    /*

    Tablist

     */

    private TablistSlots playerSlots;
    private TablistSlots partySlots;

    private static List<Integer> players = IntStream.range(21, 60).boxed().collect(Collectors.toList());
    //private static List<Integer> friends = IntStream.range(1, 20).boxed().collect(Collectors.toList());
    private static List<Integer> town = IntStream.range(61, 80).boxed().collect(Collectors.toList());
    private static List<Integer> party = IntStream.range(1, 20).boxed().collect(Collectors.toList());

    public String selfTextureValue;
    public String selfTextureSignature;

    private static String defaultTextureValue = "eyJ0aW1lc3RhbXAiOjE1ODYwNzU3NjM1MzYsInByb2ZpbGVJZCI6IjZiMjIwMzdkYzA0MzQyNzE5NGYyYWRiMDAzNjhiZjE2IiwicHJvZmlsZU5hbWUiOiJMaWdodG5lbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjRkNDA3OTk1ZjVjNGFhOTlhMjg5ZmI0YWFhMDQwZTFjYzVlOWIzM2JiOWJlYWM2NTRkZmZhMDljYzZjZmQ5MiJ9fX0=";
    private static String defaultTextureSignature = "caGGJhsAP6hC1gzzo1lwm4bAPbW6XE5KUqLzlXOaTYGB2eqpCzq9oUUZg26kxCQ0io57VPOpQtQfdUHqSyVBpN+AGcBBuNYWSY5X0F0isxC/t1JpX4wcLW7Y7CCEj2nOQ0SEkJU5wJyDCdCHbk4VU4kvMd00oqlsTwIG5PzovT/8oq8uC9nME3/Tzds7lqAYHDBKJgMFSQ9D17RwnpQc0DoNpJ9KM2AxNtJa5M2UFr+d7x5z5Kicc+fG+4VX4wwVcRhnpGzzQTKL3gCOURvAOCfi9NBRADw2Ch7Tw8xj3c+c4NSgGoQ+2yaqU3BpwEiSvoNva7WidSwfQ3x99DCwZ6Gth8n9OWC/p1X9lfZ2sAF1VUkXg/j6pTvhsS+yDk3tZDbdJ8HyyN4NFZIN5H/2K8c+GgUy4zfnO2/2O5FOtArzzpo/z9GCaO4rbPL8OQ1KIRKC1CbBqLkqsHZjhTLHZ46EQFxODlCEhoLTxfB1mwxjLePSEROwxgT4zAP6hx0uRKqby73yx7BX51thg+xPZ5U04wI7ts76YWNAUFwd8Acj+HkxrTekAKacFkK6U9Me118OpZwgBXe4TXnpcfh4/X8grT21TZm4vxk0YESlmkzqIFQYnsPLoHVGmowLbNpntBe/iDIEs+pTcVgtONXcBA0DLPkcgqKxMh2yUmZP50w=";

    public void pushCurrentTextures() {
        File pFile = new File("plugins/Rift/data/textures/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("TextureValue", selfTextureValue);
            pData.set("TextureSignature", selfTextureSignature);
            pData.save(pFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pullCurrentTextures() {
        File pFile = new File("plugins/Rift/data/textures/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("TextureValue")) {
            defaultTextureValue = pData.getString("TextureValue");
            defaultTextureSignature = pData.getString("TextureSignature");
        } else {
            getTextureMap();
            pushCurrentTextures();
        }
    }

    public String getTextureValue() {
        return selfTextureValue;
    }

    public String getTextureSignature() {
        return selfTextureSignature;
    }

    public void getTextureMap() {
        if (main.getTextureValues().containsKey(player.getUniqueId())) {
            selfTextureValue = main.getTextureValues().get(player.getUniqueId());
            selfTextureSignature = main.getTextureSigs().get(player.getUniqueId());
        } else {
            try {
                URL url = new URL("https://api.mineskin.org/generate/user/" + player.getUniqueId());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(response.toString());

                JsonElement ja = element.getAsJsonObject().get("data");
                JsonElement texture = ja.getAsJsonObject().get("texture");

                selfTextureValue = texture.getAsJsonObject().get("value").toString().replaceAll("\"", "");
                selfTextureSignature = texture.getAsJsonObject().get("signature").toString().replaceAll("\"", "");

                main.getTextureValues().put(player.getUniqueId(), selfTextureValue);
                main.getTextureSigs().put(player.getUniqueId(), selfTextureSignature);
            } catch (IOException e) {
                selfTextureValue = defaultTextureValue;
                selfTextureSignature = defaultTextureSignature;

                main.getTextureValues().put(player.getUniqueId(), selfTextureValue);
                main.getTextureSigs().put(player.getUniqueId(), selfTextureSignature);
                e.printStackTrace();
            }
        }
    }

    public List<Player> getSortedOnlinePlayers() {
        List<Player> pl = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (main.getChat().getPlayerPrefix(p).contains("Owner") && !pl.contains(p)) {
                pl.add(p);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (main.getChat().getPlayerPrefix(p).contains("Admin") && !pl.contains(p)) {
                pl.add(p);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (main.getChat().getPlayerPrefix(p).contains("Mod") && !pl.contains(p)) {
                pl.add(p);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (main.getChat().getPlayerPrefix(p).contains("Helper") && !pl.contains(p)) {
                pl.add(p);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!pl.contains(p)) {
                pl.add(p);
            }
        }
        return pl;
    }

    public void updatePlayerCount() {
        int tl = 20;
        String gpName = "#" + tl;
        String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
        uuid+=tl;
        GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
        prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
        String name = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "♦ ONLINE PLAYERS [" + Bukkit.getOnlinePlayers().size() + "]";
        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), 20000, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(Main.color(name)));
        PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
        try {
            main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void updatePartyCount() {
        int tl = 0;
        String gpName = "#0" + tl;
        String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
        uuid+= "0" + tl;
        GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
        prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
        String name;
        if (main.getPM().hasParty(player)) {
            name = ChatColor.YELLOW + "" + ChatColor.BOLD + "♦ PARTY MEMBERS [" + main.getPM().getParty(player).getPlayers().size() + "]";
        } else {
            name = ChatColor.YELLOW + "" + ChatColor.BOLD + "♦ PARTY MEMBERS [0]";
        }
        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), 20000, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(Main.color(name)));
        PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
        try {
            main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void nameUpdate(Player pl, boolean placeholder) {
        Scoreboard sc = Main.getInstance().getSC();
        String teamName = "z" + pl.getDisplayName();
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }
        Team real = null;
        for (Team t : sc.getTeams()) {
            if (t.getName().equals(teamName)) {
                real = t;
                real.addEntry(pl.getName());
                break;
            }
        }
        if (real == null) {
            real = sc.registerNewTeam(teamName);
        }
        if (placeholder) {
            Main.getInstance().getFake().addEntry(pl.getName());
        } else {
            Chat c = Main.getInstance().getChat();
            String prefix = Main.color(c.getPlayerPrefix(pl));
            String suffix = Main.color(c.getPlayerSuffix(pl));
            if (prefix.length() > 2) {
                real.setPrefix(prefix + " ");
            } else {
                real.setPrefix(prefix);
            }
            real.setSuffix(" " + suffix);

            /*String color = "&7";
            if (pl.hasPermission("core.owner")) {
                color = "&e";
            }
            String name = Main.color(color + pl.getDisplayName() + "&f");
            if (name.length() > 16) {
                name = name.substring(0, 16);
            }

            GameProfile pprofile = ((CraftPlayer) pl).getHandle().getProfile();
            Field ff;
            try {
                ff = pprofile.getClass().getDeclaredField("name");
                ff.setAccessible(true);
                ff.set(pprofile, name);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }*/
        }

        pl.setScoreboard(sc);
    }

    public void tabUpdate() {
        nameUpdate(player, false);

        PacketContainer tabPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        WrappedChatComponent header = WrappedChatComponent.fromText(Main.color("\n&d&lThe Rift &7[1.15.2]\n"));

        WrappedChatComponent footer = WrappedChatComponent.fromText(Main.color("\n&5therift.net\n"));
        tabPacket.getChatComponents().write(0, header);
        tabPacket.getChatComponents().write(1, footer);
        try {
            main.getProtocolManager().sendServerPacket(player, tabPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        List<Player> partyL = new ArrayList<>();
        for (TabSlot t : partySlots.getTs()) {
            if (t.getPlayer() != null) {
                if (!t.getPlayer().isOnline()) {
                    continue;
                }
                partyL.add(t.getPlayer());
            } else {
                break;
            }
        }

        Chat c = Main.getInstance().getChat();
        if (player != null && main.getPM().hasParty(player)) {

            boolean updatePartyNumber = false;

            partyL = new ArrayList<>();
            for (TabSlot t : partySlots.getTs()) {
                if (t.getPlayer() != null && main.getPM().getParty(player).getPlayers().contains(t.getPlayer())) {
                    partyL.add(t.getPlayer());
                } else {
                    break;
                }
            }
            int index = 0;
            for (int tl : party) {
                int tlIndex = tl - party.get(0);
                if (index >= main.getPM().getParty(player).getPlayers().size()) {
                    if (partySlots.getTs().get(tlIndex).getPlayer() != null) {
                        updatePartyNumber = true;
                        PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                        fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                        String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
                        if (tl < 10) {
                            uuid += "0" + tl;
                        } else {
                            uuid += tl;
                        }
                        String gpName;
                        if (tl < 10) {
                            gpName = "#0" + tl;
                        } else {
                            gpName = "#" + tl;
                        }
                        GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
                        prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
                        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), 20000, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
                        fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
                        try {
                            main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        partySlots.getTs().get(tlIndex).wipe();
                    } else {
                        break;
                    }
                } else {
                    PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                    fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                    String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
                    if (tl < 10) {
                        uuid += "0" + tl;
                    } else {
                        uuid += tl;
                    }
                    Player target = ((Player) main.getPM().getParty(player).getPlayers().toArray()[index]);
                    if (partySlots.getTs().get(tlIndex).getPlayer() != null && target.equals(partySlots.getTs().get(tlIndex).getPlayer()) && (partySlots.getTs().get(tlIndex).getPrefix().equals(c.getPlayerPrefix(target)) && partySlots.getTs().get(tlIndex).getSuffix().equals(c.getPlayerSuffix(target)))) {
                        index++;
                        continue;
                    }
                    updatePartyNumber = true;
                    partySlots.getTs().get(tlIndex).put(target, c.getPlayerPrefix(target), c.getPlayerSuffix(target));
                    String suffix = Main.color(c.getPlayerSuffix(target));
                    String name = target.getDisplayName();
                    if (!suffix.isEmpty()) {
                        name = name + " " + suffix;
                    }
                    if (target.equals(player)) {
                        name = Main.color("&6") + name;
                    } else {
                        name = Main.color("&7") + name;
                    }
                    String gpName;
                    if (tl < 10) {
                        gpName = "#0" + tl;
                    } else {
                        gpName = "#" + tl;
                    }
                    GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
                    prof.getProperties().put("textures", new Property("textures", main.getRP(partySlots.getTs().get(tlIndex).getPlayer()).selfTextureValue, main.getRP(partySlots.getTs().get(tlIndex).getPlayer()).selfTextureSignature));
                    //GameProfile prof = new GameProfile(null, tl + "");
                    PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), ((CraftPlayer)target).getHandle().ping, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(name));
                    fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
                    try {
                        main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                index++;
            }
            if (updatePartyNumber) {
                updatePartyCount();
            }
        } else if (!partyL.isEmpty()){
            updatePartyCount();
            int index = 0;
            for (TabSlot t : partySlots.getTs()) {
                if (index >= partyL.size()) {
                    break;
                }
                PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
                if (t.getTl() < 10) {
                    uuid += "0" + t.getTl();
                } else {
                    uuid += t.getTl();
                }

                String gpName;
                if (t.getTl() < 10) {
                    gpName = "#0" + t.getTl();
                } else {
                    gpName = "#" + t.getTl();
                }
                GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);

                prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
                //GameProfile prof = new GameProfile(null, tl + "");
                PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), 20000, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
                fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
                try {
                    main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                t.wipe();
                index++;
            }
        }

        List<Player> plyrs = getSortedOnlinePlayers();

        boolean updatePlayerCount = false;

        int index = 0;
        for (int tl : players) {
            int tlIndex = tl - players.get(0);
            if (index >= Bukkit.getOnlinePlayers().size()) {
                if (playerSlots.getTs().get(tlIndex).getPlayer() != null) {
                    updatePlayerCount = true;
                    PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                    fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                    String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
                    if (tl < 10) {
                        uuid += "0" + tl;
                    } else {
                        uuid += tl;
                    }
                    String gpName;
                    if (tl < 10) {
                        gpName = "#0" + tl;
                    } else {
                        gpName = "#" + tl;
                    }

                    GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
                    prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
                    PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), 20000, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""));
                    fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
                    try {
                        main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    playerSlots.getTs().get(tlIndex).wipe();
                } else {
                    break;
                }
            } else {
                PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
                fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
                if (tl < 10) {
                    uuid += "0" + tl;
                } else {
                    uuid += tl;
                }
                Player target = ((Player) plyrs.toArray()[index]);
                if (playerSlots.getTs().get(tlIndex).getPlayer() != null && target.equals(playerSlots.getTs().get(tlIndex).getPlayer()) && (playerSlots.getTs().get(tlIndex).getPrefix().equals(c.getPlayerPrefix(target)) && playerSlots.getTs().get(tlIndex).getSuffix().equals(c.getPlayerSuffix(target)))) {
                    index++;
                    continue;
                }
                updatePlayerCount = true;
                playerSlots.getTs().get(tlIndex).put(target, c.getPlayerPrefix(target), c.getPlayerSuffix(target));
                String prefix = Main.color(c.getPlayerPrefix(target));
                String suffix = Main.color(c.getPlayerSuffix(target));
                String name = target.getDisplayName();
                if (prefix.length() > 2) {
                    name = prefix + " " + name;
                } else {
                    name = prefix + name;
                }
                if (!suffix.isEmpty()) {
                    name = name + " " + suffix;
                }

                if (name.length() > 39) {
                    name = name.substring(0, 40);
                }

                String gpName;
                if (tl < 10) {
                    gpName = "#0" + tl;
                } else {
                    gpName = "#" + tl;
                }
                GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);
                prof.getProperties().put("textures", new Property("textures", main.getRP(playerSlots.getTs().get(tlIndex).getPlayer()).selfTextureValue, main.getRP(playerSlots.getTs().get(tlIndex).getPlayer()).selfTextureSignature));
                PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), ((CraftPlayer)target).getHandle().ping, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(name));
                fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
                try {
                    main.getProtocolManager().sendServerPacket(player, fakePlayerPacket);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        if (updatePlayerCount) {
            updatePlayerCount();
        }
    }

    public void tabSetup() {
        getTextureMap();
        pushCurrentTextures();
        partySlots = new TablistSlots();
        playerSlots = new TablistSlots();
        for (Integer i : players) {
            playerSlots.getTs().add(new TabSlot(i, null, null, null));
        }
        for (Integer i : party) {
            partySlots.getTs().add(new TabSlot(i, null, null, null));
        }
        Chat c = Main.getInstance().getChat();
        List<Player> plyrs = getSortedOnlinePlayers();
        Player p = player;
        int index = 0;
        for (TabSlot t : playerSlots.getTs()) {
            if (index < plyrs.size()) {
                t.put(plyrs.get(index), c.getPlayerPrefix(plyrs.get(index)), c.getPlayerSuffix(plyrs.get(index)));
            } else {
                break;
            }
            index++;
        }
        int tl = 0;
        while (tl <= 79) {
            PacketContainer fakePlayerPacket = main.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
            fakePlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            String uuid = "7af87a08-170a-49be-8a1d-7dc8a89ba3";
            if (tl < 10) {
                uuid+="0" + tl;
            } else {
                uuid+=tl;
            }

            String gpName;
            if (tl < 10) {
                gpName = "#0" + tl;
            } else {
                gpName = "#" + tl;
            }
            GameProfile prof = new GameProfile(UUID.fromString(uuid), gpName);

            String name = "";
            if (tl == 0) {
                name = ChatColor.YELLOW + "" + ChatColor.BOLD + "♦ PARTY MEMBERS [0]";
            }
            if (tl == 20) {
                name = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "♦ ONLINE PLAYERS [" + Bukkit.getOnlinePlayers().size() + "]";
            }
            if (tl == 60) {
                name = ChatColor.AQUA + "" + ChatColor.BOLD + "♦ TOWN MEMBERS [0]";
            }

            name = Main.color(name);
            int latency = 20000;
            if (players.contains(tl) && playerSlots.getTs().get(tl - 21).getPlayer() != null) {
                name = playerSlots.getTs().get(tl - 21).getPlayer().getDisplayName();

                String prefix = Main.color(c.getPlayerPrefix(playerSlots.getTs().get(tl - 21).getPlayer()));
                String suffix = Main.color(c.getPlayerSuffix(playerSlots.getTs().get(tl - 21).getPlayer()));

                if (prefix.length() > 2) {
                    name = prefix + " " + name;
                } else {
                    name = prefix + name;
                }
                if (!suffix.isEmpty()) {
                    name = name + " " + suffix;
                }

                if (name.length() > 39) {
                    name = name.substring(0, 40);
                }

                latency = ((CraftPlayer)playerSlots.getTs().get(tl-21).getPlayer()).getHandle().ping;
                prof.getProperties().put("textures", new Property("textures", main.getTextureValues().get(playerSlots.getTs().get(tl-21).getPlayer().getUniqueId()), main.getTextureSigs().get(playerSlots.getTs().get(tl-21).getPlayer().getUniqueId())));
            } else {
                prof.getProperties().put("textures", new Property("textures", defaultTextureValue, defaultTextureSignature));
            }

            PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromHandle(prof), latency, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(name));
            fakePlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
            try {
                main.getProtocolManager().sendServerPacket(p, fakePlayerPacket);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            tl++;
        }
        nameUpdate(player, false);

        tabUpdate();
    }

    /*
    Cleanup after yourself!
     */

    public void scrub() {
        if (player.hasPotionEffect(PotionEffectType.SLOW) && player.getPotionEffect(PotionEffectType.SLOW).getAmplifier() == 2 && player.getPotionEffect(PotionEffectType.SLOW).getDuration() > 999) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        for (StatusObject s : so) {
            s.scrub();
        }
        so.clear();
        if (main.getCM().getFall().contains(player.getUniqueId())) {
            main.getCM().getFall().remove(player.getUniqueId());
        }
        if (main.getCM().getFallMap().containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(main.getCM().getFallMap().get(player.getUniqueId()));
            main.getCM().getFallMap().remove(player.getUniqueId());
        }
        BukkitScheduler scheduler = Bukkit.getScheduler();
        List<String> skillsToRemove = new ArrayList<>();
        skillsToRemove.addAll(getToggles());
        for (String s : skillsToRemove) {
            getSkillFromName(s).toggleEnd(player);
        }
        skillsToRemove = new ArrayList<>();

        skillsToRemove.addAll(getPassives());
        for (String s : skillsToRemove) {
            getPassives().remove(s);
            List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
            for (Map<Integer, String> map : getPassiveTasks()) {
                if (map.containsValue(s)) {
                    tasksToRemove.add(map);
                }
            }

            for (Map<Integer, String> map : tasksToRemove) {
                scheduler.cancelTask((int) map.keySet().toArray()[0]);
                getPassiveTasks().remove(map);
            }
        }
        board.scrub();
        board = null;
        damages = new ArrayList<>();
        cooldowns = new HashMap<>();
        statuses = new ArrayList<>();
        passives = new ArrayList<>();
        passiveTasks = new ArrayList<>();
        toggleTasks = new ArrayList<>();
        toggles = new ArrayList<>();
        player = null;
        pclass = null;
        chatChannel = null;

        playerSlots.scrub();
        partySlots.scrub();

        super.scrub();
    }
}
