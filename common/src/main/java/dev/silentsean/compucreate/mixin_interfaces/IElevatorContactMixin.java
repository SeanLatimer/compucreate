package dev.silentsean.compucreate.mixin_interfaces;

import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

public interface IElevatorContactMixin {
    @Nullable
    IPeripheral compuCreate$peripheral();
}
