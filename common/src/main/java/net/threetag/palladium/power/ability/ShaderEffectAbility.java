package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class ShaderEffectAbility extends Ability {

    public static final MapCodec<ShaderEffectAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("shader").forGetter(ab -> ab.shader),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, ShaderEffectAbility::new));

    public final ResourceLocation shader;

    public ShaderEffectAbility(ResourceLocation shader, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.shader = shader;
    }

    @Override
    public AbilitySerializer<ShaderEffectAbility> getSerializer() {
        return AbilitySerializers.SHADER_EFFECT.get();
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> entry, PowerHolder holder, boolean enabled) {
        if (enabled && Platform.getEnvironment() == Env.CLIENT) {
            this.applyShader(entity, this.shader);
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> entry, PowerHolder holder, boolean enabled) {
        if (enabled && Platform.getEnvironment() == Env.CLIENT) {
            this.removeShader(entity, this.shader);
        }
    }

    @Environment(EnvType.CLIENT)
    public void applyShader(LivingEntity entity, ResourceLocation shader) {
        var mc = Minecraft.getInstance();

        if (entity == mc.player) {
            // TODO mixin into checkPostEffect
            mc.gameRenderer.setPostEffect(shader);
        }
    }

    @Environment(EnvType.CLIENT)
    public void removeShader(LivingEntity entity, ResourceLocation shader) {
        var mc = Minecraft.getInstance();
        var current = mc.gameRenderer.currentPostEffect();

        if (entity == mc.player && current != null && current.equals(shader)) {
            mc.gameRenderer.clearPostEffect();
        }
    }

    @Environment(EnvType.CLIENT)
    public static ResourceLocation get(Player player) {
        for (AbilityInstance<ShaderEffectAbility> instance : AbilityUtil.getEnabledInstances(player, AbilitySerializers.SHADER_EFFECT.get())) {
            return instance.getAbility().shader;
        }
        return null;
    }

    public static class Serializer extends AbilitySerializer<ShaderEffectAbility> {

        @Override
        public MapCodec<ShaderEffectAbility> codec() {
            return CODEC;
        }
    }
}
