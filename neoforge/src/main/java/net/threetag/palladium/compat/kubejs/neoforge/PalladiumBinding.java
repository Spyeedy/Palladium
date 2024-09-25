package net.threetag.palladium.compat.kubejs.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.SuperpowerUtil;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladium.util.ScoreboardUtil;
import net.threetag.palladium.util.icon.Icon;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({"rawtypes", "InstantiationOfUtilityClass"})
public class PalladiumBinding {

    public final PowerUtil powers = new PowerUtil();
    public final SuperpowerUtil superpowers = new SuperpowerUtil();
    public final AbilityUtil abilities = new AbilityUtil();
    public final ScoreboardUtil scoreboard = new ScoreboardUtil();

    public void swingArm(LivingEntity entity, InteractionHand hand) {
        entity.swing(hand, true);
    }

    public Icon createItemIcon(ItemStack itemStack) {
        return new ItemIcon(itemStack);
    }

    public Icon createTextureIcon(ResourceLocation path) {
        return new TexturedIcon(path);
    }

    public Object getProperty(Entity entity, CharSequence key) {
        AtomicReference result = new AtomicReference();
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            PalladiumProperty property = handler.getPropertyByName(key.toString());

            if (property != null) {
                result.set(handler.get(property));
            }
        });

        return result.get();
    }

    public boolean hasProperty(Entity entity, String key) {
        AtomicBoolean result = new AtomicBoolean(false);
        EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
            result.set(handler.getPropertyByName(key) != null);
        });
        return result.get();
    }

    public void setItemInSlot(LivingEntity entity, @Nullable PlayerSlot slot, ItemStack stack) {
        if (slot != null) {
            slot.setItem(entity, stack);
        }
    }

    public ItemStack getItemInSlot(LivingEntity entity, @Nullable PlayerSlot slot) {
        if (slot != null) {
            return slot.getItems(entity).stream().findFirst().orElse(ItemStack.EMPTY);
        }
        return ItemStack.EMPTY;
    }
}
