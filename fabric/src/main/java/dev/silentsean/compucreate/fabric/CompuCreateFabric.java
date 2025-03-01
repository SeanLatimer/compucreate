package dev.silentsean.compucreate.fabric;

import com.simibubi.create.AllBlockEntityTypes;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dev.silentsean.compucreate.CompuCreate;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorContactMixin;
import net.fabricmc.api.ModInitializer;

public final class CompuCreateFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CompuCreate.init();

    }

    public static void registerPeripherals() {
        CompuCreate.LOGGER.info("Registering peripherals");
        PeripheralLookup.get().registerForBlockEntity((f, s) -> ((IElevatorContactMixin) f).compuCreate$peripheral(), AllBlockEntityTypes.ELEVATOR_CONTACT.get());
    }
}
