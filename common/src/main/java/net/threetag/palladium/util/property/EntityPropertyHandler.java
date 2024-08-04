package net.threetag.palladium.util.property;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.network.SyncPropertyPacket;
import net.threetag.palladiumcore.network.NetworkManager;

import java.util.Objects;
import java.util.Optional;

public class EntityPropertyHandler extends PropertyManager implements PropertyManager.Listener {

    final Entity entity;

    public EntityPropertyHandler(Entity entity) {
        this.entity = entity;
        this.setListener(this);
        PalladiumEvents.REGISTER_PROPERTY.invoker().register(this);
    }

    public Entity getEntity() {
        return entity;
    }

    public static Optional<EntityPropertyHandler> getHandler(Entity entity) {
        if (entity instanceof PalladiumEntityExtension ext) {
            return Optional.of(ext.palladium$getPropertyHandler());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public <T> void onChanged(PalladiumProperty<T> property, PalladiumPropertyValue<T> valueHolder, T oldValue, T newValue) {
        if (!entity.level().isClientSide) {
            if (Objects.equals(oldValue, newValue)) {
                if (property.getSyncType() == SyncOption.EVERYONE) {
                    NetworkManager.get().sendToPlayersInDimension((ServerLevel) entity.level(), new SyncPropertyPacket<>(entity.getId(), valueHolder));
                } else if (property.getSyncType() == SyncOption.SELF && this.entity instanceof ServerPlayer serverPlayer) {
                    NetworkManager.get().sendToPlayer(serverPlayer, new SyncPropertyPacket<>(serverPlayer.getId(), valueHolder));
                }
            }
        }
    }
}
