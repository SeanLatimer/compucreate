package dev.silentsean.compucreate.fabric.mixin;

import com.simibubi.create.Create;
import dev.silentsean.compucreate.fabric.CompuCreateFabric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Create.class)
public class CreateInitMixin {
    @Inject(method = "onInitialize", at = @At(value = "RETURN"), remap = false)
    public void onInitialize(CallbackInfo ci) {
        CompuCreateFabric.registerPeripherals();
    }
}