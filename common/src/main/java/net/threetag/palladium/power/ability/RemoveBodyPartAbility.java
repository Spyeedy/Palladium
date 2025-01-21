package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class RemoveBodyPartAbility extends Ability {

    public static final MapCodec<RemoveBodyPartAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(BodyPart.CODEC).fieldOf("body_parts").forGetter(ab -> ab.bodyParts),
                    Codec.BOOL.fieldOf("affects_first_person").forGetter(ab -> ab.affectsFirstPerson),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, RemoveBodyPartAbility::new));

    public final List<BodyPart> bodyParts;
    public final boolean affectsFirstPerson;

    public RemoveBodyPartAbility(List<BodyPart> bodyParts, boolean affectsFirstPerson, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.bodyParts = bodyParts;
        this.affectsFirstPerson = affectsFirstPerson;
    }

    @Override
    public AbilitySerializer<RemoveBodyPartAbility> getSerializer() {
        return AbilitySerializers.REMOVE_BODY_PART.get();
    }

    public static class Serializer extends AbilitySerializer<RemoveBodyPartAbility> {

        @Override
        public MapCodec<RemoveBodyPartAbility> codec() {
            return CODEC;
        }
    }
}
