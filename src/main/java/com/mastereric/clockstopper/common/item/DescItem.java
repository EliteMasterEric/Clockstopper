package com.mastereric.clockstopper.common.item;

import net.minecraft.client.item.TooltipOptions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;

import java.util.List;

public abstract class DescItem extends Item {
    public DescItem(Settings var1) {
        super(var1);
    }

    @Override
    public void buildTooltip(ItemStack itemStack, World world, List<TextComponent> descList, TooltipOptions tooltipOptions) {
        super.buildTooltip(itemStack, world, descList, tooltipOptions);
        descList.add(new TranslatableTextComponent(this.getTranslationKey() + ".desc"));
    }
}
