package dev.silentsean.compucreate.computercraft.peripherals;

import com.simibubi.create.content.contraptions.elevator.*;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.silentsean.compucreate.CompuCreate;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorColumnMixin;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorContactMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.IntConsumer;

public class ElevatorContactPeripheral implements IPeripheral {
    private final AttachedComputerSet attachedComputers = new AttachedComputerSet();
    private final ElevatorContactBlockEntity tileEntity;
    private final World world;

    private final IntConsumer onTargetFloorChangedListener = yLevel -> {
        queueEvent("target_floor_changed", getTargetFloor());
    };

    public ElevatorContactPeripheral(@NotNull ElevatorContactBlockEntity tileEntity) {
        this.tileEntity = tileEntity;
        this.world = tileEntity.getWorld();
    }

    @Override
    public String getType() {
        return "compucreate:elevator_contact";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof ElevatorContactPeripheral o && tileEntity == o.tileEntity;
    }

    @Override
    public void attach(IComputerAccess computer) {
        var column = ElevatorColumn.get(world, tileEntity.columnCoords);
        if(column != null) {
            ((IElevatorColumnMixin)column).compucreate$subscribeTargetChanged(onTargetFloorChangedListener);
        }
        attachedComputers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        attachedComputers.remove(computer);
    }

    @Override
    public @Nullable Object getTarget() {
        return this.tileEntity;
    }

    @LuaFunction(mainThread = true)
    public final String getTargetFloor() {
        var target = this.getTargetBlockEntity();
        if (target != null) {
            return target.shortName;
        }
        return null;
    }

    @LuaFunction(mainThread = true)
    public final String getCurrentFloor() {
        if (this.tileEntity != null) {
            return tileEntity.lastReportedCurrentFloor;
        }
        return null;
    }

    @LuaFunction(mainThread = true)
    public final String[] getFloorNames() {
        if (this.tileEntity != null) {
            return new String[]{tileEntity.shortName, tileEntity.longName};
        }
        return null;
    }

    @LuaFunction(mainThread = true)
    public final int callToFloor(String shortName) {
        if (this.tileEntity == null) {
            return 0;
        }
        var column = ElevatorColumn.get(world, tileEntity.columnCoords);
        if(column == null) {
            return 0;
        }

        int oldTargetY = column.getTargetedYLevel();
        Optional<BlockPos> targetOpt = column.getContacts().stream()
                .filter(pos -> {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof ElevatorContactBlockEntity ecbe) {
                        return shortName.equals(ecbe.shortName);
                    }
                    return false;
                })
                .findFirst();
        var targetPos = targetOpt.orElse(null);
        if (targetPos == null) {
            return 0;
        }

        BlockState blockState = world.getBlockState(targetPos);
        Block block = blockState.getBlock();
        if (block instanceof ElevatorContactBlock ecb) {
            ecb.callToContactAndUpdate(column, blockState, world, targetPos, false);
            return targetPos.getY() - oldTargetY;
        }
        return 0;
    }

    private ElevatorContactBlockEntity getTargetBlockEntity() {
        var column = ElevatorColumn.get(world, tileEntity.columnCoords);
        if (column == null) {
            CompuCreate.LOGGER.warn("ElevatorColumn at coords {} is null", tileEntity.columnCoords);
            return null;
        }
        var targetPos = column.contactAt(column.getTargetedYLevel());
        if (targetPos == null) {
            CompuCreate.LOGGER.warn(
                    "Failed to find target position for contact at Y {} for column {} ",
                    column.getTargetedYLevel(),
                    tileEntity.columnCoords);
            return null;
        }

        var target = world.getBlockEntity(targetPos);
        if (target instanceof ElevatorContactBlockEntity) {
            return (ElevatorContactBlockEntity) target;
        }
        return null;
    }

    public void queueEvent(String eventName, Object... args) {
        attachedComputers.queueEvent(eventName, args);
    }
}
