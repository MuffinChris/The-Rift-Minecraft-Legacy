package com.java.rpg.mobs.grassy;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.java.Main;
import com.java.rpg.classes.ElementalStack;
import com.java.rpg.classes.LevelRange;
import com.java.rpg.classes.MobEXP;
import com.java.rpg.classes.RPGConstants;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import net.minecraft.server.v1_15_R1.*;
import com.java.rpg.player.Items;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarriorZombie extends EntityZombie {

    public WarriorZombie(EntityTypes<? extends EntityZombie> warriorZombieEntityTypes, World world) {
        super(EntityTypes.ZOMBIE, world);

        setBaby(false);

        createEquipment();
        setupStats();
    }

    public void createEquipment() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(100, 0, 0));
        bootsMeta.setUnbreakable(true);
        boots.setItemMeta(bootsMeta);
        boots = Items.removeArmor(boots);
        setSlot(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(boots));

        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(Color.fromRGB(100, 0, 0));
        legsMeta.setUnbreakable(true);
        legs.setItemMeta(legsMeta);
        legs = Items.removeArmor(legs);
        setSlot(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(legs));

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(Color.fromRGB(100, 0, 0));
        chestMeta.setUnbreakable(true);
        chest.setItemMeta(chestMeta);
        chest = Items.removeArmor(chest);
        setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(chest));

        /*PlayerProfile skull = Bukkit.createProfile("WarriorZombie");
        skull.setId(UUID.fromString("f553cb6e-7a83-46c3-8a32-b2e399804837"));
        ProfileProperty texture = new ProfileProperty("textures", "\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTZiOGQ4NzQ1ZjZmYzdhMGE3NzM1NGNlMWE5ZjMwNDY4MTdmNjZkMmQzYWZkMWJjZGFjNmQyZDEwZjM3OSJ9fX0=");
        skull.setProperty(texture);

        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) skullItem.getItemMeta();
        sm.setPlayerProfile(skull);
        skullItem.setItemMeta(sm);

        setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(skullItem));*/

        ItemStack helm = new ItemStack(Material.IRON_HELMET);
        ItemMeta helmMeta = helm.getItemMeta();
        helmMeta.setUnbreakable(true);
        helm.setItemMeta(helmMeta);
        helm = Items.removeArmor(helm);
        setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(helm));

        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.setUnbreakable(true);
        axe.setItemMeta(axeMeta);
        axe = Items.removeDamage(axe);

        setSlot(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(axe));

        MobEXP.removeDropChances(getBukkitEntity());
    }

    public void setupStats() {
        LivingEntity ent = getBukkitLivingEntity();

        MobEXP.setCustomName(ent, "&7[" + RPGConstants.earth + RPGConstants.fire + RPGConstants.strong + " &7| " + RPGConstants.water + RPGConstants.weak + "&7] &cZombie Warrior");
        MobEXP.setLevel(ent, new LevelRange(5, 10).getRandomLevel());
        int level = MobEXP.getLevel(ent);
        MobEXP.setExp(ent, RPGConstants.mobExp.get(level) * 1.2);
        MobEXP.setArmor(ent, 125 + level * 5);
        MobEXP.setMagicResist(ent, 50);
        MobEXP.setHPRegen(ent, level * 5 + 50);
        MobEXP.setElementalDefense(ent, new ElementalStack(0, 75, 0, 100, 25, -50));

        ent.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        ent.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(150 + level * 10);
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1250 + level * 50);
        ent.setHealth(ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        ent.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        ent.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);

        ent.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.0);

        NBTEntity nent = new NBTEntity(ent);
        nent.setByte("CanPickUpLoot", (byte) 0);

        MobEXP.setSetup(ent, 1);
    }

    public WarriorZombie(World world) {
        super(world.getWorld().getHandle());


    }



}
