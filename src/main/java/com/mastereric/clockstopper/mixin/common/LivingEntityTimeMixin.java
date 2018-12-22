package com.mastereric.clockstopper.mixin.common;

import com.mastereric.clockstopper.Clockstopper;
import com.mastereric.clockstopper.common.EntityTimeUtil;
import com.mastereric.clockstopper.util.LogUtility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.mastereric.clockstopper.common.EntityTimeUtil.MAGIC_NUMBER;

@Mixin(LivingEntity.class)
public abstract class LivingEntityTimeMixin extends Entity {

    public DamageSource timeStopDamageSource = null;

    @Shadow public abstract void setHealth(float var1);

    @Shadow public abstract float getHealth();

    public LivingEntityTimeMixin(EntityType<?> var1, World var2) {
        // The mixin just ignores this but it's needed to compile.
        super(var1, var2);
    }

    @Inject(method = "method_6095", at = @At("HEAD"), cancellable = true)
    private void handleTimeStoppedDeath(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (EntityTimeUtil.getTimeStopped(this)) {
            this.setHealth(MAGIC_NUMBER); // Magic number, tells me this entity is already dead (NANI?)

            this.timeStopDamageSource = source;
            // setReturnValue implicitly cancels the callback, making this act like a 'return' line.
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void handleTimeStoppedDamage(CallbackInfoReturnable<Boolean> cir) {
        if (EntityTimeUtil.getTimeStopped(this) && this.getHealth() == MAGIC_NUMBER) {
            // This entity is already dead, ignore the damage.
            // setReturnValue implicitly cancels the callback, making this act like a 'return' line.
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void handleTimeStoppedUpdate(CallbackInfo ci) {
        if (this.getHealth() == MAGIC_NUMBER) {
            // Strike the killing blow.
            if (this.timeStopDamageSource != null) {
                LogUtility.info("Using saved damage source: %s", this.timeStopDamageSource.name);
                this.damage(this.timeStopDamageSource, 20.0F);
            } else {
                LogUtility.info("Using generic damage source.");
                this.damage(Clockstopper.TIMESTOP_DAMAGE, 20.0F);
            }
        }
    }
}
