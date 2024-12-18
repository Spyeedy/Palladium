package net.threetag.palladium.mixin;

import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelSummary.class, priority = 1001)
public class LevelSummaryMixin {

    @Inject(method = "isExperimental", at = @At(value = "RETURN"), cancellable = true)
    private void isExperimental(CallbackInfoReturnable<Boolean> cir) {
        // TODO config
        cir.setReturnValue(false);
    }

}
