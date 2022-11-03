package me.justahuman.bendingvisualizer.protocollib;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.justahuman.bendingvisualizer.BendingVisualizer;
import me.justahuman.bendingvisualizer.protocollib.wrappers.WrapperPlayServerEntityDestroy;
import me.justahuman.bendingvisualizer.protocollib.wrappers.WrapperPlayServerEntityHeadRotation;
import me.justahuman.bendingvisualizer.protocollib.wrappers.WrapperPlayServerNamedEntitySpawn;
import me.justahuman.bendingvisualizer.protocollib.wrappers.WrapperPlayServerPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FakeNPCEntity implements Listener {
    private final Player player;
    private final FakeNPCData npcData;
    private final UUID uuid;

    private int entityID = -1;

    private Location location;

    private final WrapperPlayServerPlayerInfo PLAYER_JOIN = new WrapperPlayServerPlayerInfo();
    private final WrapperPlayServerPlayerInfo PLAYER_LEAVE = new WrapperPlayServerPlayerInfo();
    private final WrapperPlayServerEntityDestroy PLAYER_DESTROY = new WrapperPlayServerEntityDestroy();
    private final WrapperPlayServerNamedEntitySpawn PLAYER_TELEPORT = new WrapperPlayServerNamedEntitySpawn();
    private final WrapperPlayServerEntityHeadRotation HEAD_ROTATION = new WrapperPlayServerEntityHeadRotation();

    /**
     *
     * @param player The player to display the NPC for
     * @param npcData The NPC's name, skin, etc.
     */
    public FakeNPCEntity(Player player, FakeNPCData npcData) {
        this.player = player;
        this.npcData = npcData;

        this.uuid = UUID.randomUUID();

        WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(), npcData.getDisplayName());

        List<PlayerInfoData> data = Collections.singletonList(new PlayerInfoData(
                profile,
                1,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText(npcData.getDisplayName())
        ));

        PLAYER_LEAVE.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        PLAYER_LEAVE.setData(data);

        PLAYER_JOIN.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        PLAYER_JOIN.setData(data);
    }

    public FakeNPCData getNpcData() {
        return npcData;
    }

    public void spawn(Location location, Location eyeLocation) {
        if (!isSpawned()) {
            PLAYER_JOIN.sendPacket(player);
            entityID = (int)(Math.random() * Integer.MAX_VALUE);
            PLAYER_DESTROY.setEntityIds(new int[]{entityID});
            HEAD_ROTATION.setEntityID(entityID);
            Bukkit.getPluginManager().registerEvents(this, BendingVisualizer.getPlugin());
        }

        teleport(location, eyeLocation);
    }

    private void teleport(Location location, Location eyeLocation) {
        this.location = location;

        PLAYER_TELEPORT.setEntityID(entityID);
        PLAYER_TELEPORT.setPlayerUUID(uuid);
        PLAYER_TELEPORT.setPosition(location.toVector());
        PLAYER_TELEPORT.setYaw(location.getYaw());
        PLAYER_TELEPORT.setPitch(location.getPitch());

        HEAD_ROTATION.setHeadYaw(eyeLocation.getYaw());

        PLAYER_TELEPORT.sendPacket(player);
        HEAD_ROTATION.sendPacket(player);
    }

    public void remove() {
        if (isSpawned()) {
            entityID = -1;


            PLAYER_DESTROY.sendPacket(player);
            PLAYER_LEAVE.sendPacket(player);


            HandlerList.unregisterAll(this);
        }
    }

    public boolean isSpawned() {
        return entityID != -1;
    }

    public Location getLocation() {
        return location;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (event.getPlayer().equals(player)) {
            remove();
        }
    }

    @RequiredArgsConstructor
    public static class FakeNPCData {
        @Getter
        private final String displayName;
        @Getter
        private final String skinName;
    }
}
