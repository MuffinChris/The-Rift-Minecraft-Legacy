package com.java.rpg.holograms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.java.Main;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Hologram implements Listener {

    private Main main = Main.getInstance();

    private EntityArmorStand stand;
    private String text;
    private Entity entity;
    private int lifetime;
    private HologramType type;

    //private int id;

    private List<Player> targets;

    public EntityArmorStand getStand() {
        return stand;
    }

    public Hologram() {

    }

    @EventHandler
    public void manipulate(PlayerArmorStandManipulateEvent e)
    {
        if(!e.getRightClicked().isVisible())
        {
            e.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void damageStand(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand && !((ArmorStand) e.getEntity()).isVisible()) {
            e.setCancelled(true);
        }
    }

    public enum HologramType {
        DAMAGE, HOLOGRAM, EXP, STATUS
    }

    public void resetLifetime() {
        lifetime = 0;
    }

    public Hologram(Entity e, Location loc, String text, HologramType type, List<Player> targets) {
        this.targets = targets;
        if (text != null) {
            this.text = text;
        } else {
            this.text = "";
        }
        text = this.text;
        this.type = type;
        entity = e;
        lifetime = 0;
        Location initLoc;
        if (type == HologramType.DAMAGE || type == HologramType.EXP || type == HologramType.STATUS) {
            if (type == HologramType.EXP) {
                double neg = Math.random();
                double neg2 = Math.random();
                double mod = 0.1;
                double mod2 = 0.1;
                if (neg < 0.5) {
                    neg = -0.3;
                    mod*=-1;
                } else {
                    neg = 0.3;
                }
                if (neg2 < 0.5) {
                    neg2 = -0.3;
                    mod2*=-1;
                } else {
                    neg2 = 0.3;
                }
                initLoc = loc.clone().add(new Vector(Math.random() * neg + mod, e.getHeight()/2.0, Math.random() * neg2 + mod2));
            } else {
                double neg = Math.random();
                double neg2 = Math.random();
                double mod = 0.25;
                double mod2 = 0.25;
                if (neg < 0.5) {
                    neg = -0.2;
                    mod*=-1;
                } else {
                    neg = 0.2;
                }
                if (neg2 < 0.5) {
                    neg2 = -0.2;
                    mod2*=-1;
                } else {
                    neg2 = 0.2;
                }
                if (type == HologramType.STATUS) {
                    initLoc = loc.clone().add(new Vector(Math.random() * neg + mod, e.getHeight() + 0.1, Math.random() * neg2 + mod2));
                } else {
                    initLoc = loc.clone().add(new Vector(Math.random() * neg + mod, e.getHeight() + (Math.random() * 0.5) - 0.5, Math.random() * neg2 + mod2));
                }
            }
        } else {
            initLoc = loc;
        }

        loc = initLoc;


        stand = new EntityArmorStand(((CraftWorld)e.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        PacketContainer packet2 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet2.getIntegers().write(0, stand.getId());
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));

        packet2.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        for (Player p : targets) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            try {
                main.getProtocolManager().sendServerPacket(p, packet2);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        teleport(loc);

        PacketContainer packet3 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet3.getIntegers().write(0, stand.getId());
        dataWatcher = new WrappedDataWatcher();
        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(Main.color(text))[0].getHandle());
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);

        packet3.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        for (Player p : targets) {
            try {
                main.getProtocolManager().sendServerPacket(p, packet3);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        main.getHolos().add(this);

    }

    public void updateViewership() {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        PacketContainer packet2 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet2.getIntegers().write(0, stand.getId());
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(14, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10));

        packet2.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        for (Player p : targets) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            try {
                main.getProtocolManager().sendServerPacket(p, packet2);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        center();

        PacketContainer packet3 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet3.getIntegers().write(0, stand.getId());
        dataWatcher = new WrappedDataWatcher();
        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(Main.color(text))[0].getHandle());
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);

        packet3.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        for (Player p : targets) {
            try {
                main.getProtocolManager().sendServerPacket(p, packet3);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void teleport(Location loc) {

        stand.setPosition(loc.getX(), loc.getY(), loc.getZ());
        PacketContainer packet2 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet2.getIntegers().write(0, stand.getId());
        packet2.getDoubles().write(0, loc.getX());
        packet2.getDoubles().write(1, loc.getY());
        packet2.getDoubles().write(2, loc.getZ());
        //stand.setPosition(loc.getX(), loc.getY(), loc.getZ());

        //PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(stand);
        for (Player p : targets) {
            //((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            try {
                main.getProtocolManager().sendServerPacket(p, packet2);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean shouldRemove() {
        if (!(entity instanceof LivingEntity) || entity.isDead()) {
            return true;
        }
        return false;
    }

    public Entity getEntity() {
        return entity;
    }

    public void center() {
        if (entity != null && !entity.isDead()) {
            Location clone = entity.getLocation().add(new Vector(0, entity.getHeight() - 0.2, 0));
            teleport(clone);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String s) {
        text = s;
        /*ChatComponentText chatComponentText = new ChatComponentText(Main.color(text));
        stand.setCustomName(chatComponentText);*/
        PacketContainer packet2 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet2.getIntegers().write(0, stand.getId());


        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();

        Optional<?> opt = Optional
                .of(WrappedChatComponent
                        .fromChatMessage(Main.color(text))[0].getHandle());
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

        packet2.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

        for (Player p : targets) {
            try {
                main.getProtocolManager().sendServerPacket(p, packet2);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getLifetime() {
        return lifetime;
    }

    public HologramType getType() {
        return type;
    }

    public void rise() {
        // say sike right now
    }

    public void incrementLifetime() {
        lifetime++;
    }

    public List<Player> getTargets() {
        return targets;
    }

    public void sendRemovePacket(Player p) {
        PacketContainer packet2 = main.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet2.getIntegerArrays().write(0, new int[]{stand.getId()});
        try {
            main.getProtocolManager().sendServerPacket(p, packet2);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void remove(Player p) {
        if (targets.contains(p)) {
            targets.remove(p);
            sendRemovePacket(p);
            //PacketPlayOutEntityDestroy pack = new PacketPlayOutEntityDestroy(stand.getId());
            //((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
        }
    }

    public void destroy() {
        for (Player p : targets) {
            sendRemovePacket(p);
            //PacketPlayOutEntityDestroy pack = new PacketPlayOutEntityDestroy(stand.getId());
            //((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
        }
        text = null;
        entity = null;
        type = null;
        stand = null;
        targets = null;
        lifetime = 0;
    }

    public Location getLocation() {
        return new Location(entity.getWorld(), stand.getPositionVector().x, stand.getPositionVector().y, stand.getPositionVector().z);
    }

}
