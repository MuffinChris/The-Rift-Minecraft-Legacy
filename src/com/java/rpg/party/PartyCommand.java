package com.java.rpg.party;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PartyCommand implements CommandExecutor, Listener {

    @EventHandler
    public void onLeaveParty(PlayerQuitEvent e) {
        if (main.getPM().hasParty(e.getPlayer())) {
            if (main.getPM().getParty(e.getPlayer()).getLeader().equals(e.getPlayer())) {
                main.getPM().getParty(e.getPlayer()).disband();
            } else {
                main.getPM().getParty(e.getPlayer()).removePlayer(e.getPlayer());
            }
        }
        if (main.getInvites().containsKey(e.getPlayer())) {
            main.getInvites().remove(e.getPlayer());
        }
    }

    private Main main = Main.getInstance();

    @EventHandler
    public void cancelClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lYOUR PARTY")) {
            e.setCancelled(true);
        }
    }

    public void sendPlayerInv(Player p) {
        Party party = main.getPM().getParty(p);
        int size = 9;
        if (party.getPlayers().size() > 8) {
            size = 18;
            if (party.getPlayers().size() > 17) {
                size = 27;
            }
            if (party.getPlayers().size() > 26) {
                size = 36;
            }
        }
        Inventory playerInv = Bukkit.createInventory(null, size, Main.color("&e&lYOUR PARTY"));
        ArrayList<String> lore;
        ItemStack sp = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta spMeta = (SkullMeta) sp.getItemMeta();
        spMeta.setOwningPlayer(p);
        spMeta.setDisplayName(Main.color("&e&l" + p.getName()));
        lore = new ArrayList<String>();
        if (party.getLeader().equals(p)) {
            lore.add(Main.color("&8< &6Party Leader &8>"));
        }
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat dF = new DecimalFormat("#");
        RPGPlayer rp = main.getPC().get(p.getUniqueId());
        lore.add(Main.color("&8» &eClass: &f" + rp.getPClass().getName()));
        lore.add(Main.color("&8» &eLevel: &f" + rp.getLevel()));
        lore.add(Main.color("&8» &aExp: &f" + rp.getPrettyExp() + " &8/ &f" + rp.getPrettyMaxExp() + " &8(&a" + rp.getPrettyPercent() + "%&8)"));
        lore.add("");
        lore.add(Main.color("&8» &cHP: &f" + df.format(p.getHealth()) + "&8/" + "&c" + df.format(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())));
        lore.add(Main.color("&8» &bMana: &f" + dF.format(rp.getCMana()) + "&8/&b" + dF.format(rp.getPClass().getCalcMana(rp.getLevel()))));
        lore.add(Main.color("&8» &9Mana Regen: &f" + df.format(rp.getPClass().getCalcManaRegen(rp.getLevel())) + "/s"));
        spMeta.setLore(lore);
        sp.setItemMeta(spMeta);
        playerInv.setItem(0, sp);

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        lore = new ArrayList<String>();
        if (party.getLeader().equals(p)) {
            lore.add(Main.color("&8» &e/party invite <player> &8|| &fInvite a player to your party."));
            lore.add(Main.color("&8» &e/party kick <player> &8|| &fRemove a player from your party."));
            lore.add(Main.color("&8» &e/party leave &8|| &fLeave your current party."));
            lore.add(Main.color("&8» &e/party pvp <true/false> &8|| &fEnable or disable PVP within party."));
            lore.add(Main.color("&8» &e/party share <true/false> &8|| &fEnable or disable EXP Share in party."));
            lore.add(Main.color("&8» &e/party chat &8|| &fToggle party-only chat."));
        } else {
            lore.add(Main.color("&8» &e/party leave &8|| &fLeave your current party."));
            lore.add(Main.color("&8» &e/party chat &8|| &fToggle party-only chat."));
        }
        meta.setDisplayName(Main.color("&eCommands:"));
        meta.setLore(lore);
        glass.setItemMeta(meta);
        playerInv.setItem(1, glass);

        int mod = 0;
        for (int i = 2; i < party.getPlayers().size() + 2; i++) {
            lore = new ArrayList<String>();
            Player t = party.getPlayers().get(i - 2);
            if (t.equals(p)) {
                mod--;

            } else {
                ItemStack player = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta smeta = (SkullMeta) player.getItemMeta();
                smeta.setDisplayName(Main.color("&e&l" + t.getName()));
                if (party.getLeader().equals(t)) {
                    lore.add(Main.color("&8< &6Party Leader &8>"));
                }
                smeta.setOwningPlayer(t);
                RPGPlayer tp = main.getPC().get(t.getUniqueId());
                df = new DecimalFormat("#.##");
                lore.add("");
                lore.add(Main.color("&8» &eClass: &f" + tp.getPClass().getName()));
                lore.add(Main.color("&8» &eLevel: &f" + tp.getLevel()));
                lore.add(Main.color("&8» &aExp: &f" + tp.getPrettyExp() + " &8/ &f" + tp.getPrettyMaxExp() + " &8(&a" + tp.getPrettyPercent() + "%&8)"));
                lore.add("");
                lore.add(Main.color("&8» &cHP: &f" + df.format(t.getHealth()) + "&8/" + "&c" + df.format(t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())));
                lore.add(Main.color("&8» &bMana: &f" + dF.format(tp.getCMana()) + "&8/&b" + dF.format(tp.getPClass().getCalcMana(tp.getLevel()))));
                lore.add(Main.color("&8» &9Mana Regen: &f" + df.format(tp.getPClass().getCalcManaRegen(tp.getLevel())) + "/s"));
                smeta.setLore(lore);
                player.setItemMeta(smeta);
                playerInv.setItem(i + mod, player);
            }
        }

        p.openInventory(playerInv);
    }

    private boolean is(String comp, String s) {
        String[] strings = s.split(",");
        for (String str : strings) {
            if (str.equalsIgnoreCase(comp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            String acpts = "leave,chat,invite,kick,create,deny,decline,accept,pvp,share";
            String acpts2 = "true,false";
            Player p = (Player) sender;
            if (args.length > 0 && is(args[0],"accept")) {
                if (main.getPM().hasParty(p)) {
                    Main.msg(p, "&cYou must leave your current party!");
                    return false;
                }
                if (main.getInvites().containsKey(p)) {
                    if (main.getInvites().get(p) instanceof Party) {
                        main.getInvites().get(p).addPlayer(p);
                        main.getInvites().remove(p);
                    } else {
                        main.getInvites().remove(p);
                        Main.msg(p, "&cParty invite has expired.");
                    }
                } else {
                    Main.msg(p, "&cNo pending invites.");
                }
                return false;
            }
            if (args.length > 0 && is(args[0],"deny,decline")) {
                if (main.getInvites().containsKey(p)) {
                    main.getInvites().get(p).sendMessage("&c" + p.getName() + " has declined the invite.");
                    main.getInvites().remove(p);
                    Main.msg(p, "&cYou have declined the Party invite.");
                } else {
                    Main.msg(p, "&cNo pending invites.");
                }
                return false;
            }
            if (args.length > 0 && is(args[0],"create")) {
                if (main.getPM().hasParty(p)) {
                    Main.msg(p, "&cYou must leave your current party!");
                    return false;
                }
                main.getPM().createParty(p);
                if (main.getInvites().containsKey(p)) {
                    main.getInvites().get(p).sendMessage("&c" + p.getName() + " has declined the invite.");
                    main.getInvites().remove(p);
                }
                Main.msg(p, "&aYou created a party!");
                return false;
            }
            if (!main.getPM().hasParty(p)) {
                Main.msg(p, "&fUsage: /party <create/accept>");
                return false;
            }
            Party party = main.getPM().getParty(p);
            if (args.length == 0) {
                    if (party.getLeader().equals(p)) {
                        sendPlayerInv(p);
                    } else {
                        sendPlayerInv(p);
                    }
            } else if (args.length == 1) {
                    if (party.getLeader().equals(p)) {
                        if (is(args[0],"invite,kick")) {
                            Main.msg(p, "&fUsage: /party <invite/kick> <player>");
                        } else if (is(args[0],"leave,disband")) {
                            party.disband();
                            main.getPM().cleanParties();
                            return false;
                        } else if (is(args[0],"pvp,share")) {
                            Main.msg(p, "&fUsage: /party <pvp/share> <true/false>");
                        }
                    }
                    if (is(args[0],"leave,chat")) {
                        if (is(args[0],"leave")) {
                            party.removePlayer(p);
                        } else if (is(args[0],"chat")) {
                            if (main.getRP(p).getChatChannel() == RPGPlayer.ChatChannel.Party) {
                                main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Global);
                                Main.msg(p, "&cDisabled Party Chat.");
                                return false;
                            } else {
                                main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Global);
                                Main.msg(p, "&aEnabled Party Chat.");
                                return false;
                            }
                        }
                    } else {
                        if (!is(args[0],acpts)) {
                            if (party.getLeader().equals(p)) {
                                Main.msg(p, "&fUsage: /party <invite/kick/pvp/share> <arg>");
                            } else {
                                Main.msg(p, "&fUsage: /party <leave/chat>");
                            }
                        }
                    }
            } else if (args.length == 2) {
                if (party.getLeader().equals(p)) {
                    if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("kick")) {
                        if (Bukkit.getPlayer(args[1]) instanceof Player) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (args[0].equalsIgnoreCase("invite")) {
                                if (party.hasPlayer(target)) {
                                    Main.msg(p, "&cPlayer is already in your party!");
                                } else {
                                    if (target.equals(p)) {
                                        Main.msg(p, "&cYou cannot invite yourself!");
                                        return false;
                                    }
                                    if (main.getPM().hasParty(target)) {
                                        Main.msg(p, "&cPlayer is already in a party!");
                                        return false;
                                    }
                                    if (main.getInvites().containsKey(target)) {
                                        main.getInvites().replace(target, party);
                                    } else {
                                        main.getInvites().put(target, party);
                                    }
                                    Main.msg(p, "&eYou have invited " + target.getName() + " &eto your party!");
                                    Main.msg(target, "&eYou have been invited to " + p.getName() + "'s party!");
                                    Main.msg(target, "&eAccept this invite with: " + "&f/party accept");
                                }
                            } else {
                                if (party.hasPlayer(target)) {
                                    if (target.equals(p)) {
                                        Main.msg(p, "&cYou cannot kick yourself!");
                                        return false;
                                    }
                                    Main.msg(p, "&cYou have kicked " + target.getName() + " &cfrom the party!");
                                    party.kickPlayer(target);
                                } else {
                                    Main.msg(p, "&cPlayer is not in your party!");
                                }
                            }
                        } else {
                            Main.msg(p, "&cInvalid Target.");
                        }
                    } else if (args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("disband")) {
                        party.disband();
                        return false;
                    } else {
                        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                            if (args[1].equalsIgnoreCase("true")) {
                                if (args[0].equalsIgnoreCase("pvp")) {
                                    party.setPvp(true);
                                    Main.msg(p, "&aEnabled Party PVP.");
                                } else {
                                    party.setShare(true);
                                    Main.msg(p, "&aEnabled Party EXP Share.");
                                }
                            } else {
                                if (args[0].equalsIgnoreCase("pvp")) {
                                    party.setPvp(false);
                                    Main.msg(p, "&cDisabled Party PVP.");
                                } else {
                                    party.setShare(false);
                                    Main.msg(p, "&cDisabled Party EXP Share.");
                                }
                            }
                        } else {
                            if (!is(args[0],acpts) || (!is(args[1],acpts2) && is(args[0],"share,pvp"))) {
                                if (party.getLeader().equals(p)) {
                                    Main.msg(p, "&fUsage: /party <invite/kick/pvp/share> <arg>");
                                } else {
                                    Main.msg(p, "&fUsage: /party <leave/chat>");
                                }
                            }
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("leave")) {
                        party.removePlayer(p);
                    } else {
                        if (!is(args[0],acpts)) {
                            Main.msg(p, "&fUsage: /party <leave/chat>");
                        }
                    }
                }
            }
        } else {
            Main.so("Console cannot manage Parties.");
        }
        return false;
    }

}
