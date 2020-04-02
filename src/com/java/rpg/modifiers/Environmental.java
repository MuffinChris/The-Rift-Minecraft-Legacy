package com.java.rpg.modifiers;

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent;
import com.destroystokyo.paper.event.entity.EnderDragonFlameEvent;
import com.java.Main;
import com.java.holograms.Hologram;
import com.java.rpg.classes.MobEXP;
import com.java.rpg.classes.RPGConstants;
import net.minecraft.server.v1_15_R1.EntityIronGolem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftIronGolem;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.text.DecimalFormat;

public class Environmental implements Listener {

    private Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void potions(PlayerItemConsumeEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().getType() == Material.POTION) {
                if (e.getItem().getItemMeta() instanceof PotionMeta) {
                    PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();
                    if (meta.getBasePotionData().getType() == PotionType.INSTANT_DAMAGE) {
                        double hp = e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                        if (meta.getBasePotionData().isUpgraded()) {
                            double dmg = 100 + hp * 0.15;
                            double d = main.getRP(e.getPlayer()).getMR();
                            dmg*=(RPGConstants.defenseDiv)/(RPGConstants.defenseDiv + d);
                            e.getPlayer().damage(dmg);
                            DecimalFormat df = new DecimalFormat("#.##");
                            Hologram magic = new Hologram(e.getPlayer(), e.getPlayer().getLocation(), RPGConstants.magic + df.format(dmg), Hologram.HologramType.DAMAGE);
                        } else {
                            double dmg = 50 + hp * 0.1;
                            double d = main.getRP(e.getPlayer()).getMR();
                            dmg*=(RPGConstants.defenseDiv)/(RPGConstants.defenseDiv + d);
                            e.getPlayer().damage(dmg);
                            DecimalFormat df = new DecimalFormat("#.##");
                            Hologram magic = new Hologram(e.getPlayer(), e.getPlayer().getLocation(), RPGConstants.magic + df.format(dmg), Hologram.HologramType.DAMAGE);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void addDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !e.isCancelled()) {
            LivingEntity p = (LivingEntity) e.getEntity();
            double hp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            boolean fire = false;
            boolean poison = false;
            boolean wither = false;
            boolean lightning = false;
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setDamage(e.getDamage() * (hp / 30.0));
            }
            /*if (e.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
                e.setDamage(e.getDamage() / 4.0 * hp / 20.0);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    double fd = main.getRP(pl).getMR();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                } else {
                    double fd = MobEXP.getMagicResist(p);
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                }
            }*/
            if (e.getCause() == EntityDamageEvent.DamageCause.POISON) {

                e.setDamage(hp * 0.025);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    double fd = main.getRP(pl).getEarthDefense();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                } else {
                    double fd = MobEXP.getElementalDefense(p).getEarth();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                }

                if (p.getHealth() > e.getDamage()) {
                    poison = true;
                } else {
                    e.setCancelled(true);
                }
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                e.setDamage(hp * 0.125);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.STARVATION) {
                e.setDamage(hp * 0.1);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                e.setDamage(hp * 0.1);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.WITHER) {
                wither = true;
                e.setDamage(hp / 40.0);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                e.setDamage(hp * 0.025);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    double fd = main.getRP(pl).getFireDefense();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                } else {
                    double fd = MobEXP.getElementalDefense(p).getFire();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                }
                fire = true;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                e.setDamage(hp * 0.025);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    double fd = main.getRP(pl).getFireDefense();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                } else {
                    double fd = MobEXP.getElementalDefense(p).getFire();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                }
                fire = true;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                e.setDamage(hp * 0.05);
                if (p instanceof Player) {
                    Player pl = (Player) p;
                    double fd = main.getRP(pl).getFireDefense();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                } else {
                    double fd = MobEXP.getElementalDefense(p).getFire();
                    e.setDamage(e.getDamage() * (RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                }
                fire = true;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                e.setDamage((e.getDamage() / 20.0) * hp * 0.75);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                e.setDamage((e.getDamage() / 20.0) * hp * 0.5);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                e.setDamage((e.getDamage() / 20) * hp);
                lightning = true;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                e.setDamage(e.getDamage() * (hp / 40.0));
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.MELTING) {
                e.setDamage(e.getDamage() * (hp / 20.0));
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                e.setDamage(e.getDamage() * (hp / 20.0));
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                e.setDamage(hp / 40.0);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.DRYOUT) {
                e.setDamage(hp / 10.0);
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (e.getDamage() < 1000000) {
                    e.setDamage(hp * 0.1);
                } else {
                    e.setDamage(1000000000);
                }
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
                e.setDamage((e.getDamage() / 30.0) * hp);
            }

            if (!(e.getEntity() instanceof ArmorStand) && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.CUSTOM && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE && e.getCause() != EntityDamageEvent.DamageCause.MAGIC && !fire && !poison && !wither) {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.physical + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }
            if (fire) {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.fire + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }

            if (wither) {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), "&8❤" + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }

            if (poison) {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.earth + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }
            if (lightning) {
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.electric + df.format(e.getDamage()), Hologram.HologramType.DAMAGE);
                magic.rise();
            }
        }
    }

    @EventHandler
    public void ePearlDamage (PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            e.getPlayer().setNoDamageTicks(1);
        }
    }

    @EventHandler
    public void dragonBreath (EnderDragonFlameEvent e) {
        e.getAreaEffectCloud().setCustomName("EdragFlame");
    }

    @EventHandler
    public void dragonFireball (EnderDragonFireballHitEvent e) {
        e.getAreaEffectCloud().setCustomName("EdragFlame");
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void lingeringPotion(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof AreaEffectCloud) {
            AreaEffectCloud cloud = (AreaEffectCloud) e.getDamager();
            if (cloud.getBasePotionData().getType() == PotionType.INSTANT_DAMAGE || (cloud.getCustomName() != null && cloud.getCustomName().equals("EdragFlame"))) {
                if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand)) {
                    if ((cloud.getCustomName() != null && cloud.getCustomName().equals("EdragFlame"))) {
                        double dmg = (e.getDamage() / 3.0) * (((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.15);

                        DecimalFormat df = new DecimalFormat("#.##");
                        Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.trued + df.format(dmg), Hologram.HologramType.DAMAGE);
                        e.setDamage(dmg);
                    } else {
                        double dmg = (e.getDamage() / 3.0) * (((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 0.04);

                        if (e.getEntity() instanceof Player) {
                            Player pl = (Player) e.getEntity();
                            double fd = main.getRP(pl).getMR();
                            dmg*=((RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                        } else {
                            double fd = MobEXP.getMagicResist((LivingEntity) e.getEntity());
                            dmg*=((RPGConstants.defenseDiv/(RPGConstants.defenseDiv + fd)));
                        }

                        DecimalFormat df = new DecimalFormat("#.##");
                        Hologram magic = new Hologram(e.getEntity(), e.getEntity().getLocation(), RPGConstants.magic + df.format(dmg), Hologram.HologramType.DAMAGE);
                        e.setDamage(dmg);
                    }
                }
            }
        }
    }

    @EventHandler
    public void bonusRegen (EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof LivingEntity && !e.isCancelled()) {
            if (e.getEntity().isDead()) {
                e.setCancelled(true);
            } else {
                LivingEntity ent = (LivingEntity) e.getEntity();
                double hp = ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                    e.setAmount(hp * 0.025);
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
                    e.setAmount((e.getAmount() * (hp/40)));
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC) {
                    e.setAmount((e.getAmount() / 30) * hp);
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
                    e.setAmount(hp * 0.015);
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL) {
                    e.setAmount(hp * 0.005);
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.WITHER) {
                    e.setAmount(hp * 0.005);
                } else if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.WITHER_SPAWN) {
                    e.setAmount(25);
                }
                DecimalFormat df = new DecimalFormat("#.##");
                Hologram magic = new Hologram(ent, ent.getLocation(), "&a❤" + df.format(e.getAmount()), Hologram.HologramType.DAMAGE);
                magic.rise();
                if (e.getEntity() instanceof Player) {
                    Main.sendHp((Player) e.getEntity());
                }
                DecimalFormat dF = new DecimalFormat("#.##");
                if (main.getHpBars().containsKey(ent)) {
                    main.getHpBars().get(ent).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                }
            }
        }
    }

}
