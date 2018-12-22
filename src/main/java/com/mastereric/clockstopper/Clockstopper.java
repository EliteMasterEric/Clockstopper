package com.mastereric.clockstopper;

import com.mastereric.clockstopper.client.TimeStopAreaEntityRenderer;
import com.mastereric.clockstopper.client.network.ClockstopperNetworkHandler;
import com.mastereric.clockstopper.client.network.TimeStopAreaEntitySpawnClientPacket;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import com.mastereric.clockstopper.common.entity.damage.TimeStopDamageSource;
import com.mastereric.clockstopper.common.item.StopwatchItem;
import com.mastereric.clockstopper.util.LogUtility;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.entity.EntityTrackingRegistry;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Clockstopper implements ModInitializer {

    public abstract static class Items {
        public static Item STOPWATCH;

        static void initializeItems() {
            STOPWATCH = initializeItem(new StopwatchItem(), "stopwatch");
        }

        static Item initializeItem(Item item, String name) {
            Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, name), item);
            return item;
        }
    }

    public abstract static class Entities {
        public static EntityType TIME_STOP_AREA;

        public static EntityRenderer TIME_STOP_AREA_RENDER_SLIM;
        public static EntityRenderer TIME_STOP_AREA_RENDER_DEFAULT;

        static void initializeEntities() {
            LogUtility.info("Initializing entities...");
            TIME_STOP_AREA = registerEntity(TimeStopAreaEntity.class, "timestoparea",
                    TimeStopAreaEntitySpawnClientPacket::new,
                    ClockstopperNetworkHandler::handleTimeStopAreaEntitySpawnClientPacket,
                    160, 10, true);
        }

        public static void initializeEntityRenderers() {
            // Entity renderer code should only be run on the Client.
            LogUtility.info("Initializing entity renderers...");
            //registerEntityRenderer(TimeStopAreaEntity.class, (ctx, ctx2) -> new TimeStopAreaEntityRenderer(ctx, false));
        }

        static EntityType registerEntity(Class<? extends Entity> entityClass, String name,
                                         Function<Entity, Packet> packetCreator,
                                         BiConsumer<PacketContext, PacketByteBuf> packetHandler) {
            // Use this only for living entities that don't require tracking.
            return registerEntity(entityClass, name, packetCreator, packetHandler, -1, -1, false);
        }

        static EntityType registerEntity(Class<? extends Entity> entityClass, String name,
                                         Function<Entity, Packet> packetCreator,
                                         BiConsumer<PacketContext, PacketByteBuf> packetHandler,
                                         int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity) {
            // Manually enabling tracking is required for non-living entities.
            // trackingDistance represents how close the player must be for the entity to be tracked.
            //
            // updateInvervalTicks represents how many ticks between updates there are.
            boolean tracking = trackingDistance != -1 || updateIntervalTicks != -1 || alwaysUpdateVelocity;

            FabricEntityTypeBuilder builder = FabricEntityTypeBuilder.create(entityClass);
            // Entity tracking. Reference net.minecraft.server.network.EntityTracker.add(Entity) for correct logic.
            if (tracking) {
                builder.trackable(trackingDistance, updateIntervalTicks, alwaysUpdateVelocity);
            }
            EntityType<?> entityType = builder.build();
            // Currently, Fabric requires networking logic for non-LivingEntity entities to be done manually.
            if (tracking) {
                EntityTrackingRegistry.INSTANCE.registerSpawnPacketProvider(
                        entityType, packetCreator);
                CustomPayloadPacketRegistry.CLIENT.register(
                        new Identifier(Reference.MOD_ID, "spawn_"+name), packetHandler);
            }
            Registry.register(Registry.ENTITY_TYPE, new Identifier(Reference.MOD_ID, name), entityType);
            return entityType;
        }

        static void registerEntityRenderer(Class<? extends Entity> entityClass, EntityRendererRegistry.Factory factory) {
            EntityRendererRegistry.INSTANCE.register(entityClass, factory);
        }
    }

    public abstract static class Sounds {
        public static SoundEvent TIME_STOP_START_SOUND;

        public static void initializeSounds() {
            TIME_STOP_START_SOUND = initializeSound("timestop.start");
        }

        static SoundEvent initializeSound(String name) {
            return (SoundEvent)Registry.register(Registry.SOUND_EVENT, (String)name, new SoundEvent(new Identifier(Reference.MOD_ID, name)));
        }
    }

    public static final DamageSource TIMESTOP_DAMAGE = new TimeStopDamageSource();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        LogUtility.info("Initializing Clockstopper %s.", Reference.MOD_VERSION);
        LogUtility.printLogLevel();

        Items.initializeItems();
        Entities.initializeEntities();
	}
}
