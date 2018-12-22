package com.mastereric.clockstopper.common;

import com.mastereric.clockstopper.Clockstopper;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import com.mastereric.clockstopper.mixin.common.LivingEntityTimeMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public abstract class EntityTimeUtil {
    public static final float MAGIC_NUMBER = 0.042F;

    public static TrackedData<Boolean> TIME_STOPPED;

    public static void spawnTimeStopAreaEntity(World world, PlayerEntity activator) {
        TimeStopAreaEntity timeStopAreaEntity = new TimeStopAreaEntity(world, activator);
        world.spawnEntity(timeStopAreaEntity);
        world.playSound(null, timeStopAreaEntity.x, timeStopAreaEntity.y, timeStopAreaEntity.z,
                SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYER, 1.0F, 1.0F);
    }

    public static boolean getTimeStopped(Entity entity) {
        //noinspection RedundantCast
        return (Boolean)entity.getDataTracker().get(TIME_STOPPED);
    }

    public static void setTimeStopped(Entity entity, boolean timeStopped) {
        entity.getDataTracker().set(TIME_STOPPED, timeStopped);
    }

    /*
        Normally, if time was stopped, nothing would happen period.
        However, to get time stopping to behave the way I want, I have some manual behavior changes.
        This function performs behavior only for living entities.
     */
    private static void manuallyUpdateLiving(LivingEntity entity) {
        // Manage HurtTime rendering normally.
        if (entity.hurtTime > 0)
            --entity.hurtTime;
        // Manage the period of invulnerability when hit normally.
        if (entity.field_6008 > 0)
            --entity.field_6008;
    }

    private static void manuallyUpdateNonliving(Entity entity) {
    }


    public static void conditionallyUpdate(Entity entity) {
        if (getTimeStopped(entity) && !(entity instanceof PlayerEntity)) {
            if (entity instanceof LivingEntity) {
                manuallyUpdateLiving((LivingEntity) entity);
            } else {
                manuallyUpdateNonliving(entity);
            }
        } else {
            entity.update();
        }
    }
}
