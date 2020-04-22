package com.java.rpg.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.java.Main;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.classes.utility.LevelRange;
import com.java.rpg.damage.utility.PhysicalStack;
import com.java.rpg.player.utility.XPList;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTList;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

import static com.java.rpg.entity.EntityUtils.*;

public class Mobs implements Listener {

    private Main main = Main.getInstance();

    private static Map<Biome, LevelRange> biomeLevels;

    public static Map<Biome, LevelRange> getBL() {
        return biomeLevels;
    }

    private static List<EntityType> peaceful;

    private static Map<EntityType, Double> xpmods;

    private static BiomeSettings settings = new BiomeSettings();

    public static Map<LivingEntity, XPList> xp;

    public Map<LivingEntity, XPList> getXP() {
        return xp;
    }

    public Mobs() {
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
        xpmods.put(EntityType.MAGMA_CUBE , 0.2);
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
        xpmods.put(EntityType.SLIME , 0.2);
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

    @EventHandler
    public void noExpDrop (EntityDeathEvent e) {
        e.setDroppedExp(0);
    }

    @EventHandler
    public void noExpBlocks (BlockBreakEvent e) {
        e.setExpToDrop(0);
    }

    @EventHandler
    public void cleanHolos (ChunkUnloadEvent e) {
        /* Unused */
    }

    @EventHandler
    public void nameTags (PlayerInteractEntityEvent e) {
        if (e.getHand() == EquipmentSlot.HAND || e.getHand() == EquipmentSlot.OFF_HAND) {
            ItemStack item;
            if (e.getHand() == EquipmentSlot.HAND) {
                item = e.getPlayer().getInventory().getItemInMainHand();
            } else {
                item = e.getPlayer().getInventory().getItemInOffHand();
            }

            if (item.getType() == Material.NAME_TAG) {
                if (item.hasItemMeta()) {
                    setCustomName(e.getRightClicked(), item.getItemMeta().getDisplayName());
                }
            }
        }
    }

    @EventHandler
    public void mobSpawnEvent (CreatureSpawnEvent e) {
        LivingEntity ent = e.getEntity();
        if (!(ent instanceof ArmorStand) && !(ent instanceof Player)) {
            if (ent instanceof Phantom) {
                double random = Math.random();
                if (random >= 0.05) {
                    ent.remove();
                }
            }
            if (getSetup(ent) == 0) {
                if (hasLevel(ent)) {
                    setupEnt(ent, getLevel(ent));
                } else {
                    setupEnt(ent, new LevelRange(0, 10).getRandomLevel());
                }
            }
        }
    }

    public void setupEnt(LivingEntity ent, int lvl) {
        int level = lvl;
        if (lvl == -1) {
            level = biomeLevels.get(ent.getLocation().getBlock().getBiome()).getRandomLevel();
        }

        if (peaceful.contains(ent.getType())) {
            level = Math.max(0, Math.min(level, 5));
        }

        if (ent.getType() == EntityType.IRON_GOLEM) {
            level = Math.max(level, 35);
        }

        if (ent.getType() == EntityType.VILLAGER) {
            level = Math.max(level, 20);
        }

        if (ent.getAttribute(Attribute.GENERIC_ARMOR) != null) {
            ent.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
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
                setHPRegen(ent, ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.01);
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
                setHPRegen(ent, ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.01);
            }
        }

        setLevel(ent, level);
        setExp(ent, calcExp(ent));
    }

    public static double calcExp(LivingEntity ent) {
        int level = EntityUtils.getLevel(ent);
        final double ceil = Math.ceil(Math.pow(2, (level + 60.0) / 10.5) - 0.0);
        if (xpmods.containsKey(ent.getType())) {

            return xpmods.get(ent.getType()) * ceil;
        } else {
            return ceil;
        }
    }

