package net.threetag.palladium.client.renderer.entity.layer;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;

public class EntityRenderLayers extends PalladiumEntityData<LivingEntity> {

    protected EntityRenderLayers(LivingEntity entity) {
        super(entity);
    }

    @Override
    public void load(CompoundTag nbt, HolderLookup.Provider registryLookup) {

    }

    @Override
    public CompoundTag save(HolderLookup.Provider registryLookup) {
        return new CompoundTag();
    }

    public static EntityRenderLayers create(LivingEntity living) {
        if(Platform.getEnvironment() == Env.CLIENT) {
            return new ClientEntityRenderLayers(living);
        } else {
            return new EntityRenderLayers(living);
        }
    }

}
