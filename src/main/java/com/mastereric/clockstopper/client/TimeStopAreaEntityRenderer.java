package com.mastereric.clockstopper.client;

import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.sortme.Living;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class TimeStopAreaEntityRenderer extends EntityRenderer<TimeStopAreaEntity> {
    private PlayerEntityRenderer internalRenderer;
    public TimeStopAreaEntityRenderer(EntityRenderDispatcher var1, boolean thinArms) {
        super(var1);
        internalRenderer = new PlayerEntityRenderer(var1, thinArms);
    }

    @Override
    public void method_3936(TimeStopAreaEntity entity, double x, double y, double z, float yaw, float partialTick) {
        TimeStopAreaEntity.TimeStopAreaInternalPlayerEntity internalPlayerEntity = entity.getInternalPlayerEntity();
        internalRenderer.method_3936(internalPlayerEntity, x, y, z, yaw, partialTick);
    }

    @Nullable
    @Override
    protected Identifier getTexture(TimeStopAreaEntity entity) {
        return entity.getOwnerSkinTexture();
    }
}
