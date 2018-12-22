package com.mastereric.clockstopper.common.entity;

import com.mastereric.clockstopper.Clockstopper;
import com.mastereric.clockstopper.util.LogUtility;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.ScoreboardEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sortme.OptionMainHand;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TimeStopAreaEntity extends LivingEntity {
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(TimeStopAreaEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Byte> MAIN_HAND = DataTracker.registerData(TimeStopAreaEntity.class, TrackedDataHandlerRegistry.BYTE);

    private final DefaultedList<ItemStack> inventory_hands;
    private final DefaultedList<ItemStack> inventory_armor;

    private TimeStopAreaInternalPlayerEntity internalPlayerEntity;

    private TimeStopAreaEntity(World world) {
        super(Clockstopper.Entities.TIME_STOP_AREA, world);
        inventory_hands = DefaultedList.create(2, ItemStack.EMPTY);
        inventory_armor = DefaultedList.create(4, ItemStack.EMPTY);
    }

    public TimeStopAreaEntity(World world, PlayerEntity owner) {
        this(world, owner.x, owner.y, owner.z, owner);
    }

    public TimeStopAreaEntity(World world, double xPos, double yPos, double zPos, @Nullable PlayerEntity owner) {
        this(world);
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;
        if (owner != null) {
            setOwnerUuid(owner.getUuid());
            setMainHand(owner.getMainHand());
            equipItems(owner);
        }
        // Else, use the defaults.
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(MAIN_HAND, (byte) 1);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag var1) { }

    @Override
    public Iterable<ItemStack> getItemsArmor() {
        return inventory_armor;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot var1) {
        switch (var1) {
            case HEAD:
                return inventory_armor.get(0);
            case CHEST:
                return inventory_armor.get(1);
            case LEGS:
                return inventory_armor.get(2);
            case FEET:
                return inventory_armor.get(3);
            case HAND_MAIN:
                return inventory_hands.get(0);
            case HAND_OFF:
                return inventory_hands.get(1);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void setEquippedStack(EquipmentSlot var1, ItemStack var2) {
        switch (var1) {
            case HEAD:
                inventory_armor.set(0, var2);
            case CHEST:
                inventory_armor.set(1, var2);
            case LEGS:
                inventory_armor.set(2, var2);
            case FEET:
                inventory_armor.set(3, var2);
            case HAND_MAIN:
                inventory_hands.set(0, var2);
            case HAND_OFF:
                inventory_hands.set(1, var2);
        }
    }

    public void equipItems(PlayerEntity entity) {
        setEquippedStack(EquipmentSlot.HEAD, entity.getEquippedStack(EquipmentSlot.HEAD));
        setEquippedStack(EquipmentSlot.CHEST, entity.getEquippedStack(EquipmentSlot.CHEST));
        setEquippedStack(EquipmentSlot.LEGS, entity.getEquippedStack(EquipmentSlot.LEGS));
        setEquippedStack(EquipmentSlot.FEET, entity.getEquippedStack(EquipmentSlot.FEET));
        setEquippedStack(EquipmentSlot.HAND_MAIN, entity.getEquippedStack(EquipmentSlot.HAND_MAIN));
        setEquippedStack(EquipmentSlot.HAND_OFF, entity.getEquippedStack(EquipmentSlot.HAND_OFF));
    }

    @Override
    public void writeCustomDataToTag(CompoundTag var1) { }

    @Nullable
    public UUID getOwnerUuid() {
        return (UUID)((Optional)this.dataTracker.get(OWNER_UUID)).orElse((Object)null);
    }

    public void setOwnerUuid(UUID input) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(input));
    }

    @Override
    public OptionMainHand getMainHand() {
        return this.dataTracker.get(MAIN_HAND) == 0 ? OptionMainHand.LEFT : OptionMainHand.RIGHT;
    }

    public void setMainHand(OptionMainHand var1) {
        this.dataTracker.set(MAIN_HAND, (byte)(var1 == OptionMainHand.LEFT ? 0 : 1));
    }

    public PlayerEntity getOwner() {
        return this.world.getPlayerByUuid(getOwnerUuid());
    }

    @Nullable
    private ScoreboardEntry getOwnerScoreboard() {
        return Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).method_2871(getOwnerUuid());
    }

    @Nullable
    public GameProfile getOwnerProfile() {
        ScoreboardEntry ownerScoreboard = getOwnerScoreboard();
        return ownerScoreboard == null ?  new GameProfile(null, "TimeStopArea") : ownerScoreboard.getProfile();
    }

    public String getOwnerSkinType() {
        ScoreboardEntry ownerScoreboard = getOwnerScoreboard();
        return ownerScoreboard == null ? "default" : ownerScoreboard.method_2977();
    }

    public Identifier getOwnerSkinTexture() {
        ScoreboardEntry ownerScoreboard = getOwnerScoreboard();
        return ownerScoreboard == null ? DefaultSkinHelper.getTexture() : ownerScoreboard.getSkinTexture();
    }

    @SuppressWarnings("EntityConstructor")
    public final class TimeStopAreaInternalPlayerEntity extends OtherClientPlayerEntity {
        TimeStopAreaInternalPlayerEntity(World var1, GameProfile var2) {
            super(var1, var2);
        }
        TimeStopAreaEntity getParent() {
            return TimeStopAreaEntity.this;
        }
        @Override
        public OptionMainHand getMainHand() {
            return getParent().getMainHand();
        }
        @Override
        public ItemStack getEquippedStack(EquipmentSlot var1) {
            return getParent().getEquippedStack(var1);
        }
        @Override
        public boolean shouldRenderName() {
            return false;
        }
    }

    public TimeStopAreaInternalPlayerEntity getInternalPlayerEntity() {
        if (internalPlayerEntity == null)
            internalPlayerEntity = new TimeStopAreaInternalPlayerEntity(world, getOwnerProfile());
        return internalPlayerEntity;
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    /*
    TODO: Copy bounding box code from ConduitEntity
    */
}
