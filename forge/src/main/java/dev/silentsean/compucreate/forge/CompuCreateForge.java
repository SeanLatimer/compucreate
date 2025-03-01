package dev.silentsean.compucreate.forge;

import com.simibubi.create.content.contraptions.elevator.ElevatorContactBlockEntity;
import dev.silentsean.compucreate.CompuCreate;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorContactMixin;
import net.minecraft.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CompuCreate.MOD_ID)
public final class CompuCreateForge {
    public CompuCreateForge() {
        // Run our common setup.
        CompuCreate.init();

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, CompuCreateForge::attachPeripherals);
    }

    public static void attachPeripherals(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof ElevatorContactBlockEntity blockEntity) {
            PeripheralProvider.attach(event, blockEntity, f -> ((IElevatorContactMixin) f).compuCreate$peripheral());
        }
    }
}
