package dev.silentsean.compucreate.mixin;

import com.jozufozu.flywheel.util.WeakHashSet;
import com.simibubi.create.content.contraptions.elevator.ElevatorColumn;
import dev.silentsean.compucreate.mixin_interfaces.IElevatorColumnMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.IntConsumer;

@Mixin(ElevatorColumn.class)
public class ElevatorColumnMixin implements IElevatorColumnMixin {
    @Unique
    private final Set<IntConsumer> compucreate$targetChangedListeners =
            Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));


    @Inject(method = "target", at = @At(value = "RETURN"), remap = false)
    public void targetChanged(int yLevel, CallbackInfo ci) {
        synchronized (compucreate$targetChangedListeners) {
            for (IntConsumer listener : compucreate$targetChangedListeners) {
                listener.accept(yLevel);
            }
        }
    }

    @Unique
    public void compucreate$subscribeTargetChanged(IntConsumer listener) {
        compucreate$targetChangedListeners.add(listener);
    }

    @Unique
    public void compucreate$unsubscribeTargetChanged(IntConsumer listener) {
        compucreate$targetChangedListeners.remove(listener);
    }
}
