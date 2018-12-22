package com.mastereric.clockstopper.mixin.common;

import com.mastereric.clockstopper.common.EntityTimeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class WorldTimeMixin {
    /*
        The below methods basically re-implement F***e's updateBlocked field.
        If the property value is true, the entity is not updated.
     */

    /**
     * method_8553 adds entity updates.
     * This function adds a conditional statement to the second call to Entity.update.
     */
    @Redirect(method = "method_8553", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;update()V"))
    private void handleTimeStopped1(Entity entity) {
        EntityTimeUtil.conditionallyUpdate(entity);
    }

    /**
     * method_8553 adds entity updates.
     * This function adds a conditional statement to the second call to Entity.update.
     */
    @Redirect(method = "updateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;update()V"))
    private void handleTimeStopped2(Entity entity) {
        EntityTimeUtil.conditionallyUpdate(entity);
    }


}
