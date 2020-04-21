package com.java.rpg.classes.skills.Pyromancer;

import com.java.Main;
import com.java.rpg.classes.skills.Pyromancer.supers.Flamethrower;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.classes.Skill;
import com.java.rpg.damage.utility.PhysicalStack;
import com.java.rpg.entity.Mobs;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

import static com.java.rpg.entity.EntityUtils.*;

public class Fireball extends Skill implements Listener {


    private Main main = Main.getInstance();

    private double magicDamage = 120;
    private double apscale = 0.4;

    private ElementalStack elementalDamage = new ElementalStack(0, 0, 0, 150, 0);

    private int range = 4; // in seconds of travel

    public Fireball() {
        super("Fireball", 50, 3 * 20, 0, 0, SkillType.CAST, new Flamethrower(), Material.FIRE_CHARGE);
    }

    public List<String> getDescription(Player p) {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Shoot a flaming projectile that travels for &e" + range + "&7 seconds."));
        desc.add(Main.color("&7It deals &b" + getMagicDmg(p) + " &7+ " + getEDmg(p) + " &7damage to nearby foes"));
        desc.add(Main.color("&7and ignites them for 5 seconds."));
        return desc;
    }

    public List<String> getDescription() {
        List<String> desc = new ArrayList<>();
        desc.add(Main.color("&bActive:"));
        desc.add(Main.color("&7Shoot a flaming projectile that travels for &e" + range + "&7 seconds."));
        desc.add(Main.color("&7It deals &b" + magicDamage + " &7+ " + elementalDamage.getFancyNumberFire() + " &7damage to nearby foes"));
        desc.add(Main.color("&7and ignites them for 5 seconds."));
        return desc;
    }

    public double getMagicDmg(Player p) {
        return magicDamage + main.getRP(p).getAP() * apscale;
    }
    public ElementalStack getEDmg(Player p) {
        return elementalDamage;
    }

    public void cast(Player p) {
        super.cast(p);
        Location loc = new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() - 0.1, p.getEyeLocation().getZ());
        final Arrow arrow = p.getWorld().spawn(loc, Arrow.class);
        setElementalDamage(arrow, getEDmg(p));
        setMagicDamage(arrow, getMagicDmg(p));
        setCustomName(arrow, "Fireball");
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(arrow.getEntityId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        arrow.setShooter(p);
        arrow.setVelocity(p.getEyeLocation().getDirection().multiply(2));
        arrow.setBounce(false);
        arrow.setGravity(false);
        arrow.setKnockbackStrength(0);
        arrow.setSilent(true);
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final int task = scheduler.scheduleSyncRepeatingTask(main, () -> {
            if (!arrow.isDead()) {
                p.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 15, 0.04, 0.04, 0.04, 0.04, null, true);
                if (arrow.isOnGround() || arrow.isDead()) {
                    lightEntities(arrow, p, arrow.getLocation(), getMagicDamage(arrow), getElementalDamage(arrow));
                    arrow.remove();
                    arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 50, 0.04, 0.04, 0.04, 0.04, null, true);
                }
            }
        }, 1, 1);

        scheduler.scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                if (!(arrow.isOnGround() || arrow.isDead())) {
                    lightEntities(arrow, p, arrow.getLocation(), getMagicDamage(arrow), getElementalDamage(arrow));
                    arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 20, 0.04, 0.04, 0.04, 0.04, null, true);
                }
                scheduler.cancelTask(task);
                arrow.remove();
            }
        }, 20 * range);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
    }

    public static void lightEntities(Entity e, Player caster, Location loc, double damage, ElementalStack elementalStack) {
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 25, 0.12, 0.12, 0.12, 0.12, null, true);
        for (LivingEntity ent : Main.getInstance().getNearbyLivingEntitiesTargetValid(loc, caster, 1.1)) {
            ent.setFireTicks(Math.min(100 + ent.getFireTicks(), 200));
            spellDamage(caster, ent, new PhysicalStack(), elementalStack, damage, 0);
            ent.getLocation().getWorld().playSound(ent.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0F, 1.0F);
        }
    }
}
