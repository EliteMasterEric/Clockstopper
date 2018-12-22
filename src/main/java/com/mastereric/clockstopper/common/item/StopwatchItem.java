package com.mastereric.clockstopper.common.item;

import com.mastereric.clockstopper.common.EntityTimeUtil;
import com.mastereric.clockstopper.common.entity.TimeStopAreaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StopwatchItem extends DescItem {
    public StopwatchItem() {
        super(new Item.Settings().itemGroup(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        EntityTimeUtil.spawnTimeStopAreaEntity(world, playerEntity);
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    /*
    // FOR TESTING ONLY
    @Override
    public boolean interactWithEntity(ItemStack itemStack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand currentHand) {
        LogUtility.info("Interacted with entity: %s (%f, %f, %f)", livingEntity.getDisplayName(),
                livingEntity.x, livingEntity.y, livingEntity.z);
        boolean timeStopped = EntityTimeUtil.getTimeStopped(livingEntity);
        if (timeStopped) {
            LogUtility.info("Starting entity.");
            EntityTimeUtil.setTimeStopped(livingEntity, false);
        } else {
            LogUtility.info("Stopping entity.");
            EntityTimeUtil.setTimeStopped(livingEntity, true);
        }
        return true;
    }
    */
}
