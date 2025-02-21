package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.CodecExtras;

import java.util.List;

public class ParticleAbility extends Ability {

    public static final MapCodec<ParticleAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CodecExtras.listOrPrimitive(ResourceLocation.CODEC).fieldOf("emitter").forGetter(ab -> ab.particleEmitterIds),
                    BuiltInRegistries.PARTICLE_TYPE.holderByNameCodec().fieldOf("particle_type").forGetter(ab -> ab.particleTypeHolder),
                    CompoundTag.CODEC.optionalFieldOf("options", new CompoundTag()).forGetter(ab -> ab.options),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, ParticleAbility::new));

    public final List<ResourceLocation> particleEmitterIds;
    public final Holder<ParticleType<?>> particleTypeHolder;
    public final CompoundTag options;

    public ParticleAbility(List<ResourceLocation> particleEmitterIds, Holder<ParticleType<?>> particleTypeHolder, CompoundTag options, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.particleEmitterIds = particleEmitterIds;
        this.particleTypeHolder = particleTypeHolder;
        this.options = options;
    }

    @Override
    public AbilitySerializer<ParticleAbility> getSerializer() {
        return AbilitySerializers.PARTICLES.get();
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> instance, boolean enabled) {
        if (enabled && entity.level().isClientSide) {
            this.tickClient(entity);
        }
    }

    @Environment(EnvType.CLIENT)
    private void tickClient(LivingEntity entity) {
        if (entity instanceof AbstractClientPlayer player) {
            ParticleType<?> type = this.particleTypeHolder.value();
            ParticleOptions options = type.codec().codec().parse(entity.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.options).getOrThrow();
            for (ResourceLocation id : this.particleEmitterIds) {
                var emitter = ParticleEmitterManager.INSTANCE.get(id);

                if (emitter != null) {
                    emitter.spawnParticles(entity.level(), player, options, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
                }
            }
        }
    }

    public static class Serializer extends AbilitySerializer<ParticleAbility> {

        @Override
        public MapCodec<ParticleAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ParticleAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Spawns particles around the entity.")
                    .add("emitter", Documented.typeListOrPrimitive(TYPE_RESOURCE_LOCATION), "List of emitter IDs where the particles spawn at.")
                    .add("particle_type", TYPE_PARTICLE_TYPE, "ID of the particle you want to spawn.")
                    .addOptional("options", TYPE_NBT, "Additional options for the particle (like color of a dust particle).")
                    .setExampleObject(new ParticleAbility(List.of(ResourceLocation.fromNamespaceAndPath("example", "emitter_id")), provider.get(ResourceKey.create(Registries.PARTICLE_TYPE, ResourceLocation.withDefaultNamespace("dust"))).get(), null, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
