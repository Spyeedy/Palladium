package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

@Environment(EnvType.CLIENT)
public interface PlayerModelCacheExtension {

    PlayerModel<AbstractClientPlayer> palladium$getCachedModel();

}
