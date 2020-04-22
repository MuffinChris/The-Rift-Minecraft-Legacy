package com.java.rpg.classes;

import com.java.Main;
import com.java.rpg.holograms.Hologram;
import com.java.rpg.damage.utility.Damage;
import com.java.rpg.classes.skills.Pyromancer.WorldOnFire;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.damage.utility.PhysicalStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skill {

    private Main main = Main.getInstance();

    private Material skillicon;
    public Material getSkillIcon() {
        if (skillicon == null) {
            return Material.LIME_DYE;
        }
        return skillicon;
    }

    public enum SkillType
    {
        CAST, TARGET, TOGGLE, PASSIVE, PASSIVE_CAST, PASSIVE_TARGET, PASSIVE_TOGGLE
    }
    private SkillType skillType;
    public SkillType getSkillType() {
        return skillType;
    }

    private int manaCost;
    private int cooldown;
    private int warmup;
    private int level;

    private int passiveTicks;

    private int toggleTicks;
    private int toggleMana;
    private int toggleCooldown;

    private String name = "DEFAULT_NAME";
    private List<String> description;

    private int targetRange;

    private Skill upgradedSkill;

    private Map<Player, List<Integer>> tasks;

    public Skill(String name, int manaCost, int cooldown, int warmup, int level, SkillType skilltype, Skill upgradedSkill, Material skillicon) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.toggleCooldown = cooldown;
        this.warmup = warmup;
        this.level = level;
        skillType = skilltype;
        this.skillicon = skillicon;
        this.upgradedSkill = upgradedSkill;
        tasks = new HashMap<>();
    }

    public boolean isUpgradeable() {
        return upgradedSkill != null;
    }

    public Skill getUpgradedSkill() {
        return upgradedSkill;
    }

    public int getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(int t) {
        targetRange = t;
    }

    public Map<Player, List<Integer>> getTasks() {
        return tasks;
    }

    public void setDescription(List<String> desc) {
        description = desc;
    }

    public int getPassiveTicks() {
        return passiveTicks;
    }

    public void setPassiveTicks(int p) {
        passiveTicks = p;
    }

    public void setToggleTicks(int p) {
        toggleTicks = p;
    }

    public int getToggleTicks() {
        return toggleTicks;
    }

    public void setToggleCooldown(int i) {
        toggleCooldown = i;
    }

    public int getToggleCooldown() {
        return toggleCooldown;
    }

    public List<String> getDescription(Player p) {
        return new ArrayList<>();
    }
    public List<String> getDescription() {
        return new ArrayList<>();
    }

    public int getManaCost() {
        return manaCost;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getWarmup() {
        return warmup;
    }

    public int getLevel() {
        return level;
    }

    public int getToggleMana() {
        return toggleMana;
    }

    public void setToggleMana(int m) {
        toggleMana = m;
    }

    public String getName() {
        return name;
    }

    public void targetParticles(Player p, LivingEntity t) {

    }

    public void cast(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateCast(this);
        /*if (main.getRP(p).getToggles().contains("WorldOnFire")) {
            p.setFireTicks(p.getFireTicks() + 100);
        }*/
    }

    public void target(Player p, LivingEntity t) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateCast(this);
        /*if (main.getRP(p).getToggles().contains("WorldOnFire")) {
            p.setFireTicks(p.getFireTicks() + 100);
        }*/
    }

    public int toggleInit(Player p) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> toggleCont(p), 0, toggleTicks);
    }

    public void toggleEnd(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().endToggle(this);
        Main.getInstance().getCM().cleanToggle(p, this);
    }

    public boolean toggleCont(Player p) {
        if (Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana() <= 0 || main.getRP(p).getStun().getValue() > 0) {
            toggleEnd(p);
            return false;
        }
        Main.getInstance().getPC().get(p.getUniqueId()).setMana(Main.getInstance().getPC().get(p.getUniqueId()).getCMana() - getToggleMana());
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateToggle(this);
        return true;
    }

    public void passive(Player p) {

    }

    public void warmup(Player p) {
        Main.getInstance().getPC().get(p.getUniqueId()).getBoard().updateWarmup(this, getWarmup());
    }

    public static void damageNoKB(Player caster, LivingEntity target, double damage) {
        double kbr = target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(kbr);
    }

    public static void spellDamage(Player caster, LivingEntity target, PhysicalStack damage, ElementalStack edmg, double magicDamage, double trueDamage) {
        if (!Main.getInstance().isValidTarget(target, caster)) {
            return;
        }
        if (Main.getInstance().getRP(caster).getPassives().contains("WorldOnFire")) {
            edmg.setFire(WorldOnFire.getEmp() * edmg.getFire());
        }
        Main.getInstance().getRP(caster).getDamages().add(new Damage(caster, target, damage, magicDamage, trueDamage, edmg, 1));
        damageNoKB(caster, target, damage.getTotal() + edmg.getTotal() + magicDamage + trueDamage);
    }

    public static void healTarget(Player target, double hp) {
        if (target.getHealth() + hp <= target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
            target.setHealth(target.getHealth() + hp);

            List<Player> players = new ArrayList<>(target.getWorld().getNearbyPlayers(target.getEyeLocation(), 24));
            DecimalFormat df = new DecimalFormat("#.##");
            Hologram magic = new Hologram(target, target.getLocation(), "&a❤" + df.format(hp), Hologram.HologramType.DAMAGE, players);
        } else {
            target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }
    }

    public void clearTasks(Player p) {
        if (getTasks().containsKey(p)) {
            for (int i : getTasks().get(p)) {
                Bukkit.getScheduler().cancelTask(i);
            }
            getTasks().get(p).clear();
        }
    }

    public void addTask(Player p, int id) {
        List<Integer> tasks;
        if (getTasks().containsKey(p)) {
            tasks = getTasks().get(p);
        } else {
            tasks = new ArrayList<>();
        }
        tasks.add(id);
        getTasks().put(p, tasks);
    }

}
