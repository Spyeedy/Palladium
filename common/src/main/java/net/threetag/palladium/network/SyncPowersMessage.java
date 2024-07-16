package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.Power;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record SyncPowersMessage(Map<ResourceLocation, Power> powers) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncPowersMessage> TYPE = new Type<>(Palladium.id("sync_powers"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPowersMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ResourceLocation.STREAM_CODEC,
                    UUIDUtil.STREAM_CODEC
            ), TeamMembers::members,
            SyncPowersMessage::new);

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        ClientPowerManager.updatePowers(this.powers);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
