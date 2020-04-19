package com.java.towns;

import com.java.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.EventPriority;

import java.util.ArrayList;
import java.util.List;

public class TownCommand implements CommandExecutor, Listener {

    private Main main = Main.getInstance();

    private Town getTownFromCitizen(Citizen c) {
        Town t = null;
        for (Town ct : main.getTowns()) {
            if (ct.getName().equals(c.getTown())) {
                t = ct;
                break;
            }
        }
        return t;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if (args.length == 0) {
            if (main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equals("None"))
                sendTownlessInv(p);
            else
                sendTownInv(p);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                Main.msg(p, Main.color("&4Must specify a town name!"));
                return false;
            } else if (args[0].equalsIgnoreCase("leave")) {
                return leaveTown(p);
            } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("disband") || args[0].equalsIgnoreCase("remove")) {
                return deleteTown(p);
            } else if (args[0].equalsIgnoreCase("invite")) {
                Main.msg(p, Main.color("&4Must specify player to invite!"));
                return false;
            } else if (args[0].equalsIgnoreCase("accept")) {
                return acceptInvite(p);
            } else if (args[0].equalsIgnoreCase("decline")) {
                return declineInvite(p);
            } else if (args[0].equalsIgnoreCase("kick")) {
                // TODO: implement
            } else if (args[0].equalsIgnoreCase("promote")) {
                // TODO: implement
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                return sendInvite(p, args[1]); // args[1] is username of player to inv
            } else if (args[0].equalsIgnoreCase("create")) {
                return createNewTown(p, args[1]); // args[1] is name of town
            }
        }

        return false;
    }

    private ItemStack getNewTownItemStack() {
        ItemStack sp = new ItemStack(Material.NETHER_STAR);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&6Create New Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to create a town!"));
            }
        });

        spMeta.addEnchant(Enchantment.MENDING, 1, true);
        spMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getRemoveTownItemStack() {
        ItemStack sp = new ItemStack(Material.TNT);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&4Delete Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to delete your current town!"));
                add(Main.color("&6Must be Owner rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getLeaveTownItemStack() {
        ItemStack sp = new ItemStack(Material.BARRIER);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&aLeave Town"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to leave your current town!"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getInviteItemStack() {
        ItemStack sp = new ItemStack(Material.EMERALD);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&aInvite"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to invite a player!"));
                add(Main.color("&6Must have permission to invite"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getKickItemStack() {
        ItemStack sp = new ItemStack(Material.REDSTONE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&cKick"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to kick player!"));
                add(Main.color("&6Must have permission to kick"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getPromoteItemStack() {
        ItemStack sp = new ItemStack(Material.GREEN_DYE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&9Promote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to promote a player!"));
                add(Main.color("&6Must be higher than promotion rank"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getDemoteItemStack() {
        ItemStack sp = new ItemStack(Material.RED_DYE);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&dDemote"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fClick to demote a player!"));
                add(Main.color("&6Must be higher rank than player"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private ItemStack getLeaderboardItemStack() {
        ItemStack sp = new ItemStack(Material.PAPER);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&dLeaderboard"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fShows the top towns on the server!"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    public void sendTownInv(Player p) {
        String townName = main.getUUIDCitizenMap().get(p.getUniqueId()).getTown();
        Inventory menu = Bukkit.createInventory(null, 36, Main.color("&b&l" + townName + " Menu"));

        // delete town
        menu.setItem(10, getRemoveTownItemStack());
        // leave town
        menu.setItem(11, getLeaveTownItemStack());

        // town leaderboard
        menu.setItem(21, getLeaderboardItemStack());

        // town search

        // invite
        menu.setItem(13, getInviteItemStack());
        // kick
        menu.setItem(14, getKickItemStack());
        // promote
        menu.setItem(15, getPromoteItemStack());
        // demote
        menu.setItem(16, getDemoteItemStack());

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);

        for (int i = 0; i < 9; i++) {
            menu.setItem(i, ph);
        }
        for (int i = 27; i <= 35; i++) {
            menu.setItem(i, ph);
        }

        p.openInventory(menu);
        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }

    public void sendAreYouSureInv(Player p, String s) {
        Inventory menu = Bukkit.createInventory(null, 27, Main.color("&e&l" + s));

        ItemStack yesStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemStack noStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta yesMeta = yesStack.getItemMeta();
        ItemMeta noMeta = noStack.getItemMeta();
        yesMeta.setDisplayName(Main.color("&a&lYes"));
        noMeta.setDisplayName(Main.color("&c&lNo"));
        yesStack.setItemMeta(yesMeta);
        noStack.setItemMeta(noMeta);
        menu.setItem(11, yesStack);
        menu.setItem(15, noStack);

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 0; i < 9; i++) {
            menu.setItem(i, ph);
        }
        for (int i = 18; i < 27; i++) {
            menu.setItem(i, ph);
        }

        p.openInventory(menu);
        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }

    public void sendTownlessInv(Player p) {
        Inventory menu = Bukkit.createInventory(null, 27, Main.color("&e&lTown Menu"));

        // town create
        menu.setItem(10, getNewTownItemStack());

        // town leaderboard
        menu.setItem(21, getLeaderboardItemStack());

        // town search

        ItemStack ph = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta phM = ph.getItemMeta();
        phM.setDisplayName(" ");
        ph.setItemMeta(phM);
        for (int i = 0; i < 9; i++) {
            menu.setItem(i, ph);
        }
        for (int i = 18; i < 27; i++) {
            menu.setItem(i, ph);
        }

        p.openInventory(menu);
        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }

    private boolean createNewTown(Player p, String townName) {

        if (townName.equalsIgnoreCase("")) {
            if (!main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equalsIgnoreCase("None")) {
                Main.msg(p, Main.color("&4You are already in a town!"));
                return false;
            }

            Main.msg(p, Main.color("&l&eEnter Town Name (A-Z and spaces only): "));

            main.getUUIDCitizenMap().get(p.getUniqueId()).setCreationStatus("Prompted");
            new BukkitRunnable() {
                public void run() {
                    Citizen mc = main.getUUIDCitizenMap().get(p.getUniqueId());
                    if (!mc.getCreationStatus().equals("Normal")) {
                        mc.setCreationStatus("Normal");
                        Main.msg(p, Main.color("&4Prompt Timed Out."));
                    }
                }
            }.runTaskLater(Main.getInstance(), 20 * 60);

        } else {
            for (String tn : main.getFullTownList()) {
                if (tn.equalsIgnoreCase(townName)) {
                    Main.msg(p, Main.color("&4Town name already taken!"));
                    return false;
                }
            }

            if (!townName.matches("[a-zA-Z ]+")) {
                Main.msg(p, Main.color("&4Town names can only contain characters (A-Z) and spaces"));
                return false;
            }

            if (townName.equalsIgnoreCase("None")) {
                Main.msg(p, Main.color("&4That is a protected town name!"));
                return false;
            }

            Town nt = new Town(p, townName);
            main.getTowns().add(nt);

            List<String> fullTowns = main.getFullTownList();
            fullTowns.add(townName);
            main.setFullTownList(fullTowns);

            Main.msg(p, Main.color("&l&aSuccessfully created town: &f" + townName));
        }
        return true;

    }

    private boolean leaveTown(Player p) {

        // TODO: if the owner of a town leaves -- then either the town should disband or the max level role needs to be transferred (randomly to a maxlvl -1 member?)
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (c.getTown().equalsIgnoreCase("None")) {
            Main.msg(p, "&4You aren't in any town!");
            return false;
        }

        Town t = getTownFromCitizen(c);

        c.setTown("None"); // default values
        c.setRank(-1); // %
        Main.msg(p, "&4You have successfully left " + t.getName());

        t.getCitizenList().removeCitizen(c);

        if (t.getCitizenList().citimap.size() == 0) {
            // there are no more people in this town -- delete it
            main.getTowns().remove(t);
            List<String> fullTowns = main.getFullTownList();
            fullTowns.remove(t.getName());
            main.setFullTownList(fullTowns);

            Bukkit.broadcastMessage(Main.color("&l&4Town " + t.getName() + " has been disbanded!"));
        }
        c.pushFiles();
        t.pushFiles();
        return true;
    }

    private boolean deleteTownPre(Player p) {
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        Town t = getTownFromCitizen(c);

        if (c.getRank() != t.getRanks().size() - 1) { // make sure user is the highest possible rank
            Main.msg(p, "&4You don't have permission to do this!");
            return false;
        }
        return true;
    }

    private boolean deleteTown(Player p) {
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        Town t = getTownFromCitizen(c);
        if (c.getRank() != t.getRanks().size() - 1) { // make sure user is the highest possible rank
            Main.msg(p, "&4You don't have permission to do this!");
            return false;
        }
        Main.msg(p, "&aTown successfully disbanded");
        for (Citizen ct : t.getCitizenList().citimap.values()) {
            ct.setRank(-1);
            ct.setTown("None");
            Citizen ctmain = main.getUUIDCitizenMap().get(ct.getPlayer());
            ct.setRank(-1);
            ctmain.setTown("None");
        }

        main.getTowns().remove(t);
        List<String> fullTowns = main.getFullTownList();
        fullTowns.remove(t.getName());
        main.setFullTownList(fullTowns);
        t.deleteFiles();
        Bukkit.broadcastMessage(Main.color("&l&4Town " + t.getName() + " has been disbanded!"));
        return true;
    }

    private boolean sendInvite(Player p, String recieverName) {

        Citizen cp = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (cp.getRank() < 3) {
            Main.msg(p, "&4You do not have sufficient permissions to invite!");
            return false;
        }

        if (recieverName.equals("")) {
            cp.setInviteSentStatus("Prompted");
            Main.msg(p, Main.color("&l&eEnter Username: "));

            new BukkitRunnable() {
                public void run() {
                    if (!cp.getInviteStatus().equals("Normal")) {
                        cp.setInviteStatus("Normal");
                        Main.msg(p, Main.color("&4Invite Timed Out."));
                    }
                }
            }.runTaskLater(Main.getInstance(), 20 * 60);


        } else {
            Player r = Bukkit.getPlayer(recieverName);
            if (r == null) {
                Main.msg(p, Main.color("&4Player not found"));
                return false;
            }


            Citizen cr = main.getUUIDCitizenMap().get(r.getUniqueId());
            Town t = getTownFromCitizen(cp);

            if (getTownFromCitizen(cr) != null) {
                Main.msg(r, "&4This player is already in another town!");
                return false;
            }

            if (!cr.getInviteStatus().equalsIgnoreCase("Normal")) {
                Main.msg(r, "&4This player already has a pending invite!");
                return false;
            }

            t.invite(p, r);
            cr.pushFiles();
        }
        return true;
    }

    private boolean acceptInvite(Player p) {
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (c.getInviteStatus().equalsIgnoreCase("Normal")) {
            Main.msg(p, Main.color("&4You have no pending invites!"));
            return false;
        }

        Town tinv = null;
        for (Town t : main.getTowns()) {
            if (t.getName().equalsIgnoreCase(c.getInviteStatus())) {
                tinv = t;
                break;
            }
        }

        c.setTown(c.getInviteStatus());
        c.setRank(0); // lowest possible rank

        tinv.getCitizenList().addPlayer(p);

        Main.msg(p, Main.color("&aJoined town " + c.getInviteStatus()));

        c.setInviteStatus("Normal");
        return true;
    }

    private boolean declineInvite(Player p) {
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (!c.getInviteStatus().equalsIgnoreCase("Pending")) {
            Main.msg(p, Main.color("&4You have no pending invites!"));
            return false;
        }

        Town tinv = null;
        for (Town t : main.getTowns()) {
            if (t.getName().equalsIgnoreCase(c.getInviteStatus())) {
                tinv = t;
                break;
            }
        }

        c.setInviteStatus("Normal");
        Main.msg(p, "&aYou successfully declined the invite");
        return true;


    }

    private boolean promotePlayer(Player p, String r){
        if(r.equalsIgnoreCase("")){
            Main.msg(p, Main.color("&l&eWho do you want to promote?"));

            main.getUUIDCitizenMap().get(p.getUniqueId()).setPromoteStatus("Prompted");
            new BukkitRunnable() {
                public void run() {
                    Citizen mc = main.getUUIDCitizenMap().get(p.getUniqueId());
                    if (!mc.getPromoteStatus().equals("Normal")) {
                        mc.setPromoteStatus("Normal");
                        Main.msg(p, Main.color("&4Prompt Timed Out."));
                    }
                }
            }.runTaskLater(Main.getInstance(), 20 * 60);
        } else {
            Player reciever = Bukkit.getPlayer(r);
            Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());
            Town t = getTownFromCitizen(c);
            t.promote(p, reciever);
        }
        return true;
    }

    private boolean showLeaderboard(Player p) {

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("§e§lTown Menu")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            String itemDispName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (itemDispName.contains("Create New Town")) {
                createNewTown((Player) e.getWhoClicked(), "");
            }
            else if(itemDispName.contains("Leaderboard")) {

            }

            e.getWhoClicked().closeInventory();
        } else if (e.getView().getTitle().contains("§b§l" + main.getUUIDCitizenMap().get(e.getWhoClicked().getUniqueId()).getTown() + " Menu")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            String itemDispName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (itemDispName.contains("Leave Town")) {
                //LeaveTown((Player) e.getWhoClicked());
                sendAreYouSureInv((Player) e.getWhoClicked(), "Leave Town?");
            } else if (itemDispName.contains("Delete Town")) {
                if (deleteTownPre((Player) e.getWhoClicked())) {
                    sendAreYouSureInv((Player) e.getWhoClicked(), "Disband Town?");
                }
            } else if (itemDispName.contains("Invite")) {
                sendInvite((Player) e.getWhoClicked(), "");
            } else if (itemDispName.contains("Promote")) {
                promotePlayer((Player) e.getWhoClicked(), "");
            }

            e.getWhoClicked().closeInventory();
        } else if (e.getView().getTitle().equals("§e§lDisband Town?")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            String itemDispName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (itemDispName.equals("§a§lYes")) {
                deleteTown((Player) e.getWhoClicked());
            } else if (itemDispName.equals("§c§lNo")) {
                return;
            }
            e.getWhoClicked().closeInventory();
        } else if (e.getView().getTitle().contains("§e§lLeave Town?")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            String itemDispName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (itemDispName.equals("§a§lYes")) {
                leaveTown((Player) e.getWhoClicked());
            } else if (itemDispName.equals("§c§lNo")) {
                e.getWhoClicked().closeInventory();
                return;
            }
            e.getWhoClicked().closeInventory();
        }
    }
}