package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeModifierAbility extends Ability {

    public static final MapCodec<AttributeModifierAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(ab -> ab.attribute),
                    Codec.DOUBLE.fieldOf("amount").forGetter(ab -> ab.amount),
                    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(ab -> ab.operation),
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ab -> ab.id),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, AttributeModifierAbility::new));

    public final Holder<Attribute> attribute;
    public final double amount;
    public final AttributeModifier.Operation operation;
    public final ResourceLocation id;

    public AttributeModifierAbility(Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation, ResourceLocation id, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
        this.id = id;
    }

    @Override
    public AbilitySerializer<AttributeModifierAbility> getSerializer() {
        return AbilitySerializers.ATTRIBUTE_MODIFIER.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> ability, boolean enabled) {
        if (enabled) {
            AttributeInstance attributeInstance = entity.getAttribute(this.attribute);

            if (attributeInstance == null || entity.level().isClientSide) {
                return;
            }

            AttributeModifier modifier = attributeInstance.getModifier(id);

            // Remove modifier if amount or operation dont match
            if (modifier != null && (modifier.amount() != this.amount || modifier.operation() != this.operation)) {
                attributeInstance.removeModifier(this.id);
                modifier = null;
            }

            if (modifier == null) {
                modifier = new AttributeModifier(this.id, this.amount, this.operation);
                attributeInstance.addTransientModifier(modifier);
            }
        } else {
            this.lastTick(entity, ability);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> instance) {
        var attributeInstance = entity.getAttribute(this.attribute);
        if (attributeInstance != null && attributeInstance.getModifier(this.id) != null) {
            attributeInstance.removeModifier(this.id);
        }
    }

    public static String getAttributeList() {
        return BuiltInRegistries.ATTRIBUTE.keySet().stream().map(ResourceLocation::toString).sorted(Comparator.naturalOrder()).collect(Collectors.joining(", "));
    }

    public static class Serializer extends AbilitySerializer<AttributeModifierAbility> {

        @Override
        public MapCodec<AttributeModifierAbility> codec() {
            return CODEC;
        }
    }
}
