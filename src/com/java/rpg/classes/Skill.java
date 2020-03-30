package com.java.rpg.classes;

import com.java.Main;
import com.java.holograms.Hologram;
import com.java.rpg.Damage;
import com.java.rpg.classes.skills.Pyromancer.WorldOnFire;
import com.java.rpg.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Skill {

    private Main main = Main.getInstance();

    public void spellDamage(Player caster, LivingEntity target, double damage) {
        if (target.isDead() || target.isInvulnerable()) {
            return;
        }
        if (target instanceof Player) {
            Player t = (Player) target;
            if (t.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (main.getPM().getParty(t) instanceof Party && !main.getPM().getParty(t).getPvp()) {
                if (main.getPM().getParty(t).getPlayers().contains(caster)) {
                    return;
                }
            }
        }
        if (Main.getInstance().getRP(caster).getPassives().contains("WorldOnFire") && target.getFireTicks() > 0) {
            damage*= WorldOnFire.getEmp();
        }
        main.getRP(caster).getDamages().add(new Damage(caster, target, Damage.DamageType.MAGIC, damage, 5));
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
    }
    public static void spellDamageStatic(Player caster, LivingEntity target, double damage) {
        if (target.isDead() || target.isInvulnerable()) {
            return;
        }
        if (target instanceof Player) {
            Player t = (Player) target;
            if (t.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (Main.getInstance().getPM().getParty(t) instanceof Party && !Main.getInstance().getPM().getParty(t).getPvp()) {
                if (Main.getInstance().getPM().getParty(t).getPlayers().contains(caster)) {
                    return;
                }
            }
        }
        if (Main.getInstance().getRP(caster).getPassives().contains("WorldOnFire") && target.getFireTicks() > 0) {
            damage*= WorldOnFire.getEmp();
        }
        Main.getInstance().getRP(caster).getDamages().add(new Damage(caster, target, Damage.DamageType.MAGIC, damage, 5));
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);

    }

    public void trueDamageSpell(Player caster, LivingEntity target, double damage) {
        if (target.isDead() || target.isInvulnerable()) {
            return;
        }
        if (target instanceof Player) {
            Player t = (Player) target;
            if (t.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (main.getPM().getParty(t) instanceof Party && !main.getPM().getParty(t).getPvp()) {
                if (main.getPM().getParty(t).getPlayers().contains(caster)) {
                    return;
                }
            }
        }
        if (Main.getInstance().getRP(caster).getPassives().contains("WorldOnFire") && target.getFireTicks() > 0) {
            damage*= WorldOnFire.getEmp();
        }
        main.getRP(caster).getDamages().add(new Damage(caster, target, Damage.DamageType.TRUE, damage, 5));
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
    }

    public static void trueDamageSpellStatic(Player caster, LivingEntity target, double damage) {
        if (target.isDead() || target.isInvulnerable()) {
            return;
        }
        if (target instanceof Player) {
            Player t = (Player) target;
            if (t.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (Main.getInstance().getPM().getParty(t) instanceof Party && !Main.getInstance().getPM().getParty(t).getPvp()) {
                if (Main.getInstance().getPM().getParty(t).getPlayers().contains(caster)) {
                    return;
                }
            }
        }
        if (Main.getInstance().getRP(caster).getPassives().contains("WorldOnFire") && target.getFireTicks() > 0) {
            damage*= WorldOnFire.getEmp();
        }
        Main.getInstance().getRP(caster).getDamages().add(new Damage(caster, target, Damage.DamageType.TRUE, damage, 5));
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        target.setNoDamageTicks(0);
        target.damage(damage, caster);
        target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
    }

    public static void healTarget(Player target, double hp) {
        if (target.getHealth() + hp <= target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
            target.setHealth(target.getHealth() + hp);
            DecimalFormat df = new DecimalFormat("#.##");
            Hologram magic = new Hologram(target, target.getLocation(), "&a&l❤" + df.format(hp), Hologram.HologramType.DAMAGE);
            magic.rise();
        } else {
            target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        }
    }

    private int manaCost;
    private double cooldown;
    private int warmup;
    private int level;

    private int passiveTicks;
    private int toggleTicks;
    private int toggleMana;

    private String name;
    private String flavor;

    private String type;

    private List<String> description;

    private int targetRange;

    public int getTargetRange() {
        return targetRange;
    }

    public void setTargetRange(int t) {
        targetRange = t;
    }

    private List<Integer> tasks;

    public Skill(String name, int manaCost, double cooldown, int warmup, int level, String flavor, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.warmup = warmup;
        this.level = level;
        this.flavor = flavor;
        this.type = type;
        tasks = new ArrayList<>();
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public String getType() {
        return type;
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

    public List<String> getDescription(Player p) {
        return description;
    }

    public String getFlavor() {
        return flavor;
    }

    public int getManaCost() {
        return manaCost;
    }

    public double getCooldown() {
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
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                toggleCont(p);
            }
        }, 1, toggleTicks);
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

}
