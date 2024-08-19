package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class CrossbowItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public CrossbowItem parse(JsonObject json, Item.Properties properties) {
        float velocityMultiplier = GsonHelper.getAsFloat(json, "velocity_multiplier", 1F);
        float inaccuracy = GsonHelper.getAsFloat(json, "inaccuracy", 1F);
        int useDuration = GsonHelper.getAsInt(json, "use_duration", 72000);
        TagKey<Item> projectiles = TagKey.create(Registries.ITEM, GsonUtil.getAsResourceLocation(json, "projectiles", ResourceLocation.withDefaultNamespace("arrows")));
        TagKey<Item> heldProjectiles = json.has("held_projectiles") ? TagKey.create(Registries.ITEM, GsonUtil.getAsResourceLocation(json, "held_projectiles")) : null;

        return new ExtCrossbowItem(velocityMultiplier, inaccuracy, useDuration, stack -> stack.is(projectiles), heldProjectiles == null ? null : stack -> stack.is(heldProjectiles), properties);
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Crossbow");

        builder.addProperty("velocity_multiplier", Float.class)
                .description("Velocity multiplier for the shot projectile, works differently to the bow one.")
                .fallback(1F).exampleJson(new JsonPrimitive(1F));

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
        return ResourceLocation.withDefaultNamespace("crossbow");
    }

    public static class ExtCrossbowItem extends net.minecraft.world.item.CrossbowItem {

        private final float velocityMultiplier, inaccuracy;
        private final int useDuration;
        private final Predicate<ItemStack> projectiles;
        @Nullable
        private final Predicate<ItemStack> heldProjectiles;

        public ExtCrossbowItem(float velocityMultiplier, float inaccuracy, int useDuration, Predicate<ItemStack> projectiles, @Nullable Predicate<ItemStack> heldProjectiles, Properties properties) {
            super(properties);
            this.velocityMultiplier = velocityMultiplier;
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
        public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
            ItemStack itemStack = player.getItemInHand(usedHand);
            ChargedProjectiles chargedProjectiles = (ChargedProjectiles) itemStack.get(DataComponents.CHARGED_PROJECTILES);
            if (chargedProjectiles != null && !chargedProjectiles.isEmpty()) {
                this.performShooting(level, player, usedHand, itemStack, getShootingPower(chargedProjectiles) * this.velocityMultiplier, this.inaccuracy, null);
                return InteractionResultHolder.consume(itemStack);
            } else if (!player.getProjectile(itemStack).isEmpty()) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(itemStack);
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }

    }
}