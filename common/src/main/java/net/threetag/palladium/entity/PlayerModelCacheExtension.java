package net.threetag.palladium.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Environment(EnvType.CLIENT)
public interface PlayerModelCacheExtension {

    PlayerModel palladium$getCachedModel();

    default void palladium$animateInFirstPerson(LocalPlayer player, PlayerRenderer playerRenderer) {
//        var renderState = playerRenderer.createRenderState(player, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        this.palladium$getCachedModel().resetPose();
//        this.palladium$getCachedModel().setupAnim(renderState);
        // TODO arm zRot keeps increasing without limit -> spins around player only in first person
    }

}
