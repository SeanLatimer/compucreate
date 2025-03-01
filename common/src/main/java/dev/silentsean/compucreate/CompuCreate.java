package dev.silentsean.compucreate;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class CompuCreate {
    public static final String MOD_ID = "compucreate";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        LOGGER.info("CompuCreate init.");
        //ComputerCraftAPI.registerGenericSource(new ElevatorContactPeripheral());
    }
}
