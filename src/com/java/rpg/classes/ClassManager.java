package com.java.rpg.classes;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.java.Main;
import com.java.rpg.classes.skills.Earthshaker.*;
import com.java.rpg.classes.skills.Pyromancer.*;
import com.java.rpg.classes.skills.Wanderer.Adrenaline;
import com.java.rpg.classes.skills.Wanderer.Bulwark;
import com.java.rpg.classes.statuses.Stuns;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.utility.RPGConstants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class ClassManager implements Listener {

    private Main main = Main.getInstance();


    private List<UUID> fall;

    private Map<UUID, Integer> fallMap;


    public List<UUID> getFall() {
        return fall;
    }

    public Map<UUID, Integer> getFallMap() {
        return fallMap;
    }


    public ClassManager() {
        fall = new ArrayList<>();
        fallMap = new HashMap<>();
        createClasses();

    }

    @EventHandler
    public void expGain (PlayerPickupExperienceEvent e) {
        e.setCancelled(true);
        e.getExperienceOrb().setExperience(0);
        e.getExperienceOrb().remove();
    }

    @EventHandler
    public void expChange (PlayerLevelChangeEvent e) {
        if (e.getNewLevel() != main.getRP(e.getPlayer()).getLevel()) {
            e.getPlayer().setLevel(main.getRP(e.getPlayer()).getLevel());
        }
    }


    @EventHandler
    public void onClick (PlayerInteractEvent e) {
        if (!Stuns.isStunned(e.getPlayer()) && e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (main.getPC().get(e.getPlayer().getUniqueId()) != null) {
                if (main.getPC().get(e.getPlayer().getUniqueId()).getStatuses() != null) {
                    List<String> statuses = main.getPC().get(e.getPlayer().getUniqueId()).getStatuses();
                    List<Integer> indexesToRemove = new ArrayList<>();
                    int index = 0;
                    for (String status : statuses) {
                        if (status.contains("Warmup")) {
                            indexesToRemove.add(index);
                            index++;
                        }
                    }
                    for (int ind : indexesToRemove) {
                        statuses.remove(ind);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (!main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().put(e.getPlayer().getUniqueId(), new RPGPlayer(e.getPlayer()));
        }
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if (main.getPC().containsKey(e.getPlayer().getUniqueId())) {
            main.getPC().get(e.getPlayer().getUniqueId()).pushFiles();
            main.getPC().get(e.getPlayer().getUniqueId()).scrub();
            new BukkitRunnable() {
                public void run() {
                    main.getPC().remove(e.getPlayer().getUniqueId());
                }
            }.runTaskLater(Main.getInstance(), 10L);
        }
    }

    /*
    @EventHandler (priority = EventPriority.HIGHEST)
    public void armorMR (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (main.getPC().get(p) instanceof RPGPlayer && main.getPC().get(p).getPClass() instanceof PlayerClass) {
                double armor = main.getPC().get(p.getUniqueId()).getPClass().getCalcArmor(main.getPC().get(p.getUniqueId()).getLevel());
                double mr = main.getPC().get(p.getUniqueId()).getPClass().getCalcMR(main.getPC().get(p.getUniqueId()).getLevel());

                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                    e.setDamage(e.getDamage() * 1 - (armor / RPGConstants.defenseDiv));
                }

                if (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                    e.setDamage(e.getDamage() * 1 - (mr / RPGConstants.defenseDiv));
                }
            }
        }
    }*/

    public static Map<String, PlayerClass> classes = new HashMap<>();

    public static void createClasses() {
        List<Skill> skillsNone = new ArrayList<>();
        skillsNone.add(new Adrenaline());
        skillsNone.add(new Bulwark());

        classes.put(RPGConstants.defaultClassName, new PlayerClass(RPGConstants.defaultClassName, "&e" + RPGConstants.defaultClassName, RPGConstants.defaultHP, 40.0, PlayerClass.ResourceType.MANA,100, 2, 3, 0.1, "SWORD", 10, 60, 0, 6, 1, 40, 32, 0.5, 0.25, skillsNone,  180, new ElementalStack(40, 40, 40, 40, 40), new ElementalStack(0.25, 0.25, 0.25, 0.25, 0.25)));

        List<Skill> skillsPyro = new ArrayList<>();
        skillsPyro.add(new Fireball());
        skillsPyro.add(new WorldOnFire());
        skillsPyro.add(new Blaze());
        skillsPyro.add(new Pyroclasm());
        skillsPyro.add(new Combust());

        classes.put("Pyromancer", new PlayerClass("Pyromancer", "&6Pyromancer", 1500.0, 30, PlayerClass.ResourceType.MANA, 400, 5, 7, 0.14, "HOE", 10, 60, 0, 2.5, 20,20, 22, 0.41, 0.22, skillsPyro,  110, new ElementalStack(5, 20, 30, 50, 100), new ElementalStack(0, 0, 0, 1, 0.25)));

        List<Skill> skillsEarthshaker = new ArrayList<>();
        skillsEarthshaker.add(new StoneSkin());
        skillsEarthshaker.add(new Overwhelm());
        skillsEarthshaker.add(new Quake());
        skillsEarthshaker.add(new RockToss());
        skillsEarthshaker.add(new RollingStone());
        skillsEarthshaker.add(new Shatterstrike());


        classes.put("Earthshaker", new PlayerClass("Earthshaker", "&6Earthshaker", 2000.0, 40, PlayerClass.ResourceType.MANA,500, 5, 7, 0.14, "HOE", 10, 150, 0, 2.5, 20,20, 22, 0.41, 0.22, skillsEarthshaker,  110, new ElementalStack(5, 100, 30, 50, 100), new ElementalStack(0, 1, 0, 1, 0)));


    }

    public PlayerClass getPClassFromString(String s) {
        for (String cl : classes.keySet()) {
            if (cl.equalsIgnoreCase(s)) {
                return classes.get(cl);
            }
        }
        return null;
    }

    public void cleanToggle(Player p, Skill s) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        rp.getToggles().remove(s.getName());
        rp.getBoard().endToggle(s);
        List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
        for (Map<Integer, String> map : rp.getToggleTasks()) {
            if (map.containsValue(s.getName())) {
                tasksToRemove.add(map);
            }
        }

        for (Map<Integer, String> map : tasksToRemove) {
            scheduler.cancelTask((int) map.keySet().toArray()[0]);
            rp.getToggleTasks().remove(map);
        }
    }

    /*
    public void cleanToggles(Player p) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        List<String> skillsToRemove = new ArrayList<>();
        if (rp.getPClass() instanceof PlayerClass) {
            rp.getToggles().clear();
            List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
            for (Map<Integer, String> map : rp.getToggleTasks()) {
                tasksToRemove.add(map);
            }
            for (Map<Integer, String> map : tasksToRemove) {
                scheduler.cancelTask((int) map.keySet().toArray()[0]);
                rp.getToggleTasks().remove(map);
            }
        }
        skillsToRemove = new ArrayList<>();
    }*/

    public void passives(Player p) {
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        if (rp != null && rp.getPClass() != null) {
            List<Skill> skillsToAdd = new ArrayList<>();
            if (!rp.getSkillsAll().isEmpty()) {
                for (Skill s : rp.getSkillsAll()) {
                    if (rp.isPassive(s) && s.getLevel() <= rp.getLevel()) {
                        if (!rp.getPassives().contains(s.getName())) {
                            skillsToAdd.add(s);
                        }
                    }
                }
            }
            if (skillsToAdd.isEmpty()) {
                for (Skill s : skillsToAdd) {
                    rp.getPassives().add(s.getName());
                    int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                        public void run() {
                            s.passive(p);
                        }
                    }, 0L, s.getPassiveTicks());
                    Map<Integer, String> taskSkill = new HashMap<>();
                    taskSkill.put(task, s.getName());
                    rp.getPassiveTasks().add(taskSkill);
                }
            }
            cleanPassives(p);
        } else {
            cleanPassives(p);
        }
    }

    public void cleanPassives(Player p) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        if (rp != null) {
            List<String> skillsToRemove = new ArrayList<>();
            for (String s : rp.getPassives()) {
                if (rp.getSkillFromName(s) == null) {
                    skillsToRemove.add(s);
                    continue;
                }
                if (rp.getPClass() == null || (!rp.getSkillsAll().contains(rp.getSkillFromName(s)) || rp.getSkillFromName(s).getLevel() > rp.getLevel())) {
                    skillsToRemove.add(s);
                }
            }
            for (String s : skillsToRemove) {
                rp.getPassives().remove(s);
                List<Map<Integer, String>> tasksToRemove = new ArrayList<>();
                for (Map<Integer, String> map : rp.getPassiveTasks()) {
                    if (map.containsValue(s)) {
                        tasksToRemove.add(map);
                    }
                }

                for (Map<Integer, String> map : tasksToRemove) {
                    scheduler.cancelTask((int) map.keySet().toArray()[0]);
                    rp.getPassiveTasks().remove(map);
                }
                tasksToRemove = new ArrayList<>();
            }
            skillsToRemove = new ArrayList<>();
        }
    }

}
