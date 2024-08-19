package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class FluidWalkingAbility extends Ability {

    public static final MapCodec<FluidWalkingAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    TagKey.codec(Registries.FLUID).fieldOf("fluid_tag").forGetter(ab -> ab.fluidTag),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, FluidWalkingAbility::new));

    public final TagKey<Fluid> fluidTag;

    public FluidWalkingAbility(TagKey<Fluid> fluidTag, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.fluidTag = fluidTag;
    }

    public static boolean canWalkOn(LivingEntity entity, FluidState fluid) {
        if (fluid.is(FluidTags.WATER) && AbilityUtil.isTypeEnabled(entity, AbilitySerializers.WATER_WALK.get())) {
            return true;
        } else {
            return AbilityUtil.getEnabledInstances(entity, AbilitySerializers.FLUID_WALKING.get()).stream().anyMatch(e -> fluid.is(e.getAbility().fluidTag));
        }
    }

    @Override
    public AbilitySerializer<FluidWalkingAbility> getSerializer() {
        return AbilitySerializers.FLUID_WALKING.get();
    }

    public static class Serializer extends AbilitySerializer<FluidWalkingAbility> {

        @Override
        public MapCodec<FluidWalkingAbility> codec() {
            return CODEC;
        }
    }
}
