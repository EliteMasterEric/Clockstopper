package com.mastereric.clockstopper.client;

import com.mastereric.clockstopper.Clockstopper;
import com.mastereric.clockstopper.util.LogUtility;
import net.fabricmc.api.ClientModInitializer;

@SuppressWarnings("unused")
public class ClientClockstopper implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LogUtility.info("Initializing client...");
        Clockstopper.Entities.initializeEntityRenderers();
        Clockstopper.Sounds.initializeSounds();
    }
}
