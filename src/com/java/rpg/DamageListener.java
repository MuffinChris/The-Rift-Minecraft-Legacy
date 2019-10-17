package com.java.rpg;

import com.java.Main;
import com.java.holograms.Hologram;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class DamageListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        /*
        List<Damage> remove = new ArrayList<>();
        for (Damage d : main.getDmg()) {
            if (d.getCaster() == e.getPlayer()) {
                remove.add(d);
            }
        }

        try {
            for (Damage d : remove) {
                main.getDmg().remove(d);
            }
        } catch (ConcurrentModificationException ex) {
            Main.so("&cConcModExec when clearing Damage List of logged of player " + e.getPlayer().getName());
        }*/
    }

    @EventHandler (priority = EventPriority.LOW)
    public void attack (EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) && !e.isCancelled() && !(e.getEntity() instanceof ArmorStand)) {
            Player damager;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                } else {
                    return;
                }
            } else {
                damager = (Player) e.getDamager();
            }
            if (e.getEntity() instanceof LivingEntity) {

                for (Damage d : main.getRP(damager).getDamages()) {
                    if (d.getCaster() == damager && d.getPlayer() == e.getEntity()) {
                        return;
                    }
                }

                main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), Damage.DamageType.ATTACK, e.getDamage()));
                if (main.getPC().get(damager.getUniqueId()) != null) {
                    if (main.getPC().get(damager.getUniqueId()).getStatuses() != null) {
                        List<String> statuses = main.getPC().get(damager.getUniqueId()).getStatuses();
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

                /*
                for (Damage d : main.getDmg()) {
                    if (d.getCaster() == damager && d.getPlayer() == e.getEntity()) {
                        return;
                    }
                }
                main.getDmg().add(new Damage(damager, (LivingEntity) e.getEntity(), Damage.DamageType.ATTACK, e.getDamage()));
                if (main.getPC().get(damager.getUniqueId()) != null) {
                    if (main.getPC().get(damager.getUniqueId()).getStatuses() != null) {
                        List<String> statuses = main.getPC().get(damager.getUniqueId()).getStatuses();
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
                */
            }
        }
    }

    public boolean sharePartyPvp(Player p1, Player p2) {
        if (main.getPM().hasParty(p1) && main.getPM().hasParty(p2)) {
            if (main.getPM().getParty(p1).getPlayers().contains(p2) && !main.getPM().getParty(p1).getPvp()) {
                return true;
            }
        }
        return false;
    }


    @EventHandler (priority = EventPriority.HIGH)
    public void onDamage (EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !e.isCancelled() && !(e.getEntity() instanceof ArmorStand)) {
            if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player) {

            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), "&c&l❤" + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }
        }
        if ((e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) && e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !e.isCancelled()) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            /*if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                Player damager = null;
                if (e.getDamager() instanceof Player) {
                    Player d = (Player) e.getDamager();
                    if (main.getPM().hasParty(p) && main.getPM().hasParty(d)) {
                        if (main.getPM().getParty(p).getPlayers().contains(d) && !main.getPM().getParty(p).getPvp()) {
                            e.setCancelled(true);
                            damager = d;
                        }
                    }
                }
                if (e.getDamager() instanceof Projectile) {
                    if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                        Player d = (Player) ((Projectile) e.getDamager()).getShooter();
                        if (main.getPM().hasParty(p) && main.getPM().hasParty(d)) {
                            if (main.getPM().getParty(p).getPlayers().contains(d) && !main.getPM().getParty(p).getPvp()) {
                                e.setCancelled(true);
                                damager = d;
                            }
                        }
                    }
                }
                if (e.isCancelled()) {
                    int index = 0;
                    boolean found = false;
                    for (Damage d : main.getDmg()) {
                        if (d.getPlayer() == e.getEntity()) {
                            if (d.getCaster() == damager) {
                                found = true;
                            }
                        }
                        index++;
                    }
                    if (found) {
                        main.getDmg().get(index).scrub();
                        main.getDmg().remove(index);
                    }
                    return;
                }
            }*/
            int index = 0;
            boolean found = false;
            Player damager;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                } else {
                    return;
                }
            } else {
                damager = (Player) e.getDamager();
            }

            if (e.getEntity() instanceof Player) {
                if (sharePartyPvp(damager, (Player) e.getEntity())) {
                    e.setCancelled(true);
                    return;
                }
            }

            for (Damage d : main.getRP(damager).getDamages()) {
                if (d.getPlayer() == e.getEntity()) {
                    if (d.getCaster() == damager) {
                        found = true;
                        double pstrength = 1.0;
                        if (d.getCaster().isOnline()) {
                            pstrength+=((main.getPC().get(damager.getUniqueId()).getPStrength2().getValue() * 1.0) / 100.0);
                        }
                        boolean party = false;
                        if (d.getPlayer() instanceof Player) {
                            if (sharePartyPvp(d.getCaster(), (Player) d.getPlayer())) {
                                e.setCancelled(true);
                                party = true;
                            }
                        }
                        if (!party) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            if (d.getDamageType() == Damage.DamageType.SPELL_MAGIC) {
                                double damage = d.getDamage() * pstrength;
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double mr = rp.getPClass().getCalcMR(rp.getLevel());
                                    damage = damage * (300.0 / (300 + mr));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&b&l⚡" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.SPELL_PHYSICAL) {
                                double damage = d.getDamage() * pstrength;
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&b&l⚔" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.SPELL_TRUE) {
                                double damage = d.getDamage() * pstrength;
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&d&l♦" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                    e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                    damage *= 1.25;
                                }
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&c&l❤" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK_MAGIC) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
                                    damage *= 1.25;
                                }
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&9&l⚡" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK_TRUE) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
                                    damage *= 1.25;
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&5&l♦" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                        }
                    }
                }
                index++;
            }
            if (found) {
                /*
                BukkitScheduler sched = Bukkit.getScheduler();
                sched.cancelTask(main.getDmg().get(index).getTask());
                Bukkit.broadcastMessage(sched.isCurrentlyRunning(main.getDmg().get(index).getTask()) + "");
                */
                if (main.getRP(damager).getDamages().get(index) != null) {
                    main.getRP(damager).getDamages().get(index).scrub();
                    main.getRP(damager).getDamages().remove(index);
                }
            }

            /*
            for (Damage d : main.getDmg()) {
                if (d.getPlayer() == e.getEntity()) {
                    if (d.getCaster() == damager) {
                        found = true;
                        double pstrength = 1.0;
                        if (d.getCaster().isOnline()) {
                            pstrength+=((main.getPC().get(damager.getUniqueId()).getPStrength2().getValue() * 1.0) / 100.0);
                        }
                        boolean party = false;
                        if (d.getPlayer() instanceof Player) {
                            if (sharePartyPvp(d.getCaster(), (Player) d.getPlayer())) {
                                e.setCancelled(true);
                                party = true;
                            }
                        }
                        if (!party) {
                            DecimalFormat df = new DecimalFormat("#.##");
                            if (d.getDamageType() == Damage.DamageType.SPELL_MAGIC) {
                                double damage = d.getDamage() * pstrength;
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double mr = rp.getPClass().getCalcMR(rp.getLevel());
                                    damage = damage * (300.0 / (300 + mr));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&b&l⚡" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.SPELL_PHYSICAL) {
                                double damage = d.getDamage() * pstrength;
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&b&l⚔" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.SPELL_TRUE) {
                                double damage = d.getDamage() * pstrength;
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&d&l♦" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                    e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                    damage *= 1.25;
                                }
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&c&l❤" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK_MAGIC) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
                                    damage *= 1.25;
                                }
                                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                    Player p = (Player) e.getEntity();
                                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                    double am = rp.getPClass().getCalcArmor(rp.getLevel());
                                    damage = damage * (300.0 / (300 + am));
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&9&l⚡" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                            if (d.getDamageType() == Damage.DamageType.ATTACK_TRUE) {
                                double damage = d.getDamage();
                                BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                double crit = Math.random();
                                if (crit < 0.25) {
                                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5F, 0.5F);
                                    damage *= 1.25;
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), "&5&l♦" + df.format(damage), Hologram.HologramType.DAMAGE);
                                magic.rise();
                                ent.setKiller(damager);
                                //ent.damage(damage);
                                //e.setCancelled(true);
                                e.setDamage(damage);
                                break;
                            }
                        }
                    }
                }
                index++;
            }
            if (found) {

                //BukkitScheduler sched = Bukkit.getScheduler();
                //sched.cancelTask(main.getDmg().get(index).getTask());
                //Bukkit.broadcastMessage(sched.isCurrentlyRunning(main.getDmg().get(index).getTask()) + "");

                if (main.getDmg().get(index) != null) {
                    main.getDmg().get(index).scrub();
                    main.getDmg().remove(index);
                }
            }*/
        }
        if (!(e.getDamager() instanceof Player) && e.getEntity() instanceof Player) {
            if (!(e.getDamager() instanceof Projectile) || (e.getDamager() instanceof Projectile && !(((Projectile)e.getDamager()).getShooter() instanceof Player))) {
                Player p = (Player) e.getEntity();
                RPGPlayer rp = main.getRP(p);
                double am = rp.getPClass().getCalcArmor(rp.getLevel());
                double damage = e.getDamage();
                damage = damage * (300.0 / (300 + am));
                e.setDamage(damage);
            }
        }
    }

}
