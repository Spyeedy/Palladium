package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.PlayerUtil;

import java.util.List;

public class PlaySoundAbility extends Ability {

    public static final MapCodec<PlaySoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("sound").forGetter(ab -> ab.sound),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("volume", 1F).forGetter(ab -> ab.volume),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("pitch", 1F).forGetter(ab -> ab.pitch),
                    Codec.BOOL.optionalFieldOf("looping", false).forGetter(ab -> ab.looping),
                    Codec.BOOL.optionalFieldOf("play_self", false).forGetter(ab -> ab.playSelf),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, PlaySoundAbility::new));

    public final ResourceLocation sound;
    public final float volume, pitch;
    public final boolean looping, playSelf;

    public PlaySoundAbility(ResourceLocation sound, float volume, float pitch, boolean looping, boolean playSelf, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = looping;
        this.playSelf = playSelf;
    }

    @Override
    public AbilitySerializer<PlaySoundAbility> getSerializer() {
        return AbilitySerializers.PLAY_SOUND.get();
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> instance) {
        if (this.looping) {
            if (Platform.getEnvironment() == Env.CLIENT) {
                this.startSound(entity, instance);
            }
        } else if (!entity.level().isClientSide) {
            if (this.playSelf) {
                if (entity instanceof Player player) {
                    PlayerUtil.playSound(player, entity.getX(), entity.getEyeY(), entity.getZ(), this.sound, entity.getSoundSource(), this.volume, this.pitch);
                }
            } else {
                PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 100, this.sound, entity.getSoundSource(), this.volume, this.pitch);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void startSound(LivingEntity entity, AbilityInstance<?> instance) {
        if (!this.playSelf || Minecraft.getInstance().player == entity) {
            // TODO
//            Minecraft.getInstance().getSoundManager().play(new AbilitySound(instance.getReference(), entity, this.sound, entity.getSoundSource(), this.volume, this.pitch));
        }
    }

    public static class Serializer extends AbilitySerializer<PlaySoundAbility> {

        @Override
        public MapCodec<PlaySoundAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, PlaySoundAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Plays a sound.")
                    .add("sound", TYPE_RESOURCE_LOCATION, "The sound that is being played.")
                    .addOptional("volume", TYPE_FLOAT, "The volume for the played sound.", 1F)
                    .addOptional("pitch", TYPE_FLOAT, "The pitch for the played sound.", 1F)
                    .addOptional("looping", TYPE_BOOLEAN,  "Whether or not the sound should loop during the time the ability is enabled.", false)
                    .addOptional("play_self", TYPE_BOOLEAN, "Whether or not the sound should be played to just the player executing the ability, or to all players.", false)
                    .setExampleObject(new PlaySoundAbility(ResourceLocation.withDefaultNamespace("item.elytra.flying"), 1F, 1F, false, false, AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
