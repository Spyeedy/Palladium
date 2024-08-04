package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.Openable;
import net.threetag.palladiumcore.network.NetworkManager;
import org.jetbrains.annotations.NotNull;

public class ToggleOpenableEquipmentPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ToggleOpenableEquipmentPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("toggle_openable_equipment"));
    public static final StreamCodec<FriendlyByteBuf, ToggleOpenableEquipmentPacket> STREAM_CODEC = StreamCodec.unit(new ToggleOpenableEquipmentPacket());

    public static void handle(ToggleOpenableEquipmentPacket packet, NetworkManager.Context context) {
        var opened = false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            var stack = context.getPlayer().getItemBySlot(slot);

            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
                opened = opened || openable.isOpen(stack);
            }
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            var stack = context.getPlayer().getItemBySlot(slot);

            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
                openable.setOpen(context.getPlayer(), stack, !opened);
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
