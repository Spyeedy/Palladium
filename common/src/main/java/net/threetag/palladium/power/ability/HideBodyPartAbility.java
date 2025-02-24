package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class HideBodyPartAbility extends Ability {

    // TODO

    public static final MapCodec<HideBodyPartAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(BodyPart.CODEC).fieldOf("body_parts").forGetter(ab -> ab.bodyParts),
                    Codec.BOOL.optionalFieldOf("affects_first_person", true).forGetter(ab -> ab.affectsFirstPerson),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
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

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, HideBodyPartAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Hides the specified body parts of the entity.")
                    .add("body_parts", Documented.typeEnum(BodyPart.values()), "The body parts to hide.")
                    .addOptional("affects_first_person", TYPE_BOOLEAN, "Determines if the first person arm should disappear as well (if it's disabled).")
                    .setExampleObject(new HideBodyPartAbility(List.of(BodyPart.HEAD, BodyPart.CHEST), false, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
