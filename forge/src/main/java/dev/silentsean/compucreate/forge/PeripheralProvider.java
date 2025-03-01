package dev.silentsean.compucreate.forge;

import dan200.computercraft.api.peripheral.IPeripheral;
import dev.silentsean.compucreate.CompuCreate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

final class PeripheralProvider<O extends BlockEntity> implements ICapabilityProvider {
    public static final Capability<IPeripheral> CAPABILITY_PERIPHERAL = CapabilityManager.get(new CapabilityToken<>() {
    });
    private static final Identifier PERIPHERAL = new Identifier(CompuCreate.MOD_ID, "peripheral");
    private final O blockEntity;
    private final Function<O, IPeripheral> factory;
    private @Nullable LazyOptional<IPeripheral> peripheral;

    private PeripheralProvider(O blockEntity, Function<O, IPeripheral> factory) {
        this.blockEntity = blockEntity;
        this.factory = factory;
    }

    static <O extends BlockEntity> void attach(AttachCapabilitiesEvent<BlockEntity> event, O blockEntity, Function<O, IPeripheral> factory) {
        var provider = new PeripheralProvider<>(blockEntity, factory);
        event.addCapability(PERIPHERAL, provider);
        event.addListener(provider::invalidate);
    }

    private void invalidate() {
        if (peripheral != null) peripheral.invalidate();
        peripheral = null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction direction) {
        if (capability != CAPABILITY_PERIPHERAL) return LazyOptional.empty();
        if (blockEntity.isRemoved()) return LazyOptional.empty();

        var peripheral = this.peripheral;
        return (peripheral == null ? (this.peripheral = LazyOptional.of(() -> factory.apply(blockEntity))) : peripheral).cast();
    }
}
