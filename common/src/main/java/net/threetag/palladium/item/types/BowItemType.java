package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BowItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public BowItem parse(JsonObject json, Item.Properties properties) {
        float velocity = GsonHelper.getAsFloat(json, "velocity", 3F);
        float inaccuracy = GsonHelper.getAsFloat(json, "inaccuracy", 1F);
        int useDuration = GsonHelper.getAsInt(json, "use_duration", 72000);
        TagKey<Item> projectiles = TagKey.create(Registries.ITEM, GsonUtil.getAsResourceLocation(json, "projectiles", ResourceLocation.withDefaultNamespace("arrows")));
        TagKey<Item> heldProjectiles = json.has("held_projectiles") ? TagKey.create(Registries.ITEM, GsonUtil.getAsResourceLocation(json, "held_projectiles")) : null;

        return new ExtBowItem(velocity, inaccuracy, useDuration, stack -> stack.is(projectiles), heldProjectiles == null ? null : stack -> stack.is(heldProjectiles), properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Bow");

        builder.addProperty("velocity", Float.class)
                .description("Velocity multiplier for the shot projectile")
                .fallback(3F).exampleJson(new JsonPrimitive(3F));

        builder.addProperty("inaccuracy", Float.class)
                .description("Inaccuracy for the shot projectile")
                .fallback(1F).exampleJson(new JsonPrimitive(1F));

        builder.addProperty("use_duration", Integer.class)
                .description("Amount of ticks the bow can be used for")
                .fallback(72000).exampleJson(new JsonPrimitive(72000));

        builder.addProperty("projectiles", ResourceLocation.class)
                .description("Item tag which contains all items that can be shot. By default all Minecraft arrows")
                .fallback(ResourceLocation.withDefaultNamespace("arrows")).exampleJson(new JsonPrimitive("minecraft:arrows"));

        builder.addProperty("held_projectiles", ResourceLocation.class)
                .description("Item tag which contains all items that can be shot by being in the off hand. Can be left out to fallback to the 'projectiles' option")
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:arrows"));
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("bow");
    }

    public static class ExtBowItem extends BowItem {

        private final float velocity, inaccuracy;
        private final int useDuration;
        private final Predicate<ItemStack> projectiles;
        @Nullable
        private final Predicate<ItemStack> heldProjectiles;

        public ExtBowItem(float velocity, float inaccuracy, int useDuration, Predicate<ItemStack> projectiles, @Nullable Predicate<ItemStack> heldProjectiles, Properties properties) {
            super(properties);
            this.velocity = velocity;
            this.inaccuracy = inaccuracy;
            this.useDuration = useDuration;
            this.projectiles = projectiles;
            this.heldProjectiles = heldProjectiles;
        }

        @Override
        public int getUseDuration(ItemStack stack, LivingEntity entity) {
            return this.useDuration;
        }

        @Override
        public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
            return this.projectiles;
        }

        @Override
        public @NotNull Predicate<ItemStack> getSupportedHeldProjectiles() {
            return this.heldProjectiles == null ? this.getAllSupportedProjectiles() : this.heldProjectiles;
        }

        @Override
        public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
            if (livingEntity instanceof Player player) {
                ItemStack itemStack = player.getProjectile(stack);
                if (!itemStack.isEmpty()) {
                    int i = this.getUseDuration(stack, livingEntity) - timeCharged;
                    float f = getPowerForTime(i);
                    if (!((double) f < 0.1)) {
                        List<ItemStack> list = draw(stack, itemStack, player);
                        if (level instanceof ServerLevel serverLevel) {
                            if (!list.isEmpty()) {
                                this.shoot(serverLevel, player, player.getUsedItemHand(), stack, list, f * this.velocity, this.inaccuracy, f == 1.0F, null);
                            }
                        }

                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                        player.awardStat(Stats.ITEM_USED.get(this));
                    }
                }
            }
        }
    }
}
