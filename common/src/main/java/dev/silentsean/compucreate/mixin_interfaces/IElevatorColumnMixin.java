package dev.silentsean.compucreate.mixin_interfaces;

import java.util.function.IntConsumer;

public interface IElevatorColumnMixin {
    void compucreate$subscribeTargetChanged(IntConsumer listener);
    void compucreate$unsubscribeTargetChanged(IntConsumer listener);
}
