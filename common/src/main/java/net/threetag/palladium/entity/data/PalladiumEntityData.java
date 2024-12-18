package net.threetag.palladium.entity.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.core.event.PalladiumEntityEvents;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public abstract class PalladiumEntityData<T extends Entity> {

    private final T entity;

    protected PalladiumEntityData(T entity) {
        this.entity = entity;
    }

    public abstract void load(CompoundTag nbt, HolderLookup.Provider registryLookup);

    public abstract CompoundTag save(HolderLookup.Provider registryLookup);

    public void tick() {
    }

    public T getEntity() {
        return this.entity;
    }

    @SuppressWarnings("unchecked")
    public static <T extends PalladiumEntityData<?>> T get(Entity entity, PalladiumEntityDataType<T> type) {
        return (T) ((PalladiumEntityExtension) entity).palladium$getDataMap().get(type);
    }

    public static void registerEvents() {
        PalladiumEntityEvents.TICK_POST.register(entity -> {
            for (PalladiumEntityDataType<?> type : entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.ENTITY_DATA_TYPE)) {
                var data = get(entity, type);

                if (data != null) {
                    data.tick();
                }
            }
        });
    }

}
