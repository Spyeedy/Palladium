package net.threetag.palladium.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.SkinChangeAbility;
import net.threetag.palladium.util.PlayerModelChangeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerSkinHandler {

    private static final List<Pair<Integer, SkinProvider>> PROVIDER = new ArrayList<>();

    public static ResourceLocation getCurrentSkin(GameProfile gameProfile, ResourceLocation defaultSkin) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player != null) {
            ResourceLocation start = defaultSkin;

            for (Pair<Integer, SkinProvider> pair : PROVIDER) {
                start = pair.getSecond().getSkin(player, start, defaultSkin);
            }

            return start;
        }

        return defaultSkin;
    }

    public static String getCurrentModelType(GameProfile gameProfile, String modelType) {
        AbstractClientPlayer player = (AbstractClientPlayer) Objects.requireNonNull(Minecraft.getInstance().level).getPlayerByUUID(gameProfile.getId());

        if (player != null && !PROVIDER.isEmpty()) {
            var overriddenType = PROVIDER.getLast().getSecond().getModelType(player);

            if (overriddenType == PlayerModelChangeType.KEEP) {
                return modelType;
            } else if (overriddenType == PlayerModelChangeType.NORMAL) {
                return "default";
            } else {
                return "slim";
            }
        }

        return modelType;
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
