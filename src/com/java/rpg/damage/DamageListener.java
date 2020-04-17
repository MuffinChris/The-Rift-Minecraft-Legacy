package com.java.rpg.damage;

import com.java.Main;
import com.java.rpg.holograms.Hologram;
import com.java.rpg.classes.utility.RPGConstants;
import com.java.rpg.damage.utility.*;
import com.java.rpg.classes.*;
import com.java.rpg.mobs.MobEXP;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

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
        if ((main.isPlayer(e.getDamager()) || e.getDamager() instanceof Projectile) && !e.isCancelled() && !(e.getEntity() instanceof ArmorStand)) {
            Player damager;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                    e.setDamage(damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * e.getDamage()/6.0);
                } else {
                    return;
                }
            } else {
                damager = (Player) e.getDamager();
            }
            if (main.isPlayer(damager) && e.getEntity() instanceof LivingEntity) {

                for (Damage d : main.getRP(damager).getDamages()) {
                    if (d.getCaster() == damager && d.getPlayer() == e.getEntity()) {
                        return;
                    }
                }
                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                    main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(0, 0, main.getRP(damager).getAD() * 0.25), 0, 0, new ElementalStack(), 1));
                } else {
                    if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        if (damager.getInventory().getItemInMainHand().getType().toString().contains("SWORD")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(0, 0, e.getDamage()), 0, 0, new ElementalStack(), 1));
                        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("AXE")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage() * 0.5, e.getDamage() * 0.5, 0), 0, 0, new ElementalStack(), 1));
                        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("SHOVEL")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage() * 0.2, e.getDamage() * 0.8, 0), 0, 0, new ElementalStack(), 1));
                        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("TRIDENT")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage(), 0, 0), 0, 0, new ElementalStack(), 1));
                        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("HOE")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage() * 0.25, e.getDamage() * 0.5, e.getDamage() * 0.25), 0, 0, new ElementalStack(), 1));
                        } else if (damager.getInventory().getItemInMainHand().getType().toString().contains("PICKAXE")) {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage() * 0.25, e.getDamage() * 0.5, e.getDamage() * 0.25), 0, 0, new ElementalStack(), 1));
                        } else {
                            main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(0, e.getDamage(), 0), 0, 0, new ElementalStack(), 1));
                        }
                    } else if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(e.getDamage(), 0, 0), 0, 0, new ElementalStack(), 1));
                    } else {
                        main.getRP(damager).getDamages().add(new Damage(damager, (LivingEntity) e.getEntity(), new PhysicalStack(0, e.getDamage(), 0), 0, 0, new ElementalStack(), 1));
                    }
                }

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


    @EventHandler (priority = EventPriority.HIGH)
    public void onDamage (EntityDamageByEntityEvent e) {
        if ((main.isPlayer(e.getDamager()) || e.getDamager() instanceof Projectile) && e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !e.isCancelled()) {
            LivingEntity ent = (LivingEntity) e.getEntity();
            int index = 0;
            boolean found = false;
            Player damager = null;
            boolean keepGoing = true;
            if (e.getDamager() instanceof Projectile) {
                Projectile p = (Projectile) e.getDamager();
                if (p.getShooter() instanceof Player) {
                    damager = (Player) p.getShooter();
                } else {
                    keepGoing = false;
                }

                if ((e.getDamager() instanceof ThrownPotion) || (e.getDamager() instanceof AreaEffectCloud)) {
                    keepGoing = false;
                }
            } else {
                damager = (Player) e.getDamager();
            }

            if (keepGoing) {

                if (e.getEntity() instanceof Player) {
                    if (main.sharePartyPvp(damager, (Player) e.getEntity())) {
                        e.setCancelled(true);
                        return;
                    }
                }

                for (Damage d : main.getRP(damager).getDamages()) {
                    if (d.getPlayer() == e.getEntity()) {
                        if (d.getCaster() == damager) {
                            found = true;
                            boolean party = false;
                            if (d.getPlayer() instanceof Player) {
                                if (main.sharePartyPvp(damager, (Player) d.getPlayer())) {
                                    e.setCancelled(true);
                                    party = true;
                                }
                            }
                            if (!party) {
                                double damage = 0;
                                PhysicalStack physicalDamage = d.getPhysicalDamage();
                                double magicDamage = d.getMagicDamage();
                                double trueDamage = d.getTrueDamage();
                                ElementalStack elementalDamage = d.getElementalDamage();

                                DecimalFormat df = new DecimalFormat("#.##");

                                if (elementalDamage.getAir() > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double def = rp.getAirDefense();
                                        elementalDamage.setAir(elementalDamage.getAir() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + def)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        elementalDamage.setAir(elementalDamage.getAir() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getElementalDefense((LivingEntity) e.getEntity()).getAir())));
                                    }
                                }

                                if (elementalDamage.getEarth() > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double def = rp.getEarthDefense();
                                        elementalDamage.setEarth(elementalDamage.getEarth() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + def)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        elementalDamage.setEarth(elementalDamage.getEarth() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getElementalDefense((LivingEntity) e.getEntity()).getEarth())));
                                    }
                                }

                                if (elementalDamage.getElectric() > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double def = rp.getElectricDefense();
                                        elementalDamage.setElectric(elementalDamage.getElectric() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + def)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        elementalDamage.setElectric(elementalDamage.getElectric() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getElementalDefense((LivingEntity) e.getEntity()).getElectric())));
                                    }
                                }

                                if (physicalDamage.getPuncture() > 0) {
                                    BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                    e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                                    double crit = Math.random();
                                    if (crit < RPGConstants.punctureCritChance) {
                                        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                        e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                        physicalDamage.setPuncture(physicalDamage.getPuncture() * RPGConstants.punctureCritDamage);
                                    }
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double am = rp.getArmor() * RPGConstants.punctureArmorPenPercentage;
                                        physicalDamage.setPuncture(physicalDamage.getPuncture() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        physicalDamage.setPuncture(physicalDamage.getPuncture() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + (MobEXP.getArmor((LivingEntity) e.getEntity()) * RPGConstants.punctureArmorPenPercentage))));
                                    }
                                }
                                if (physicalDamage.getImpact() > 0) {
                                    BlockData blood = Material.BEDROCK.createBlockData();
                                    e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double am = rp.getArmor();
                                        physicalDamage.setImpact(physicalDamage.getImpact() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        physicalDamage.setImpact(physicalDamage.getImpact() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + (MobEXP.getArmor((LivingEntity) e.getEntity())))));
                                    }
                                }
                                if (physicalDamage.getSlash() > 0) {
                                    BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                                    e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                                    double crit = Math.random();
                                    if (crit < RPGConstants.slashCritChance) {
                                        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                        e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                        physicalDamage.setSlash(physicalDamage.getSlash() * RPGConstants.slashCritDamage);
                                    }
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double am = rp.getArmor() * RPGConstants.slashArmorPenPercentage;
                                        physicalDamage.setSlash(physicalDamage.getSlash() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        physicalDamage.setSlash(physicalDamage.getSlash() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + (MobEXP.getArmor((LivingEntity) e.getEntity()) * RPGConstants.slashArmorPenPercentage))));
                                    }
                                }
                                if (magicDamage > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double mr = rp.getMR();
                                        magicDamage = magicDamage * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + mr));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        magicDamage = magicDamage * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getMagicResist((LivingEntity) e.getEntity())));
                                    }
                                    //ent.damage(damage);
                                    //e.setCancelled(true);
                                    //e.setDamage(damage);
                                    //break;
                                }
                                //if (trueDamage > 0) {
                                    //ent.damage(damage);
                                    //e.setCancelled(true);
                                    //e.setDamage(damage);
                                    //break;
                                //}

                                if (elementalDamage.getFire() > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double def = rp.getFireDefense();
                                        elementalDamage.setFire(elementalDamage.getFire() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + def)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        elementalDamage.setFire(elementalDamage.getFire() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getElementalDefense((LivingEntity) e.getEntity()).getFire())));
                                    }
                                }

                                if (elementalDamage.getIce() > 0) {
                                    if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                        Player p = (Player) e.getEntity();
                                        RPGPlayer rp = main.getPC().get(p.getUniqueId());
                                        double def = rp.getIceDefense();
                                        elementalDamage.setIce(elementalDamage.getIce() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + def)));
                                    }
                                    if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                                        elementalDamage.setIce(elementalDamage.getIce() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getElementalDefense((LivingEntity) e.getEntity()).getIce())));
                                    }
                                }


                                //double statusEffect = Math.random();
                                /*if (elementalDamage.anyNonzero() && statusEffect < RPGConstants.baseStatusChance && elementalDamage.getStatus()) {
                                    String element = elementalDamage.pickStatusEffect();
                                    if (element.equals("AIR")) {
                                        ent.setVelocity(new Vector(0, 1.25, 0));
                                        ent.getWorld().spawnParticle(Particle.CLOUD, ent.getLocation().add(new Vector(0, 0.25, 0)), 20, 0.01, 0.04, 0.04, 0.04);
                                        ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);

                                        if (ent instanceof Player) {
                                            RPGPlayer rp = main.getRP((Player) ent);
                                            rp.getStun().getStatuses().add(new StatusValue(damager.getName() + ":Vortex", 0, 20, System.currentTimeMillis(), false));
                                        } else {
                                            ent.setAI(false);
                                            new BukkitRunnable() {
                                                public void run() {
                                                    if (!ent.isDead()) {
                                                        ent.setAI(true);
                                                    }
                                                }
                                            }.runTaskLater(Main.getInstance(), 20L);
                                        }

                                        Hologram magic = new Hologram(ent, ent.getLocation(), "&fVortex &7[" + RPGConstants.air + "&7]", Hologram.HologramType.STATUS);
                                        magic.rise();
                                    }
                                    if (element.equals("EARTH")) {
                                        ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 4));
                                        BlockData dirt = Material.DIRT.createBlockData();
                                        ent.getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, dirt);
                                        ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 1.0F, 1.0F);
                                        if (elementalDamage.getPercent("EARTH") > 0.75) {
                                            elementalDamage.setEarth(elementalDamage.getEarth() * 2);
                                            Hologram magic = new Hologram(ent, ent.getLocation(), "&2Decimate &7[" + RPGConstants.earth + "&7]", Hologram.HologramType.STATUS);
                                            magic.rise();
                                        } else {
                                            Hologram magic = new Hologram(ent, ent.getLocation(), "&2Cripple &7[" + RPGConstants.earth + "&7]", Hologram.HologramType.STATUS);
                                            magic.rise();
                                        }
                                    }
                                    if (element.equals("ELECTRIC")) {
                                        //chain lightning nearby enemies
                                        Hologram magic = new Hologram(ent, ent.getLocation(), "&eChain Lightning &7[" + RPGConstants.electric + "&7]", Hologram.HologramType.STATUS);
                                        magic.rise();
                                        for (LivingEntity en : ent.getLocation().getNearbyLivingEntities(8)) {
                                            if (en instanceof ArmorStand) {
                                                continue;
                                            }
                                            if (en instanceof Player) {
                                                Player p = (Player) en;
                                                if (main.getPM().getParty(p) != null && !main.getPM().getParty(p).getPvp()) {
                                                    if (main.getPM().getParty(p).getPlayers().contains(damager)) {
                                                        continue;
                                                    }
                                                }
                                                if (p.equals(damager)) {
                                                    continue;
                                                }
                                            }
                                            if (!(Math.abs(en.getLocation().getY() - ent.getLocation().getY()) < 3)) {
                                                continue;
                                            }
                                            //Skill.spellDamageStatic(damager, en, 0, new ElementalStack(0, 0, elementalDamage.getElectric() * 0.25, 0, 0, 0));
                                            double dmg = elementalDamage.getElectric() * 0.25;

                                            Skill.spellDamageStatic(damager, en, 0, new ElementalStack(0, 0, dmg, 0, 0,  false));

                                            Hologram chainlight = new Hologram(en, en.getLocation(), RPGConstants.electric + dmg, Hologram.HologramType.DAMAGE);
                                            chainlight.rise();

                                            en.getWorld().playSound(en.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
                                            en.getWorld().spawnParticle(Particle.CRIT_MAGIC, en.getEyeLocation(), 25, 0.05, 0.5, 0.04, 0.04, null, true);
                                            drawLine(ent, en);

                                        }
                                    }
                                    if (element.equals("FIRE")) {
                                        ent.getWorld().spawnParticle(Particle.LAVA, ent.getLocation().add(new Vector(0, 0.25, 0)), 10, 0.01, 0.04, 0.04, 0.04);
                                        ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 1.0F);
                                        if (!(ent instanceof Player)) {
                                            MobEXP.setArmor(ent, MobEXP.getArmor(ent) * 0.75);
                                            MobEXP.setMagicResist(ent, MobEXP.getMagicResist(ent) * 0.75);
                                            ElementalStack newDef = MobEXP.getElementalDefense(ent);
                                            newDef.setFire(newDef.getFire() * 0.5);
                                            MobEXP.setElementalDefense(ent, newDef);
                                        } else {
                                            // create player statusobject
                                        }
                                        Hologram magic = new Hologram(ent, ent.getLocation(), "&cShred &7[" + RPGConstants.fire + "&7]", Hologram.HologramType.STATUS);
                                        magic.rise();
                                    }
                                    if (element.equals("ICE")) {
                                        //freeze
                                        ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                                        ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 10));
                                        ent.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 254));
                                        BlockData ice = Material.ICE.createBlockData();
                                        ent.getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, ice);
                                        ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                                        Hologram magic = new Hologram(ent, ent.getLocation(), "&bFreeze &7[" + RPGConstants.ice + "&7]", Hologram.HologramType.STATUS);
                                        magic.rise();
                                    }
                                }*/

                                damage += physicalDamage.getTotal();
                                damage += magicDamage;
                                damage += trueDamage;
                                damage += elementalDamage.getTotal();

                                ent.setKiller(damager);
                                e.setDamage(damage);

                                if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Player)) {
                                    double dt = MobEXP.getDamageThreshold((LivingEntity) e.getEntity());
                                    if (dt > 0) {
                                        if (e.getDamage() <= dt) {
                                            if (physicalDamage.getTotal() > 0) {
                                                physicalDamage = new PhysicalStack(0, 1, 0);
                                            }
                                            if (magicDamage > 0) {
                                                magicDamage = 1;
                                            }
                                            if (elementalDamage.getTotal() > 0) {
                                                elementalDamage.setAir(Math.min(1, elementalDamage.getAir()));
                                                elementalDamage.setEarth(Math.min(1, elementalDamage.getEarth()));
                                                elementalDamage.setElectric(Math.min(1, elementalDamage.getElectric()));
                                                elementalDamage.setFire(Math.min(1, elementalDamage.getFire()));
                                                elementalDamage.setIce(Math.min(1, elementalDamage.getIce()));
                                            }

                                            e.setDamage(physicalDamage.getTotal() + magicDamage + trueDamage + elementalDamage.getTotal());
                                        }
                                    }
                                }

                                String hologram = "";

                                if (elementalDamage.getAir() > 0)
                                    hologram = hologram + RPGConstants.air + df.format(elementalDamage.getAir()) + " ";
                                if (elementalDamage.getEarth() > 0)
                                    hologram = hologram + RPGConstants.earth + df.format(elementalDamage.getEarth()) + " ";
                                if (elementalDamage.getElectric() > 0)
                                    hologram = hologram + RPGConstants.electric + df.format(elementalDamage.getElectric()) + " ";
                                if (physicalDamage.getPuncture() > 0)
                                    hologram = hologram + RPGConstants.puncture + df.format(physicalDamage.getPuncture()) + " ";
                                if (physicalDamage.getImpact() > 0)
                                    hologram = hologram + RPGConstants.impact + df.format(physicalDamage.getImpact()) + " ";
                                if (physicalDamage.getSlash() > 0)
                                    hologram = hologram + RPGConstants.slash + df.format(physicalDamage.getSlash()) + " ";
                                if (magicDamage > 0)
                                    hologram = hologram + RPGConstants.magic + df.format(magicDamage) + " ";
                                if (trueDamage > 0)
                                    hologram = hologram + RPGConstants.trued + df.format(trueDamage) + " ";
                                if (elementalDamage.getFire() > 0)
                                    hologram = hologram + RPGConstants.fire + df.format(elementalDamage.getFire()) + " ";
                                if (elementalDamage.getIce() > 0)
                                    hologram = hologram + RPGConstants.ice + df.format(elementalDamage.getIce()) + " ";

                                List<Player> players = new ArrayList<>(ent.getWorld().getNearbyPlayers(ent.getEyeLocation(), 24));
                                if (!players.contains(damager)) {
                                    players.add(damager);
                                }
                                Hologram magic = new Hologram(ent, ent.getLocation(), hologram.substring(0, hologram.length() - 1), Hologram.HologramType.DAMAGE, players);
                                break;
                            }
                        }
                    }
                    index++;
                }
                if (found) {
                    if (!main.getRP(damager).getDamages().isEmpty()) {
                        if (main.getRP(damager).getDamages().get(index) != null) {
                            main.getRP(damager).getDamages().get(index).scrub();
                            main.getRP(damager).getDamages().remove(index);
                        }
                    }
                    return;
                }
            }
        }
        if (!(e.getDamager() instanceof Player) && e.getEntity() instanceof Player) {
            if ((!(e.getDamager() instanceof Projectile) || (e.getDamager() instanceof Projectile && !(((Projectile)e.getDamager()).getShooter() instanceof Player))) && !(e.getDamager() instanceof ThrownPotion) && !(e.getDamager() instanceof AreaEffectCloud)) {
                String npHolo = "";
                Player p = (Player) e.getEntity();
                RPGPlayer rp = main.getRP(p);
                double am = rp.getArmor();
                double damage = e.getDamage();
                damage = damage * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + am));
                if ((e.getDamager() instanceof LivingEntity || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof LivingEntity))) {
                    LivingEntity damager;

                    if (e.getDamager() instanceof Projectile) {
                        damager = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
                    } else {
                        damager = (LivingEntity) e.getDamager();
                    }

                    PhysicalStack physicalDamage;
                    if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() != null) {
                        physicalDamage = MobEXP.getPhysicalRangedDamage(damager);
                        if (physicalDamage.getTotal() <= 0) {
                            physicalDamage = new PhysicalStack(e.getDamage(), 0, 0);
                        }
                    } else {
                        if (MobEXP.getPhysicalDamage(damager).getTotal() <= 0) {
                            physicalDamage = new PhysicalStack(0, e.getDamage(), 0);
                        } else {
                            physicalDamage = MobEXP.getPhysicalDamage(damager);
                        }
                    }
                    if (physicalDamage.getTotal() > 0) {

                        DecimalFormat df = new DecimalFormat("#.##");

                        LivingEntity ent = (LivingEntity) e.getEntity();
                        if (physicalDamage.getPuncture() > 0) {
                            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                            double crit = Math.random();
                            if (crit < RPGConstants.punctureCritChance) {
                                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                physicalDamage.setPuncture(physicalDamage.getPuncture() * RPGConstants.punctureCritDamage);
                            }
                            if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                am = rp.getArmor() * RPGConstants.punctureArmorPenPercentage;
                                physicalDamage.setPuncture(physicalDamage.getPuncture() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            }
                            npHolo = npHolo + RPGConstants.puncture + df.format(physicalDamage.getPuncture()) + " ";
                        }
                        if (physicalDamage.getImpact() > 0) {
                            BlockData blood = Material.BEDROCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                            if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                physicalDamage.setImpact(physicalDamage.getImpact() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            }
                            npHolo = npHolo + RPGConstants.impact + df.format(physicalDamage.getImpact()) + " ";
                        }
                        if (physicalDamage.getSlash() > 0) {
                            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                            double crit = Math.random();
                            if (crit < RPGConstants.slashCritChance) {
                                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                physicalDamage.setSlash(physicalDamage.getSlash() * RPGConstants.slashCritDamage);
                            }
                            if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                                am = rp.getArmor() * RPGConstants.slashArmorPenPercentage;
                                physicalDamage.setSlash(physicalDamage.getSlash() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            }
                            npHolo = npHolo + RPGConstants.slash + df.format(physicalDamage.getSlash()) + " ";
                        }
                        damage = physicalDamage.getTotal();
                    }
                }
                e.setDamage(damage);
                List<Player> players = new ArrayList<>(e.getEntity().getWorld().getNearbyPlayers(e.getEntity().getLocation(), 24));
                if (npHolo.isEmpty()) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.physical + df.format(e.getDamage()), Hologram.HologramType.DAMAGE, players);
                } else {
                    Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), npHolo, Hologram.HologramType.DAMAGE, players);
                }
            }
        }
        if (!(e.getDamager() instanceof Player) && !e.isCancelled() && !(e.getEntity() instanceof ArmorStand) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof Player) && !(e.getDamager() instanceof ThrownPotion) && !(e.getDamager() instanceof AreaEffectCloud)) {
            if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player) {

            } else {
                String npHolo = "";
                if (!(e.getEntity() instanceof Player) && (e.getDamager() instanceof LivingEntity || (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof LivingEntity))) {
                    LivingEntity damager;
                    if (e.getDamager() instanceof Projectile) {
                        damager = (LivingEntity) ((Projectile)e.getDamager()).getShooter();
                    } else {
                        damager = (LivingEntity) e.getDamager();
                    }

                    double damage = e.getDamage();
                    double am = MobEXP.getArmor((LivingEntity) e.getEntity());
                    damage = damage * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am));

                    PhysicalStack physicalDamage;
                    if (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() != null) {
                        physicalDamage = MobEXP.getPhysicalRangedDamage(damager);
                        if (physicalDamage.getTotal() <= 0) {
                            physicalDamage = new PhysicalStack(e.getDamage(), 0, 0);
                        }
                    } else {
                        if (MobEXP.getPhysicalDamage(damager).getTotal() <= 0) {
                            physicalDamage = new PhysicalStack(0, e.getDamage(), 0);
                        } else {
                            physicalDamage = MobEXP.getPhysicalDamage((LivingEntity) damager);
                        }
                    }
                    if (physicalDamage.getTotal() > 0) {
                        DecimalFormat df = new DecimalFormat("#.##");

                        LivingEntity ent = (LivingEntity) e.getEntity();
                        if (physicalDamage.getPuncture() > 0) {
                            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                            double crit = Math.random();
                            if (crit < RPGConstants.punctureCritChance) {
                                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                physicalDamage.setPuncture(physicalDamage.getPuncture() * RPGConstants.punctureCritDamage);
                            }
                            am = am * RPGConstants.punctureArmorPenPercentage;
                            physicalDamage.setPuncture(physicalDamage.getPuncture() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            npHolo = npHolo + RPGConstants.puncture + df.format(physicalDamage.getPuncture()) + " ";
                        }
                        if (physicalDamage.getImpact() > 0) {
                            BlockData blood = Material.BEDROCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 25, 0.5, 1, 0.5, blood);
                            physicalDamage.setImpact(physicalDamage.getImpact() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            npHolo = npHolo + RPGConstants.impact + df.format(physicalDamage.getImpact()) + " ";
                        }
                        if (physicalDamage.getSlash() > 0) {
                            BlockData blood = Material.REDSTONE_BLOCK.createBlockData();
                            e.getEntity().getWorld().spawnParticle(Particle.BLOCK_DUST, e.getEntity().getLocation(), 100, 0.5, 1, 0.5, blood);
                            double crit = Math.random();
                            if (crit < RPGConstants.slashCritChance) {
                                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
                                e.getEntity().getWorld().spawnParticle(Particle.CRIT, ent.getEyeLocation(), 50, 0.2, 0.04, 0.04, 0.04);
                                physicalDamage.setSlash(physicalDamage.getSlash() * RPGConstants.slashCritDamage);
                            }
                            am = am * RPGConstants.slashArmorPenPercentage;
                            physicalDamage.setSlash(physicalDamage.getSlash() * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + am)));
                            npHolo = npHolo + RPGConstants.slash + df.format(physicalDamage.getSlash()) + " ";
                        }
                        damage = physicalDamage.getTotal();
                    }
                    e.setDamage(damage);
                }
                List<Player> players = new ArrayList<>(e.getEntity().getWorld().getNearbyPlayers(e.getEntity().getLocation(), 24));
                if (npHolo.isEmpty()) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.physical + df.format(e.getDamage()), Hologram.HologramType.DAMAGE, players);
                } else {
                    Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), npHolo, Hologram.HologramType.DAMAGE, players);
                }
            }
        }
        if (!(e.getEntity() instanceof ArmorStand) && (e.getEntity() instanceof LivingEntity) && e.getDamager() instanceof ThrownPotion) {
            ThrownPotion pot = (ThrownPotion) e.getDamager();
            if (pot.getItem().getType() == Material.LINGERING_POTION) {
                return;
            } else {
                LivingEntity ent = (LivingEntity) e.getEntity();
                double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                double dmg = hp / 20.0 * e.getDamage()/3.0;
                if (e.getEntity() instanceof Player && main.getPC().containsKey(((Player) e.getEntity()).getUniqueId())) {
                    Player p = (Player) e.getEntity();
                    RPGPlayer rp = main.getPC().get(p.getUniqueId());
                    double mr = rp.getMR();
                    dmg = dmg * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + mr));
                }
                if (!(e.getEntity() instanceof Player) && (e.getEntity() instanceof LivingEntity) && !(e.getEntity() instanceof ArmorStand)) {
                    dmg = dmg * (RPGConstants.defenseDiv / (RPGConstants.defenseDiv + MobEXP.getMagicResist((LivingEntity) e.getEntity())));
                }

                e.setDamage(dmg);

                List<Player> players = new ArrayList<>(e.getEntity().getWorld().getNearbyPlayers(e.getEntity().getLocation(), 24));
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.magic + df.format(e.getDamage()), Hologram.HologramType.DAMAGE, players);
                magic.rise();
            }
        }
    }

    public void drawLine(LivingEntity caster, LivingEntity ent) {
        Location point1 = new Location(ent.getWorld(), ent.getEyeLocation().getX(), (ent.getEyeLocation().getY() - 0.7), ent.getEyeLocation().getZ());
        Location point2 = new Location(caster.getWorld(), caster.getEyeLocation().getX(), (caster.getEyeLocation().getY() - 0.7), caster.getEyeLocation().getZ());
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(0.3);
        double length = 0;
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 180, 255), 2);
        for (; length < distance; p1.add(vector)) {
            caster.getWorld().spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 3, 0.01, 0.01, 0.01, 0.01,dust, true);
            caster.getWorld().spawnParticle(Particle.CRIT_MAGIC, p1.getX(), p1.getY(), p1.getZ(), 5, 0.01, 0.01, 0.01, 0.01, null, true);
            length += 0.3;
        }
    }

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
