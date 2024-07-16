package net.threetag.palladium.compat.kubejs.neoforge;

import dev.latvian.mods.kubejs.entity.KubeEntityEvent;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.CustomProjectile;

public record ProjectileTickEventJS(CustomProjectile projectile) implements KubeEntityEvent {

    @Override
    public Entity getEntity() {
        return this.projectile;
    }

    public boolean hasTag(String tag) {
        return this.projectile.getTags().contains(tag);
    }
}
