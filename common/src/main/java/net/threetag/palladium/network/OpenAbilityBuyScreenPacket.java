package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.power.BuyAbilityScreen;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public record OpenAbilityBuyScreenPacket(AbilityReference reference, AbilityConfiguration.UnlockData unlockData,
                                         boolean available) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OpenAbilityBuyScreenPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("open_ability_buy_screen"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAbilityBuyScreenPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, OpenAbilityBuyScreenPacket::reference,
            AbilityConfiguration.UnlockData.STREAM_CODEC, OpenAbilityBuyScreenPacket::unlockData,
            ByteBufCodecs.BOOL, OpenAbilityBuyScreenPacket::available,
            OpenAbilityBuyScreenPacket::new
    );

    @Environment(EnvType.CLIENT)
    public static void handle(OpenAbilityBuyScreenPacket packet, NetworkManager.Context context) {
        if (Minecraft.getInstance().screen instanceof PowersScreen powersScreen) {
            AbilityInstance entry = packet.reference.getInstance(Minecraft.getInstance().player);

            if (entry != null) {
                powersScreen.openOverlayScreen(new BuyAbilityScreen(packet.reference, packet.unlockData, packet.available, powersScreen));
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}