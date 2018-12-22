package com.mastereric.clockstopper.client.network;

import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import com.mastereric.clockstopper.util.LogUtility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PacketByteBuf;

@Environment(EnvType.CLIENT)
public abstract class ClockstopperNetworkHandler {
    public static void handleTimeStopAreaEntitySpawnClientPacket(PacketContext packetContext, PacketByteBuf packetByteBuf) {
        TimeStopAreaEntitySpawnClientPacket packet
                = new TimeStopAreaEntitySpawnClientPacket(packetByteBuf);

        LogUtility.debug("Client spawning server-designated Time Stop Area entity, at pos (%f, %f, %f)",
                packet.getX(), packet.getY(), packet.getZ());

        // Copied logic from net.minecraft.client.network.ClientPlayNetworkHandler.onEntitySpawn
        TimeStopAreaEntity entity = new TimeStopAreaEntity(packetContext.getPlayer().getEntityWorld(),
                packet.getX(), packet.getY(), packet.getZ(), null);
        entity.pitch = packet.getPitch();
        entity.yaw = packet.getYaw();
        //entity.getParts() is null
        entity.setEntityId(packet.getId());
        entity.setUuid(packet.getUuid());
        entity.setVelocityClient((double)packet.getVelocityX() / 8000.0D,
                (double)packet.getVelocityY() / 8000.0D,
                (double)packet.getVelocityZ() / 8000.0D);
        entity.setOwnerUuid(packet.getOwner());

        // This unmapped function adds the entity to the Client's world.
        MinecraftClient.getInstance().world.method_2942(packet.getId(), entity);
    }
}
