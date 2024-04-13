package net.threetag.palladium.power.ability;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.particleemitter.ParticleEmitterManager;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ParticleTypeProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;

public class ParticleAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> PARTICLE_EMITTER = new ResourceLocationProperty("emitter").configurable("Configuration for where the particle spawns at. Check wiki for information.");
    public static final PalladiumProperty<ParticleType<?>> PARTICLE = new ParticleTypeProperty("particle").configurable("ID of the particle you want to spawn.");
    public static final PalladiumProperty<String> OPTIONS = new StringProperty("options").configurable("Additional options for the particle (like color of a dust particle)");

    public ParticleAbility() {
        this.withProperty(PARTICLE_EMITTER, new ResourceLocation("example:emitter"))
                .withProperty(PARTICLE, ParticleTypes.DUST)
                .withProperty(OPTIONS, "255 40 40");
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level().isClientSide) {
            this.tickClient(entity, entry);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Environment(EnvType.CLIENT)
    private void tickClient(LivingEntity entity, AbilityEntry entry) {
        if (entity instanceof AbstractClientPlayer player) {
            var emitter = ParticleEmitterManager.INSTANCE.get(entry.getProperty(PARTICLE_EMITTER));

            if (emitter != null) {
                try {
                    ParticleType type = entry.getProperty(PARTICLE);
                    ParticleOptions options = type.getDeserializer().fromCommand(type, new StringReader(" " + entry.getProperty(OPTIONS).trim() + " "));
                    emitter.spawnParticle(entity.level(), player, options, Minecraft.getInstance().getDeltaFrameTime());
                } catch (CommandSyntaxException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }
}
