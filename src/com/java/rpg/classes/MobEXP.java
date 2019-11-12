package com.java.rpg.classes;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.java.Main;
import com.java.rpg.player.XPList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobEXP implements Listener {

    private Main main = Main.getInstance();

    private static Map<Biome, LevelRange> biomeLevels;

    public static Map<Biome, LevelRange> getBL() {
        return biomeLevels;
    }

    private static List<EntityType> peaceful;

    private static Map<EntityType, Double> xpmods;

    public MobEXP() {
        biomeLevels = new HashMap<>();

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
        xpmods.put(EntityType.ENDER_DRAGON , 500.0);
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

    public void setLevel(LivingEntity ent, int level) {
        if (ent.getCustomName() == null) {
            if (ent.getName().contains("Lv.")) {
                ent.setCustomName(Main.color(ChatColor.stripColor(ent.getName()).substring(0, ChatColor.stripColor(ent.getName()).indexOf("Lv.") - 1).trim() + " &6Lv. " + level));
                ent.setCustomNameVisible(true);
            } else {
                ent.setCustomName(Main.color(ChatColor.stripColor(ent.getName()).trim() + " &6Lv. " + level));
                ent.setCustomNameVisible(true);
            }
        } else if (ent.getCustomName().contains("Lv.")){
            ent.setCustomName(Main.color(ChatColor.stripColor(ent.getCustomName()).substring(0, ChatColor.stripColor(ent.getCustomName()).indexOf("Lv.") - 1).trim() + " &6Lv. " + level));
            ent.setCustomNameVisible(true);
        }
    }

    public void scaleHealth(LivingEntity ent, int level, double modifier) {
        double hp = 200 + Math.pow(level, 1.25) * 8 * Math.pow(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()/20.0, 1.0/2.5);
        hp*=modifier;
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public void scaleDamage(LivingEntity ent, int level, double modifier) {
        if (ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            double damage = level * 3 * Math.sqrt(ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue()/4.0);
            damage += ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 3;
            damage *= modifier;
            ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        }

        if (ent instanceof Creeper) {
            Creeper creeper = (Creeper) ent;
            if (level >= 0 && level < 10) {
                creeper.setExplosionRadius(1);
            } else if (level >= 10 && level < 20) {
                creeper.setExplosionRadius(2);
            } else if (level >= 20 && level < 30) {
                creeper.setExplosionRadius(3);
            } else if (level >= 30 && level < 40) {
                creeper.setExplosionRadius(4);
            } else {
                creeper.setExplosionRadius(5);
                if (level == 50) {
                    creeper.setExplosionRadius(7);
                    creeper.setPowered(true);
                }
            }
        }
    }

    public void setExp(LivingEntity ent, double exp) {
        if (ent.getMetadata("EXP") != null && ent.hasMetadata("EXP")) {
            ent.removeMetadata("EXP", Main.getInstance());
        }
        ent.setMetadata("EXP", new FixedMetadataValue(Main.getInstance(), exp));
    }

    public double getExp(LivingEntity ent) {
        if (getLevel(ent) == -1 || !(Integer.valueOf(getLevel(ent)) instanceof Integer)) {
            return 0;
        }
        return (0.5 * Math.pow(getLevel(ent), 2.1) + 50) * (Math.random() * 0.1 + 1) * xpmods.get(ent.getType());
    }


    private static Map<LivingEntity, XPList> xp;

    public Map<LivingEntity, XPList> getXP() {
        return xp;
    }

    public int getLevel(LivingEntity ent) {
        if (ent.getName().contains("Lv. ")) {
            String name = ChatColor.stripColor(ent.getName());
            name+=" ";
            name = name.substring(0, name.indexOf("Lv. ") + 6).trim();
            return Integer.valueOf(name.substring(name.indexOf("Lv. ") + 4));
        }
        return -1;
    }

    public void setupEnt(LivingEntity ent, int lvl) {
            int level = lvl;
            if (lvl == -1) {
                level = biomeLevels.get(ent.getLocation().getBlock().getBiome()).getRandomLevel();
            }

            if (peaceful.contains(ent.getType())) {
                level = Math.max(0, Math.min(level, 20));
            }

            if (ent.getName().contains("Lv.")) {
                setLevel(ent, level);
                level = getLevel(ent);
            } else {
                scaleHealth(ent, level, 1);
                scaleDamage(ent, level, 1);
            }

            if (ent instanceof Slime || ent instanceof MagmaCube) {
                scaleHealth(ent, level, 0.5);
            }

            setLevel(ent, level);
            //setExp(ent, (0.75 * Math.pow(level, 2.1) + 50) * (Math.random() * 0.1 + 1) * xpmods.get(ent.getType()));
            if (!xp.containsKey(ent)) {
                xp.put(ent, new XPList());
            }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void mobDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof ArmorStand) && !(e.getDamager() instanceof Player)) {
            if (e.getDamager() instanceof IronGolem) {
                e.setDamage(e.getDamage() + getLevel((IronGolem) e.getDamager()) * 3);
            }
            if (e.getDamager() instanceof Slime) {
                e.setDamage(e.getDamage() + getLevel((Slime) e.getDamager()) * 1.5);
            }
            if (e.getDamager() instanceof MagmaCube) {
                e.setDamage(e.getDamage() + getLevel((MagmaCube) e.getDamager()) * 1.8);
            }
        }
        if (e.getDamager() instanceof EvokerFangs) {
            EvokerFangs fangs = (EvokerFangs) e.getDamager();
            if (fangs.getOwner() instanceof Evoker) {
                e.setDamage(getLevel(fangs.getOwner()) * 3 + 25);
            }
        }
        if (e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
                if (!(ent instanceof Player) && !(ent instanceof ArmorStand)) {
                    if (ent instanceof Skeleton) {
                        e.setDamage(getLevel(ent) * 3 + 25);
                    }
                    if (ent instanceof Snowman) {
                        e.setDamage(10);
                    }
                    if (ent instanceof Llama) {
                        e.setDamage(getLevel(ent) * 2 + 10);
                    }
                    if (ent instanceof Stray) {
                        e.setDamage(getLevel(ent) * 2.75 + 25);
                    }
                    if (ent instanceof Blaze) {
                        e.setDamage(getLevel(ent) * 2 + 10);
                    }
                    if (ent instanceof Shulker) {
                        e.setDamage(getLevel(ent) * 2 + 15);
                    }
                    if (ent instanceof Evoker) {
                        e.setDamage(getLevel(ent) * 3 + 30);
                    }
                    if (ent instanceof Illusioner) {
                        e.setDamage(getLevel(ent) * 2 + 10);
                    }
                    if (ent instanceof Pillager) {
                        e.setDamage(getLevel(ent) * 3 + 40);
                    }
                    if (ent instanceof Wither) {
                        e.setDamage(getLevel(ent) * 2 + 50);
                    }
                    if (ent instanceof EnderDragon) {
                        e.setDamage(getLevel(ent) * 2 + 40);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onSpawn (EntityAddToWorldEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player) && !(e.getEntity() instanceof Slime) && !(e.getEntity() instanceof MagmaCube)) {
            new BukkitRunnable() {
                public void run() {
                    LivingEntity ent = (LivingEntity) e.getEntity();
                    setupEnt(ent, -1);
                    if (ent instanceof Phantom) {
                        double random = Math.random();
                        if (random >= 0.05) {
                            ent.remove();
                        }
                    }
                }
            }.runTaskLater(Main.getInstance(), 1);
        }
        if (e.getEntity() instanceof Slime || e.getEntity() instanceof MagmaCube) {
            new BukkitRunnable() {
                public void run() {
                    int level = getLevel((LivingEntity) e.getEntity());
                    if (level == -1) {
                        setupEnt((LivingEntity) e.getEntity(), -1);
                    } else {
                        setupEnt((LivingEntity) e.getEntity(), level);
                    }
                }
            }.runTaskLater(Main.getInstance(), 1);
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
            LivingEntity ent = (LivingEntity) e.getEntity();
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
