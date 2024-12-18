package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class DamageImmunityAbility extends Ability {

    public static final MapCodec<DamageImmunityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(DamageType.CODEC).fieldOf("damage_types").forGetter(ab -> ab.damageTypes),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, DamageImmunityAbility::new));

    public final List<Holder<DamageType>> damageTypes;

    public DamageImmunityAbility(List<Holder<DamageType>> damageTypes, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.damageTypes = damageTypes;
    }

    public static boolean isImmuneAgainst(AbilityInstance<DamageImmunityAbility> entry, DamageSource source) {
        if (!entry.isEnabled()) {
            return false;
        }

        for (Holder<DamageType> tag : entry.getAbility().damageTypes) {
            if (tag.is(source.typeHolder())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbilitySerializer<DamageImmunityAbility> getSerializer() {
        return AbilitySerializers.DAMAGE_IMMUNITY.get();
    }

    public static class Serializer extends AbilitySerializer<DamageImmunityAbility> {

        @Override
        public MapCodec<DamageImmunityAbility> codec() {
            return CODEC;
        }
    }
}
