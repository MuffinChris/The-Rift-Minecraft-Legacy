package com.java.towns;

import com.java.Main;
import com.java.rpg.classes.RPGPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.EventPriority;
import org.w3c.dom.Text;

import java.io.File;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TownCoreCommand implements CommandExecutor, Listener {

    final int TOWNS_PER_PAGE = 5;
    final int MEMBERS_PER_PAGE = 40;

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
            if (main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equalsIgnoreCase(Citizen.defaultTownName))
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
                return sendInvite(p, "");
            } else if (args[0].equalsIgnoreCase("accept")) {
                return acceptInvite(p);
            } else if (args[0].equalsIgnoreCase("decline")) {
                return declineInvite(p);
            } else if (args[0].equalsIgnoreCase("leaderboard")) {
                return showLeaderboard(p, 0);
            } else if(args[0].equalsIgnoreCase("list")) {
                return showTownList(p, 0);
            } else if (args[0].equalsIgnoreCase("kick")) {
                return kickPlayer(p, "");
            } else if (args[0].equalsIgnoreCase("promote")) {
                return promotePlayer(p, "");
            } else if (args[0].equalsIgnoreCase("demote")) {
                return demotePlayer(p, "");
            }

            if (args[0].equalsIgnoreCase("chat")) {
                return toggleTownChat(p);
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                return sendInvite(p, args[1]); // args[1] is username of player to inv
            } else if (args[0].equalsIgnoreCase("create")) {
                return createNewTown(p, args[1]); // args[1] is name of town
            } else if (args[0].equalsIgnoreCase("kick")) {
                return kickPlayer(p, args[1]);
            } else if (args[0].equalsIgnoreCase("promote")) {
                return promotePlayer(p, args[1]);
            } else if (args[0].equalsIgnoreCase("demote")) {
                return demotePlayer(p, args[1]);
            }

            if (args[0].equalsIgnoreCase("show")) {
                return sendTownMemberInv(p, args[1], args[1] + " Members", 0);
            }

            if (args[0].equalsIgnoreCase("leaderboard")) {
                return showLeaderboard(p, Integer.parseInt(args[1]));
            }

            if(args[0].equalsIgnoreCase("list")) {
                return showTownList(p, Integer.parseInt(args[1]));
            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("search")) {
                return searchTown(p, args[1], Integer.parseInt(args[2]));
            }
        }

        Main.msg(p, Main.color("&cUnknown command! Use /town to see menu"));
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

    private ItemStack getSearchItemStack() {
        ItemStack sp = new ItemStack(Material.COMPASS);

        ItemMeta spMeta = sp.getItemMeta();
        spMeta.setDisplayName(Main.color("&dSearch"));
        spMeta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&fSearch for a town by name!"));
            }
        });
        sp.setItemMeta(spMeta);

        return sp;
    }

    private void sendTownInv(Player p) {
        String townName = main.getUUIDCitizenMap().get(p.getUniqueId()).getTown();
        Inventory menu = Bukkit.createInventory(null, 36, Main.color("&b&l" + townName + " Menu"));

        // delete town
        menu.setItem(10, getRemoveTownItemStack());
        // leave town
        menu.setItem(11, getLeaveTownItemStack());

        // town leaderboard
        menu.setItem(22, getLeaderboardItemStack());

        // town search
        menu.setItem(23, getSearchItemStack());

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

    private void sendAreYouSureInv(Player p, String s) {
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

    private void sendTownlessInv(Player p) {
        Inventory menu = Bukkit.createInventory(null, 27, Main.color("&e&lTown Menu"));

        // town create
        menu.setItem(10, getNewTownItemStack());

        // town leaderboard
        menu.setItem(13, getLeaderboardItemStack());

        // town search
        menu.setItem(14, getSearchItemStack());

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

    private boolean sendTownMemberInv(Player p, String townName, String invName, int page) {
        ItemStack prev = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack next = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName(Main.color("&c&lGo to Page " + (page - 1)));

        ItemMeta nextMeta = prev.getItemMeta();
        nextMeta.setDisplayName(Main.color("&a&lGo to Page " + (page + 1)));

        prev.setItemMeta(prevMeta);
        next.setItemMeta(nextMeta);

        if (!main.getFullTownList().contains(townName)) {
            Main.msg(p, Main.color("&cTown not found!"));
            return false;
        }

        Town t = new Town(townName);

        Inventory inv = Bukkit.createInventory(null, 54, Main.color("&6&l" + invName + " &l(Page " + (page + 1) + ")"));

        ItemStack dt = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta dataMeta = dt.getItemMeta();
        dataMeta.setDisplayName(townName);
        dt.setItemMeta(dataMeta);

        inv.setItem(49, dt);

        if(page - 1 >= 0) {
            for (int i = 45; i <= 48; i++) {
                inv.setItem(i, prev);
            }
        }

        if(page + 1 < Math.ceil((double)t.getSize() / MEMBERS_PER_PAGE)) {
            for (int i = 50; i <= 53; i++) {
                inv.setItem(i, next);
            }
        }



        int stind = page * MEMBERS_PER_PAGE;
        if (stind > t.getSize() || stind < 0) {
            Main.msg(p, Main.color("&cInvalid page number!"));
            return false;
        }

        for (int i = 0; i < MEMBERS_PER_PAGE; i++) {
            inv.setItem(i, getPlayerHead(t.getCitizenList().get(stind + i), t));
        }

        p.openInventory(inv);
        return true;
    }

    private ItemStack getPlayerHead(UUID puuid, Town t) {

        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) is.getItemMeta();

        OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(puuid);

        meta.setOwningPlayer(op);
        meta.setDisplayName(Main.color("&f&eUsername: &f" + op.getName()));

        File pFile = new File("plugins/Rift/data/towns/" + puuid + ".yml");
        FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);

        String rankName = t.getRanks().get(pData.getInt("Rank"));

        meta.setLore(new ArrayList<String>() {
            {
                add(Main.color("&6Rank: " + rankName));
            }
        });

        is.setItemMeta(meta);

        return is;

    }

    public boolean createNewTown(Player p, String townName) {

        if (townName.equalsIgnoreCase("")) {
            if (!main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equalsIgnoreCase(Citizen.defaultTownName)) {
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
                    Main.msg(p, Main.color("&cTown name already taken!"));
                    return false;
                }
            }

            if (!townName.matches("[a-zA-Z ]+")) {
                Main.msg(p, Main.color("&cTown names can only contain characters (A-Z) and spaces"));
                return false;
            }

            if (townName.equalsIgnoreCase(Citizen.defaultTownName)) {
                Main.msg(p, Main.color("&cThat is a protected town name!"));
                return false;
            }

            Town nt = new Town(p, townName);
            main.getTowns().add(nt);
            nt.pushFiles();

            List<String> fullTowns = main.getFullTownList();
            fullTowns.add(townName);
            main.setFullTownList(fullTowns);

            Main.msg(p, Main.color("&l&aSuccessfully created town: &f" + townName));
        }
        return true;

    }

    public boolean leaveTown(Player p) {

        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (c.getTown().equalsIgnoreCase(Citizen.defaultTownName)) {
            Main.msg(p, "&cYou aren't in any town!");
            return false;
        }

        Town t = getTownFromCitizen(c);

        //check if player is leader and town has other players
        //if(c.getRank() == t.maxRank) { main.msg(p, "owner"); }
        //main.msg(p, "" + t.getSize());
        if(c.getRank() == t.maxRank && t.getSize() > 1) {
            sendTownMemberInv(p, c.getTown(), "Appoint new " + t.getRanks().get(c.getRank()), 0);
            return true;
        }

        t.leave(p);
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

    public boolean deleteTown(Player p) {
        Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());

        Town t = getTownFromCitizen(c);
        if (c.getRank() != t.getRanks().size() - 1) { // make sure user is the highest possible rank
            Main.msg(p, "&4You don't have permission to do this!");
            return false;
        }
        Main.msg(p, "&aTown successfully disbanded");
        for (UUID pt : t.getCitizenList()) {
            Citizen ct = main.getUUIDCitizenMap().get(pt);
            ct.setRank(-1);
            ct.setTown(Citizen.defaultTownName);

            ct.pushFiles();
        }

        main.getTowns().remove(t);
        List<String> fullTowns = main.getFullTownList();
        fullTowns.remove(t.getName());
        main.setFullTownList(fullTowns);
        t.deleteFiles();
        Bukkit.broadcastMessage(Main.color("&l&4Town " + t.getName() + " has been disbanded!"));
        return true;
    }

    public boolean sendInvite(Player p, String receiverName) {

        Citizen cp = main.getUUIDCitizenMap().get(p.getUniqueId());

        if (cp.getRank() < 3) {
            Main.msg(p, "&4You do not have sufficient permissions to invite!");
            return false;
        }

        if (receiverName.equals("")) {
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
            Player r = Bukkit.getPlayer(receiverName);
            if (r == null) {
                Main.msg(p, Main.color("&4Player not found"));
                return false;
            }


            Citizen cr = main.getUUIDCitizenMap().get(r.getUniqueId());
            Town t = getTownFromCitizen(cp);

            if (getTownFromCitizen(cr) != null) {
                Main.msg(p, "&4This player is already in another town!");
                return false;
            }

            if (!cr.getInviteStatus().equalsIgnoreCase("Normal")) {
                Main.msg(p, "&4This player already has a pending invite!");
                return false;
            }

            t.invite(p, r);
            cr.pushFiles();
        }
        return true;
    }

    private boolean acceptInvite(Player receiver) {
        Citizen c = main.getUUIDCitizenMap().get(receiver.getUniqueId());

        if (c.getInviteStatus().equalsIgnoreCase("Normal")) {
            Main.msg(receiver, Main.color("&aYou have no pending invites!"));
            return false;
        }

        Town townInvite = null;
        for (Town t : main.getTowns()) {
            if (t.getName().equalsIgnoreCase(c.getInviteStatus())) {
                townInvite = t;
                break;
            }
        }

        if (townInvite == null) {
            Main.msg(receiver, Main.color("&aThis town no longer exists!"));
            return false;
        }

        c.setTown(c.getInviteStatus());
        c.setRank(0); // lowest possible rank

        for (UUID p : townInvite.getCitizenList()) {
            if (Bukkit.getPlayer(p) != null) {
                Main.msg(Bukkit.getPlayer(p), Main.color("&a" + receiver.getDisplayName() + " has accepted their invite and joined the town!"));
            }
        }

        townInvite.getCitizenList().add(receiver.getUniqueId());
        c.pushFiles();
        townInvite.pushFiles();
        Main.msg(receiver, Main.color("&aJoined town " + c.getInviteStatus()));

        c.setInviteStatus("Normal");
        return true;
    }

    private boolean declineInvite(Player receiver) {
        Citizen c = main.getUUIDCitizenMap().get(receiver.getUniqueId());

        if (!c.getInviteStatus().equalsIgnoreCase("Pending")) {
            Main.msg(receiver, Main.color("&4You have no pending invites!"));
            return false;
        }

        c.setInviteStatus("Normal");
        Main.msg(receiver, Main.color("&aYou successfully declined the invite)"));
        return true;
    }

    public boolean promotePlayer(Player p, String r) {
        if (r.equalsIgnoreCase("")) {
            sendTownMemberInv(p, main.getUUIDCitizenMap().get(p.getUniqueId()).getTown(), "Promote", 0);
        } else {
            OfflinePlayer receiver = Bukkit.getOfflinePlayer(r);
            if (!receiver.hasPlayedBefore()) {
                Main.msg(p, "Could not find this player");
                return false;
            }
            Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());
            Town t = getTownFromCitizen(c);
            if (receiver.isOnline()) {
                t.promote(p, receiver.getPlayer());
                return true;
            } else {
                t.promote(p, receiver);
            }
        }
        return true;
    }

    public boolean demotePlayer(Player p, String r) {
        if (r.equalsIgnoreCase("")) {
            sendTownMemberInv(p, main.getUUIDCitizenMap().get(p.getUniqueId()).getTown(), "Demote", 0);
        } else {
            OfflinePlayer receiver = Bukkit.getOfflinePlayer(r);
            if (!receiver.hasPlayedBefore()) {
                Main.msg(p, "Could not find this player");
                return false;
            }
            Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());
            Town t = getTownFromCitizen(c);
            if (receiver.isOnline()) {
                t.demote(p, receiver.getPlayer());
                return true;
            } else {
                t.demote(p, receiver);
            }
        }
        return true;
    }

    public boolean kickPlayer(Player p, String r) {
        if (r.equalsIgnoreCase("")) {
            sendTownMemberInv(p, main.getUUIDCitizenMap().get(p.getUniqueId()).getTown(), "Kick", 0);
        } else {
            OfflinePlayer receiver = Bukkit.getOfflinePlayer(r);
            if (!receiver.hasPlayedBefore()) {
                Main.msg(p, "Could not find this player");
                return false;
            }
            Citizen c = main.getUUIDCitizenMap().get(p.getUniqueId());
            Town t = getTownFromCitizen(c);
            if (receiver.isOnline()) {
                t.kick(p, receiver.getPlayer());
            } else {
                t.kick(p, receiver);
            }
            return true;
        }
        return true;
    }

    private boolean showTownList(Player p, int x) {

        sendTownListPage(p, x);

        int npage = (int) ((x + 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));
        int ppage = (int) ((x - 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));

        TextComponent nextText = new TextComponent("[Next Page]");
        TextComponent previousText = new TextComponent("[Previous Page]");
        nextText.setBold(true);
        previousText.setBold(true);
        nextText.setColor(ChatColor.GREEN);
        previousText.setColor(ChatColor.RED);
        nextText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town list " + npage));
        previousText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town list " + ppage));
        // TODO: maybe do some hover events to make it more obvious what's going on
        TextComponent invText2 = new TextComponent();

        invText2.addExtra(previousText);
        invText2.addExtra(" or ");
        invText2.addExtra(nextText);
        p.sendMessage(invText2);

        return true;
    }

    private void sendTownListPage(Player p, int x) {
        Main.sendCenteredMessage(p, Main.color("&a&m------------------\n"));
        Main.sendCenteredMessage(p, Main.color("&b&lTOWN LIST"));
        List<String> fullTownNames = main.getFullTownList();

        ArrayList<Town> fullTowns = new ArrayList<Town>();

        for (int i = 0; i < fullTownNames.size(); i++) {
            fullTowns.add(new Town(fullTownNames.get(i)));
        }

        fullTowns.sort((o1, o2) -> {
            // this is kind of a weird metric (lvl^2 * citizenCount)
            int o1score = -o1.getSize();
            int o2score = -o2.getSize();

            return Integer.compare(o1score, o2score);
        });


        for (int i = x * TOWNS_PER_PAGE; i < (x + 1) * TOWNS_PER_PAGE; i++) {
            if (i >= fullTowns.size()) break;

            TextComponent fullText = new TextComponent();
            TextComponent towntc = new TextComponent();

            fullText.setText((i + 1) + "   ");
            fullText.setColor(ChatColor.GOLD);
            fullText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town show " + fullTowns.get(i).getName()));


            towntc.setText(fullTowns.get(i).getName());
            towntc.setColor(ChatColor.BOLD);

            TextComponent lvl = new TextComponent();
            lvl.setText("Level: " + fullTowns.get(i).getLevel());
            lvl.setColor(ChatColor.BLUE);

            TextComponent sz = new TextComponent();
            sz.setText("\nSize: " + fullTowns.get(i).getSize());
            sz.setColor(ChatColor.BLUE);

            TextComponent click = new TextComponent();
            click.setText("\n\nClick to see full list of members!");
            click.setColor(ChatColor.GREEN);
            click.setBold(true);

            towntc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append(lvl).append(sz).append(click).create()));
            fullText.addExtra(towntc);

            Main.sendCenteredMessage(p, fullText);

        }
        Main.sendCenteredMessage(p, Main.color("&a&m------------------"));

    }

    private boolean showLeaderboard(Player p, int x) {

        sendLeaderboardPage(p, x);

        int npage = (int) ((x + 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));
        int ppage = (int) ((x - 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));

        TextComponent nextText = new TextComponent("[Next Page]");
        TextComponent previousText = new TextComponent("[Previous Page]");
        nextText.setBold(true);
        previousText.setBold(true);
        nextText.setColor(ChatColor.GREEN);
        previousText.setColor(ChatColor.RED);
        nextText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town leaderboard " + npage));
        previousText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town leaderboard " + ppage));
        // TODO: maybe do some hover events to make it more obvious what's going on
        TextComponent invText2 = new TextComponent();

        invText2.addExtra(previousText);
        invText2.addExtra(" or ");
        invText2.addExtra(nextText);
        p.sendMessage(invText2);

        return true;
    }

    private void sendLeaderboardPage(Player p, int x) {
        Main.sendCenteredMessage(p, Main.color("&a&m------------------\n"));
        Main.sendCenteredMessage(p, Main.color("&b&lLEADERBOARD"));
        List<String> fullTownNames = main.getFullTownList();

        ArrayList<Town> fullTowns = new ArrayList<Town>();

        for (int i = 0; i < fullTownNames.size(); i++) {
            fullTowns.add(new Town(fullTownNames.get(i)));
        }

        fullTowns.sort((o1, o2) -> {
            // this is kind of a weird metric (lvl^2 * citizenCount)
            int o1score = -(int) Math.pow((o1.getLevel() + 1), 2) * o1.getSize();
            int o2score = -(int) Math.pow((o2.getLevel() + 1), 2) * o2.getSize();

            return Integer.compare(o1score, o2score);
        });


        for (int i = x * TOWNS_PER_PAGE; i < (x + 1) * TOWNS_PER_PAGE; i++) {
            if (i >= fullTowns.size()) break;

            TextComponent fullText = new TextComponent();
            TextComponent towntc = new TextComponent();

            fullText.setText((i + 1) + "   ");
            fullText.setColor(ChatColor.GOLD);
            fullText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town show " + fullTowns.get(i).getName()));


            towntc.setText(fullTowns.get(i).getName());
            towntc.setColor(ChatColor.BOLD);

            TextComponent lvl = new TextComponent();
            lvl.setText("Level: " + fullTowns.get(i).getLevel());
            lvl.setColor(ChatColor.BLUE);

            TextComponent sz = new TextComponent();
            sz.setText("\nSize: " + fullTowns.get(i).getSize());
            sz.setColor(ChatColor.BLUE);

            TextComponent click = new TextComponent();
            click.setText("\n\nClick to see full list of members!");
            click.setColor(ChatColor.GREEN);
            click.setBold(true);

            towntc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append(lvl).append(sz).append(click).create()));
            fullText.addExtra(towntc);

            Main.sendCenteredMessage(p, fullText);

        }
        Main.sendCenteredMessage(p, Main.color("&a&m------------------"));

    }

    public boolean searchTown(Player p, String query, int pageNum) {
        if (pageNum < 0) {
            Main.msg(p, Main.color("&cInvalid page number!"));
            return false;
        }

        if (query.equals("")) {
            Main.msg(p, Main.color("&l&eEnter Search Query: "));

            main.getUUIDCitizenMap().get(p.getUniqueId()).setSearchStatus("Prompted");
            new BukkitRunnable() {
                public void run() {
                    Citizen mc = main.getUUIDCitizenMap().get(p.getUniqueId());
                    if (!mc.getSearchStatus().equals("Normal")) {
                        mc.setSearchStatus("Normal");
                        Main.msg(p, Main.color("&4Prompt Timed Out."));
                    }
                }
            }.runTaskLater(Main.getInstance(), 20 * 60);
        } else {
            if (!query.matches("[a-zA-Z ]+")) {
                Main.msg(p, Main.color("&cSearches should only be letters (A-Z) and spaces!"));
                return false;
            }
            return sendSearchResults(p, query, pageNum);
        }
        return true;
    }

    private boolean sendSearchResults(Player p, String query, int pageNum) {
        sendSearchPage(p, query, pageNum);

        int npage = (int) ((pageNum + 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));
        int ppage = (int) ((pageNum - 1) % Math.ceil((double) (main.getFullTownList().size() / TOWNS_PER_PAGE)));

        TextComponent nextText = new TextComponent("[Next Page]");
        TextComponent previousText = new TextComponent("[Previous Page]");
        nextText.setBold(true);
        previousText.setBold(true);
        nextText.setColor(ChatColor.GREEN);
        previousText.setColor(ChatColor.RED);
        nextText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town search " + query + " " + npage));
        previousText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town leaderboard " + query + " " + ppage));
        // TODO: maybe do some hover events to make it more obvious what's going on
        TextComponent invText2 = new TextComponent();

        invText2.addExtra(previousText);
        invText2.addExtra(" or ");
        invText2.addExtra(nextText);
        p.sendMessage(invText2);

        return true;
    }

    private void sendSearchPage(Player p, String query, int pageNum) {
        Main.sendCenteredMessage(p, Main.color("&a&m------------------\n"));
        Main.sendCenteredMessage(p, Main.color("&b&lSearch Results (Query: " + query + ")"));
        List<String> fullTownNames = main.getFullTownList();

        ArrayList<Town> fullTowns = new ArrayList<Town>();

        for (int i = 0; i < fullTownNames.size(); i++) {
            fullTowns.add(new Town(fullTownNames.get(i)));
        }

        fullTowns.sort((o1, o2) -> {
            // this is kind of a weird metric (lvl^2 * citizenCount)
            int o1score = pogdistance(o1.getName(), query);
            int o2score = pogdistance(o2.getName(), query);

            return Integer.compare(o1score, o2score);
        });


        for (int i = pageNum * TOWNS_PER_PAGE; i < (pageNum + 1) * TOWNS_PER_PAGE; i++) {
            if (i >= fullTowns.size()) break;

            TextComponent fullText = new TextComponent();
            TextComponent towntc = new TextComponent();

            fullText.setText((i + 1) + "   ");
            fullText.setColor(ChatColor.GOLD);
            fullText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/town show " + fullTowns.get(i).getName()));


            towntc.setText(fullTowns.get(i).getName());
            towntc.setColor(ChatColor.BOLD);

            TextComponent lvl = new TextComponent();
            lvl.setText("Level: " + fullTowns.get(i).getLevel());
            lvl.setColor(ChatColor.BLUE);

            TextComponent sz = new TextComponent();
            sz.setText("\nSize: " + fullTowns.get(i).getSize());
            sz.setColor(ChatColor.BLUE);

            TextComponent click = new TextComponent();
            click.setText("\n\nClick to see full list of members!");
            click.setColor(ChatColor.GREEN);
            click.setBold(true);

            towntc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder().append(lvl).append(sz).append(click).create()));
            fullText.addExtra(towntc);

            Main.sendCenteredMessage(p, fullText);

        }
        Main.sendCenteredMessage(p, Main.color("&a&m------------------"));
    }

    private boolean toggleTownChat(Player p) {
        if (main.getUUIDCitizenMap().get(p.getUniqueId()).getTown().equalsIgnoreCase(Citizen.defaultTownName)) {
            Main.msg(p, "&cYou must be in a town to use this command");
            main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Global);
            return false;
        }
        if (main.getRP(p).getChatChannel() == RPGPlayer.ChatChannel.Town) {
            Main.msg(p, "&cYou have disabled town chat.");
            main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Global);
        } else {
            Main.msg(p, "&aYou have enabled town chat.");
            main.getRP(p).setChatChannel(RPGPlayer.ChatChannel.Town);
        }
        return true;
    }

    private int pogdistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int c = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + c, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    private int getItemStackPage(ItemStack s) {
        String nm = s.getItemMeta().getDisplayName();
        String[] a = nm.split(" ");
        return Integer.parseInt(a[a.length - 1]);
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
            } else if (itemDispName.contains("Leaderboard")) {
                showLeaderboard((Player) e.getWhoClicked(), 0);
            } else if (itemDispName.contains("Search")) {
                searchTown((Player) e.getWhoClicked(), "", 0);
            }
            e.getWhoClicked().closeInventory();


        } else if (e.getView().getTitle().contains("§b§l" + main.getUUIDCitizenMap().get(e.getWhoClicked().getUniqueId()).getTown() + " Menu")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            String itemDispName = e.getCurrentItem().getItemMeta().getDisplayName();
            if (itemDispName.contains("Leave Town")) {
                sendAreYouSureInv((Player) e.getWhoClicked(), "Leave Town?");
            } else if (itemDispName.contains("Delete Town")) {
                if (deleteTownPre((Player) e.getWhoClicked())) {
                    sendAreYouSureInv((Player) e.getWhoClicked(), "Disband Town?");
                }
            } else if (itemDispName.contains("Invite")) {
                sendInvite((Player) e.getWhoClicked(), "");
                e.getWhoClicked().closeInventory();
            } else if (itemDispName.contains("Promote")) {
                promotePlayer((Player) e.getWhoClicked(), "");
                //e.getWhoClicked().closeInventory();
            } else if (itemDispName.contains("Demote")) {
                demotePlayer((Player) e.getWhoClicked(), "");
                //e.getWhoClicked().closeInventory();
            } else if (itemDispName.contains("Kick")) {
                kickPlayer((Player) e.getWhoClicked(), "");
            } else if (itemDispName.contains("Leaderboard")) {
                showLeaderboard((Player) e.getWhoClicked(), 0);
                e.getWhoClicked().closeInventory();
            } else if (itemDispName.contains(("Search"))) {
                searchTown((Player) e.getWhoClicked(), "", 0);
                e.getWhoClicked().closeInventory();
            }


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

        } else if (e.getView().getTitle().contains("§6§lPromote")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player p = (Player) e.getWhoClicked();
                Town t = getTownFromCitizen(main.getUUIDCitizenMap().get(p.getUniqueId()));
                SkullMeta sm = (SkullMeta) e.getCurrentItem().getItemMeta();
                sm.getOwningPlayer();
                if (!sm.getOwningPlayer().isOnline()) {
                    t.promote(p, sm.getOwningPlayer());
                } else {
                    t.promote(p, sm.getOwningPlayer().getPlayer());
                }
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                sendTownMemberInv((Player) e.getWhoClicked(), e.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), "Promote", getItemStackPage(e.getCurrentItem()));
            }


        } else if (e.getView().getTitle().contains("§6§lDemote")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player p = (Player) e.getWhoClicked();
                Town t = getTownFromCitizen(main.getUUIDCitizenMap().get(p.getUniqueId()));
                SkullMeta sm = (SkullMeta) e.getCurrentItem().getItemMeta();
                sm.getOwningPlayer();
                if (!sm.getOwningPlayer().isOnline()) {
                    t.demote(p, sm.getOwningPlayer());
                } else {
                    t.demote(p, sm.getOwningPlayer().getPlayer());
                }
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                sendTownMemberInv((Player) e.getWhoClicked(), e.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), "Demote", getItemStackPage(e.getCurrentItem()));
            }


        } else if (e.getView().getTitle().contains("§6§lKick")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player p = (Player) e.getWhoClicked();
                Town t = getTownFromCitizen(main.getUUIDCitizenMap().get(p.getUniqueId()));
                SkullMeta sm = (SkullMeta) e.getCurrentItem().getItemMeta();
                sm.getOwningPlayer();
                if (!sm.getOwningPlayer().isOnline()) {
                    t.kick(p, sm.getOwningPlayer());
                    return;
                } else {
                    t.kick(p, sm.getOwningPlayer().getPlayer());
                }
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                sendTownMemberInv((Player) e.getWhoClicked(), e.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), "Kick", getItemStackPage(e.getCurrentItem()));
            }


        } else if (e.getView().getTitle().contains("§6§lAppoint new")) {
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            e.setCancelled(true);

            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player p = (Player) e.getWhoClicked();
                Town t = getTownFromCitizen(main.getUUIDCitizenMap().get(p.getUniqueId()));
                SkullMeta sm = (SkullMeta) e.getCurrentItem().getItemMeta();
                if(!sm.getOwningPlayer().isOnline()){
                    OfflineCitizen newLeader = new OfflineCitizen(sm.getOwningPlayer());
                    newLeader.setRank(t.maxRank);
                } else {
                    main.getUUIDCitizenMap().get(sm.getOwningPlayer().getUniqueId()).setRank(t.maxRank);
                    Main.msg(sm.getOwningPlayer().getPlayer(), Main.color("&c&lYou are the new " + t.getRankName(t.maxRank) + "!"));
                }
                t.leave(p);
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                sendTownMemberInv((Player) e.getWhoClicked(), e.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), "Kick", getItemStackPage(e.getCurrentItem()));
            }
        } else if (e.getView().getTitle().startsWith("§6§l") && e.getView().getTitle().contains("Members")) { // town member inventory
            if (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                String invName = e.getClickedInventory().getItem(49).getItemMeta().getDisplayName() + " Members";
                sendTownMemberInv((Player) e.getWhoClicked(), e.getClickedInventory().getItem(49).getItemMeta().getDisplayName(), invName, getItemStackPage(e.getCurrentItem()));
            }
            e.setCancelled(true);

        }
    }
}