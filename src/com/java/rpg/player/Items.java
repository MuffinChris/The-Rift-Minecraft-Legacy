package com.java.rpg.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.java.Main;
import com.java.rpg.classes.LevelRange;
import com.java.rpg.classes.StatusValue;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.util.*;

public class Items implements Listener {

    private Main main = Main.getInstance();

    private static Map<Material, Integer> weight;
    private static Map<Material, LevelRange> armor;
    private static Map<Material, Integer> durability;

    private Map<UUID, Long> recievedWeight;

    public Map<UUID, Long> recievedWeight() {
        return recievedWeight;
    }

    @EventHandler
    public void onLeave (PlayerQuitEvent e) {
        if (recievedWeight.containsKey(e.getPlayer().getUniqueId())) {
            recievedWeight.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent e) {
        if (!recievedWeight.containsKey(e.getPlayer().getUniqueId())) {
            recievedWeight.put(e.getPlayer().getUniqueId(), 0L);
        }
    }

    public Items() {
        recievedWeight = new HashMap<>();

        weight = new HashMap<>();
        weight.put(org.bukkit.Material.ELYTRA, 5);
        weight.put(org.bukkit.Material.TURTLE_HELMET, 5);

        weight.put(org.bukkit.Material.DIAMOND_HELMET, 30);
        weight.put(org.bukkit.Material.DIAMOND_CHESTPLATE, 50);
        weight.put(org.bukkit.Material.DIAMOND_LEGGINGS, 45);
        weight.put(org.bukkit.Material.DIAMOND_BOOTS, 25);

        weight.put(org.bukkit.Material.IRON_HELMET, 25);
        weight.put(org.bukkit.Material.IRON_CHESTPLATE, 50);
        weight.put(org.bukkit.Material.IRON_LEGGINGS, 40);
        weight.put(org.bukkit.Material.IRON_BOOTS, 20);

        weight.put(org.bukkit.Material.CHAINMAIL_HELMET, 20);
        weight.put(org.bukkit.Material.CHAINMAIL_CHESTPLATE, 40);
        weight.put(org.bukkit.Material.CHAINMAIL_LEGGINGS, 35);
        weight.put(org.bukkit.Material.CHAINMAIL_BOOTS, 15);

        weight.put(org.bukkit.Material.GOLDEN_HELMET, 15);
        weight.put(org.bukkit.Material.GOLDEN_CHESTPLATE, 35);
        weight.put(org.bukkit.Material.GOLDEN_LEGGINGS, 30);
        weight.put(org.bukkit.Material.GOLDEN_BOOTS, 10);

        weight.put(org.bukkit.Material.LEATHER_HELMET, 10);
        weight.put(org.bukkit.Material.LEATHER_CHESTPLATE, 25);
        weight.put(org.bukkit.Material.LEATHER_LEGGINGS, 20);
        weight.put(org.bukkit.Material.LEATHER_BOOTS, 5);

        armor = new HashMap<>();
        armor.put(org.bukkit.Material.ELYTRA, new LevelRange(20, 25));
        armor.put(org.bukkit.Material.TURTLE_HELMET, new LevelRange(20, 25));

        armor.put(org.bukkit.Material.DIAMOND_HELMET, new LevelRange(30, 35));
        armor.put(org.bukkit.Material.DIAMOND_CHESTPLATE, new LevelRange(40, 45));
        armor.put(org.bukkit.Material.DIAMOND_LEGGINGS, new LevelRange(35, 40));
        armor.put(org.bukkit.Material.DIAMOND_BOOTS, new LevelRange(25, 30));

        armor.put(org.bukkit.Material.IRON_HELMET, new LevelRange(25, 30));
        armor.put(org.bukkit.Material.IRON_CHESTPLATE, new LevelRange(35, 40));
        armor.put(org.bukkit.Material.IRON_LEGGINGS, new LevelRange(30, 35));
        armor.put(org.bukkit.Material.IRON_BOOTS, new LevelRange(20, 25));

        armor.put(org.bukkit.Material.CHAINMAIL_HELMET, new LevelRange(20, 25));
        armor.put(org.bukkit.Material.CHAINMAIL_CHESTPLATE, new LevelRange(30, 35));
        armor.put(org.bukkit.Material.CHAINMAIL_LEGGINGS, new LevelRange(25, 35));
        armor.put(org.bukkit.Material.CHAINMAIL_BOOTS, new LevelRange(15, 20));

        armor.put(org.bukkit.Material.GOLDEN_HELMET, new LevelRange(15, 20));
        armor.put(org.bukkit.Material.GOLDEN_CHESTPLATE, new LevelRange(25, 30));
        armor.put(org.bukkit.Material.GOLDEN_LEGGINGS, new LevelRange(20, 25));
        armor.put(org.bukkit.Material.GOLDEN_BOOTS, new LevelRange(10, 15));

        armor.put(org.bukkit.Material.LEATHER_HELMET, new LevelRange(10, 15));
        armor.put(org.bukkit.Material.LEATHER_CHESTPLATE, new LevelRange(20, 25));
        armor.put(org.bukkit.Material.LEATHER_LEGGINGS, new LevelRange(15, 20));
        armor.put(org.bukkit.Material.LEATHER_BOOTS, new LevelRange(5, 10));

        durability = new HashMap<>();
        durability.put(org.bukkit.Material.ELYTRA, 3000);
        durability.put(org.bukkit.Material.TURTLE_HELMET, 800);

        durability.put(org.bukkit.Material.DIAMOND_HELMET, 1600);
        durability.put(org.bukkit.Material.DIAMOND_CHESTPLATE, 1600);
        durability.put(org.bukkit.Material.DIAMOND_LEGGINGS, 1600);
        durability.put(org.bukkit.Material.DIAMOND_BOOTS, 1600);

        durability.put(org.bukkit.Material.IRON_HELMET, 900);
        durability.put(org.bukkit.Material.IRON_CHESTPLATE, 900);
        durability.put(org.bukkit.Material.IRON_LEGGINGS, 900);
        durability.put(org.bukkit.Material.IRON_BOOTS, 900);

        durability.put(org.bukkit.Material.CHAINMAIL_HELMET, 650);
        durability.put(org.bukkit.Material.CHAINMAIL_CHESTPLATE, 650);
        durability.put(org.bukkit.Material.CHAINMAIL_LEGGINGS, 650);
        durability.put(org.bukkit.Material.CHAINMAIL_BOOTS, 650);

        durability.put(org.bukkit.Material.GOLDEN_HELMET, 300);
        durability.put(org.bukkit.Material.GOLDEN_CHESTPLATE, 300);
        durability.put(org.bukkit.Material.GOLDEN_LEGGINGS, 300);
        durability.put(org.bukkit.Material.GOLDEN_BOOTS, 300);

        durability.put(org.bukkit.Material.LEATHER_HELMET, 200);
        durability.put(org.bukkit.Material.LEATHER_CHESTPLATE, 200);
        durability.put(org.bukkit.Material.LEATHER_LEGGINGS, 200);
        durability.put(org.bukkit.Material.LEATHER_BOOTS, 200);
    }

    public static boolean isArmor(String s) {
        return (s.contains("HELMET") || s.contains("CHESTPLATE") || s.contains("LEGGINGS") || s.contains("BOOTS") || s.contains("ELYTRA"));
    }

    public static String getNiceName(ItemStack i) {
        net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
        return nmsStack.getName().getString();
    }

    public ItemStack setDurability(ItemStack i, int d) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Durability: ")) {
                        ItemMeta meta = i.getItemMeta();
                        List<String> lore = meta.getLore();
                        lore.set(i.getItemMeta().getLore().indexOf(s), s.substring(0, s.indexOf(": ") + 5) + ChatColor.stripColor(s.substring(s.indexOf(": " + 4))).replaceFirst(String.valueOf(getDurability(i)), String.valueOf(d)));
                        meta.setLore(lore);
                        i.setItemMeta(meta);
                        return i;
                    }
                }
            }
        }

        return i;
    }

    public ItemStack decrementDurability(ItemStack i) {
        return setDurability(i, Math.max(getDurability(i) - 1, 0));
    }

    public static int getDurabilityMax(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Durability: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf("/") + 1));
                    }
                }
            }
        }
        return -1;
    }

    public static int getDurability(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Durability: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf(": ") + 2, s.indexOf("/")));
                    }
                }
            }
        }
        return -1;
    }

    public static int getLevelReq(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Level Req: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf(": ") + 2));
                    }
                }
            }
        }
        return 0;
    }

    public static int getArmor(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Armor: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf(": ") + 2));
                    }
                }
            }
        }
        return 0;
    }

    public static int getMR(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Magic Resist: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf(": ") + 2));
                    }
                }
            }
        }
        return 0;
    }

    public static int getWeight(ItemStack i) {
        if (i != null && i.getItemMeta() != null && i.getItemMeta().getLore() != null) {
            if (i.getItemMeta().hasLore()) {
                for (String s : i.getItemMeta().getLore()) {
                    if (s.contains("§7Weight: ")) {
                        s = ChatColor.stripColor(s);
                        return Integer.parseInt(s.substring(s.indexOf(": ") + 2));
                    }
                }
            }
        }
        return 0;
    }

    public static ItemStack primitize(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        boolean hasDura = false;
        if (meta.hasLore() && meta.getLore() != null) {
            for (String s : meta.getLore()) {
                if (s.contains("§7Durability: ")) {
                    hasDura = true;
                }
            }
        }
        meta.setDisplayName(Main.color("&7Primitive " + getNiceName(i)));
        List<String> lore;
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        if (!hasDura) {
            lore.add(Main.color("&8[ &7Basic Armor &8]"));
            lore.add(Main.color(""));
            lore.add(Main.color("&8[&a♢&8] &7Durability: &f" + durability.get(i.getType()) + "&8/&f" + durability.get(i.getType())));
            lore.add(Main.color("&8[&d★&8] &7Level Req: &f0"));
            lore.add(Main.color(""));
            lore.add(Main.color("&8[&6⛛&8] &7Armor: &f" + armor.get(i.getType()).getRandomLevel()));
            lore.add(Main.color("&8[&e♖&8] &7Weight: &f" + weight.get(i.getType())));
            meta.setLore(lore);
            i.setItemMeta(meta);
        }
        i.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return i;
    }

    public ItemStack primitizeRange(ItemStack i) {
        i = primitize(i);
        for (String s : i.getLore()) {
            if (s.contains("§7Armor: ")) {
                List<String> lore = i.getLore();
                lore.set(i.getLore().indexOf(s), Main.color(s.replaceFirst(String.valueOf(getArmor(i)), armor.get(i.getType()).getMin() + "&7-&f" + armor.get(i.getType()).getMax())));
                ItemMeta meta = i.getItemMeta();
                meta.setLore(lore);
                i.setItemMeta(meta);
            }
        }
        return i;
    }

    @EventHandler
    public void onArmorDmg (PlayerItemDamageEvent e) {
        if (isArmor(e.getItem().getType().toString())) {
            if (getDurability(e.getItem()) != -1) {
                e.setCancelled(true);
                e.getItem().setItemMeta(decrementDurability(e.getItem()).getItemMeta());
                if (getDurability(e.getItem()) <= 0) {
                    Main.msg(e.getPlayer(), "&cYour &f" + getNiceName(e.getItem()) + " &cbroke!");
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                }
            }
        }
    }

    public static ItemStack fixItem(ItemStack i) throws Exception {
        ItemStack nItem = i;
        if (i != null && isArmor(i.getType().toString())) {

            NBTItem nbtItem = new NBTItem(i);

            net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);

            nItem = primitize(nItem);


            if (nmsStack.getTag() == null || (nmsStack.getTag().getList("AttributeModifiers", 0) == null)) {
                Bukkit.broadcastMessage("Got past the if statement");
                NBTTagCompound itemTagC = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
                NBTTagList modifiers = new NBTTagList();
                NBTTagCompound itemC = new NBTTagCompound();

                Constructor<NBTTagString> constructorS = NBTTagString.class.getDeclaredConstructor(String.class);
                constructorS.setAccessible(true);
                NBTTagString nbts = constructorS.newInstance("generic.armor");

                Constructor<NBTTagDouble> constructorD = NBTTagDouble.class.getDeclaredConstructor(double.class);
                constructorD.setAccessible(true);
                NBTTagDouble nbtd = constructorD.newInstance(0);

                Constructor<NBTTagInt> constructorI = NBTTagInt.class.getDeclaredConstructor(int.class);
                constructorI.setAccessible(true);
                NBTTagInt nbti = constructorI.newInstance(0);
                NBTTagInt nbtiL = constructorI.newInstance(894654);
                NBTTagInt nbtiM = constructorI.newInstance(2827);

                itemC.set("AttributeName", nbts);
                itemC.set("Name", nbts);
                itemC.set("Amount", nbtd);
                itemC.set("Operation", nbti);
                itemC.set("UUIDLeast", nbtiL);
                itemC.set("UUIDMost", nbtiM);

                String item = i.toString().toLowerCase();

                NBTTagString nbthe = constructorS.newInstance("head");
                NBTTagString nbtch = constructorS.newInstance("chest");
                NBTTagString nbtle = constructorS.newInstance("legs");
                NBTTagString nbtbo = constructorS.newInstance("feet");

                if (item.contains("helmet") || item.contains("cap")) {
                    itemC.set("Slot", nbthe);
                }
                if (item.contains("chestplate") || item.contains("tunic") || item.contains("elytra")) {
                    itemC.set("Slot", nbtch);
                }
                if (item.contains("leggings") || item.contains("pants")) {
                    itemC.set("Slot", nbtle);
                }
                if (item.contains("boots")) {
                    itemC.set("Slot", nbtbo);
                }

                modifiers.add(itemC);
                itemTagC.set("AttributeModifiers", modifiers);
                nmsStack.setTag(itemTagC);
                nItem = CraftItemStack.asBukkitCopy(nmsStack);

                /*ItemMeta meta = nItem.getItemMeta();
                List<String> lore = new ArrayList<>();
                if (meta.hasLore()) {
                    lore = meta.getLore();
                }
                if (weight.containsKey(nItem.getType())) {
                    if (lore.isEmpty() || !lore.contains(Main.color("&eWeight: &f" + weight.get(nItem.getType())))) {
                        lore.add(Main.color("&eWeight: &f" + weight.get(nItem.getType())));
                    }
                }
                meta.setLore(lore);
                nItem.setItemMeta(meta);*/
            }
        }
        return nItem;
    }

    public void updateArmor(Player p) {
        for (int i = 0; i < p.getInventory().getContents().length; i++) {
            if (p.getInventory().getItem(i) instanceof ItemStack) {
                ItemStack it = p.getInventory().getItem(i);
                if (it != null && it.getType() != null && isArmor(it.getType().toString())) {
                    try {
                        p.getInventory().setItem(i, fixItem(it));
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    @EventHandler
    public void newArmor (PlayerArmorChangeEvent e) {
        updateArmor(e.getPlayer());
        double fullweight = 0;
        double maxweight = main.getRP(e.getPlayer()).getPClass().getWeight();
        for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
            /*if (armor != null && weight.containsKey(armor.getType())) {
                fullweight += weight.get(armor.getType());
            }*/
            if (armor != null) {
                fullweight += getWeight(armor);
            }
        }
        if (recievedWeight.get(e.getPlayer().getUniqueId()) + 50 < System.currentTimeMillis() && e.getPlayer().getLastLogin() + 2000  < System.currentTimeMillis() &&  (((e.getOldItem() == null && e.getNewItem() != null)||(e.getOldItem() == null && e.getNewItem() != null))||(e.getOldItem() != null && e.getNewItem() != null && e.getOldItem().getType() != e.getNewItem().getType()))) {
            Main.msg(e.getPlayer(), "&e&lArmor Weight: &f" + fullweight + " &8/ &f" + maxweight);
            recievedWeight.replace(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
        if (fullweight > maxweight) {
            if (fullweight < maxweight * 1.5) {
                clearArmorWS(e.getPlayer());
                Main.msg(e.getPlayer(), "&cYour armor is too heavy, you're afflicted with slowness.");
                main.getRP(e.getPlayer()).getWalkspeed().getStatuses().add(new StatusValue("ARMOR:" + e.getPlayer().getName(), -10, 0, 0, true));
            } else if (fullweight < maxweight * 2) {
                clearArmorWS(e.getPlayer());
                Main.msg(e.getPlayer(), "&cYour armor is far too heavy, you're afflicted with slowness.");
                main.getRP(e.getPlayer()).getWalkspeed().getStatuses().add(new StatusValue("ARMOR:" + e.getPlayer().getName(), -15, 0, 0, true));
            } else if (fullweight < maxweight * 2.5) {
                clearArmorWS(e.getPlayer());
                Main.msg(e.getPlayer(), "&cYour armor is extremely heavy, you're afflicted with high slowness.");
                main.getRP(e.getPlayer()).getWalkspeed().getStatuses().add(new StatusValue("ARMOR:" + e.getPlayer().getName(), -19, 0, 0, true));
            } else {
                clearArmorWS(e.getPlayer());
                Main.msg(e.getPlayer(), "&cYour armor is impossibly heavy, you can't move!");
                main.getRP(e.getPlayer()).getWalkspeed().getStatuses().add(new StatusValue("ARMOR:" + e.getPlayer().getName(), -50, 0, 0, true));
            }
        } else {
            main.getRP(e.getPlayer()).updateWS();
        }
    }

    public void clearArmorWS(Player p) {
        main.getRP(p).getWalkspeed().clearBasedTitle("ARMOR", p);
    }

    @EventHandler
    public void jump (PlayerJumpEvent e) {
        double fullweight = 0;
        double maxweight = main.getRP(e.getPlayer()).getPClass().getWeight();
        for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
            if (armor != null && weight.containsKey(armor.getType())) {
                fullweight+=weight.get(armor.getType());
            }
        }
        if (fullweight > maxweight) {
            Main.msg(e.getPlayer(), "&cYour armor is too heavy to jump in!");
            e.setCancelled(true);
        }
    }

    /*@EventHandler
    public void onCraft (CraftItemEvent e) {
        if (e.getCurrentItem() != null && durability.containsKey(e.getCurrentItem().getType())) {
            e.getCurrentItem().setItemMeta(primitize(e.getCurrentItem()).getItemMeta());
        }
    }*/

    @EventHandler
    public void prepCraft (PrepareItemCraftEvent e) {
        if (e.getRecipe() != null && durability.containsKey(e.getRecipe().getResult().getType())) {
            e.getInventory().setResult(primitize(e.getRecipe().getResult()));
        }
    }

}