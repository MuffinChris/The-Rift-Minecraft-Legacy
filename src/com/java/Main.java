package com.java;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.destroystokyo.paper.Title;
import com.java.communication.ChatFunctions;
import com.java.communication.MsgCommand;
import com.java.communication.PlayerinfoListener;
import com.java.communication.playerinfoManager;
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
import com.java.holograms.EntityHealthBars;
import com.java.holograms.Hologram;
import com.java.rpg.*;
import com.java.rpg.classes.*;
import com.java.rpg.classes.casting.BindCommand;
import com.java.rpg.classes.casting.Skillcast;
import com.java.rpg.classes.commands.admin.CDCommand;
import com.java.rpg.classes.commands.admin.ManaCommand;
import com.java.rpg.classes.commands.playerinfo.*;
import com.java.rpg.classes.skills.Pyromancer.*;
import com.java.rpg.classes.skills.Pyromancer.Fireball;
import com.java.rpg.classes.skills.Wanderer.Bulwark;
import com.java.rpg.classes.statuses.Stuns;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.classes.utility.StatusObject;
import com.java.rpg.classes.utility.StatusValue;
import com.java.rpg.mobs.CustomEntityType;
import com.java.rpg.mobs.MobEXP;
import com.java.rpg.mobs.commands.BiomeLevelCommand;
import com.java.rpg.mobs.commands.BiomesCommand;
import com.java.rpg.mobs.grassy.WarriorZombie;
import com.java.rpg.modifiers.DamageListener;
import com.java.rpg.modifiers.Environmental;
import com.java.rpg.modifiers.utility.Damage;
import com.java.rpg.modifiers.utility.DamageTypes;
import com.java.rpg.player.*;
import com.java.rpg.player.Items;
import com.java.rpg.player.utility.PlayerListener;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.slikey.effectlib.EffectManager;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
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
import org.bukkit.util.Vector;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends JavaPlugin {

    /*

    Future Planning Segment (LOTS OF GOOD IDEAS FOR INTERESTING MECHANICS):
        Rift Theme:
            - The dungeons are all portal to nother dimension style.
            - idk use pink and purple and incorporate portals.

            - Witcher 3 Style "Great Frost" / "Eternal Winter"
            - Gotta beat it away in future expansions
            - this is the big bad! (perhaps quests o-o?)
        Dynamics and Environment:
            - Weather
             -. Days, seasons, and weather (hypixel style). Useful for economy etc. Also make weather matter (Winter will fuck you up! Winter can also cause Mob faction of winter to spawn and kill u)
             -. Add Dynamic Town Mechanics (resources from nearby resource locations, biome benefits, penalize traveling far [for example, making resource gens need to be close if u want more u need to optimize])
        Farmer Profession:
            - Breed crops to become resistant to weather, provide more resources, etc
        Towns and Professions Interactions:
            - Make it cost lots of resources to build Town machines. Any sort of automation, spawn portals, NPC housing etc needs to follow strict schematic like nether portal and conduit but also gives big buffs
            - NPC Housing could just count as a bed with a door lmao
            - Said buildings that protect and empower town (for example town not protected form start, u need to build a shield gen or smthn) cost resources to maintain
            - Make repairing items cost a lot of mixed ingots and resources so you really need to get more resources which means traveling or optimizing.
            - Resource gens differ based on biome! Exploration! Conquest. New mobs to find and see
            - World Border should be something smaller, idk like 15k by 15k to encourage fights and land management
        Dungeons:
            - Dungeons should also give resource rewards to supplement town resource costs
            - Dungeons can have resource zones that give superb resources that regenerate but ofc u get fukin ganked
            - Stunned bosses cant use abilities!
            - Nearby Player size should cause boss to scale. Nearby player size should cause amount of mob spawning
            - SAML Flags on bosses

        Professions:
        Two Professions Primary and Secondary (100 | 50)
        Blacksmith:
            - Do not damage Anvils when using it
            - Can make repair kits
            - Can combine enchantments past big levels (custom define this stuff)
        Enchanter:
            - Can pick any enchantment from Enchantment table (at a large cost of course)
            - Regular players have similar to Vanilla randomized enchantments
        Alchemist:
            - Custom Alchemy Menu that uses many ingredients to craft potions
            - Potions have charges (ie 5/5), and my own custom effects
            - Potions have a max uses (until they must be crafted again). Until then, it requires minor amounts of
                refilling reagents (ie, water + small amnt of initial crafting ingredients) to refill the charges.
                However they break after max uses is gone over.
        Chef:
            - Can cook custom food with bonuses (more hunger regen, health regen, stat buffs minor for long duration etc)
            - Golden apples for custom food!
        Farmer:
            - Can rightclick crops to harvest, seed remains placed.
            - Gets many drops from the farm.
            - Can breed crops together

        In general, solo players should use the market or publicly available vendors.

        Leveling:
        - superskills at 30 and 40. 45 is max level gear cap. 50 is prestige.
        - Every 1 level
        - Update levelup msg

        Elemental Info:

                (When on items)
                Impact: Raw Damage, No Pen
                Puncture: Armor Pen, Crit, Low Damage
                Slash: All Rounder

                (When on items)
                Fire: &c✴ (Raw DPS (High Speed, High Dmg, lack of armor))
                Ice: &b❆ (CRIT)
                Water: &3✾ (REMOVE)
                Earth: &2❈ (SLOW ATK SPEED, HIGH DMG)
                Air: &f✸ (All Rounder (dmg, speed, everything!))
                Electric: &e⚡ (ATTACK SPEED)

                Armor:
                Armor
                MR
                Elemental Defense
                Attribute Bonuses
                Variation of 20%ish (Rerolls!)

                Items:
                Spells should scale off of item with a spell modifier! Base skill should have zero scaling

                Fire > Ice
                Fire < Water
                Fire < Earth

                Ice > Earth
                Ice > Air
                Ice < Fire

                Water > Fire
                Water > Earth
                Water < Electric
                Water < Ice

                Earth > Fire
                Earth > Electric
                Earth < Ice
                Earth < Water

                Electric < Earth
                Electric > Water
                Electric > Air

                Air < Ice
                Air < Electric
                Air > Earth
         Accessories:
            Bracelet, Ring, Ring, Amulet

    DIRECT LINE TODO LIST:

        -3. Mob HP Bar and Name as hologram

        -2. Add Impact, Slash, Puncture (fully implement with items!)

        -1. Add Attributes (heroes style)

        0. Make abilities not able to target or damage tamed mobs! (testing)

        1. Find a better solution for mob burning. Perhaps XP cap gain if Environmental but only if the XP is very high! (testing)

        2. Disable Drowned Conversion (have own ocean mobs). Make sure to update DmgThreshold and EDefense when a mob transforms.

        3. List command works by permissions (ranks) (testing)

        4 (RELEVANT RN): Add elemental damage from non player sources (mobs)

        5. Setting to hide dmg holograms and EXP and TOGGLE OFFHAND!!!

        6. add elemental scaling and balance damage for skills. Fix pyro fire checks.

        7. Make guardan lasers and spikes do damage

        8. Make legacy projectiles like fireballs hold a PersistentDataContainer instead of customname (will need to hold Elemental Dmg). Same with Enderdragon damage

        9. Iron Golems and Villagers need to have a base level that is higher than 0.

        10. Pyroclasm targets allies and in general non pvp targets. Separate targetting func into Target Ally, Target Enemy, Target All.

        11. Improve visuals of Skills GUI. Update Class and Info cmd

        12. zombie warrior has enchanted item!!!?!??!?! Look into it

        13. Make experience bottles give class exp

        14. Make wither always lvl 50

        15. Display Raids and Boss Health on the top bar? (change color of top bar)

        16. Wither Explosions are crazy Strong.

        17. All biomes capped 1-20 (some a bit higher, but never extremes) Dungeons forced to level

        18. More vanilla exp drop from better mobs

        19. Balance totems of undying (make them good, perhaps have durability or charges!)

        20. Design all custom mobs, step away from formulas. Make sure to use Name PData tag incase mob has dif name than type.

        21. Disallow Shield usage for non shield classes

        22. Iron golem healing is absolute dogshit

        23. Allow spells to be casted twice! (no errors lul)

        0. ITEM STUFF / IDEAS:
        - make classes use specific item type.
        - TIERS: Mythical, Legendary, Epic, Rare, Uncommon, Common
        - Mythical has unique shit, less randomization, and specific items. Rest are fully randomized.
        - Randomize stats to each weapon that a class must wear (for balancing and better stat allocation)
        - Armor is the more general random portion? (cause u can wear any armor. Perhaps balance based on weight, lower weight generally more magical stats)

        (stuff to do / fix)
        - RANDOMIZE FISHING LOOT AND CHEST LOOT (NATHANWOLF MADE POST ON SPIGOT ABOUT BLOCKPOPULATOR AND TILE ENTITIES)
        - Protection can give bonus armor points? Perhaps percentage armor increase.
        - Test if curse of binding (and even vanishing) breaks shit
        - Put enchantments in specific lore section (hide enchs with itemflag, then place in lore)
        - To make enchantments vanilla irrelevant need to check Ench Table Ench, and Anvil Ench
        - Note to self: Combining Enchantments seems fine. Takes the lore of the first item. So just need to make sure lore will contain the new enchantment. USE NBT!
        - Need to make Villager traded items custom! (perhaps make villagers trade high end random items)
        - Elytra durability (ALL DURABILITY) needs to have affects other than removal of stats (elytra is an active)
        Links for Info:
            https://minecraft.gamepedia.com/Item_repair
            https://minecraft.gamepedia.com/Enchanting
            https://minecraft.gamepedia.com/Item_durability

        - Add all durability items to custom durability (Fishing Rods n Stuff)
        - Make Custom Anvil and Enchantment Table
        - In general custom enchantments. Allow unbreaking up to 10, etc, essentially remove Vanilla enchants, everything custom. That means removing Proj Prot etc.
        - Need to make drops and loot not have vanilla enchantments. If the enchantment is not supported (like proj prot), remove it!
        - Make sure to change existing ench changes (ie. mending)


     RIFT THEME IDEAS:
        Players can open a personal rift using a Rift Key or Rift Gem or Rift Stone
        The personal rift is an end world with no blocks, cancel all mob spawning, perhaps do custom biome?
        The personal rift needs to disable things like Wither and stuff.
        Personal rift = hide all players including self, and check if login world is Personal Rift area.
        Surrounded with black wool or concrete and barriers, grant them a 5x5 chunk of space to work in.

        Towns:
            - Anchors - "Anchor" points that allow people to teleport to using rift tech
            - Rift generator - Open a rift and enter it, can teleport to an anchor or random location
            - Terrain control and claiming needs to be done based off of a source block, ie. Nexus
            - Regenerator Core - Heal all damage done to blocks through explosions and mobs


        Rifts can open and spawn bosses
        Rifts can open for other things too...
        Blood Moon - Rifts spawn with red mobs, drops red essence or something of the sort.

    * Class ideas:
    * Build out of components for certain classes, like Warframe. Unlock em!
    * Alternate Resource Systems, like Rage, Shadow, etc, thematic!
    *
    */

    public String noperm = "&cNo permission!";

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
                Bukkit.getServer().broadcastMessage(Main.color("&8\u00BB &a&lServer Automatically Restarting in 5 minutes!"));
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

    // MOB HP REGEN
    public void hpRegen() {
        new BukkitRunnable() {
            public void run() {
                for (World w : Bukkit.getWorlds()) {
                    for (LivingEntity e : w.getLivingEntities()) {
                        if (!(e instanceof Player) && !(e instanceof ArmorStand) && !(e.isDead())) {
                            if (e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() > e.getHealth() && MobEXP.getHPRegen(e) > 0) {
                                LivingEntity ent = e;
                                DecimalFormat df = new DecimalFormat("#.##");
                                //Hologram magic = new Hologram(ent, ent.getLocation(), "&a❤" + df.format(MobEXP.getHPRegen(e)), Hologram.HologramType.DAMAGE);
                                //magic.rise();
                                DecimalFormat dF = new DecimalFormat("#.##");
                                if (getHpBars().containsKey(ent)) {
                                    getHpBars().get(ent).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                                }
                            }
                            e.setHealth(Math.max(0, Math.min(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), MobEXP.getHPRegen(e) + e.getHealth())));
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
                    hpBars.get(e).destroy();
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
                        ArmorStand stand = h.getStand();
                        stand.teleport(stand.getLocation().add(new Vector(0, 0.025, 0)));
                        h.incrementLifetime();
                        if (h.getType() == Hologram.HologramType.EXP || h.getType() == Hologram.HologramType.STATUS) {
                            if (h.getLifetime() * 0.02 >= 1) {
                                h.destroy();
                            }
                        } else {
                            if (h.getLifetime() * 0.025 >= 1) {
                                h.destroy();
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
                            h.destroy();
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

    public DamageTypes dmg = new DamageTypes();

    public List<Damage> getDmg() {
        return dmg.getDamages();
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

    public Map<Player, Boolean> pchat = new HashMap<Player, Boolean>();
    public Map<Player, Boolean> getPChat() {
        return pchat;
    }

    private PartyManager pm;
    public PartyManager getPM() {
        return pm;
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
        if (getRP(p).getPClass().getSuperSkills().contains(getRP(p).getSkillFromName(name))) {
            int index = 0;
            for (Skill sk : getRP(p).getPClass().getSuperSkills()) {
                if (sk.getName().equalsIgnoreCase(name)) {
                    return getRP(p).getSkillLevels().get(getRP(p).getPClass().getSkills().get(index).getName());
                }
                index++;
            }
        }
        if (getRP(p).getSkillLevels().containsKey(name)) {
            return getRP(p).getSkillLevels().get(name);
        }
        return -1;
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
            if (getPC().get(p).getPClass() instanceof PlayerClass) {
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
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&c❤ " + dF.format(p.getHealth()) + "     &b✦ " + rp.getPrettyCMana())));
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
                            getPC().get(p).getBoard().update();
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
        }.runTaskTimer(Main.getInstance(), 100L, 20 * 180);
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

    @Override
    public void onEnable() {

        so("&dRIFT: &fEnabling Plugin!");

        setupChat();
        so("&dRIFT: &fHooked into Vault Chat!");

        protocolManager = ProtocolLibrary.getProtocolManager();
        so("&dRIFT&7: &fProtocolLib hooked!");

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
        getCommand("biomes").setExecutor(new BiomesCommand());

        getCommand("warp").setExecutor(new WarpsCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("delwarp").setExecutor(new DelWarpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());

        getCommand("sp").setExecutor(new SkillpointCommand());
        getCommand("settings").setExecutor(new SettingsCommand());
        getCommand("bind").setExecutor(new BindCommand());
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
        Bukkit.getPluginManager().registerEvents(new MobEXP(), this);
        Bukkit.getPluginManager().registerEvents(new AFKInvuln(), this);
        Bukkit.getPluginManager().registerEvents(new SettingsCommand(), this);
        Bukkit.getPluginManager().registerEvents(new CustomDeath(), this);
        Bukkit.getPluginManager().registerEvents(new Stuns(), this);
        Bukkit.getPluginManager().registerEvents(new Food(), this);
        Bukkit.getPluginManager().registerEvents(new Items(), this);
        Bukkit.getPluginManager().registerEvents(new NPCTag(), this);

        //Skills
        Bukkit.getPluginManager().registerEvents(new Skillcast(), this);
        Bukkit.getPluginManager().registerEvents(new BindCommand(), this);

        Bukkit.getPluginManager().registerEvents(new Bulwark(), this);

        Bukkit.getPluginManager().registerEvents(new Fireball(), this);
        Bukkit.getPluginManager().registerEvents(new MeteorShower(), this);
        Bukkit.getPluginManager().registerEvents(new WorldOnFire(), this);
        Bukkit.getPluginManager().registerEvents(new InfernoVault(), this);
        Bukkit.getPluginManager().registerEvents(new Pyroclasm(), this);
        Bukkit.getPluginManager().registerEvents(new Combust(), this);
        so("&dRIFT: &fRegistered events!");


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
                if (e instanceof LivingEntity && !(e instanceof Player)) {
                    if (!getHpBars().containsKey(e)) {
                        DecimalFormat dF = new DecimalFormat("#.##");
                        getHpBars().put(e, new Hologram(e, e.getLocation().add(new Vector(0, e.getHeight() - 0.2, 0)), "&f" + dF.format(((LivingEntity)e).getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
                    }
                }
            }
        }

        autorestart();

        so("&dRIFT: &fSetup complete!");

    }

    @Override
    public void onDisable() {

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTasks(this);

        for (Hologram h : holograms) {
            h.destroy();
        }

        List<String> projectiles = new ArrayList<>();
        projectiles.add("Fireball");
        projectiles.add("Meteor");
        projectiles.add("Combust");

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e != null && e.getCustomName() != null && e.getCustomName() instanceof String && projectiles.contains(e.getCustomName())) {
                    e.remove();
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

    public Location getSpawn() {
        File pFile = new File("plugins/Rift/warps.yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
        if (pData.contains("Spawn")) {
            return (Location) pData.get("Spawn");
        } else {
            return null;
        }
    }

}
