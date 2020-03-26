package com.java.rpg.classes;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.java.Main;
import com.java.rpg.mobs.BiomeSettings;
import com.java.rpg.player.XPList;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MobEXP implements Listener {

    private Main main = Main.getInstance();

    private static Map<Biome, LevelRange> biomeLevels;

    public static Map<Biome, LevelRange> getBL() {
        return biomeLevels;
    }

    private static List<EntityType> peaceful;

    private static Map<EntityType, Double> xpmods;

    private static BiomeSettings settings = new BiomeSettings();

    public MobEXP() {
        biomeLevels = new LinkedHashMap<>();

        biomeLevels.clear();
        biomeLevels.put(Biome.BADLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.BADLANDS_PLATEAU, new LevelRange(45, 50));
        biomeLevels.put(Biome.BAMBOO_JUNGLE, new LevelRange(30, 35));
        biomeLevels.put(Biome.BAMBOO_JUNGLE_HILLS, new LevelRange(30, 35));
        biomeLevels.put(Biome.BEACH, new LevelRange(1, 10));
        biomeLevels.put(Biome.BIRCH_FOREST, new LevelRange(1, 15));
        biomeLevels.put(Biome.BIRCH_FOREST_HILLS, new LevelRange(1, 10));
        biomeLevels.put(Biome.COLD_OCEAN, new LevelRange(10, 25));
        biomeLevels.put(Biome.DARK_FOREST, new LevelRange(20, 35));
        biomeLevels.put(Biome.DARK_FOREST_HILLS, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_COLD_OCEAN, new LevelRange(30, 40));
        biomeLevels.put(Biome.DEEP_FROZEN_OCEAN, new LevelRange(40, 50));
        biomeLevels.put(Biome.DEEP_LUKEWARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DEEP_WARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.DESERT, new LevelRange(15, 25));
        biomeLevels.put(Biome.DESERT_HILLS, new LevelRange(10, 20));
        biomeLevels.put(Biome.DESERT_LAKES, new LevelRange(5, 10));
        biomeLevels.put(Biome.END_BARRENS, new LevelRange(40, 50));
        biomeLevels.put(Biome.END_HIGHLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.END_MIDLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.ERODED_BADLANDS, new LevelRange(40, 50));
        biomeLevels.put(Biome.FLOWER_FOREST, new LevelRange(1, 15));
        biomeLevels.put(Biome.FOREST, new LevelRange(1, 10));
        biomeLevels.put(Biome.FROZEN_OCEAN, new LevelRange(30, 50));
        biomeLevels.put(Biome.FROZEN_RIVER, new LevelRange(20, 35));
        biomeLevels.put(Biome.GIANT_SPRUCE_TAIGA, new LevelRange(25, 30));
        biomeLevels.put(Biome.GIANT_SPRUCE_TAIGA_HILLS, new LevelRange(25, 30));
        biomeLevels.put(Biome.GIANT_TREE_TAIGA, new LevelRange(25, 35));
        biomeLevels.put(Biome.GIANT_TREE_TAIGA_HILLS, new LevelRange(25, 35));
        biomeLevels.put(Biome.GRAVELLY_MOUNTAINS, new LevelRange(10, 20));
        biomeLevels.put(Biome.ICE_SPIKES, new LevelRange(50, 50));
        biomeLevels.put(Biome.JUNGLE, new LevelRange(30, 40));
        biomeLevels.put(Biome.JUNGLE_EDGE, new LevelRange(25, 35));
        biomeLevels.put(Biome.JUNGLE_HILLS, new LevelRange(30, 40));
        biomeLevels.put(Biome.LUKEWARM_OCEAN, new LevelRange(20, 30));
        biomeLevels.put(Biome.MODIFIED_BADLANDS_PLATEAU, new LevelRange(45, 50));
        biomeLevels.put(Biome.MODIFIED_GRAVELLY_MOUNTAINS, new LevelRange(10, 25));
        biomeLevels.put(Biome.MODIFIED_JUNGLE, new LevelRange(30, 50));
        biomeLevels.put(Biome.MODIFIED_JUNGLE_EDGE, new LevelRange(25, 40));
        biomeLevels.put(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, new LevelRange(0, 0));
        biomeLevels.put(Biome.MOUNTAIN_EDGE, new LevelRange(10, 20));
        biomeLevels.put(Biome.MOUNTAINS, new LevelRange(15, 25));
        biomeLevels.put(Biome.MUSHROOM_FIELD_SHORE, new LevelRange(20, 30));
        biomeLevels.put(Biome.MUSHROOM_FIELDS, new LevelRange(20, 30));
        biomeLevels.put(Biome.NETHER, new LevelRange(20, 50));
        biomeLevels.put(Biome.OCEAN, new LevelRange(5, 30));
        biomeLevels.put(Biome.PLAINS, new LevelRange(1, 10));
        biomeLevels.put(Biome.RIVER, new LevelRange(1, 15));
        biomeLevels.put(Biome.SAVANNA, new LevelRange(10, 20));
        biomeLevels.put(Biome.SAVANNA_PLATEAU, new LevelRange(10, 25));
        biomeLevels.put(Biome.SHATTERED_SAVANNA, new LevelRange(20, 35));
        biomeLevels.put(Biome.SHATTERED_SAVANNA_PLATEAU, new LevelRange(20, 40));
        biomeLevels.put(Biome.SMALL_END_ISLANDS, new LevelRange(45, 50));
        biomeLevels.put(Biome.SNOWY_BEACH, new LevelRange(15, 20));
        biomeLevels.put(Biome.SNOWY_MOUNTAINS, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA_HILLS, new LevelRange(15, 25));
        biomeLevels.put(Biome.SNOWY_TAIGA_MOUNTAINS, new LevelRange(20, 35));
        biomeLevels.put(Biome.SNOWY_TUNDRA, new LevelRange(10, 25));
        biomeLevels.put(Biome.STONE_SHORE, new LevelRange(5, 15));
        biomeLevels.put(Biome.SUNFLOWER_PLAINS, new LevelRange(1, 5));
        biomeLevels.put(Biome.SWAMP, new LevelRange(35, 45));
        biomeLevels.put(Biome.SWAMP_HILLS, new LevelRange(30, 40));
        biomeLevels.put(Biome.TAIGA, new LevelRange(15, 20));
        biomeLevels.put(Biome.TAIGA_HILLS, new LevelRange(10, 15));
        biomeLevels.put(Biome.TAIGA_MOUNTAINS, new LevelRange(20, 30));
        biomeLevels.put(Biome.TALL_BIRCH_FOREST, new LevelRange(5, 10));
        biomeLevels.put(Biome.TALL_BIRCH_HILLS, new LevelRange(5, 10));
        biomeLevels.put(Biome.THE_END, new LevelRange(50, 50));
        biomeLevels.put(Biome.THE_VOID, new LevelRange(50, 50));
        biomeLevels.put(Biome.WARM_OCEAN, new LevelRange(10, 20));
        biomeLevels.put(Biome.WOODED_BADLANDS_PLATEAU, new LevelRange(35, 45));
        biomeLevels.put(Biome.WOODED_HILLS, new LevelRange(5, 15));
        biomeLevels.put(Biome.WOODED_MOUNTAINS, new LevelRange(5, 20));

        xpmods = new HashMap<>();
        xpmods.clear();
        xpmods.put(EntityType.DONKEY, 0.1);
        xpmods.put(EntityType.BEE, 0.1);
        xpmods.put(EntityType.BAT , 0.05);
        xpmods.put(EntityType.BLAZE , 1.2);
        xpmods.put(EntityType.CAT , 0.1);
        xpmods.put(EntityType.CAVE_SPIDER , 1.1);
        xpmods.put(EntityType.CHICKEN , 0.1);
        xpmods.put(EntityType.COD , 0.1);
        xpmods.put(EntityType.COW , 0.1);
        xpmods.put(EntityType.CREEPER , 1.5);
        xpmods.put(EntityType.DOLPHIN , 0.75);
        xpmods.put(EntityType.DROWNED , 1.1);
        xpmods.put(EntityType.ELDER_GUARDIAN , 4.0);
        xpmods.put(EntityType.ENDER_DRAGON , 1000.0);
        xpmods.put(EntityType.ENDERMAN , 2.5);
        xpmods.put(EntityType.ENDERMITE , 1.1);
        xpmods.put(EntityType.EVOKER , 1.3);
        xpmods.put(EntityType.FOX , 0.2);
        xpmods.put(EntityType.GHAST , 1.25);
        xpmods.put(EntityType.GIANT , 3.0);
        xpmods.put(EntityType.GUARDIAN , 1.2);
        xpmods.put(EntityType.HORSE , 0.1);
        xpmods.put(EntityType.HUSK , 1.1);
        xpmods.put(EntityType.ILLUSIONER , 1.4);
        xpmods.put(EntityType.IRON_GOLEM , 1.5);
        xpmods.put(EntityType.LLAMA , 0.2);
        xpmods.put(EntityType.MULE , 0.1);
        xpmods.put(EntityType.OCELOT , 0.1);
        xpmods.put(EntityType.MAGMA_CUBE , 0.25);
        xpmods.put(EntityType.PANDA , 0.2);
        xpmods.put(EntityType.PARROT , 0.15);
        xpmods.put(EntityType.PHANTOM , 1.25);
        xpmods.put(EntityType.PIG , 0.1);
        xpmods.put(EntityType.PIG_ZOMBIE , 1.1);
        xpmods.put(EntityType.PILLAGER , 1.2);
        xpmods.put(EntityType.PLAYER , 5.0);
        xpmods.put(EntityType.POLAR_BEAR , 0.75);
        xpmods.put(EntityType.PUFFERFISH , 0.25);
        xpmods.put(EntityType.RABBIT , 0.1);
        xpmods.put(EntityType.RAVAGER , 1.1);
        xpmods.put(EntityType.SALMON , 0.1);
        xpmods.put(EntityType.SHEEP , 0.1);
        xpmods.put(EntityType.SHULKER , 0.9);
        xpmods.put(EntityType.SILVERFISH , 1.1);
        xpmods.put(EntityType.SKELETON , 1.0);
        xpmods.put(EntityType.SLIME , 0.25);
        xpmods.put(EntityType.SNOWMAN , 0.3);
        xpmods.put(EntityType.SPIDER , 0.9);
        xpmods.put(EntityType.SQUID , 0.2);
        xpmods.put(EntityType.STRAY , 1.0);
        xpmods.put(EntityType.SKELETON_HORSE , 0.3);
        xpmods.put(EntityType.TRADER_LLAMA , 0.25);
        xpmods.put(EntityType.TROPICAL_FISH , 0.1);
        xpmods.put(EntityType.VEX , 1.25);
        xpmods.put(EntityType.VILLAGER , 0.1);
        xpmods.put(EntityType.VINDICATOR , 1.3);
        xpmods.put(EntityType.WANDERING_TRADER , 1.0);
        xpmods.put(EntityType.WITCH , 1.0);
        xpmods.put(EntityType.WITHER , 100.0);
        xpmods.put(EntityType.WITHER_SKELETON , 1.9);
        xpmods.put(EntityType.WOLF , 1.0);
        xpmods.put(EntityType.ZOMBIE , 1.0);
        xpmods.put(EntityType.ZOMBIE_HORSE , 0.25);
        xpmods.put(EntityType.ZOMBIE_VILLAGER , 1.1);

        xp = new HashMap<>();

        peaceful = new ArrayList<>();
        peaceful.add(EntityType.HORSE);
        peaceful.add(EntityType.MULE);
        peaceful.add(EntityType.COW);
        peaceful.add(EntityType.MUSHROOM_COW);
        peaceful.add(EntityType.SHEEP);
        peaceful.add(EntityType.PIG);
        peaceful.add(EntityType.TROPICAL_FISH);
        peaceful.add(EntityType.COD);
        peaceful.add(EntityType.SQUID);
        peaceful.add(EntityType.RABBIT);
        peaceful.add(EntityType.TURTLE);
        peaceful.add(EntityType.SALMON);
        peaceful.add(EntityType.BAT);
        peaceful.add(EntityType.CAT);
        peaceful.add(EntityType.OCELOT);
        peaceful.add(EntityType.PUFFERFISH);
        peaceful.add(EntityType.PANDA);
        peaceful.add(EntityType.PARROT);
        peaceful.add(EntityType.LLAMA);
        peaceful.add(EntityType.SKELETON_HORSE);
        peaceful.add(EntityType.ZOMBIE_HORSE);
        peaceful.add(EntityType.DONKEY);
        peaceful.add(EntityType.DOLPHIN);
        peaceful.add(EntityType.CHICKEN);
        peaceful.add(EntityType.FOX);
        peaceful.add(EntityType.BEE);


        new BukkitRunnable() {
            public void run() {
                for (World w : Bukkit.getServer().getWorlds()) {
                    for (LivingEntity ent : w.getLivingEntities()) {
                        if (!(ent instanceof ArmorStand) && !(ent instanceof Player)) {
                            setupEnt(ent, -1);
                        }
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 10L);

        new BukkitRunnable() {
            public void run() {
                List<LivingEntity> remove = new ArrayList<>();
                for (LivingEntity ent : xp.keySet()) {
                    if (ent.isDead()) {
                        remove.add(ent);
                        xp.get(ent).scrub();
                    } else {
                        xp.get(ent).removeDc();
                    }
                }
                for (LivingEntity ent : remove) {
                    xp.remove(ent);
                }
            }
        }.runTaskTimer(Main.getInstance(), 10, 400);

    }

    public static void setExp(LivingEntity ent, double exp) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE, exp);

        /*
        if (ent.getMetadata("EXP") != null && ent.hasMetadata("EXP")) {
            ent.removeMetadata("EXP", Main.getInstance());
        }
        ent.setMetadata("EXP", new FixedMetadataValue(Main.getInstance(), exp));*/
    }

    public static double getExp(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE);
        }
        return 0;

        /*
        if (getLevel(ent) == -1 || !(Integer.valueOf(getLevel(ent)) instanceof Integer)) {
            return 0;
        }
        return (RPGConstants.mobXpScalar * Math.pow(getLevel(ent), RPGConstants.mobXpPow) + RPGConstants.mobXpBase) * (Math.random() * 0.1 + 1) * xpmods.get(ent.getType());*/
    }

    public static double calcExp(LivingEntity ent) {
        int level = getLevel(ent);
        return RPGConstants.mobExp.get(level) * (Math.random() * 0.06 + 1);
        //return (RPGConstants.mobXpScalar * Math.pow(level, RPGConstants.mobXpPow) + RPGConstants.mobXpBase) * (Math.random() * 0.1 + 1) * xpmods.get(ent.getType());
    }

    public static int getLevel(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER);
        }
        /*
        if (ent.getName().contains("Lv. ")) {
            String name = ChatColor.stripColor(ent.getName());
            name+=" ";
            name = name.substring(0, name.indexOf("Lv. ") + 6).trim();
            return Integer.valueOf(name.substring(name.indexOf("Lv. ") + 4));
        }*/
        return -1;
    }

    public static boolean hasLevel(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        return (data.has(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER));
    }

    public static void setLevel(LivingEntity ent, int level) {

        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER, level);
        setLevelVisual(ent, level);

        /*

        if (ent.getCustomName() == null) {
            if (ent.getName().contains("Lv.")) {
                ent.setCustomName(Main.color(ChatColor.stripColor(ent.getName()).substring(0, ChatColor.stripColor(ent.getName()).indexOf("Lv.") - 1).trim() + " &6Lv. " + level));
            } else {
                ent.setCustomName(Main.color(ChatColor.stripColor(ent.getName()).trim() + " &6Lv. " + level));
            }
        } else if (ent.getCustomName().contains("Lv.")){
            ent.setCustomName(Main.color(ChatColor.stripColor(ent.getCustomName()).substring(0, ChatColor.stripColor(ent.getCustomName()).indexOf("Lv.") - 1).trim() + " &6Lv. " + level));
        }*/
    }

    public static void setLevelVisual(LivingEntity ent, int level) {
        ent.setCustomName(Main.color("&f" + getNiceName(ent) + " &6Lv. " + level));
    }

    public static String getNiceName(LivingEntity ent) {
        //net.minecraft.server.v1_15_R1.Entity nmsEnt = CraftLivingEntity.getEntity((CraftServer) Bukkit.getServer(), ((CraftEntity) ent).getHandle()).getHandle();
        net.minecraft.server.v1_15_R1.Entity nmsEnt = ((CraftEntity) ent).getHandle();
        return WordUtils.capitalize((nmsEnt.getMinecraftKeyString().replace("minecraft:", "")).replace("_", " "));
    }

    public static double getArmor(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static void setArmor(LivingEntity ent, double armor) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE, armor);
    }

    public static void setMagicResist(LivingEntity ent, double mr) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE, mr);
    }

    public static double getMagicResist(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static double getDamageThreshold(LivingEntity ent){
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE);
        }
        return 0;
    }
    public static void setDamageThreshold(LivingEntity ent, double dt) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE, dt);
    }

    public static double getHPRegen(LivingEntity ent){
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE);
        }
        return 0;
    }
    public static void setHPRegen(LivingEntity ent, double r) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE, r);
    }

    public void scaleHealth(LivingEntity ent, int level, double modifier) {

        double hp = RPGConstants.mobHpBase + Math.pow(level, RPGConstants.mobHpLevelPow) * RPGConstants.mobHpLevelScalar * Math.pow(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/20.0, RPGConstants.mobHpBasePow);
        if (peaceful.contains(ent.getType())) {
            if (ent instanceof Horse) {
                hp = Math.max(hp, 10000);
            } else {
                hp = Math.max(Math.min(hp, 2000), 750);
            }
        }
        hp*=modifier;
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public void scaleDamage(LivingEntity ent, int level, double modifier) {
        if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            double damage = Math.pow(level, RPGConstants.mobDmgLevelPow) * RPGConstants.mobDmgLevelScalar * Math.sqrt(ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue()/4.0);
            damage += ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 3;
            damage *= modifier;
            ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        }

        if (ent instanceof Creeper) {
            Creeper creeper = (Creeper) ent;
            if (level >= 0 && level < 10) {
                creeper.setExplosionRadius(3);
                creeper.setMaxFuseTicks(40);
            } else if (level >= 10 && level < 20) {
                creeper.setExplosionRadius(3);
                creeper.setMaxFuseTicks(30);
            } else if (level >= 20 && level < 30) {
                creeper.setExplosionRadius(4);
                creeper.setMaxFuseTicks(30);
            } else if (level >= 30 && level < 40) {
                creeper.setExplosionRadius(4);
                creeper.setMaxFuseTicks(25);
            } else {
                creeper.setExplosionRadius(5);
                creeper.setMaxFuseTicks(20);
                if (level == 50) {
                    creeper.setPowered(true);
                }
            }
        }
    }


    private static Map<LivingEntity, XPList> xp;

    public Map<LivingEntity, XPList> getXP() {
        return xp;
    }

    public void setupEnt(LivingEntity ent, int lvl) {
        int level = lvl;
        if (lvl == -1) {
            level = biomeLevels.get(ent.getLocation().getBlock().getBiome()).getRandomLevel();
        }

        if (peaceful.contains(ent.getType())) {
            level = Math.max(0, Math.min(level, 5));
        }

        if (hasLevel(ent)) {
            level = getLevel(ent);
            if (ent instanceof Slime) {
                if (!(peaceful.contains(ent.getType()))) {
                    double damage = Math.pow(level, RPGConstants.mobDmgLevelPow) * RPGConstants.mobDmgLevelScalar * 0.1 + 5;
                    setDamageThreshold(ent, damage);
                    setArmor(ent, level * 3);
                    setMagicResist(ent, level * 2);
                }
                setHPRegen(ent, ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.005);
            }
        } else {
            if (ent instanceof Slime) {
                scaleHealth(ent, level, 0.4);
            } else {
                if (ent.getType() == EntityType.IRON_GOLEM) {
                    scaleHealth(ent, level, 3);
                } else {
                    scaleHealth(ent, level, 1);
                    scaleDamage(ent, level, 1);
                }
                if (!(peaceful.contains(ent.getType()))) {
                    double damage = Math.pow(level, RPGConstants.mobDmgLevelPow) * RPGConstants.mobDmgLevelScalar * 0.1 + 5;
                    setDamageThreshold(ent, damage);
                    setArmor(ent, level * 3);
                    setMagicResist(ent, level * 2);
                }
                setHPRegen(ent, ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.005);
            }
        }

        setLevel(ent, level);
        setExp(ent, calcExp(ent));
        if (!xp.containsKey(ent)) {
            xp.put(ent, new XPList());
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void mobDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof ArmorStand) && !(e.getDamager() instanceof Player)) {
            if (e.getDamager() instanceof IronGolem) {
                int level = getLevel((IronGolem) e.getDamager());
                double damage = 50 + Math.pow(level, RPGConstants.mobDmgLevelPow) * 3 * RPGConstants.mobDmgLevelScalar * Math.sqrt(4/4.0);
                e.setDamage(damage);
            }
            if (e.getDamager() instanceof Slime) {
                int level = getLevel((Slime) e.getDamager());
                int size = ((Slime)e.getDamager()).getSize();
                double damage = 20 + Math.pow(level, RPGConstants.mobDmgLevelPow) * size * 0.2 * RPGConstants.mobDmgLevelScalar * Math.sqrt(2/4.0);
                e.setDamage(damage);
            }
            if (e.getDamager() instanceof MagmaCube) {
                int level = getLevel((MagmaCube) e.getDamager());
                int size = ((MagmaCube)e.getDamager()).getSize();
                double damage = 30 + Math.pow(level, RPGConstants.mobDmgLevelPow) * size * 0.24 * RPGConstants.mobDmgLevelScalar * Math.sqrt(2/4.0);
                e.setDamage(damage);
            }
            if (e.getDamager() instanceof EnderDragon) {
                if (e.getEntity() instanceof LivingEntity) {
                    int level = getLevel((EnderDragon) e.getDamager());
                    double damage = 100 + Math.pow(level, RPGConstants.mobDmgLevelPow) * 3 * RPGConstants.mobDmgLevelScalar * Math.sqrt(8/4.0);
                    e.setDamage(damage);
                } else {
                    int level = getLevel((EnderDragon) e.getDamager());
                    e.setDamage((100 + Math.pow(level, RPGConstants.mobDmgLevelPow) * 4) * (Math.random() + 1));
                }
            }
        }
        if (e.getDamager() instanceof EvokerFangs) {
            EvokerFangs fangs = (EvokerFangs) e.getDamager();
            if (fangs.getOwner() instanceof Evoker) {
                e.setDamage(Math.pow(getLevel(fangs.getOwner()), RPGConstants.mobDmgLevelPow) + 25);
            }
        }
        if (e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
                if (!(ent instanceof Player) && !(ent instanceof ArmorStand)) {
                    if (ent instanceof Skeleton) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 50);
                    }
                    if (ent instanceof Snowman) {
                        e.setDamage(10);
                    }
                    if (ent instanceof Llama) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.5 + 20);
                    }
                    if (ent instanceof Stray) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.1 + 35);
                    }
                    if (ent instanceof Blaze) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.5 + 5);
                    }
                    if (ent instanceof Shulker) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.1 + 5);
                    }
                    if (ent instanceof Evoker) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 25);
                    }
                    if (ent instanceof Illusioner) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 25);
                    }
                    if (ent instanceof Pillager) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 30);
                    }
                    if (ent instanceof Wither) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 4 + 50);
                    }
                    if (ent instanceof EnderDragon) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 5 + 100);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onSpawn (EntityAddToWorldEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
            new BukkitRunnable() {
                public void run() {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    if (hasLevel(ent)) {
                        setupEnt(ent, getLevel(ent));
                    } else {
                        setupEnt(ent, -1);
                    }
                    if (ent instanceof Phantom) {
                        double random = Math.random();
                        if (random >= 0.05) {
                            ent.remove();
                        }
                    }
                }
            }.runTaskLater(Main.getInstance(), 1);
        }
    }

    @EventHandler
    public void slimeSplit (SlimeSplitEvent e) {
        e.setCancelled(true);
        int size = e.getEntity().getSize();
        if (size == 1) {
            return;
        } else if (size == 2) {
            size = 1;
        } else if (size == 4) {
            size = 2;
        } else {
            size = 4;
        }
        int level = getLevel(e.getEntity());
        EntityType type = EntityType.SLIME;
        if (e.getEntity() instanceof MagmaCube) {
            type = EntityType.MAGMA_CUBE;
        }
        for (int i = 0; i <= e.getCount(); i++) {
            LivingEntity ent = (LivingEntity) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), type);
            ((Slime) ent).setSize(size);
            setupEnt(ent, level);
        }
    }

    @EventHandler
    public void pig (PigZapEvent e) {
        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
        ent.setCustomName(Main.color("&fZombie Pigman" + " &6Lv. " + getLevel(ent)));
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        ent.setHealth(20);
        ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12);
        scaleHealth(ent, getLevel(ent), 1);
        scaleDamage(ent, getLevel(ent), 1);
    }

    @EventHandler
    public void transforms (EntityTransformEvent e) {
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.DROWNED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                ent.setCustomName(Main.color("&fDrowned" + " &6Lv. " + getLevel(ent)));
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                ent.setHealth(20);
                ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
                scaleHealth(ent, getLevel(ent), 1);
                scaleDamage(ent, getLevel(ent), 1);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.CURED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                int level = Math.min(getLevel(ent), 20);
                ent.setCustomName(Main.color("&fVillager" + " &6Lv. " + level));
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                ent.setHealth(20);
                scaleHealth(ent, level, 1);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.INFECTION) {
            if (e.getTransformedEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                ent.setCustomName(Main.color("&fZombie Villager" + " &6Lv. " + getLevel(ent)));
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                ent.setHealth(20);
                ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5);
                scaleHealth(ent, getLevel(ent), 1);
                scaleDamage(ent, getLevel(ent), 1);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.SHEARED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                ent.setCustomName(Main.color("&fCow" + " &6Lv. " + getLevel(ent)));
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
                ent.setHealth(10);
                scaleHealth(ent, getLevel(ent), 1);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.LIGHTNING) {
            if (e.getTransformedEntity() instanceof Witch) {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                ent.setCustomName(Main.color("&fWitch" + " &6Lv. " + getLevel(ent)));
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                ent.setHealth(20);
                scaleHealth(ent, getLevel(ent), 1);
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onDamage (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            Player p = null;
            if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
                if (e.getDamager() instanceof Projectile) {
                    if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                        p = (Player) ((Projectile) e.getDamager()).getShooter();
                    }
                } else {
                    p = (Player) e.getDamager();
                }
            }
            if (p != null && e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (xp.containsKey(ent)) {
                    xp.get(ent).addDamage(p, e.getDamage());
                } else {
                    xp.put(ent, new XPList());
                }
            }
        }
    }

    @EventHandler
    public void onKill (EntityDeathEvent e) {
        if (e.getEntity() instanceof LivingEntity && e.getEntity().getCustomName() != null && getLevel(e.getEntity()) != -1 && !(e.getEntity() instanceof Player || e.getEntity() instanceof ArmorStand)) {
            LivingEntity ent = e.getEntity();
            double exp = getExp(ent);
            if (xp.containsKey(ent) && exp > 0) {//e.getEntity().hasMetadata("EXP")) {
                //double exp = (double) e.getEntity().getMetadata("EXP").get(0).value();
                for (Player pl : xp.get(ent).getPercentages().keySet()) {
                    if (xp.get(ent).getIndivPer(pl).contains("100%")) {
                        main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), exp * xp.get(ent).getPercentages().get(pl), "SELF");
                    } else {
                        main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), exp * xp.get(ent).getPercentages().get(pl), xp.get(ent).getIndivPer(pl));
                    }
                }
                xp.get(ent).scrub();
                xp.remove(ent);
            } else {
                if (exp > 0 && e.getEntity().getKiller() instanceof Player) {//e.getEntity().hasMetadata("EXP") && e.getEntity().getKiller() instanceof Player) {
                    //double exp = (double) e.getEntity().getMetadata("EXP").get(0).value();
                    main.getRP(e.getEntity().getKiller()).giveExpFromSource(e.getEntity().getKiller(), e.getEntity().getLocation(), exp, e.getEntity().getKiller().getName());
                }
            }
        }
    }

}
