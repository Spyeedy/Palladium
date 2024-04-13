package net.threetag.palladium.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.client.model.animation.IAnimatablePlayer;
import net.threetag.palladium.entity.PlayerModelCacheExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin implements IAnimatablePlayer, PlayerModelCacheExtension {

    @Unique
    private ModifierLayer<IAnimation> palladium$modifierLayer;

    @Unique
    private final PlayerModel<AbstractClientPlayer> palladium$cachedModel = new PlayerModel<>(new ModelPart(Collections.emptyList(), Collections.emptyMap()), false);

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1000, this.palladium_getModifierLayer());
    }

    @Override
    public ModifierLayer<IAnimation> palladium_getModifierLayer() {
        if (this.palladium$modifierLayer == null) {
            this.palladium$modifierLayer = new ModifierLayer<>();
        }
        return this.palladium$modifierLayer;
    }

    @Override
    public PlayerModel<AbstractClientPlayer> palladium$getCachedModel() {
        return this.palladium$cachedModel;
    }
}
