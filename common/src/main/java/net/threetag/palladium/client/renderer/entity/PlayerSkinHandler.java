package net.threetag.palladium.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.SkinChangeAbility;
import net.threetag.palladium.entity.PlayerModelChangeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerSkinHandler {

    private static final List<Pair<Integer, SkinProvider>> PROVIDER = new ArrayList<>();

    public static PlayerSkin getCurrentSkin(GameProfile gameProfile, PlayerSkin original) {
        if (PROVIDER.isEmpty()) {
            return original;
        }

        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player != null) {
            ResourceLocation startSkin = original.texture();
            var modelType = original.model();

            for (Pair<Integer, SkinProvider> pair : PROVIDER) {
                startSkin = pair.getSecond().getSkin(player, startSkin, original.texture());
                modelType = pair.getSecond().getModelType(player).toPlayerSkinModelType(modelType);
            }

            if (original.model() == modelType && startSkin.equals(original.texture())) {
                return original;
            }

            return new PlayerSkin(startSkin, original.textureUrl(), original.capeTexture(), original.elytraTexture(), modelType, original.secure());
        }

        return original;
    }

    public static void registerSkinProvider(int priority, SkinProvider provider) {
        PROVIDER.add(Pair.of(priority, provider));
        PROVIDER.sort((p1, p2) -> p2.getFirst() - p1.getFirst());
    }

    public interface SkinProvider {

        ResourceLocation getSkin(AbstractClientPlayer player, ResourceLocation previousSkin, ResourceLocation defaultSkin);

        default PlayerModelChangeType getModelType(AbstractClientPlayer player) {
            return PlayerModelChangeType.KEEP;
        }

    }

    static {
        // Abilities
        registerSkinProvider(30, new SkinChangeAbility.SkinProvider());
    }

}
