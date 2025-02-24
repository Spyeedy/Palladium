package net.threetag.palladium.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;

import java.util.function.Supplier;

public class PalladiumEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Palladium.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistryHolder<EntityType<EffectEntity>> EFFECT = register("effect", () -> EntityType.Builder.<EffectEntity>of(EffectEntity::new, MobCategory.MISC).sized(0.1F, 0.1F));

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void initRenderers() {
        EntityRendererRegistry.register(EFFECT, EffectEntityRenderer::new);
    }

    private static <T extends Entity> RegistryHolder<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITIES.register(id, () -> builderSupplier.get().build(ResourceKey.create(Registries.ENTITY_TYPE, Palladium.id(id))));
    }
}
