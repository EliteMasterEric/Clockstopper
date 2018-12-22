package com.mastereric.clockstopper.client.network;

import com.mastereric.clockstopper.Reference;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.packet.CustomPayloadClientPacket;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

/**
 * Packet sent to the client to tell it to spawn a copy of the entity on the server.
 * This is done for you for EntityLiving, and hopefully Fabric will do it automatically for all entities,
 * but for now, non-living entities require custom networking code.
 */
public class TimeStopAreaEntitySpawnClientPacket extends CustomPayloadClientPacket {
    private int id;
    private UUID uuid;
    private double x;
    private double y;
    private double z;
    private int velocityX;
    private int velocityY;
    private int velocityZ;
    private int pitch;
    private int yaw;
    private Identifier channel2;
    private UUID owner;

    public TimeStopAreaEntitySpawnClientPacket(PacketByteBuf packetByteBuf) {
        super(new Identifier(Reference.MOD_ID, "spawn_timestoparea"), packetByteBuf);
        this.read(packetByteBuf);
    }

    public TimeStopAreaEntitySpawnClientPacket(Entity entity) {
        this((TimeStopAreaEntity) entity);
    }

    public TimeStopAreaEntitySpawnClientPacket(TimeStopAreaEntity timeStopAreaEntity) {
        this.channel2 = new Identifier(Reference.MOD_ID, "spawn_chatbomb");
        this.id = timeStopAreaEntity.getEntityId();
        this.uuid = timeStopAreaEntity.getUuid();
        this.x = timeStopAreaEntity.x;
        this.y = timeStopAreaEntity.y;
        this.z = timeStopAreaEntity.z;
        this.pitch = MathHelper.floor(timeStopAreaEntity.pitch * 256.0F / 360.0F);
        this.yaw = MathHelper.floor(timeStopAreaEntity.yaw * 256.0F / 360.0F);
        this.velocityX = (int)(MathHelper.clamp(timeStopAreaEntity.velocityX, -3.9D, 3.9D) * 8000.0D);
        this.velocityY = (int)(MathHelper.clamp(timeStopAreaEntity.velocityY, -3.9D, 3.9D) * 8000.0D);
        this.velocityZ = (int)(MathHelper.clamp(timeStopAreaEntity.velocityZ, -3.9D, 3.9D) * 8000.0D);
        this.owner = timeStopAreaEntity.getOwnerUuid();
    }

    @Override
    public Identifier getChannel() {
        return this.channel2;
    }

    public void read(PacketByteBuf var1) {
        this.id = var1.readVarInt();
        this.uuid = var1.readUuid();
        this.x = var1.readDouble();
        this.y = var1.readDouble();
        this.z = var1.readDouble();
        this.velocityX = var1.readShort();
        this.velocityY = var1.readShort();
        this.velocityZ = var1.readShort();
        this.pitch = var1.readByte();
        this.yaw = var1.readByte();
        this.owner = var1.readUuid();
    }

    public void write(PacketByteBuf var1) {
        var1.writeVarInt(this.id);
        var1.writeUuid(this.uuid);
        var1.writeDouble(this.x);
        var1.writeDouble(this.y);
        var1.writeDouble(this.z);
        var1.writeShort(this.velocityX);
        var1.writeShort(this.velocityY);
        var1.writeShort(this.velocityZ);
        var1.writeByte(this.pitch);
        var1.writeByte(this.yaw);
        var1.writeUuid(this.owner);
    }

    @Override
    public PacketByteBuf getData() {
        PacketByteBuf buf = (new PacketByteBuf(Unpooled.buffer()));
        write(buf);
        return buf;
    }

    @Environment(EnvType.CLIENT)
    public int getId() {
        return this.id;
    }

    @Environment(EnvType.CLIENT)
    public UUID getUuid() {
        return this.uuid;
    }

    @Environment(EnvType.CLIENT)
    public double getX() {
        return this.x;
    }

    @Environment(EnvType.CLIENT)
    public double getY() {
        return this.y;
    }

    @Environment(EnvType.CLIENT)
    public double getZ() {
        return this.z;
    }

    @Environment(EnvType.CLIENT)
    public int getVelocityX() {
        return this.velocityX;
    }

    @Environment(EnvType.CLIENT)
    public int getVelocityY() {
        return this.velocityY;
    }

    @Environment(EnvType.CLIENT)
    public int getVelocityZ() {
        return this.velocityZ;
    }

    @Environment(EnvType.CLIENT)
    public int getPitch() {
        return this.pitch;
    }

    @Environment(EnvType.CLIENT)
    public int getYaw() {
        return this.yaw;
    }

    @Environment(EnvType.CLIENT)
    public UUID getOwner() {
        return this.owner;
    }

}
