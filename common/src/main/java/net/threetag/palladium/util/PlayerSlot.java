package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class PlayerSlot {

    public static final Codec<PlayerSlot> CODEC = Codec.STRING.xmap(PlayerSlot::get, Object::toString);
    public static final StreamCodec<FriendlyByteBuf, PlayerSlot> STREAM_CODEC = StreamCodec.of((buf, slot) -> buf.writeUtf(slot.toString()), buf -> Objects.requireNonNull(PlayerSlot.get(buf.readUtf())));

    private static final Map<EquipmentSlot, PlayerSlot> EQUIPMENT_SLOTS = new HashMap<>();
    private static final Map<String, PlayerSlot> SLOTS = new HashMap<>();

    @NotNull
    public static PlayerSlot get(EquipmentSlot slot) {
        return Objects.requireNonNull(get(slot.getName()));
    }

    @Nullable
    public static PlayerSlot get(String name) {
        if (name.equalsIgnoreCase("any") || name.equalsIgnoreCase("*")) {
            return AnySlot.INSTANCE;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getName().equalsIgnoreCase(name)) {
                return EQUIPMENT_SLOTS.computeIfAbsent(slot, EquipmentPlayerSlot::new);
            }
        }

        return null;
    }

    public abstract List<ItemStack> getItems(LivingEntity entity);

    public abstract void setItem(LivingEntity entity, ItemStack stack);

    public abstract void clear(LivingEntity entity);

    public abstract Type getType();

    @Nullable
    public EquipmentSlot getEquipmentSlot() {
        return null;
    }

    public static class AnySlot extends PlayerSlot {

        public static final AnySlot INSTANCE = new AnySlot();

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            List<ItemStack> list = new ArrayList<>();
            for (ItemStack slot : entity.getAllSlots()) {
                list.add(slot);
            }
            return list;
        }

        @Override
        public void setItem(LivingEntity entity, ItemStack stack) {
            // unsupported
        }

        @Override
        public void clear(LivingEntity entity) {
            // unsupported
        }

        @Override
        public Type getType() {
            return Type.ANY_SLOT;
        }

        @Override
        public String toString() {
            return "any";
        }
    }

    private static class EquipmentPlayerSlot extends PlayerSlot {

        private final EquipmentSlot slot;

        public EquipmentPlayerSlot(EquipmentSlot slot) {
            this.slot = slot;
        }

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            return Collections.singletonList(entity.getItemBySlot(this.slot));
        }

        @Override
        public void setItem(LivingEntity entity, ItemStack stack) {
            entity.setItemSlot(this.slot, stack);
        }

        @Override
        public void clear(LivingEntity entity) {
            entity.setItemSlot(this.slot, ItemStack.EMPTY);
        }

        @Override
        public @Nullable EquipmentSlot getEquipmentSlot() {
            return this.slot;
        }

        @Override
        public Type getType() {
            return Type.EQUIPMENT_SLOT;
        }

        @Override
        public String toString() {
            return this.slot.getName();
        }
    }

    public enum Type {

        ANY_SLOT,
        EQUIPMENT_SLOT,

    }

}
