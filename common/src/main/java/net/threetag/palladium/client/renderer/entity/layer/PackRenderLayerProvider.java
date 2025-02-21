package net.threetag.palladium.client.renderer.entity.layer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.entity.PlayerSlot;
import net.threetag.palladium.power.ability.AbilityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class PackRenderLayerProvider {

    private static final List<Provider> PROVIDERS = new ArrayList<>();

    public static void register(Provider provider) {
        PROVIDERS.add(provider);
    }

    public static void forEach(Entity entity, BiConsumer<DataContext, PackRenderLayer> consumer) {
        Lookup lookup = PackRenderLayerManager.INSTANCE::get;
        for (Provider provider : PROVIDERS) {
            provider.provide(entity, lookup, consumer);
        }
    }

    static {
        // Abilities
        register((entity, lookup, layers) -> {
            if (entity instanceof LivingEntity living) {
                for (var instance : AbilityUtil.getEnabledInstances(living)) {
                    for (ResourceLocation layerId : instance.getAbility().getProperties().getRenderLayers()) {
                        var layer = lookup.get(layerId);
                        if (layer != null) {
                            layers.accept(DataContext.forAbility(living, instance), layer);
                        }
                    }
                }
            }
        });

        // Items
        register((entity, lookup, layers) -> {
            if (entity instanceof LivingEntity living) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    var stack = living.getItemBySlot(slot);
                    if (stack.has(PalladiumDataComponents.Items.RENDER_LAYERS.get())) {
                        var itemLayers = stack.get(PalladiumDataComponents.Items.RENDER_LAYERS.get());

                        for (ResourceLocation layerId : Objects.requireNonNull(itemLayers).forSlot(PlayerSlot.get(slot))) {
                            var layer = lookup.get(layerId);
                            if (layer != null) {
                                layers.accept(DataContext.forArmorInSlot(living, slot), layer);
                            }
                        }
                    }
                }
            }
        });
    }

    @FunctionalInterface
    public interface Provider {

        void provide(Entity entity, Lookup lookup, BiConsumer<DataContext, PackRenderLayer> layers);

    }

    @FunctionalInterface
    public interface Lookup {

        PackRenderLayer get(ResourceLocation id);

    }

}
