package com.mastereric.clockstopper.mixin.client;

import com.mastereric.clockstopper.client.TimeStopAreaEntityRenderer;
import com.mastereric.clockstopper.common.EntityTimeUtil;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherTimeMixin  {
    private TimeStopAreaEntityRenderer PLAYER_RENDER_DEFAULT;
    private TimeStopAreaEntityRenderer PLAYER_RENDER_SLIM;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initializePlayerRenderers(TextureManager var1, ItemRenderer var2, ReloadableResourceManager var3, CallbackInfo callbackInfo) {
        PLAYER_RENDER_DEFAULT = new TimeStopAreaEntityRenderer((EntityRenderDispatcher)(Object)this, false);
        PLAYER_RENDER_SLIM = new TimeStopAreaEntityRenderer((EntityRenderDispatcher)(Object)this, false);
    }

    /**
     * WARNING: Contains MCP names!
     * Ignore the partial render tick to prevent twitching.
     */
    @Redirect(method = "method_3954", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;method_3936(Lnet/minecraft/entity/Entity;DDDFF)V"))
    private void handleTimeStopped(EntityRenderer<Entity> renderer, Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        if (EntityTimeUtil.getTimeStopped(entity) && !(entity instanceof PlayerEntity))
            renderer.method_3936(entity, x, y, z, yaw, 0);
        else
            renderer.method_3936(entity, x, y, z, yaw, partialTicks);
    }

    @Inject(method = "getRenderer(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/render/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
    private void getRenderer(Entity var1, CallbackInfoReturnable<TimeStopAreaEntityRenderer> callbackInfoReturnable) {
        if (var1 instanceof TimeStopAreaEntity) {
            if (((TimeStopAreaEntity) var1).getOwnerSkinType().equals("slim"))
                callbackInfoReturnable.setReturnValue(PLAYER_RENDER_SLIM);
            callbackInfoReturnable.setReturnValue(PLAYER_RENDER_DEFAULT);
        }
    }
}
