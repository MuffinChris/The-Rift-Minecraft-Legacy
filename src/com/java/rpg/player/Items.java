package com.java.rpg.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.java.Main;
import com.java.rpg.classes.LevelRange;
import com.java.rpg.classes.StatusValue;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
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
        armor.put(org.bukkit.Material.ELYTRA, new LevelRange(100, 250));
        armor.put(org.bukkit.Material.TURTLE_HELMET, new LevelRange(40, 50));

        armor.put(org.bukkit.Material.DIAMOND_HELMET, new LevelRange(50, 55));
        armor.put(org.bukkit.Material.DIAMOND_CHESTPLATE, new LevelRange(70, 75));
        armor.put(org.bukkit.Material.DIAMOND_LEGGINGS, new LevelRange(65, 70));
        armor.put(org.bukkit.Material.DIAMOND_BOOTS, new LevelRange(55, 60));

        armor.put(org.bukkit.Material.IRON_HELMET, new LevelRange(40, 45));
        armor.put(org.bukkit.Material.IRON_CHESTPLATE, new LevelRange(55, 60));
        armor.put(org.bukkit.Material.IRON_LEGGINGS, new LevelRange(55, 60));
        armor.put(org.bukkit.Material.IRON_BOOTS, new LevelRange(45, 50));

        armor.put(org.bukkit.Material.CHAINMAIL_HELMET, new LevelRange(35, 40));
        armor.put(org.bukkit.Material.CHAINMAIL_CHESTPLATE, new LevelRange(50, 55));
        armor.put(org.bukkit.Material.CHAINMAIL_LEGGINGS, new LevelRange(45, 50));
        armor.put(org.bukkit.Material.CHAINMAIL_BOOTS, new LevelRange(40, 45));

        armor.put(org.bukkit.Material.GOLDEN_HELMET, new LevelRange(25, 30));
        armor.put(org.bukkit.Material.GOLDEN_CHESTPLATE, new LevelRange(35, 40));
        armor.put(org.bukkit.Material.GOLDEN_LEGGINGS, new LevelRange(35, 40));
        armor.put(org.bukkit.Material.GOLDEN_BOOTS, new LevelRange(20, 25));

        armor.put(org.bukkit.Material.LEATHER_HELMET, new LevelRange(10, 15));
        armor.put(org.bukkit.Material.LEATHER_CHESTPLATE, new LevelRange(20, 25));
        armor.put(org.bukkit.Material.LEATHER_LEGGINGS, new LevelRange(15, 20));
        armor.put(org.bukkit.Material.LEATHER_BOOTS, new LevelRange(5, 10));

        durability = new HashMap<>();
        durability.put(org.bukkit.Material.ELYTRA, 4000);
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
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("Durability")) {
                nbtItem.setInteger("Durability", Math.min(d, getDurabilityMax(i)));
                nbtItem.getItem().setItemMeta(updateDurability(nbtItem.getItem()).getItemMeta());
                return nbtItem.getItem();
            }
        }
        return i;
    }

    public ItemStack decrementDurability(ItemStack i) {
        return setDurability(i, Math.max(getDurability(i) - 1, 0));
    }

    public static int getDurabilityMax(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("MaxDurability")) {
                return nbtItem.getInteger("MaxDurability");
            }
        }
        return -1;
    }

    public static int getDurability(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("Durability")) {
                return nbtItem.getInteger("Durability");
            }
        }
        return -1;
    }

    public static int getLevelReq(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("LevelReq")) {
                return nbtItem.getInteger("LevelReq");
            }
        }
        return 0;
    }

    public static int getArmor(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("Armor")) {
                return nbtItem.getInteger("Armor");
            }
        }
        return 0;
    }

    public static int getMR(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("MagicResist")) {
                return nbtItem.getInteger("MagicResist");
            }
        }
        return 0;
    }

    public static int getWeight(ItemStack i) {
        if (i != null) {
            NBTItem nbtItem = new NBTItem(i);
            if (nbtItem.hasKey("Weight")) {
                return nbtItem.getInteger("Weight");
            }
        }
        return 0;
    }

    public static String durabilityString = "&8[&a✦&8] &7Durability: &f";
    public static String levelReqString = "&8[&e⚒&8] &7Level Req: &f";
    public static String armorString = "&8[&6⛨&8] &7Armor: &f";

    public static ItemStack primitize(ItemStack i) {
        i.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        i.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ItemMeta meta = i.getItemMeta();
        boolean hasDura = false;
        NBTItem nbtItem = new NBTItem(i);
        if (nbtItem.hasKey("Durability")) {
            hasDura = true;
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
            lore.add(Main.color(levelReqString + "0"));
            lore.add(Main.color(durabilityString + durability.get(i.getType()) + "&8/&f" + durability.get(i.getType())));
            lore.add(Main.color(""));
            int am = armor.get(i.getType()).getRandomLevel();
            lore.add(Main.color(armorString + am));
            //lore.add(Main.color("&8[&e♖&8] &7Weight: &f" + weight.get(i.getType())));
            meta.setLore(lore);
            nbtItem.getItem().setItemMeta(meta);
            nbtItem.setInteger("Durability", durability.get(i.getType()));
            nbtItem.setInteger("MaxDurability", durability.get(i.getType()));
            nbtItem.setInteger("LevelReq", 0);
            nbtItem.setInteger("Armor", am);
            nbtItem.setInteger("Weight", weight.get(i.getType()));

        }

        return nbtItem.getItem();
    }

    public boolean hasDurability(ItemStack i) {
        NBTItem nbtItem = new NBTItem(i);
        return nbtItem.hasKey("Durability");
    }

    public ItemStack primitizeRange(ItemStack i) {
        i = primitize(i);
        for (String s : i.getLore()) {
            if (s.contains("§7Armor: ")) {
                List<String> lore = i.getLore();
                lore.set(i.getLore().indexOf(s), Main.color(armorString + armor.get(i.getType()).getMin() + "&7-&f" + armor.get(i.getType()).getMax()));
                ItemMeta meta = i.getItemMeta();
                meta.setLore(lore);
                i.setItemMeta(meta);
            }
        }
        return i;
    }

    public ItemStack unrange(ItemStack i) {
        for (String s : i.getLore()) {
            if (s.contains("§7Armor: ")) {
                List<String> lore = i.getLore();
                lore.set(i.getLore().indexOf(s), Main.color(armorString + getArmor(i)));
                ItemMeta meta = i.getItemMeta();
                meta.setLore(lore);
                i.setItemMeta(meta);
            }
        }
        return i;
    }

    public ItemStack updateDurability(ItemStack i) {
        for (String s : i.getLore()) {
            if (s.contains("§7Durability: ")) {
                List<String> lore = i.getLore();
                lore.set(i.getLore().indexOf(s), Main.color(durabilityString + getDurability(i) + "&8/&f" + getDurabilityMax(i)));
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

    public static ItemStack removeArmor (ItemStack i) {
        NBTItem nbtItem = new NBTItem(i);
        NBTCompound atr = nbtItem.getCompoundList("AttributeModifiers").addCompound();
        atr.setDouble("Amount", 0.0);
        atr.setString("AttributeName", "generic.armor");
        atr.setString("Name", "generic.armor");
        atr.setInteger("Operation", 0);
        atr.setInteger("UUIDLeast", 59764);
        atr.setInteger("UUIDMost", 31483);

        NBTCompound atrT = nbtItem.getCompoundList("AttributeModifiers").addCompound();
        atrT.setDouble("Amount", 0.0);
        atrT.setString("AttributeName", "generic.armorToughness");
        atrT.setString("Name", "generic.armorToughness");
        atrT.setInteger("Operation", 0);
        atrT.setInteger("UUIDLeast", 58764);
        atrT.setInteger("UUIDMost", 32483);
        return nbtItem.getItem();
    }

    public static ItemStack removeDamage (ItemStack i) {
        NBTItem nbtItem = new NBTItem(i);
        NBTCompound atr = nbtItem.getCompoundList("AttributeModifiers").addCompound();
        atr.setDouble("Amount", 0.0);
        atr.setString("AttributeName", "generic.attackDamage");
        atr.setString("Name", "generic.attackDamage");
        atr.setInteger("Operation", 0);
        atr.setInteger("UUIDLeast", 53764);
        atr.setInteger("UUIDMost", 29483);

        NBTCompound atrT = nbtItem.getCompoundList("AttributeModifiers").addCompound();
        atrT.setDouble("Amount", 16.0);
        atrT.setString("AttributeName", "generic.attackSpeed");
        atrT.setString("Name", "generic.attackSpeed");
        atrT.setInteger("Operation", 0);
        atrT.setInteger("UUIDLeast", 54764);
        atrT.setInteger("UUIDMost", 30483);
        return nbtItem.getItem();
    }

    public static ItemStack fixItem(ItemStack i) throws Exception {
        ItemStack nItem = i;
        if (i != null && isArmor(i.getType().toString())) {

            //net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
            if (!(new NBTItem(nItem).hasKey("Durability"))) {
                nItem = primitize(nItem);
                NBTItem nbtItem = new NBTItem(nItem);
                if (nbtItem.getCompoundList("AttributeModifiers").size() == 0) {
                    nItem = removeArmor(nItem);
                }
            }
            //nbtItem.setInteger("generic.armor", 0);

            /*if (nmsStack.getTag() == null || (nmsStack.getTag().getList("AttributeModifiers", 0) == null)) {
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
            }*/
        }
        return nItem;
    }

    public void updateArmor(Player p) {
        for (int i = 36; i < 40; i++) {
            if (p.getInventory().getItem(i) instanceof ItemStack) {
                ItemStack it = p.getInventory().getItem(i);
                if (it != null && isArmor(it.getType().toString())) {
                    try {
                        if (!fixItem(it).isSimilar(it)) {
                            p.getInventory().setItem(i, fixItem(it));
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    @EventHandler
    public void newArmor (PlayerArmorChangeEvent e) {
        updateArmor(e.getPlayer());
        main.getRP(e.getPlayer()).updateWS();
        /*
        double fullweight = 0;
        double maxweight = main.getRP(e.getPlayer()).getPClass().getWeight();
        for (ItemStack armor : e.getPlayer().getInventory().getArmorContents()) {
            if (armor != null) {
                fullweight += getWeight(armor);
            }
        }*/
        /*if (recievedWeight.get(e.getPlayer().getUniqueId()) + 50 < System.currentTimeMillis() && e.getPlayer().getLastLogin() + 2000  < System.currentTimeMillis()) {
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
            clearArmorWS(e.getPlayer());
            main.getRP(e.getPlayer()).updateWS();
        }*/
    }

    public void clearArmorWS(Player p) {
        main.getRP(p).getWalkspeed().clearBasedTitle("ARMOR", p);
    }

    /*@EventHandler
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
    }*/

    /*

    Custom Crafting

     */

    @EventHandler
    public void prepCraft (PrepareItemCraftEvent e) {
        Material type = null;
        int a = 0;
        for (ItemStack i : e.getInventory().getMatrix()) {
            if (i != null) {
                if (type == null) {
                    type = i.getType();
                }
                if (i.getType() == type && durability.containsKey(i.getType())) {
                    a++;
                }
            }
        }
        if (a==2) {
            e.getInventory().setResult(null);
        }
        if (e.getRecipe() != null && !e.isRepair()) {
            if (durability.containsKey(e.getRecipe().getResult().getType())) {
                e.getInventory().setResult(removeArmor(primitizeRange(e.getRecipe().getResult())));
            }
        }
    }

    @EventHandler
    public void onCraft (CraftItemEvent e) {
        if (e.getCurrentItem() != null && durability.containsKey(e.getCurrentItem().getType()) && (new NBTItem(e.getCurrentItem()).hasKey("Armor"))) {
            e.getCurrentItem().setItemMeta(unrange(e.getCurrentItem()).getItemMeta());
        }
    }

    /*

    ANVILS

     */
    @EventHandler
    public void onAnvil (PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.ANVIL) {

            }
        }
    }

    /*

        ENCHANTMENTS SECTION!

     */

    @EventHandler
    public void cancelMend (PlayerItemMendEvent e) {
        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void fakeMend (PlayerPickupExperienceEvent e) {
        int amount = e.getExperienceOrb().getExperience()/2;
        List<ItemStack> mItems = new ArrayList<>();
        if (e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItemInMainHand());
        }
        if (e.getPlayer().getInventory().getItemInOffHand().containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItemInOffHand());
        }
        if (e.getPlayer().getInventory().getItem(36) != null && e.getPlayer().getInventory().getItem(36).containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItem(36));
        }
        if (e.getPlayer().getInventory().getItem(37) != null && e.getPlayer().getInventory().getItem(37).containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItem(37));
        }
        if (e.getPlayer().getInventory().getItem(38) != null && e.getPlayer().getInventory().getItem(38).containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItem(38));
        }
        if (e.getPlayer().getInventory().getItem(39) != null && e.getPlayer().getInventory().getItem(39).containsEnchantment(Enchantment.MENDING)) {
            mItems.add(e.getPlayer().getInventory().getItem(39));
        }
        if (!mItems.isEmpty()) {
            int rand = (int) (Math.random() * mItems.size());
            ItemStack item = mItems.get(rand);
            if (item != null && hasDurability(item) && item.containsEnchantment(Enchantment.MENDING)) {
                if (getDurability(item) != getDurabilityMax(item)) {
                    e.getExperienceOrb().setExperience(0);
                    e.getPlayer().getInventory().setItem(Arrays.asList(e.getPlayer().getInventory().getContents()).indexOf(item), setDurability(item, getDurability(item) + amount));
                }
            }
        }
    }

}
