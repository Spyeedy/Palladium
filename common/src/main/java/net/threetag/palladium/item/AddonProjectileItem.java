package net.threetag.palladium.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

public class AddonProjectileItem extends AddonItem {

    private final ResourceLocation entityTypeId;
    private final CompoundTag entityData;

    public AddonProjectileItem(ResourceLocation entityTypeId, CompoundTag entityData, Properties properties) {
        super(properties);
        this.entityTypeId = entityTypeId;
        this.entityData = entityData;
    }

    @Nullable
    public Projectile createProjectile(Level level, ItemStack stack, LivingEntity shooter) {
        CompoundTag compound = this.entityData == null ? new CompoundTag() : this.entityData;
        compound.putString("id", this.entityTypeId.toString());

        ServerLevel world = (ServerLevel) shooter.level();
        var entity = EntityType.loadEntityRecursive(compound, world, (en) -> {
            if (!(en instanceof Projectile projectile))
                return null;

            projectile.moveTo(shooter.getX(), shooter.getY() + shooter.getEyeHeight() - 0.1D, shooter.getZ(), projectile.getYRot(), projectile.getXRot());
            projectile.setOwner(shooter);

            return projectile;
        });

        return entity instanceof Projectile projectile ? projectile : null;
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public IAddonItem parse(JsonObject json, Properties properties) {
            ResourceLocation entityTypeId = GsonUtil.getAsResourceLocation(json, "entity_type", Palladium.id("custom_projectile"));
            CompoundTag entityData = null;

            if (json.has("entity_data")) {
                try {
                    entityData = TagParser.parseTag(GsonHelper.convertToJsonObject(json.get("entity_data"), "entity_data").toString());
                } catch (CommandSyntaxException e) {
                    AddonPackLog.warning("Failed to read entity_data for projectile item: " + e.getMessage());
                }
            }

            return new AddonProjectileItem(entityTypeId, entityData, properties);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Projectile Item");
            builder.setDescription("If added to the item tag for a bow or a crossbow, this item type can used as ammunitation to shoot pre-defined projectiles.");

            builder.addProperty("entity_type", ResourceLocation.class)
                    .description("ID of the entity type that should be shot. Only projectile entity works, like arrows, snowballs, or Palladium's custom projectile")
                    .fallback(Palladium.id("custom_projectile")).exampleJson(new JsonPrimitive(Palladium.id("custom_projectile").toString()));

            builder.addProperty("entity_data", CompoundTag.class)
                    .description("Custom NBT data for the shot entity")
                    .fallback(null).exampleJson(new JsonObject());
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("projectile");
        }
    }

}
