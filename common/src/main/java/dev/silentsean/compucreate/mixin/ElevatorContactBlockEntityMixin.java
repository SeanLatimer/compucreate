package dev.silentsean.compucreate.mixin;

import com.simibubi.create.content.contraptions.elevator.ElevatorContactBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.silentsean.compucreate.computercraft.peripherals.ElevatorContactPeripheral;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorContactMixin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ElevatorContactBlockEntity.class)
public class ElevatorContactBlockEntityMixin implements IElevatorContactMixin {
    @Unique
    @Nullable
    private ElevatorContactPeripheral compuCreate$peripheral;

    @Unique
    @Nullable
    public IPeripheral compuCreate$peripheral() {
        return Objects.requireNonNullElseGet(compuCreate$peripheral, () -> compuCreate$peripheral = new ElevatorContactPeripheral((ElevatorContactBlockEntity) (Object) this));
    }

    @Inject(method = "updateDisplayedFloor", at = @At(value = "RETURN"), remap = false)
    public void updateDisplayedFloor(String floorName, CallbackInfo ci) {
        if (compuCreate$peripheral != null) {
            compuCreate$peripheral.queueEvent("floor_changed", floorName);
        }
    }
}
