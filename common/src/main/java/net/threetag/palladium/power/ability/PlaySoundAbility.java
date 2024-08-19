package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.sound.AbilitySound;
import net.threetag.palladium.util.CodecUtils;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladiumcore.util.Platform;

import java.util.List;

public class PlaySoundAbility extends Ability {

    public static final MapCodec<PlaySoundAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("sound").forGetter(ab -> ab.sound),
                    CodecUtils.NON_NEGATIVE_FLOAT.optionalFieldOf("volume", 1F).forGetter(ab -> ab.volume),
                    CodecUtils.NON_NEGATIVE_FLOAT.optionalFieldOf("pitch", 1F).forGetter(ab -> ab.pitch),
                    Codec.BOOL.optionalFieldOf("looping", false).forGetter(ab -> ab.looping),
                    Codec.BOOL.optionalFieldOf("play_self", false).forGetter(ab -> ab.playSelf),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, PlaySoundAbility::new));

    public final ResourceLocation sound;
    public final float volume, pitch;
    public final boolean looping, playSelf;

    public PlaySoundAbility(ResourceLocation sound, float volume, float pitch, boolean looping, boolean playSelf, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
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
    public void firstTick(LivingEntity entity, AbilityInstance<?> instance, PowerHolder holder, boolean enabled) {
        if (enabled) {
            if (this.looping) {
                if (Platform.isClient()) {
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
    }

    @Environment(EnvType.CLIENT)
    public void startSound(LivingEntity entity, AbilityInstance<?> instance) {
        if (!this.playSelf || Minecraft.getInstance().player == entity) {
            Minecraft.getInstance().getSoundManager().play(new AbilitySound(instance.getReference(), entity, this.sound, entity.getSoundSource(), this.volume, this.pitch));
        }
    }

    public static class Serializer extends AbilitySerializer<PlaySoundAbility> {

        @Override
        public MapCodec<PlaySoundAbility> codec() {
            return CODEC;
        }
    }
}
