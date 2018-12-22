package com.mastereric.clockstopper.mixin.client;

import com.mastereric.clockstopper.common.EntityTimeUtil;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import jdk.internal.org.objectweb.asm.Opcodes;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.FloatBuffer;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererTimeMixin {
    @Shadow protected FloatBuffer field_4740;

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "method_4047", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;deathCounter:I", opcode = Opcodes.GETFIELD))
    private int handleStoppedDeathTint(LivingEntity entity) {
        if (entity.getHealth() == EntityTimeUtil.MAGIC_NUMBER)
            return 42;
        return 0;
    }

    private static final int TIME_STOP_AREA_RED = 0x6B; // 1-255
    private static final int TIME_STOP_AREA_GREEN = 0x9F;
    private static final int TIME_STOP_AREA_BLUE = 0xE5;
    private static final int TIME_STOP_AREA_ALPHA = 0x80;

    @Inject(method = "method_4047", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;flip()Ljava/nio/Buffer;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void handleTimeStopEntityTint(LivingEntity livingEntity, float var2, boolean var3, CallbackInfoReturnable<Boolean> cir) {
        // this.field_4740 is a 4-value color tint buffer.
        if (livingEntity instanceof TimeStopAreaEntity.TimeStopAreaInternalPlayerEntity) {
            // Override the color buffer before it is used.
            this.field_4740.position(0);
            this.field_4740.put(TIME_STOP_AREA_RED   / 255F);
            this.field_4740.put(TIME_STOP_AREA_GREEN / 255F);
            this.field_4740.put(TIME_STOP_AREA_BLUE  / 255F);
            this.field_4740.put(TIME_STOP_AREA_ALPHA / 255F);
        }
    }
}
