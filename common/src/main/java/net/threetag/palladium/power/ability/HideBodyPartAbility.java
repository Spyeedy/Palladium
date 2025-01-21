package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class HideBodyPartAbility extends Ability {

    public static final MapCodec<HideBodyPartAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(BodyPart.CODEC).fieldOf("body_parts").forGetter(ab -> ab.bodyParts),
                    Codec.BOOL.fieldOf("affects_first_person").forGetter(ab -> ab.affectsFirstPerson),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, HideBodyPartAbility::new));

    public final List<BodyPart> bodyParts;
    public final boolean affectsFirstPerson;

    public HideBodyPartAbility(List<BodyPart> bodyParts, boolean affectsFirstPerson, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.bodyParts = bodyParts;
        this.affectsFirstPerson = affectsFirstPerson;
    }

    @Override
    public AbilitySerializer<HideBodyPartAbility> getSerializer() {
        return AbilitySerializers.HIDE_BODY_PART.get();
    }

    public static class Serializer extends AbilitySerializer<HideBodyPartAbility> {

        @Override
        public MapCodec<HideBodyPartAbility> codec() {
            return CODEC;
        }
    }
}
