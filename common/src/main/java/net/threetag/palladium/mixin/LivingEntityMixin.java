package net.threetag.palladium.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements PalladiumLivingEntityExtension {
}
