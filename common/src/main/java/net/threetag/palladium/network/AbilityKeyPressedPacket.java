package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record AbilityKeyPressedPacket(AbilityReference reference, boolean pressed) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AbilityKeyPressedPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("ability_key_pressed"));
    public static final StreamCodec<FriendlyByteBuf, AbilityKeyPressedPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityKeyPressedPacket::reference,
            ByteBufCodecs.BOOL, AbilityKeyPressedPacket::pressed,
            AbilityKeyPressedPacket::new
    );

    public static void handle(AbilityKeyPressedPacket packet, NetworkManager.Context context) {
        AbilityInstance entry = packet.reference.getInstance(context.getPlayer());

        if (entry != null && entry.isUnlocked()) {
            entry.keyPressed(context.getPlayer(), packet.pressed);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
