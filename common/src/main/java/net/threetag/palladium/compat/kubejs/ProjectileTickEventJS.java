package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.entity.EntityEventJS;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.CustomProjectile;

public class ProjectileTickEventJS extends EntityEventJS {

    private final CustomProjectile projectile;

    public ProjectileTickEventJS(CustomProjectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public Entity getEntity() {
        return this.projectile;
    }

    public CustomProjectile getProjectile() {
        return this.projectile;
    }

    public boolean hasTag(String tag) {
        return this.projectile.getTags().contains(tag);
    }
}
