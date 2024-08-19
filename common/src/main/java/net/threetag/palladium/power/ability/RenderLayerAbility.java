package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class RenderLayerAbility extends Ability implements RenderLayerProviderAbility {

    public static final MapCodec<RenderLayerAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("render_layer").forGetter(ab -> ab.renderLayerId),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, RenderLayerAbility::new));

    public final ResourceLocation renderLayerId;

    public RenderLayerAbility(ResourceLocation renderLayerId, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.renderLayerId = renderLayerId;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public IPackRenderLayer getRenderLayer(AbilityInstance instance, LivingEntity entity, PackRenderLayerManager manager) {
        return manager.getLayer(this.renderLayerId);
    }

    @Override
    public AbilitySerializer<RenderLayerAbility> getSerializer() {
        return AbilitySerializers.RENDER_LAYER.get();
    }

    public static class Serializer extends AbilitySerializer<RenderLayerAbility> {

        @Override
        public MapCodec<RenderLayerAbility> codec() {
            return CODEC;
        }
    }
}