    public void scaleHealth(LivingEntity ent, int level, double modifier) {

        double hp = RPGConstants.mobHpBase + Math.pow(level, RPGConstants.mobHpLevelPow) * RPGConstants.mobHpLevelScalar * Math.pow(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/20.0, RPGConstants.mobHpBasePow);
        if (peaceful.contains(ent.getType())) {
            if (ent instanceof Horse) {
                hp = Math.max(hp, 10000);
            } else {
                hp = Math.max(Math.min(hp, 5000), 1000);
            }
        }
        hp*=modifier;
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public void scaleDamage(LivingEntity ent, int level, double modifier) {
        if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            double damage = Math.pow(level, RPGConstants.mobDmgLevelPow) * RPGConstants.mobDmgLevelScalar * Math.sqrt(ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue()/4.0);
            damage += ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 3 + 40;
            damage *= modifier;
            ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        }

        if (ent instanceof Creeper) {
            Creeper creeper = (Creeper) ent;
            if (level >= 0 && level < 20) {
                creeper.setExplosionRadius(3);
                creeper.setMaxFuseTicks(40);
            } else if (level >= 20 && level < 40) {
                creeper.setExplosionRadius(3);
                creeper.setMaxFuseTicks(30);
            } else if (level >= 40 && level < 60) {
                creeper.setExplosionRadius(4);
                creeper.setMaxFuseTicks(30);
            } else if (level >= 60 && level < 80) {
                creeper.setExplosionRadius(4);
                creeper.setMaxFuseTicks(25);
            } else {
                creeper.setExplosionRadius(5);
                creeper.setMaxFuseTicks(20);
                if (level == 100) {
                    creeper.setPowered(true);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void mobDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof ArmorStand) && !(e.getDamager() instanceof Player)) {
            if (e.getDamager() instanceof IronGolem) {
                int level = getLevel((IronGolem) e.getDamager());
                double damage = 50 + Math.pow(level, RPGConstants.mobDmgLevelPow) * 2 * RPGConstants.mobDmgLevelScalar * Math.sqrt(4/4.0);
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
                    double damage = 100 + Math.pow(level, RPGConstants.mobDmgLevelPow) * 4 * RPGConstants.mobDmgLevelScalar * Math.sqrt(8/4.0);
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
                    if (ent instanceof Guardian) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 75);
                    }
                    if (ent instanceof Skeleton) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 1.5 + 60);
                    }
                    if (ent instanceof Snowman) {
                        e.setDamage(10);
                    }
                    if (ent instanceof Llama) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.25 + 20);
                    }
                    if (ent instanceof Stray) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 1.25 + 35);
                    }
                    if (ent instanceof Blaze) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.25 + 5);
                    }
                    if (ent instanceof Shulker) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 0.1 + 5);
                    }
                    if (ent instanceof Evoker) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 1.25 + 25);
                    }
                    if (ent instanceof Illusioner) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 25);
                    }
                    if (ent instanceof Pillager) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 1.25 + 30);
                    }
                    if (ent instanceof Wither) {
                        e.setDamage(Math.pow(getLevel(ent), RPGConstants.mobDmgLevelPow) * 2.5 + 50);
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
        if (e.getEntity() instanceof ExperienceOrb) {
            ExperienceOrb orb = (ExperienceOrb) e.getEntity();
            orb.setExperience(0);
            return;
        }
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
            if (!xp.containsKey((LivingEntity) e.getEntity())) {
                xp.put((LivingEntity) e.getEntity(), new XPList());
            }
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
            ent.setNoDamageTicks(20);
        }
    }

    @EventHandler
    public void onTame (EntityTameEvent e) {
        if (e.getEntityType() == EntityType.WOLF) {
            new BukkitRunnable() {
                public void run() {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    int level = getLevel(ent);
                    ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(3000 + level * 20);
                    ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    setArmor(ent, 100 + level * 20);
                    setPhysicalDamage(ent, new PhysicalStack(100, 25, 100));
                    setHPRegen(ent, ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.025);
                }
            }.runTaskLater(Main.getInstance(), 1L);
        }
    }

    @EventHandler
    public void pig (PigZapEvent e) {
        new BukkitRunnable() {
            public void run() {
                LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                ent.setCustomName(Main.color("&fZombie Pigman" + " &6Lv. " + getLevel(ent)));
                ent.setHealth(20);
                ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12);
                scaleHealth(ent, getLevel(ent), 1);
                scaleDamage(ent, getLevel(ent), 1);
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }

    @EventHandler
    public void transforms (EntityTransformEvent e) {
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.DROWNED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {

                new BukkitRunnable() {
                    public void run() {
                        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                        ent.setCustomName(Main.color("&fDrowned" + " &6Lv. " + getLevel(ent)));
                        ent.setHealth(20);
                        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
                        scaleHealth(ent, getLevel(ent), 1);
                        scaleDamage(ent, getLevel(ent), 1);
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.CURED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {

                new BukkitRunnable() {
                    public void run() {
                        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                        int level = Math.min(getLevel(ent), 20);
                        ent.setCustomName(Main.color("&fVillager" + " &6Lv. " + level));
                        ent.setHealth(20);
                        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        scaleHealth(ent, level, 1);
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.INFECTION) {
            if (e.getTransformedEntity() instanceof LivingEntity) {

                new BukkitRunnable() {
                    public void run() {
                        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                        ent.setCustomName(Main.color("&fZombie Villager" + " &6Lv. " + getLevel(ent)));
                        ent.setHealth(20);
                        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5);
                        scaleHealth(ent, getLevel(ent), 1);
                        scaleDamage(ent, getLevel(ent), 1);
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.SHEARED) {
            if (e.getTransformedEntity() instanceof LivingEntity) {
                new BukkitRunnable() {
                    public void run() {
                        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                        ent.setCustomName(Main.color("&fCow" + " &6Lv. " + getLevel(ent)));
                        ent.setHealth(10);
                        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
                        scaleHealth(ent, getLevel(ent), 1);
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
        if (e.getTransformReason() == EntityTransformEvent.TransformReason.LIGHTNING) {
            if (e.getTransformedEntity() instanceof Witch) {
                new BukkitRunnable() {
                    public void run() {
                        LivingEntity ent = (LivingEntity) e.getTransformedEntity();
                        ent.setCustomName(Main.color("&fWitch" + " &6Lv. " + getLevel(ent)));
                        ent.setHealth(20);
                        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                        scaleHealth(ent, getLevel(ent), 1);
                    }
                }.runTaskLater(Main.getInstance(), 1L);
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onEnvDmg (EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && e.getCause() != EntityDamageEvent.DamageCause.MAGIC && e.getCause() != EntityDamageEvent.DamageCause.CUSTOM && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (xp.containsKey(ent)) {
                    xp.get(ent).setEnvDmg(xp.get(ent).getEnvDmg() + e.getDamage());
                } else {
                    xp.put(ent, new XPList());
                    xp.get(ent).setEnvDmg(xp.get(ent).getEnvDmg() + e.getDamage());
                }
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
                    xp.get(ent).addDamage(p, e.getDamage());
                }
            } else if (p == null && e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)){
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (xp.containsKey(ent)) {
                    xp.get(ent).setEnvDmg(xp.get(ent).getEnvDmg() + e.getDamage());
                } else {
                    xp.put(ent, new XPList());
                    xp.get(ent).setEnvDmg(xp.get(ent).getEnvDmg() + e.getDamage());
                }
            }
        }
    }

    @EventHandler
    public void onKill (EntityDeathEvent e) {
        if (e.getEntity().getCustomName() != null && getLevel(e.getEntity()) != -1 && !(e.getEntity() instanceof Player || e.getEntity() instanceof ArmorStand)) {
            LivingEntity ent = e.getEntity();
            double exp = getExp(ent);
            int level = getLevel(ent);
            if (xp.containsKey(ent) && exp > 0) {//e.getEntity().hasMetadata("EXP")) {
                //double exp = (double) e.getEntity().getMetadata("EXP").get(0).value();
                for (Player pl : xp.get(ent).getPercentages().keySet()) {
                    if (xp.get(ent).getIndivPer(pl).contains("100%") || xp.get(ent).getAloneAndLowEnv(pl)) {
                        double expToGive = exp * xp.get(ent).getPercentages().get(pl);
                        if (main.getRP(pl).getLevel() - RPGConstants.reducedExpLevelMod >= level) {
                            expToGive*=Math.floor(Math.pow((1.0 * level)/( 1.0 * main.getRP(pl).getLevel()), RPGConstants.reducedExpLevelPow));
                        }
                        if (Math.floor(expToGive) > 0) {
                            main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), expToGive, "SELF");
                        }
                    } else {
                        DecimalFormat dF = new DecimalFormat("#.##");
                        if (xp.get(ent).getAloneAndHighEnv(pl)) {
                            double expToGive = exp * xp.get(ent).getPercentages().get(pl);
                            if (main.getRP(pl).getLevel() - RPGConstants.reducedExpLevelMod >= level) {
                                expToGive*=Math.floor(Math.pow((1.0 * level)/( 1.0 * main.getRP(pl).getLevel()), RPGConstants.reducedExpLevelPow));
                            }
                            if (xp.get(ent).hasVeryHighEnv()) {
                                expToGive = Math.min(expToGive, main.getRP(pl).getMaxExp() * 0.25);
                            }
                            if (Math.floor(expToGive) > 0) {
                                main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), expToGive * Math.min(1.0, xp.get(ent).getPercentages().get(pl) + (1 - RPGConstants.xpEnvVal)), dF.format(Math.min(1.0, xp.get(ent).getPercentages().get(pl) + (1 - RPGConstants.xpEnvVal)) * 100.0) + "%");
                            }
                        } else {
                            double expToGive = exp * xp.get(ent).getPercentages().get(pl);
                            if (main.getRP(pl).getLevel() - RPGConstants.reducedExpLevelMod >= level) {
                                expToGive*=Math.floor(Math.pow((1.0 * level)/( 1.0 * main.getRP(pl).getLevel()), RPGConstants.reducedExpLevelPow));
                            }
                            if (xp.get(ent).hasVeryHighEnv()) {
                                expToGive = Math.min(expToGive, main.getRP(pl).getMaxExp() * 0.25);
                            }
                            if (Math.floor(expToGive) > 0) {
                                if (xp.get(ent).hasHighEnv()) {
                                    main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), expToGive * Math.min(1.0, xp.get(ent).getPercentages().get(pl) + ((1 - RPGConstants.xpEnvVal) / (xp.get(ent).getPercentages().size() * 1.0))), dF.format(Math.min(1.0, xp.get(ent).getPercentages().get(pl) + ((1 - RPGConstants.xpEnvVal) / (xp.get(ent).getPercentages().size()) * 1.0)) * 100.0) + "%");
                                } else {
                                    main.getRP(pl).giveExpFromSource(pl, e.getEntity().getLocation(), expToGive * xp.get(ent).getPercentages().get(pl), xp.get(ent).getIndivPer(pl));
                                }
                            }
                        }
                    }
                }
                xp.get(ent).scrub();
                xp.remove(ent);
            } else {
                if (exp > 0 && e.getEntity().getKiller() != null) {//e.getEntity().hasMetadata("EXP") && e.getEntity().getKiller() instanceof Player) {
                    //double exp = (double) e.getEntity().getMetadata("EXP").get(0).value();
                    double expToGive = exp * xp.get(ent).getPercentages().get(e.getEntity().getKiller());
                    if (main.getRP(e.getEntity().getKiller()).getLevel() - RPGConstants.reducedExpLevelMod >= level) {
                        expToGive*=Math.floor(Math.pow((1.0 * level)/( 1.0 * main.getRP(e.getEntity().getKiller()).getLevel()), RPGConstants.reducedExpLevelPow));
                    }
                    main.getRP(e.getEntity().getKiller()).giveExpFromSource(e.getEntity().getKiller(), e.getEntity().getLocation(), expToGive, e.getEntity().getKiller().getName());
                }
            }
        }
    }

}
