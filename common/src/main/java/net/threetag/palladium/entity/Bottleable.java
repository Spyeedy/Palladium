package net.threetag.palladium.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.threetag.palladium.component.PalladiumDataComponents;

import java.util.Optional;

public interface Bottleable {

    boolean fromBottle();

    void setFromBottle(boolean fromBottle);

    void saveToBottleTag(ItemStack stack);

    void loadFromBottleTag(CompoundTag tag);

    ItemStack getBottleItemStack();

    SoundEvent getPickupSound();

    /**
     * @deprecated
     */
    @Deprecated
    static void saveDefaultDataToBottleTag(Mob mob, ItemStack bottle) {
        bottle.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        CustomData.update(PalladiumDataComponents.Items.BOTTLE_ENTITY_DATA.get(), bottle, (compoundTag) -> {
            if (mob.isNoAi()) {
                compoundTag.putBoolean("NoAI", mob.isNoAi());
            }

            if (mob.isSilent()) {
                compoundTag.putBoolean("Silent", mob.isSilent());
            }

            if (mob.isNoGravity()) {
                compoundTag.putBoolean("NoGravity", mob.isNoGravity());
            }

            if (mob.hasGlowingTag()) {
                compoundTag.putBoolean("Glowing", mob.hasGlowingTag());
            }

            if (mob.isInvulnerable()) {
                compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
            }

            compoundTag.putFloat("Health", mob.getHealth());
        });
    }

    /**
     * @deprecated
     */
    @Deprecated
    static void loadDefaultDataFromBottleTag(Mob mob, CompoundTag tag) {
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI"));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent"));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity"));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing"));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        }

        if (tag.contains("Health", 99)) {
            mob.setHealth(tag.getFloat("Health"));
        }

    }

    static <T extends LivingEntity & Bottleable> Optional<InteractionResult> bottleMobPickup(Player player, InteractionHand hand, T entity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.GLASS_BOTTLE && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemStack2 = entity.getBottleItemStack();
            entity.saveToBottleTag(itemStack2);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(hand, itemStack3);
            Level level = entity.level();
//            if (!level.isClientSide) {
//                CriteriaTriggers.FILLED_BOTTLE.trigger((ServerPlayer)player, itemStack2);
//            }

            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }

}
