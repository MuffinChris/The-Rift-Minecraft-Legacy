package com.java.rpg.entity;

import com.java.Main;
import com.java.rpg.damage.utility.ElementalStack;
import com.java.rpg.damage.utility.PhysicalStack;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityUtils {

    public static void removeDropChances(Entity ent) {
        NBTEntity nent = new NBTEntity(ent);
        if (nent.hasKey("ArmorDropChances")) {
            NBTList list = nent.getFloatList("ArmorDropChances");
            for (int i = 0; i < list.size(); i++) {
                list.set(i, 0.0f);
            }
        }
        if (nent.hasKey("HandDropChances")) {
            NBTList list = nent.getFloatList("HandDropChances");
            for (int i = 0; i < list.size(); i++) {
                list.set(i, 0.0f);
            }
        }
    }

    public static void setExp(LivingEntity ent, double exp) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE, exp);
    }

    public static double getExp(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Exp"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static int getLevel(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER);
        }
        return -1;
    }

    public static boolean hasLevel(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        return (data.has(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER));
    }

    public static void setLevel(LivingEntity ent, int level) {

        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Level"), PersistentDataType.INTEGER, level);
        setLevelVisual(ent, level);
    }

    public static void setLevelVisual(LivingEntity ent, int level) {
        ent.setCustomName(Main.color("&f" + getNiceName(ent) + " &6Lv. " + level));
    }

    public static String getNiceName(LivingEntity ent) {
        if (getCustomName(ent) != null) {
            return getCustomName(ent);
        }
        net.minecraft.server.v1_15_R1.Entity nmsEnt = ((CraftEntity) ent).getHandle();
        return WordUtils.capitalize((nmsEnt.getMinecraftKeyString().replace("minecraft:", "")).replace("_", " "));
    }

    public static void setPhysicalRangedDamage(LivingEntity ent, PhysicalStack pd) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "PhysicalRangedDamage"), PersistentDataType.STRING, pd.getCommaDelim());
    }

    public static PhysicalStack getPhysicalRangedDamage (LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "PhysicalRangedDamage"), PersistentDataType.STRING)) {
            String delim = data.get(new NamespacedKey(Main.getInstance(), "PhysicalRangedDamage"), PersistentDataType.STRING);
            String[] ar = delim.split(",");
            double[] ard = new double[3];
            int index = 0;
            for (String s : ar) {
                ard[index] = Double.valueOf(s);
                index++;
            }
            return new PhysicalStack(ard[0], ard[1], ard[2]);
        }
        return new PhysicalStack(0, 0, 0);
    }

    public static void setPhysicalDamage(LivingEntity ent, PhysicalStack pd) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "PhysicalDamage"), PersistentDataType.STRING, pd.getCommaDelim());
    }

    public static PhysicalStack getPhysicalDamage (LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "PhysicalDamage"), PersistentDataType.STRING)) {
            String delim = data.get(new NamespacedKey(Main.getInstance(), "PhysicalDamage"), PersistentDataType.STRING);
            String[] ar = delim.split(",");
            double[] ard = new double[3];
            int index = 0;
            for (String s : ar) {
                ard[index] = Double.valueOf(s);
                index++;
            }
            return new PhysicalStack(ard[0], ard[1], ard[2]);
        }
        return new PhysicalStack();
    }

    public static void setElementalDamage (Entity ent, ElementalStack eDef) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "ElementalDamage"), PersistentDataType.STRING, eDef.getCommaDelim());
    }

    public static ElementalStack getElementalDamage (Entity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "ElementalDamage"), PersistentDataType.STRING)) {
            String delim = data.get(new NamespacedKey(Main.getInstance(), "ElementalDamage"), PersistentDataType.STRING);
            String[] ar = delim.split(",");
            double[] ard = new double[5];
            int index = 0;
            for (String s : ar) {
                ard[index] = Double.parseDouble(s);
                index++;
            }
            return new ElementalStack(ard[0], ard[1], ard[2], ard[3], ard[4]);
        }
        return new ElementalStack(0, 0, 0, 0, 0);
    }

    public static void setElementalDamagePerEnt (Entity ent, ElementalStack eDef) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "ElementalDamagePerEnt"), PersistentDataType.STRING, eDef.getCommaDelim());
    }

    public static ElementalStack getElementalDamagePerEnt (Entity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "ElementalDamagePerEnt"), PersistentDataType.STRING)) {
            String delim = data.get(new NamespacedKey(Main.getInstance(), "ElementalDamagePerEnt"), PersistentDataType.STRING);
            String[] ar = delim.split(",");
            double[] ard = new double[5];
            int index = 0;
            for (String s : ar) {
                ard[index] = Double.parseDouble(s);
                index++;
            }
            return new ElementalStack(ard[0], ard[1], ard[2], ard[3], ard[4]);
        }
        return new ElementalStack(0, 0, 0, 0, 0);
    }

    public static double getMagicDamage(Entity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "MagicDamage"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "MagicDamage"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static void setMagicDamage(Entity ent, double magicdmg) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "MagicDamage"), PersistentDataType.DOUBLE, magicdmg);
    }

    public static double getMagicDamagePerEnt(Entity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "MagicDamagePerEnt"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "MagicDamagePerEnt"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static void setMagicDamagePerEnt(Entity ent, double magicdmg) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "MagicDamagePerEnt"), PersistentDataType.DOUBLE, magicdmg);
    }

    public static void setElementalDefense (LivingEntity ent, ElementalStack eDef) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "ElementalDefense"), PersistentDataType.STRING, eDef.getCommaDelim());
    }

    public static ElementalStack getElementalDefense (LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "ElementalDefense"), PersistentDataType.STRING)) {
            String delim = data.get(new NamespacedKey(Main.getInstance(), "ElementalDefense"), PersistentDataType.STRING);
            String[] ar = delim.split(",");
            double[] ard = new double[5];
            int index = 0;
            for (String s : ar) {
                ard[index] = Double.valueOf(s);
                index++;
            }
            return new ElementalStack(ard[0], ard[1], ard[2], ard[3], ard[4]);
        }
        return new ElementalStack(0, 0, 0, 0, 0);
    }

    public static double getArmor(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static void setArmor(LivingEntity ent, double armor) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Armor"), PersistentDataType.DOUBLE, armor);
    }

    public static void setMagicResist(LivingEntity ent, double mr) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE, mr);
    }

    public static double getMagicResist(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "MagicResist"), PersistentDataType.DOUBLE);
        }
        return 0;
    }

    public static double getDamageThreshold(LivingEntity ent){
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE);
        }
        return 0;
    }
    public static void setDamageThreshold(LivingEntity ent, double dt) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "DamageThreshold"), PersistentDataType.DOUBLE, dt);
    }

    public static double getHPRegen(LivingEntity ent){
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE)) {
            return data.get(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE);
        }
        return 0;
    }
    public static void setHPRegen(LivingEntity ent, double r) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "HPRegen"), PersistentDataType.DOUBLE, r);
    }

    public static String getCustomName(Entity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "CustomName"), PersistentDataType.STRING)) {
            return data.get(new NamespacedKey(Main.getInstance(), "CustomName"), PersistentDataType.STRING);
        }
        return null;
    }

    public static void setCustomName(Entity ent, String s) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "CustomName"), PersistentDataType.STRING, s);
    }

    public static int getSetup(LivingEntity ent) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Main.getInstance(), "Setup"), PersistentDataType.INTEGER)) {
            return data.get(new NamespacedKey(Main.getInstance(), "Setup"), PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static void setSetup(LivingEntity ent, int i) {
        PersistentDataContainer data = ent.getPersistentDataContainer();
        data.set(new NamespacedKey(Main.getInstance(), "Setup"), PersistentDataType.INTEGER, i);
    }


}
