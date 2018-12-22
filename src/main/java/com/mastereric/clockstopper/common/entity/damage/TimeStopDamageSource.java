package com.mastereric.clockstopper.common.entity.damage;

import net.minecraft.entity.damage.DamageSource;

/**
 * Custom damage source allowing for custom death messages.
 */
public class TimeStopDamageSource extends DamageSource {
    public TimeStopDamageSource() {
        super("chatbomb");
        this.setScaledWithDifficulty();
        this.setExplosive();
    }
}
