package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.Collections;
import java.util.List;

public class RenderLayerAbility extends Ability implements RenderLayerProviderAbility<RenderLayerAbility> {

    // TODO

    public static final MapCodec<RenderLayerAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(ResourceLocation.CODEC).fieldOf("render_layer").forGetter(ab -> ab.renderLayerId),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, RenderLayerAbility::new));

    public final List<ResourceLocation> renderLayerId;

    public RenderLayerAbility(List<ResourceLocation> renderLayerId, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.renderLayerId = renderLayerId;
    }

//    @Override
//    @Environment(EnvType.CLIENT)
//    public IPackRenderLayer getRenderLayer(AbilityInstance<RenderLayerAbility> instance, LivingEntity entity, PackRenderLayerManager manager) {
//        return manager.getLayer(this.renderLayerId);
//    }

    @Override
    public AbilitySerializer<RenderLayerAbility> getSerializer() {
        return AbilitySerializers.RENDER_LAYER.get();
    }

    public static class Serializer extends AbilitySerializer<RenderLayerAbility> {

        @Override
        public MapCodec<RenderLayerAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, RenderLayerAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Allows you to add one or more render layers to the entity.")
                    .add("render_layer", Documented.typeListOrPrimitive(TYPE_RESOURCE_LOCATION), "The render layer(s) to add to the entity.")
                    .setExampleObject(new RenderLayerAbility(List.of(ResourceLocation.fromNamespaceAndPath("example", "render_layer_id")), AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
