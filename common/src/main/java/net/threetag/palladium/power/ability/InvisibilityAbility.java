package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class InvisibilityAbility extends Ability {

    // TODO add first person support

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, InvisibilityAbility::new));

    public InvisibilityAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<InvisibilityAbility> getSerializer() {
        return AbilitySerializers.INVISIBILITY.get();
    }

    public static class Serializer extends AbilitySerializer<InvisibilityAbility> {

        @Override
        public MapCodec<InvisibilityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, InvisibilityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the player invisible. Also makes mobs not see the player anymore.")
                    .setExampleObject(new InvisibilityAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
