package com.java;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.destroystokyo.paper.Title;
import com.java.communication.*;
import com.java.essentials.commands.*;
import com.java.essentials.commands.admin.*;
import com.java.essentials.commands.admin.utility.FastRestartCommand;
import com.java.essentials.commands.admin.utility.InvseeCommand;
import com.java.essentials.commands.admin.utility.KillAllCommand;
import com.java.essentials.commands.admin.utility.TimedrestartCommand;
import com.java.essentials.commands.admin.warp.DelWarpCommand;
import com.java.essentials.commands.admin.warp.SetWarpCommand;
import com.java.essentials.commands.admin.warp.SpawnCommand;
import com.java.essentials.commands.admin.warp.WarpsCommand;
import com.java.rpg.classes.skills.Wanderer.WandererListeners;
import com.java.rpg.entity.EntityUtils;
import com.java.rpg.holograms.NPCTag;
import com.java.rpg.time.TimeCommand;
import com.java.towns.Citizen;
import com.java.towns.Town;
import com.java.towns.TownCoreCommand;
import com.java.rpg.holograms.EntityHealthBars;
import com.java.rpg.holograms.Hologram;
import com.java.rpg.classes.*;
import com.java.rpg.classes.casting.BindCommand;
import com.java.rpg.classes.casting.Skillcast;
import com.java.rpg.classes.commands.admin.CDCommand;
import com.java.rpg.classes.commands.admin.ExpCommand;
import com.java.rpg.classes.commands.admin.ManaCommand;
import com.java.rpg.classes.commands.admin.SetClassCommand;
import com.java.rpg.classes.commands.player.*;
import com.java.rpg.classes.skills.Pyromancer.*;
import com.java.rpg.classes.statuses.Stuns;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.classes.utility.StatusObject;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.entity.CustomEntityType;
import com.java.rpg.entity.Mobs;
import com.java.rpg.entity.commands.BiomeLevelCommand;
import com.java.rpg.entity.commands.BiomesCommand;
import com.java.rpg.entity.grassy.WarriorZombie;
import com.java.rpg.damage.DamageListener;
import com.java.rpg.damage.Environmental;
import com.java.rpg.damage.utility.Damage;
import com.java.rpg.player.*;
import com.java.rpg.player.Items;
import com.java.rpg.player.utility.PlayerListener;
import com.java.towns.TownManager;
import de.slikey.effectlib.EffectManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.java.rpg.party.Party;
import com.java.rpg.party.PartyCommand;
import com.java.rpg.party.PartyManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends JavaPlugin {

    public String noperm = "&cNo permission!";

    private final static int CENTER_PX = 154;
    private final static int MAX_PX = 250;

    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }

    public boolean shareParty(Player p1, Player p2) {
        if (getPM().hasParty(p1) && getPM().hasParty(p2)) {
            return (getPM().getParty(p1).getPlayers().contains(p2));
        }
        return false;
    }

    public boolean sharePartyPvp(Player p1, Player p2) {
        return (shareParty(p1, p2) && !getPM().getParty(p1).getPvp());
    }

    public boolean isValidFriendly(Entity e, Player damager) {
        if (e instanceof LivingEntity) {
            if (!(e instanceof ArmorStand) && !e.isInvulnerable() && !e.isDead()) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    if (p.getGameMode() == GameMode.SURVIVAL) {
                        if (p.equals(damager)) {
                            return true;
                        }
                        if (shareParty(p, damager)) {
                            return true;
                        }
                        return false;
                        /*if (CitizensAPI.getNPCRegistry().isNPC(e) && CitizensAPI.getNPCRegistry().getNPC(e).isProtected()) {
                            return false;
                        }*/
                    } else {
                        return false;
                    }
                }
                if (e instanceof Tameable) {
                    Tameable tameable = (Tameable) e;
                    if (tameable.getOwner() != null) {
                        if (Bukkit.getPlayer(tameable.getOwner().getUniqueId()).equals(damager)) {
                            return true;
                        }
                        if (shareParty(Bukkit.getPlayer(tameable.getOwner().getUniqueId()), damager)) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean isValidRPG(Entity e, Player damager) {
        if (e instanceof LivingEntity) {
            if (!(e instanceof ArmorStand) && !e.isInvulnerable() && !e.isDead()) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    if (p.getGameMode() == GameMode.SURVIVAL) {
                        if (p.equals(damager)) {
                            return false;
                        }
                        if (CitizensAPI.getNPCRegistry().isNPC(e) && CitizensAPI.getNPCRegistry().getNPC(e).isProtected()) {
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isValidTarget(Entity e, Player damager) {
        if (e instanceof LivingEntity) {
            if (!(e instanceof ArmorStand) && !e.isInvulnerable() && !e.isDead()) {
                if (e instanceof Player) {
                    Player p = (Player) e;
                    if (p.getGameMode() == GameMode.SURVIVAL) {
                        if (p.equals(damager)) {
                            return false;
                        }
                        if (sharePartyPvp(p, damager)) {
                            return false;
                        }
                        if (CitizensAPI.getNPCRegistry().isNPC(e) && CitizensAPI.getNPCRegistry().getNPC(e).isProtected()) {
                            return false;
                        }
                        /*RegionContainer rg = WorldGuard.getInstance().getPlatform().getRegionContainer();
                        for(ProtectedRegion r : rg.get((com.sk89q.worldedit.world.World) p.getWorld()).getApplicableRegions(new BlockVector3())) {
                            //Check if region is the correct one through r.getId() if by name
                            //Do the firework thing
                        }*/

                        return true;
                    } else {
                        return false;
                    }
                }
                if (e instanceof Tameable) {
                    Tameable tameable = (Tameable) e;
                    if (tameable.getOwner() != null) {
                        if (Bukkit.getPlayer(tameable.getOwner().getUniqueId()).equals(damager)) {
                            return false;
                        }
                        if (sharePartyPvp(Bukkit.getPlayer(tameable.getOwner().getUniqueId()), damager)) {
                            return false;
                        }
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(Entity e) {
        if (e instanceof Player) {
            if (!isNPC(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNPC(Entity e) {
        return CitizensAPI.getNPCRegistry().isNPC(e);
    }

    public List<LivingEntity> getNearbyLivingEntitiesTargetValid(Location loc, Player caster, double radius) {
        List<LivingEntity> ents = new ArrayList<>();
        for (LivingEntity ent : loc.getNearbyLivingEntities(radius)) {
            if (isValidTarget(ent, caster)) {
                ents.add(ent);
            }
        }
        return ents;
    }

    public List<LivingEntity> getNearbyLivingEntitiesValid(Location loc, Player caster, double radius) {
        List<LivingEntity> ents = new ArrayList<>();
        for (LivingEntity ent : loc.getNearbyLivingEntities(radius)) {
            if (isValidTarget(ent, caster)) {
                ents.add(ent);
            }
        }
        return ents;
    }

    /*

    Auto restart

     */

    public void restartTimer(int seconds) {
        new BukkitRunnable() {
            int times = 0;
            public void run() {
                times++;
                if (times == 1) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                        p.sendTitle(new Title(Main.color("&a&lServer Restarting"), Main.color("&fIn " + seconds + " seconds!"), 5, 80, 5));
                    }
                    Bukkit.getServer().broadcastMessage(Main.color("&8\u00BB &a&lServer Restarting in &f&l" + seconds + "&a&l seconds!"));
                } else {
                    if (times % 20 == 0 || (times >= 4 * seconds - (4 * 10) && times % 4 == 0)) {
                        Bukkit.getServer().broadcastMessage(Main.color("&8\u00BB &a&lServer Restarting in &f&l" + ((seconds * 4 - times) / 4) + "&a&l seconds!"));
                    }
                }
                if (times >= 4 * seconds) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                        p.sendTitle(new Title(Main.color("&a&lSERVER RESTARTING..."), Main.color("&fSee you in a minute!"), 5, 80, 5));

                        getRP(p).updateStats();
                        getRP(p).pushFiles();

                    }
                    Bukkit.getServer().broadcastMessage(Main.color("&8\u00BB &a&lSERVER RESTARTING..."));
                    for (World w : Bukkit.getWorlds()) {
                        for (Entity e : w.getEntities()) {
                            if (e.getType() == EntityType.ARMOR_STAND) {
                                if (e.isCustomNameVisible()) {
                                    for (String s : RPGConstants.damages) {
                                        if (e.getCustomName().replace("§","&").contains(s)) {
                                            e.remove();
                                            break;
                                        }
                                    }
                                }
                                continue;
                            }
                        }
                    }

                    cancel();
                    new BukkitRunnable() {
                        public void run() {
                            Bukkit.getServer().shutdown();
                        }
                    }.runTaskLater(Main.getInstance(), 20);
                }
            }
        }.runTaskTimer(this, 0L, 5);
    }

    public void autorestart() {
        new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(Main.color("&8\u00BB &a&lServer Automatically Restarting in &f&l5 &a&lminutes!"));
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                }
                new BukkitRunnable() {
                    public void run() {
                        restartTimer(60);
                    }
                }.runTaskLater(Main.getInstance(), 5 * 60 * 20);
            }
        }.runTaskLater(this, 24 * 60 * 60 * 20);
    }

    /*

    Damage Variables

     */

    public void npcTags() {
        new BukkitRunnable() {
            public void run() {
                for (NPC npc : CitizensAPI.getNPCRegistry()) {
                    if (getNpcTags().containsKey(npc.getEntity())) {
                        List<Player> players = new ArrayList<>(npc.getEntity().getWorld().getNearbyPlayers(npc.getEntity().getLocation(), 24));
                        List<Player> remove = new ArrayList<>(getNpcTags().get(npc.getEntity()).getTargets());
                        for (Player p : getNpcTags().get(npc.getEntity()).getTargets()) {
                            if (players.contains(p)) {
                                remove.remove(p);
                            }
                        }
                        for (Player p : remove) {
                            getNpcTags().get(npc.getEntity()).remove(p);
                        }
                        for (Player p : players) {
                            if (!(getNpcTags().get(npc.getEntity()).getTargets().contains(p))) {
                                getNpcTags().get(npc.getEntity()).getTargets().add(p);
                            }
                        }

                        getNpcTags().get(npc.getEntity()).updateViewership();

                    }
                }
            }
        }.runTaskTimer(this, 10L, 40L);
    }

    // MOB HP REGEN
    public void hpRegen() {
        new BukkitRunnable() {
            public void run() {
                for (World w : Bukkit.getWorlds()) {
                    for (LivingEntity e : w.getLivingEntities()) {
                        if (!(e instanceof Player) && !(e instanceof ArmorStand) && !(e.isDead())) {
                            if (e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > e.getHealth() && EntityUtils.getHPRegen(e) > 0) {
                                LivingEntity ent = e;
                                DecimalFormat df = new DecimalFormat("#.##");
                                //Hologram magic = new Hologram(ent, ent.getLocation(), "&a❤" + df.format(MobEXP.getHPRegen(e)), Hologram.HologramType.DAMAGE);
                                //magic.rise();
                                DecimalFormat dF = new DecimalFormat("#.##");
                                if (getHpBars().containsKey(ent)) {
                                    getHpBars().get(ent).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                                }
                            }
                            e.setHealth(Math.max(0, Math.min(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), EntityUtils.getHPRegen(e) + e.getHealth())));
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1L, 60L);
    }

    public Map<Entity, Hologram> hpBars = new HashMap<>();
    public Map<Entity, Hologram> getHpBars() {
        return hpBars;
    }
    public Map<Entity, Hologram> npcTags = new HashMap<>();
    public Map<Entity, Hologram> getNpcTags() {return npcTags;}

    public void remHpBar() {
        new BukkitRunnable() {
            public void run() {
                List<Entity> remove = new ArrayList<>();
                for (Entity e : hpBars.keySet()) {
                    if (e instanceof LivingEntity) {
                        LivingEntity ent = (LivingEntity) e;
                        if (ent.isDead() || ent.getHealth() <= 0 || (ent instanceof Player && !((Player) ent).isOnline())) {
                            remove.add(e);
                        } else {
                            DecimalFormat dF = new DecimalFormat("#.##");
                            getHpBars().get(e).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                            getHpBars().get(e).incrementLifetime();
                            if (getHpBars().get(e).getLifetime() > 5) {
                                remove.add(e);
                            }
                        }
                    } else {
                        remove.add(e);
                    }
                }
                for (Entity e : remove) {
                    if (e != null && !e.isDead() && e.getCustomName() != null) {
                        e.setCustomNameVisible(false);
                    }
                    getHolos().remove(hpBars.get(e));
                    hpBars.get(e).destroy(false);
                    hpBars.remove(e);
                }
            }
        }.runTaskTimer(this, 10L, 20L);
    }

    public void riseBars() {
        new BukkitRunnable() {
            public void run() {
                for (Hologram h : getHolos()) {
                    if (h.getType() == Hologram.HologramType.DAMAGE || h.getType() == Hologram.HologramType.EXP || h.getType() == Hologram.HologramType.STATUS) {
                        //EntityArmorStand stand = h.getStand();
                        Location loc = h.getLocation().add(new Vector(0, 0.025, 0));
                        h.teleport(loc);
                        h.incrementLifetime();
                        if (h.getType() == Hologram.HologramType.EXP || h.getType() == Hologram.HologramType.STATUS) {
                            if (h.getLifetime() * 0.02 >= 1) {
                                h.destroy(false);
                            }
                        } else {
                            if (h.getLifetime() * 0.025 >= 1) {
                                h.destroy(false);
                            }
                        }
                    } else if (h.getType() == Hologram.HologramType.HOLOGRAM) {
                        h.center();
                        if (h.shouldRemove()) {
                            if (hpBars.containsKey(h.getEntity())) {
                                hpBars.remove(h.getEntity());
                            }
                            if (npcTags.containsKey(h.getEntity())) {
                                npcTags.remove(h.getEntity());
                            }
                            h.destroy(false);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 10L, 1L);
    }

    public List<Hologram> holograms = new ArrayList<>();
    public List<Hologram> getHolos() {
        return holograms;
    }

    /*
    *
    * PARTY VARIABLES
    *
    */

    public Map<Player, Party> invite = new HashMap<Player, Party>();
    public Map<Player, Party> getInvites() {
        return invite;
    }

    private PartyManager pm;
    public PartyManager getPM() {
        return pm;
    }

    /*
     *
     *  TOWN VARIABLES
     *
     */

    private Map<UUID, Citizen> uuidCitizenMap = new HashMap<UUID, Citizen>();
    public Map<UUID, Citizen> getUUIDCitizenMap() {
        return uuidCitizenMap;
    }

    private ArrayList<Town> towns = new ArrayList<Town>();
    public ArrayList<Town> getTowns() {
        return towns;
    }

    public void setFullTownList(List<String> fulltown) {
        try {
            File tFile = new File("plugins/Rift/data/townlist/townlist.yml");
            FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
            tData.set("FullTownList", fulltown);
            tData.save(tFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFullTownList() {
        try {
            File tFile = new File("plugins/Rift/data/townlist/townlist.yml");
            FileConfiguration tData = YamlConfiguration.loadConfiguration(tFile);
            return tData.getStringList("FullTownList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
     *
     * CLASS VARIABLES
     *
     */

    EffectManager em = new EffectManager(this);

    public EffectManager getEm() {
        return em;
    }

    public RPGPlayer getRP(Player p) {
        return getPC().get(p.getUniqueId());
    }

    public int getSkillLevel(Player p, String name) {
        if (getRP(p).getSkillFromUpgradedSkill(name) != null) {
            return getRP(p).getSkillLevels().get(getRP(p).getSkillFromUpgradedSkill(name).getName());
        }
        return getRP(p).getSkillLevels().get(getRP(p).getSkillFromName(name).getName());
    }

    private ClassManager cm;
    public ClassManager getCM() {
        return cm;
    }

    private Map<UUID, RPGPlayer> pc = new HashMap<>();
    public Map<UUID, RPGPlayer> getPC() {
        return pc;
    }

    public double getMana(Player pl) {
        UUID p = pl.getUniqueId();
        if (getPC().get(p) != null) {
            if (getPC().get(p).getPClass() != null) {
                return getPC().get(p).getCMana();
            }
            return getPC().get(p).getCMana();
        }
        return 0;
    }

    public void setMana(Player pl, double m) {
        UUID p = pl.getUniqueId();
        if (getPC().get(p) != null) {
            if (getPC().get(p).getPClass() != null) {
                getPC().get(p).setMana(m);
            }
        }
    }

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections)
            return radial[Math.round(yaw / 45f) & 0x7].getOppositeFace();

        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static String yawToString(float yaw) {
        BlockFace bf = radial[Math.round(yaw / 45f) & 0x7].getOppositeFace();
        if (bf == BlockFace.NORTH) {
            return "N";
        }
        if (bf == BlockFace.NORTH_WEST) {
            return "NW";
        }
        if (bf == BlockFace.NORTH_EAST) {
            return "NE";
        }
        if (bf == BlockFace.SOUTH) {
            return "S";
        }
        if (bf == BlockFace.SOUTH_EAST) {
            return "SE";
        }
        if (bf == BlockFace.SOUTH_WEST) {
            return "SW";
        }
        if (bf == BlockFace.EAST) {
            return "E";
        }
        if (bf == BlockFace.WEST) {
            return "W";
        }
        return "";
    }

    private static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    private static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    public static void sendHp(Player p) {
        if (getInstance().getRP(p) != null) {
            RPGPlayer rp = getInstance().getRP(p);
            DecimalFormat dF = new DecimalFormat("#.##");
            DecimalFormat df = new DecimalFormat("#");
            //double mr = player.getPClass().getCalcMR(player.getLevel());
            //double armor = player.getPClass().getCalcArmor(player.getLevel());
            //String mrper = Main.color("&b" + dF.format(100.0 * (1-(300.0/(300.0+mr)))) + "% MR");
            //String amper = Main.color("&c" + dF.format(100.0 * (1-(300.0/(300.0+armor)))) + "% AM");
            String ad = Main.color("&c" + dF.format(rp.getAD()) + " AD");
            String ap = Main.color("&b" + dF.format(rp.getAP()) + " AP");
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&c❤ " + dF.format(p.getHealth()) + "    &7" + df.format(p.getLocation().getX()) + " &f" + yawToString(p.getLocation().getYaw()) + " &7" + df.format(p.getLocation().getZ()) + "    &b✦ " + rp.getPrettyCMana())));
            p.setLevel(rp.getLevel());
            p.setExp(Math.min(Math.max((float) rp.getPercent(), 0.0f), 1.0F));
        }
    }

    public void hpPeriodic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    sendHp(p);
                }
            }
        }.runTaskTimer(this, 20L, 4L);
    }

    public void manaRegen() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    UUID p = pl.getUniqueId();
                    if (getPC().get(p) != null && getPC().get(p).getPClass() != null) {
                        int level = getPC().get(p).getLevel();
                        double mana = getMana(pl);
                        double maxmana = getPC().get(p).getPClass().getCalcMana(level);
                        if (mana < maxmana) {
                            if (!pl.isDead()) {
                                double manaGain = getPC().get(p).getPClass().getCalcManaRegen(level);
                                setMana(pl, Math.min(getMana(pl) + manaGain, maxmana));
                                sendHp(pl);
                            } else {
                                setMana(pl, 0);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1L, 20L);
    }

    public void cooldownsPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    UUID p = pl.getUniqueId();
                    if (getPC().get(p) != null && getPC().get(p).getBoard() != null) {
                        //try {
                            getPC().get(p).refreshCooldowns();
                            //getPC().get(p).getBoard().update();
                        //} catch (ConcurrentModificationException ex) {

                        //}
                        getPC().get(p).getBoard().updateSkillbar();
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
    }

    public void statusesPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    getRP(pl).getBoard().statusUpdate();
                    List<Damage> remo = new ArrayList<>();
                    for (Damage d : getRP(pl).getDamages()) {
                        if (d.getLifetime() <= 0) {
                            remo.add(d);
                        } else {
                            d.decLifetime();
                        }
                    }
                    for (Damage d : remo) {
                        d.scrub();
                        getRP(pl).getDamages().remove(d);
                    }
                    List<StatusObject> statuses = getRP(pl).getSo();
                    if (statuses != null) {
                        for (StatusObject so : statuses) {

                            //List<StatusValue> remove = new ArrayList<>();
                            for (StatusValue s : so.getStatuses()) {
                                if (!s.getDurationless() && 20 * 0.001 * (System.currentTimeMillis() - s.getTimestamp()) >= s.getDuration()) {
                                    so.getCBT().add(s);
                                }
                            }

                            for (StatusValue rem : so.getCBT()) {
                                rem.scrub();
                                so.getStatuses().remove(rem);
                            }

                            /*for (StatusValue rem : remove) {
                                rem.scrub();
                                so.getStatuses().remove(rem);
                            }*/
                        }
                    }
                    getRP(pl).updateWS();
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }

    public void updatePeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    getRP(pl).pushFiles();
                }
            }
        }.runTaskTimer(this, 100L, 20 * 180);
    }

    public void passivesPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    getCM().passives(pl);
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }

    public void chatPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  p : Bukkit.getOnlinePlayers()) {
                    ChatFunctions.updateName(p);
                    getRP(p).updateStats();
                }
            }
        }.runTaskTimer(this, 10L, 5L);
    }


    public void updateTablistPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!isNPC(p) && getRP(p) != null) {
                        getRP(p).tabUpdate();
                    }
                }
            }
        }.runTaskTimer(this, 20L, 10L);
    }

    /*

    Custom Mob Section

     */

    public CustomEntityType warriorZombie;

    @Override
    public void onLoad() {
        /*NBTInjector.inject();
        so("&dRIFT: &fNBTAPI Injected");*/

        warriorZombie = new CustomEntityType<WarriorZombie>("zombie_warrior", WarriorZombie.class, EntityTypes.ZOMBIE, WarriorZombie::new);
        warriorZombie.register();

        so("&dRIFT: &fCustom Mobs registered");
    }

    public Map<UUID, Boolean> muted = new HashMap<>();
    public Map<UUID, Boolean> getMuted() {
        return muted;
    }

    public Map<UUID, String> values = new HashMap<>();
    public Map<UUID, String> getTextureValues() {
        return values;
    }

    public Map<UUID, String> signatures = new HashMap<>();
    public Map<UUID, String> getTextureSigs() {
        return signatures;
    }

    private Scoreboard sc;
    public Scoreboard getSC() {
        return sc;
    }

    private Team fake;
    public Team getFake() {
        return fake;
    }

    @Override
    public void onEnable() {

        so("&dRIFT: &fEnabling Plugin!");

        setupChat();
        so("&dRIFT: &fHooked into Vault Chat!");

        protocolManager = ProtocolLibrary.getProtocolManager();
        so("&dRIFT&7: &fProtocolLib hooked!");

        sc = Bukkit.getScoreboardManager().getMainScoreboard();
        fake = sc.registerNewTeam("00Placeholders");
        /*Objective objective = sc.registerNewObjective("PL", "dummy", "ph");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);*/


        final List<WrappedGameProfile> names = new ArrayList<WrappedGameProfile>();
        names.add(new WrappedGameProfile("1", ChatColor.LIGHT_PURPLE + "DISCORD: " + ChatColor.WHITE + "discord.therift.net"));
        names.add(new WrappedGameProfile("2", ChatColor.LIGHT_PURPLE + "WEBSITE: " + ChatColor.WHITE + "therift.net"));
        //If you want to add more message, copy 'names.add(new WrappedGameProfile("number", "ur message"));'
        //Make sure that 'number' goes in order. Instance:
        //names.add(new WrappedGameProfile("1", ChatColor.LIGHT_PURPLE + "This is message 1!"));
        //names.add(new WrappedGameProfile("2", ChatColor.GREEN + "This is message 2!"));
        //names.add(new WrappedGameProfile("3", ChatColor.GREEN + "This is message 3!"));
        //names.add(new WrappedGameProfile("4", ChatColor.GREEN + "This is message 4!"));
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
            @Override
            public void onPacketSending(PacketEvent event) {
                event.getPacket().getServerPings().read(0).setPlayers(names);
            }
        });

        setupPacketListeners();
        so("&dRIFT&7: &fProtocolLib Packet Listeners Enabled!");
        getCommand("info").setExecutor(new InfoCommand());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("skill").setExecutor(new SkillCommand());
        getCommand("class").setExecutor(new ClassCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("inv").setExecutor(new InvseeCommand());
        getCommand("killall").setExecutor(new KillAllCommand());
        getCommand("gms").setExecutor(new GamemodeCommand());
        getCommand("gmc").setExecutor(new GamemodeCommand());
        getCommand("gmss").setExecutor(new GamemodeCommand());
        getCommand("lag").setExecutor(new LagCommand());
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("tell").setExecutor(new MsgCommand());
        getCommand("message").setExecutor(new MsgCommand());
        getCommand("r").setExecutor(new MsgCommand());
        getCommand("reply").setExecutor(new MsgCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("mana").setExecutor(new ManaCommand());
        getCommand("setclass").setExecutor(new SetClassCommand());
        getCommand("skills").setExecutor(new SkillsCommand());
        getCommand("cd").setExecutor(new CDCommand());
        getCommand("seen").setExecutor(new SeenCommand());
        getCommand("biome").setExecutor(new BiomeLevelCommand());
        getCommand("arestart").setExecutor(new TimedrestartCommand());
        getCommand("arestartfast").setExecutor(new FastRestartCommand());
        getCommand("level").setExecutor(new LevelCommand());
        getCommand("setlevel").setExecutor(new ExpCommand());
        getCommand("addlevel").setExecutor(new ExpCommand());
        getCommand("setexp").setExecutor(new ExpCommand());
        getCommand("addexp").setExecutor(new ExpCommand());
        getCommand("dummy").setExecutor(new DummyCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("biomes").setExecutor(new BiomesCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("warp").setExecutor(new WarpsCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("delwarp").setExecutor(new DelWarpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("sp").setExecutor(new SkillpointCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("bind").setExecutor(new BindCommand());
        getCommand("time").setExecutor(new TimeCommand());

        getCommand("town").setExecutor(new TownCoreCommand());

        so("&dRIFT: &fEnabled commands!");

        Bukkit.getPluginManager().registerEvents(new InfoCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PartyCommand(), this);
        Bukkit.getPluginManager().registerEvents(new ClassManager(), this);
        Bukkit.getPluginManager().registerEvents(new ClassCommand(), this);
        Bukkit.getPluginManager().registerEvents(new ChatFunctions(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerinfoListener(), this);
        Bukkit.getPluginManager().registerEvents(new Environmental(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new DummyCommand(), this);
        Bukkit.getPluginManager().registerEvents(new Hologram(), this);
        Bukkit.getPluginManager().registerEvents(new SkillsCommand(), this);
        Bukkit.getPluginManager().registerEvents(new EntityHealthBars(), this);
        Bukkit.getPluginManager().registerEvents(new Mobs(), this);
        Bukkit.getPluginManager().registerEvents(new JoinMenu(), this);
        Bukkit.getPluginManager().registerEvents(new SettingsCommand(), this);
        Bukkit.getPluginManager().registerEvents(new CustomDeath(), this);
        Bukkit.getPluginManager().registerEvents(new Stuns(), this);
        Bukkit.getPluginManager().registerEvents(new Food(), this);
        Bukkit.getPluginManager().registerEvents(new Items(), this);
        Bukkit.getPluginManager().registerEvents(new Accessories(), this);
        Bukkit.getPluginManager().registerEvents(new NPCTag(), this);
        Bukkit.getPluginManager().registerEvents(new TownManager(), this);
        Bukkit.getPluginManager().registerEvents(new TownCoreCommand(), this);

        //Skills
        Bukkit.getPluginManager().registerEvents(new Skillcast(), this);
        Bukkit.getPluginManager().registerEvents(new BindCommand(), this);

        Bukkit.getPluginManager().registerEvents(new PyromancerListeners(), this);
        Bukkit.getPluginManager().registerEvents(new WandererListeners(), this);
        so("&dRIFT: &fRegistered events!");

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTasks(this);

        RPGConstants loadLevels = new RPGConstants();
        hpRegen();

        pm = new PartyManager();
        cm = new ClassManager();
        hpPeriodic();
        manaRegen();
        chatPeriodic();
        passivesPeriodic();
        cooldownsPeriodic();
        statusesPeriodic();
        updatePeriodic();
        remHpBar();
        riseBars();
        npcTags();
        updateTablistPeriodic();

        /*for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getType() == EntityType.ARMOR_STAND) {
                    if (e.isCustomNameVisible()) {
                        for (String s : RPGConstants.damages) {
                            Bukkit.broadcastMessage(e.getCustomName().replace("§","&"));
                            if (e.getCustomName().replace("§","&").contains(s)) {
                                e.remove();
                                break;
                            }
                        }
                    }
                    continue;
                }
                //if (e instanceof LivingEntity && !(e instanceof Player)) {
                 //   if (!getHpBars().containsKey(e)) {
                  //      DecimalFormat dF = new DecimalFormat("#.##");
                    //    getHpBars().put(e, new Hologram(e, e.getLocation().add(new Vector(0, e.getHeight() + 0.1, 0)), "&f" + dF.format(((LivingEntity)e).getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
                    //}
                //}
            }
        }*/

        autorestart();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time sync");

        so("&dRIFT: &fSetup complete!");

    }

    @Override
    public void onDisable() {

        /*for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getType() == EntityType.ARMOR_STAND) {
                    if (e.isCustomNameVisible()) {
                        for (String s : RPGConstants.damages) {
                            if (e.getCustomName().replace("§","&").contains(s)) {
                                e.remove();
                                break;
                            }
                        }
                    }
                    continue;
                }
            }
        }*/

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTasks(this);

        for (Hologram h : holograms) {
            h.destroy(true);
        }

        List<String> projectiles = new ArrayList<>();
        projectiles.add("Fireball");
        projectiles.add("Combust");

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e != null && e.getCustomName() != null && e.getCustomName() != null && projectiles.contains(e.getCustomName())) {
                    e.remove();
                    for (String s : projectiles) {
                        if (EntityUtils.getCustomName(e) != null && EntityUtils.getCustomName(e).equalsIgnoreCase(s)) {
                            e.remove();
                        }
                    }
                }
            }
        }

        so("&dRIFT: &fDisabling Plugin!");

    }

    /*
    *
    * Msg variables
    *
     */
    public playerinfoManager msg = new playerinfoManager();
    public playerinfoManager getMsg() {
        return msg;
    }

    /*
    *
    * Party methods.
    *
     */

    public void partyStartup() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getPM().cleanParties();
            }
        }.runTaskTimerAsynchronously(this, 1L, 200L);
    }

    /*
    *
    * Chat methods
    *
     */

    private Chat chat;

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    /*
    *
    * Protocol Lib stuff
    *
     */

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
    private ProtocolManager protocolManager;
    public void setupPacketListeners() {
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                    if (event.getPacket().getNewParticles().getValues().get(0).getParticle().name().contains("DAMAGE_INDICATOR")) {
                        event.setCancelled(true);
                    }
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                if (!packet.getStrings().read(0).isEmpty() && packet.getStrings().read(0).toCharArray()[0] == '/') {

                } else {
                    if (muted.containsKey(player.getUniqueId()) && muted.get(player.getUniqueId())) {
                        Main.so("&c[MUTED] &7" + player.getName() + " &8» &f" + packet.getStrings().read(0));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.hasPermission("core.mod")) {
                                msg(p, "&c[MUTED] &7" + player.getName() + " &8» &f" + packet.getStrings().read(0));
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

    public Chat getChat() {
        return chat;
    }

    public static void so(String s) {
        Bukkit.getServer().getConsoleSender().sendMessage(color(s));
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void msg(Player p, String text) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    public static void sendCenteredMessage(Player player, String message){
        message = ChatColor.translateAlternateColorCodes('&', message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";
        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                recentColorCode = "§" + c;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else if(c == ' ') lastSpaceIndex = charIndex;
            else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
            if(messagePxSize >= MAX_PX){
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }
            charIndex++;
        }
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
        if(toSendAfter != null) sendCenteredMessage(player, toSendAfter);
    }
    public static void sendCenteredMessage(Player player, TextComponent tc){

        String message = ChatColor.translateAlternateColorCodes('&', tc.getText());

        for(BaseComponent bc : tc.getExtra()) {
            message += bc.toPlainText();
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";
        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                recentColorCode = "§" + c;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else if(c == ' ') lastSpaceIndex = charIndex;
            else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
            if(messagePxSize >= MAX_PX){
                toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                message = message.substring(0, lastSpaceIndex + 1);
                break;
            }
            charIndex++;
        }
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }

        tc.setText(sb.toString() + tc.getText());
        player.sendMessage(tc);
        if(toSendAfter != null) sendCenteredMessage(player, toSendAfter);
    }

    public Location getSpawn() {
        File pFile = new File("plugins/Rift/warps.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("Spawn")) {
            return (Location) pData.get("Spawn");
        } else {
            return null;
        }
    }

    public String getTextureValueOffline(UUID id) {
        OfflinePlayer t = Bukkit.getOfflinePlayer(id);
        File pFile = new File("plugins/Rift/data/textures/" + t.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("TextureValue")) {
            return pData.getString("TextureValue");
        } else {
            return null;
        }
    }

    public String getTextureSignatureOffline(UUID id) {
        OfflinePlayer t = Bukkit.getOfflinePlayer(id);
        File pFile = new File("plugins/Rift/data/textures/" + t.getUniqueId() + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("TextureSignature")) {
            return pData.getString("TextureSignature");
        } else {
            return null;
        }
    }

}
