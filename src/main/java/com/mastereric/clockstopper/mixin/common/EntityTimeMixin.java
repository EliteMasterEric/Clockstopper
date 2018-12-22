package com.mastereric.clockstopper.mixin.common;

import com.mastereric.clockstopper.common.EntityTimeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityTimeMixin {
    @Shadow protected DataTracker dataTracker;
    /*
        The below three methods add data tracking to entities, giving them a client/server synced flag
        which indicates whether they are stopped or not.
     */
    @Inject(at = @At("RETURN"), method = "<init>")
    private void initDataTracker(CallbackInfo callbackInfo) {
        this.dataTracker.startTracking(EntityTimeUtil.TIME_STOPPED, false);
    }

    /*
        Thanks to TehNut for this fix!
        https://github.com/TehNut/Soul-Shards-Respawn/commit/380b0b44ed7e87cd58ab4580dfc54952d37ddd04#diff-6db05c46d7a888033cdfdedd5c643c28R18
      */
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerDataTracker(CallbackInfo callbackInfo) {
        EntityTimeUtil.TIME_STOPPED = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    /**
     * method_8553 adds entity updates.
     * This function adds a conditional statement to the call to Entity.update.
     */
    @Redirect(method = "method_5842", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;update()V"))
    private void handleTimeStopped(Entity entity) {
        EntityTimeUtil.conditionallyUpdate(entity);
    }
}
