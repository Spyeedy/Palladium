package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.unlocking.BuyableUnlockingHandler;

public record OpenAbilityBuyScreenPacket(AbilityReference reference, boolean available) implements CustomPacketPayload {

    public static final Type<OpenAbilityBuyScreenPacket> TYPE = new Type<>(Palladium.id("open_ability_buy_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAbilityBuyScreenPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, OpenAbilityBuyScreenPacket::reference,
            ByteBufCodecs.BOOL, OpenAbilityBuyScreenPacket::available,
            OpenAbilityBuyScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenAbilityBuyScreenPacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
            handleClient(packet, context);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(OpenAbilityBuyScreenPacket packet, NetworkManager.PacketContext context) {
        packet.reference.optional(context.getPlayer(), null).ifPresent(ability -> {
            if (Minecraft.getInstance().screen instanceof PowersScreen powersScreen && ability.getAbility().getStateManager().getUnlockingHandler() instanceof BuyableUnlockingHandler buy) {
                var overlayScreen = buy.getScreen(powersScreen, packet.reference, packet.available);

                if (overlayScreen != null) {
                    powersScreen.openOverlayScreen(overlayScreen);
                }
            }
        });
    }

}
