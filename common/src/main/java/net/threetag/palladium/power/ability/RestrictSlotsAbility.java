package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.PlayerSlot;

import java.util.List;

public class RestrictSlotsAbility extends Ability {

    public static final MapCodec<RestrictSlotsAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecUtils.listOrPrimitive(PlayerSlot.CODEC).fieldOf("slots").forGetter(ab -> ab.slots),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, RestrictSlotsAbility::new));

    public final List<PlayerSlot> slots;

    public RestrictSlotsAbility(List<PlayerSlot> slots, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.slots = slots;
    }

    @Override
    public AbilitySerializer<RestrictSlotsAbility> getSerializer() {
        return AbilitySerializers.RESTRICT_SLOTS.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> entry, PowerHolder holder, boolean enabled) {
        if (enabled && !entity.level().isClientSide) {
            for (PlayerSlot slot : this.slots) {
                var items = slot.getItems(entity);
                slot.clear(entity);
                for (ItemStack item : items) {
                    if (!item.isEmpty()) {
                        this.drop(entity, item, slot);
                    }
                }
            }
        }
    }

    public void drop(LivingEntity entity, ItemStack stack, PlayerSlot slot) {
        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, true);
            }
        } else {
            entity.spawnAtLocation(stack);
        }
    }

    public static boolean isRestricted(LivingEntity entity, EquipmentSlot slot) {
        for (AbilityInstance<RestrictSlotsAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : instance.getAbility().slots) {
                if (playerSlot.getEquipmentSlot() == slot) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isRestricted(LivingEntity entity, String key) {
        for (AbilityInstance<RestrictSlotsAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.RESTRICT_SLOTS.get())) {
            for (PlayerSlot playerSlot : instance.getAbility().slots) {
                if (playerSlot.toString().equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class Serializer extends AbilitySerializer<RestrictSlotsAbility> {

        @Override
        public MapCodec<RestrictSlotsAbility> codec() {
            return CODEC;
        }
    }
}
