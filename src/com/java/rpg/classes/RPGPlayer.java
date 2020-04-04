package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.modifiers.utility.Damage;
import com.java.rpg.classes.casting.Skillboard;
import com.java.rpg.classes.utility.StatusObject;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.player.Items;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class RPGPlayer extends Leveleable {

    private Main main = Main.getInstance();

    private int pstrength;

    private int skillpoints;

    public int getSP() {
        return skillpoints;
    }

    public void setSP(int i) {
        skillpoints = i;
    }

    private Player player;
    private PlayerClass pclass;

    private double currentMana;

    private Skillboard board;

    private List<Damage> damages;

    public double getArmor() {
        double armor = 0;
        if (player == null) {
            return 0;
        }
        for (ItemStack i : player.getInventory().getArmorContents()) {
            if (Items.getDurability(i) <= 0) {

            } else {
                armor += Items.getArmor(i);
            }
        }
        if (getPClass() == null) {
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
        int index = 0;
        for (Skill s : main.getRP(p).getPClass().getSkills()) {
            if (main.getRP(p).getSkillLevels().get(s.getName()) == 0) {
                pSkills.add(s);
            } else {
                if (!main.getRP(p).getPClass().getSuperSkills().isEmpty() && main.getRP(p).getPClass().getSuperSkills().get(index) != null) {
                    pSkills.add(main.getRP(p).getPClass().getSuperSkills().get(index));
                }
            }
            index++;
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
        double dif = (m - getCMana());
        currentMana = Math.min(m, pclass.getCalcMana(getLevel()));
        /*
        if (dif > 0) {
            DecimalFormat df = new DecimalFormat("#");
            Hologram magic = new Hologram(player, player.getLocation(), "&9&l+" + df.format(dif), Hologram.HologramType.DAMAGE);
            magic.rise();
        } else {
            DecimalFormat df = new DecimalFormat("#");
            Hologram magic = new Hologram(player, player.getLocation(), "&9&l" + df.format(dif), Hologram.HologramType.DAMAGE);
            magic.rise();
        }
        */
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
        target = null;
        pullFiles();
        pushFiles();
        pullFiles();
        board = new Skillboard(p);
        pstrength = 100;

        damages = new ArrayList<>();

        walkspeedS.getStatuses().add(new StatusValue("Init:Walkspeed", baseWS, 0, 0, true));
    }

    public List<Damage> getDamages() {
        return damages;
    }

    public Map<String, Integer> getSkillLevels() {
        return skillLevels;
    }

    public void setPStrength(int d) {
        pstrength = d;
    }

    public int getPStrength() {
        return pstrength;
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

    public void pushFiles() {
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            if (pData.contains("Username")) {
                if (pData.getString("Username").equalsIgnoreCase(player.getName())) {
                    pData.set("Username", player.getName());
                } else {
                    pData.set("PreviousUsername", pData.getString("Username"));
                    pData.set("Username", player.getName());
                }
            } else {
                pData.set("Username", player.getName());
            }
            pData.set("LastSeen", System.currentTimeMillis());
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
            /*if (!pData.contains(name + "SP")) {
                pData.set(name + "SP", 0);
                skillpoints = 0;
            } else {
                pData.set(name + "SP", skillpoints);
            }*/
            /*pData.set(name + "AD", pclass.getBaseAD());
            pData.set(name + "AP", pclass.getBaseAP());*/
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
            String cd = "";
            for (String skill : cooldowns.keySet()) {
                cd+=skill + ":" + cooldowns.get(skill) + ",";
            }
            if (cd.contains(",")) {
                cd = cd.substring(0, cd.length() - 1);
            }
            pData.set("Cooldowns", cd);

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
            if (pData.contains("Username")) {
                if (pData.getString("Username").equalsIgnoreCase(player.getName())) {
                    pData.set("Username", player.getName());
                } else {
                    pData.set("PreviousUsername", pData.getString("Username"));
                    pData.set("Username", player.getName());
                }
            } else {
                pData.set("Username", player.getName());
            }
            pData.set("LastSeen", System.currentTimeMillis());
            String name = RPGConstants.defaultClassName;
            pclass = main.getCM().getPClassFromString(pData.getString("Current Class"));
            if (pclass instanceof PlayerClass) {
                name = pclass.getName();
            }
            setLevel(pData.getInt(name + "Level"));
            setExp(pData.getDouble(name + "Exp"));
            currentMana = (pData.getInt(name + "CMana"));

            /*if (pData.contains(name + "SP")) {
                skillpoints = pData.getInt(name + "SP");
            } else {
                pData.set(name + "SP", 0);
                skillpoints = 0;
            }*/

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
            } else {
                pushFiles();
            }
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
            /*
            if (pData.contains(name + "AD")) {
                ad = pData.getDouble(name + "AD");
            }

            if (pData.contains(name + "AP")) {
                ap = pData.getDouble(name + "AP");
            }
            */
            updateStats();
        } else {
            pushFiles();
            pullFiles();
        }
    }

    public void noneClass() {
        cooldowns.clear();
        double hp = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        pushFiles();
        pclass = main.getCM().getPClassFromString(RPGConstants.defaultClassName);
        File pFile = new File("plugins/Rift/data/classes/" + player.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        try {
            pData.set("Current Class", RPGConstants.defaultClassName);
            pData.save(pFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pullFiles();
        currentMana = 0;
        player.setHealth(hp * RPGConstants.defaultHP);
        updateStats();
    }

    public boolean changeClass(PlayerClass pc) {

        for (StatusObject sz : so) {
            for (Skill s : pclass.getSkills()) {
                sz.clearTitleIndiscrim(s.getName(), player);
            }
            for (Skill s : pclass.getSuperSkills()) {
                sz.clearTitleIndiscrim(s.getName(), player);
            }
        }

        double hp = player.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        pushFiles();
        if (pclass != null && pc.getName().equalsIgnoreCase(pclass.getName())) {
            return false;
        }
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
        float actualWs = Math.max(0, Float.valueOf(String.valueOf(df.format((getWalkspeed().getValue() * 1.0 + getWalkSpeedS().getValue() * 1.0) / 100.0))));
        if (currentWs != actualWs) {
            player.setWalkSpeed(actualWs);
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
        //}
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
        //}
        return null;
    }

    public String castSkill(String name) {
        if (pclass instanceof PlayerClass) {
            Skill s = null;
            for (Skill st : pclass.getSkills()) {
                if (name.equalsIgnoreCase(st.getName())) {
                    if (skillLevels.get(st.getName()) != 0) {
                        return "AlreadySuper";
                    }
                    s = st;
                }
            }
            for (Skill st : pclass.getSuperSkills()) {
                if (name.equalsIgnoreCase(st.getName())) {
                    if (skillLevels.get(pclass.getSkills().get(pclass.getSuperSkills().indexOf(st)).getName()) == 0) {
                        return "NotSuper";
                    }
                    s = st;
                }
            }
            if (s != null) {
            /*for (Skill s : pclass.getSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {*/
                    if (s.getLevel() <= getLevel()) {
                        if (s.getManaCost() <= currentMana || (s.getType().contains("TOGGLE") && getToggles().contains(s.getName()))) {
                            if (stun.getValue() > 0) {
                                return "Stunned";
                            }
                            String cd = getCooldown(s);
                            final BukkitScheduler scheduler = Bukkit.getScheduler();
                            if (cd.equalsIgnoreCase("Warmup")) {
                                if (!statuses.contains("Warmup" + s.getName())) {
                                    getStatuses().add("Warmup" + s.getName());
                                } else {
                                    return "AlreadyCasting";
                                }

                                if (s.getType().contains("TARGET")) {
                                    if (getNearestTargetInSight(player, s.getTargetRange() * 2) instanceof LivingEntity) {
                                        target = getNearestTargetInSight(player, s.getTargetRange() * 2);
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
                                }

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
                                String nameS = s.getName();
                                int warmup = s.getWarmup();
                                int manaCost = s.getManaCost();
                                String type = s.getType();
                                final Skill sk = s;
                                Player p = player;
                                final int task = scheduler.scheduleSyncDelayedTask(main, new Runnable() {
                                    public void run() {
                                        if (!p.isOnline() || player.isDead()) {
                                            if (p.isOnline() && p.isDead()) {
                                                statuses.remove("Warmup" + nameS);
                                                target = null;
                                            }
                                        } else {
                                            if (cooldowns.containsKey(nameS)) {
                                                cooldowns.replace(nameS, System.currentTimeMillis());
                                            } else {
                                                cooldowns.put(nameS, System.currentTimeMillis());
                                            }
                                            if (currentMana >= manaCost) {
                                                if (type.contains("TARGET")) {
                                                    if (target == null || (target != null && target.isDead())) {
                                                        statuses.remove("Warmup" + nameS);
                                                        return;
                                                    }
                                                    sk.target(player, target);
                                                    target = null;
                                                } else {
                                                    sk.cast(player);
                                                }
                                                currentMana -= manaCost;
                                            } else {
                                                Main.msg(player, "&cOut of mana to cast " + nameS + "!");
                                            }
                                            statuses.remove("Warmup" + nameS);
                                        }
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
                                        if (!getStatuses().contains("Warmup" + nameS) || (target != null) && target.isDead() && type.contains("TARGET") || (target == null && type.contains("TARGET"))) {
                                            scheduler.cancelTask(task);
                                            if (target == null && type.contains("TARGET")) {
                                                Main.msg(player, "&cYour target died!");
                                                statuses.remove("Warmup" + name);
                                            }
                                            if (target != null && target.isDead() && type.contains("TARGET")) {
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
                            if (cd.equalsIgnoreCase("Invalid")) {
                                return "Failure";
                            }
                            if (cd.contains("CD:")) {
                                return cd;
                            }
                            if (s.getType().contains("TOGGLE")) {
                                if (getToggles().contains(s.getName())) {
                                    s.toggleEnd(player);
                                } else {
                                    getToggles().add(s.getName());
                                    Map<Integer, String> map = new HashMap<>();
                                    map.put(s.toggleInit(player), s.getName());
                                    getToggleTasks().add(map);
                                    currentMana -= s.getManaCost();
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
                                    if (cooldowns.containsKey(s.getName())) {
                                        cooldowns.replace(s.getName(), System.currentTimeMillis());
                                    } else {
                                        cooldowns.put(s.getName(), System.currentTimeMillis());
                                    }
                                }
                                return "CastedSkill";
                            }
                            if (s.getType().contains("TARGET")) {
                                if (getNearestTargetInSight(player, s.getTargetRange() * 2) instanceof LivingEntity) {
                                    target = getNearestTargetInSight(player, s.getTargetRange() * 2);
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
                                if (cooldowns.containsKey(s.getName())) {
                                    cooldowns.replace(s.getName(), System.currentTimeMillis());
                                } else {
                                    cooldowns.put(s.getName(), System.currentTimeMillis());
                                }
                                currentMana -= s.getManaCost();
                                s.target(player, target);
                                if (indexesToRemove != null && indexesToRemove.size() > 0) {
                                    return "Interrupted";
                                }
                                target = null;
                                return "CastedSkill";
                            }
                            if (cd.equalsIgnoreCase("Casted")) {
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
                                if (cooldowns.containsKey(s.getName())) {
                                    cooldowns.replace(s.getName(), System.currentTimeMillis());
                                } else {
                                    cooldowns.put(s.getName(), System.currentTimeMillis());
                                }
                                currentMana -= s.getManaCost();
                                s.cast(player);
                                if (indexesToRemove != null && indexesToRemove.size() > 0) {
                                    return "Interrupted";
                                }

                                if (!s.getType().contains("CAST")) {
                                    return "CannotCast";
                                }

                                return "CastedSkill";
                                //return s.getFlavor();
                            }
                        }
                        return "NoMana";
                    }
                    return "Level" + s.getLevel();
                //}
            }
        }
        return "Invalid";
    }

    public String getCooldown(Skill s) {
        if (pclass instanceof PlayerClass) {
            for (Skill sk : getSkillsAll()) {
                if (sk.equals(s)) {
                    if (cooldowns.containsKey(s.getName())) {
                        long timeLeft = System.currentTimeMillis() - cooldowns.get(s.getName());
                        if (timeLeft * 0.001 * 20 >= s.getCooldown()) {
                            if (s.getWarmup() != 0) {
                                return "Warmup";
                            }
                            return "Casted";
                        } else {
                            DecimalFormat dF = new DecimalFormat("#.#");
                            String cd = dF.format((s.getCooldown() / 20.0) - (timeLeft/1000.0));
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

    public Skill getSkillFromSuper(String name) {
        if (pclass != null) {
            for (Skill s : pclass.getSuperSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    return pclass.getSkills().get(pclass.getSuperSkills().indexOf(s));
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
            }
            for (Skill s : pclass.getSuperSkills()) {
                if (name.equalsIgnoreCase(s.getName())) {
                    return s;
                }
            }
        }
        return null;
    }

    public void refreshCooldowns() {
        if (pclass != null) {
            for (int i = cooldowns.keySet().size() - 1; i >= 0; i--) {
                String name = cooldowns.keySet().toArray()[i].toString();
                long timeLeft = System.currentTimeMillis() - cooldowns.get(name);
                if (getSkillFromName(name) != null) {
                    if (timeLeft * 0.001 * 20 >= getSkillFromName(name).getCooldown()) {
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
        for (String s : getToggles()) {
            skillsToRemove.add(s);
        }
        for (String s : skillsToRemove) {
            getSkillFromName(s).toggleEnd(player);
        }
        skillsToRemove = new ArrayList<>();

        for (String s : getPassives()) {
            skillsToRemove.add(s);
        }
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
        super.scrub();
    }
}
