package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "getVisibilityPercent", cancellable = true)
    private void getVisibilityPercent(@Nullable Entity pLookingEntity, CallbackInfoReturnable<Double> ci) {
        if (AbilityUtil.isTypeEnabled((LivingEntity) (Object) this, AbilitySerializers.INVISIBILITY.get())) {
            ci.setReturnValue(0D);
        }
    }

}
