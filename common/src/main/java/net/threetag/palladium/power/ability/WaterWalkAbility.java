package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class WaterWalkAbility extends Ability {

    public static final MapCodec<WaterWalkAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, WaterWalkAbility::new));

    public WaterWalkAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<WaterWalkAbility> getSerializer() {
        return AbilitySerializers.WATER_WALK.get();
    }

    public static class Serializer extends AbilitySerializer<WaterWalkAbility> {

        @Override
        public MapCodec<WaterWalkAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, WaterWalkAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows the player to walk on water.")
                    .setExampleObject(new WaterWalkAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
