package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
//import net.threetag.palladiumcore.util.PlayerUtil;

import java.util.List;

public class NameChangeAbility extends Ability {

    public static final MapCodec<NameChangeAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("name").forGetter(ab -> ab.name),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, NameChangeAbility::new));

    public final Component name;

    public NameChangeAbility(Component name, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.name = name;
    }

    @Override
    public AbilitySerializer<?> getSerializer() {
        return AbilitySerializers.NAME_CHANGE.get();
    }

    @Override
    public void registerDataComponents(DataComponentMap.Builder components) {
        components.set(PalladiumDataComponents.Abilities.NAME_CHANGE_ACTIVE.get(), false);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> instance) {
        if (entity instanceof Player player) {
            instance.set(PalladiumDataComponents.Abilities.NAME_CHANGE_ACTIVE.get(), true);
            // TODO
//            PlayerUtil.refreshDisplayName(player);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> instance) {
        if (entity instanceof Player player) {
            instance.set(PalladiumDataComponents.Abilities.NAME_CHANGE_ACTIVE.get(), false);
//            PlayerUtil.refreshDisplayName(player);
        }
    }

    public static class Serializer extends AbilitySerializer<NameChangeAbility> {

        @Override
        public MapCodec<NameChangeAbility> codec() {
            return CODEC;
        }
    }
}
