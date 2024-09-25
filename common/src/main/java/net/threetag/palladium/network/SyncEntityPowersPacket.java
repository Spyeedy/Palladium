package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.PowerValidator;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.network.NetworkManager;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SyncEntityPowersPacket(int entityId, List<ResourceLocation> toRemove, List<ResourceLocation> toAdd,
                                     List<Triple<EnergyBarReference, Integer, Integer>> energyBars) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncEntityPowersPacket> TYPE = new CustomPacketPayload.Type<>(Palladium.id("sync_entity_powers"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityPowersPacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeInt(packet.entityId);
        buf.writeCollection(packet.toRemove, FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(packet.toAdd, FriendlyByteBuf::writeResourceLocation);
        buf.writeCollection(packet.energyBars, (buf1, pair) -> {
            pair.getLeft().toBuffer(buf1);
            buf1.writeInt(pair.getMiddle());
            buf1.writeInt(pair.getRight());
        });
    }, buf -> {
        var entityId = buf.readInt();
        var toRemove = buf.readList(FriendlyByteBuf::readResourceLocation);
        var toAdd = buf.readList(FriendlyByteBuf::readResourceLocation);
        var energyBars = buf.readList(buf1 -> {
            var ref = EnergyBarReference.fromBuffer(buf1);
            int val = buf1.readInt();
            int max = buf1.readInt();
            return Triple.of(ref, val, max);
        });

        return new SyncEntityPowersPacket(entityId, toRemove, toAdd, energyBars);
    });

    public static void handle(SyncEntityPowersPacket packet, NetworkManager.Context context) {
        if (context.isClient()) {
            handleClient(packet, context);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncEntityPowersPacket packet, NetworkManager.Context context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            Registry<Power> registry = level.registryAccess().registryOrThrow(PalladiumRegistryKeys.POWER);
            PowerUtil.getPowerHandler(livingEntity).ifPresent(handler -> {
                for (ResourceLocation powerId : packet.toRemove) {
                    handler.removePowerHolder(powerId);
                }

                for (ResourceLocation powerId : packet.toAdd) {
                    // TODO component tag
                    handler.setPowerHolder(new PowerHolder(livingEntity, registry.getHolder(powerId).orElseThrow(), PowerValidator.ALWAYS_ACTIVE, new CompoundTag()));
                }
            });

            for (Triple<EnergyBarReference, Integer, Integer> pair : packet.energyBars) {
                var bar = pair.getLeft().getBar(livingEntity);

                if (bar != null) {
                    bar.set(pair.getMiddle());
                    bar.setMax(pair.getRight());
                }
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
