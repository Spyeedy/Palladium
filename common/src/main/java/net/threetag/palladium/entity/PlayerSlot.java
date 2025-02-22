package net.threetag.palladium.entity;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.compat.accessories.AccessoriesCompat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class PlayerSlot {

    public static final Codec<PlayerSlot> CODEC = Codec.STRING.xmap(PlayerSlot::get, Object::toString);
    public static final StreamCodec<FriendlyByteBuf, PlayerSlot> STREAM_CODEC = StreamCodec.of((buf, slot) -> buf.writeUtf(slot.toString()), buf -> Objects.requireNonNull(PlayerSlot.get(buf.readUtf())));

    private static final Map<EquipmentSlot, PlayerSlot> EQUIPMENT_SLOTS = new HashMap<>();
    private static final Map<String, PlayerSlot> SLOTS = new HashMap<>();

    static {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            get(slot);
        }
    }

    @NotNull
    public static PlayerSlot get(EquipmentSlot slot) {
        return Objects.requireNonNull(EQUIPMENT_SLOTS.computeIfAbsent(slot, equipmentSlot -> SLOTS.computeIfAbsent(equipmentSlot.getName(), s -> new EquipmentPlayerSlot(equipmentSlot))));
    }

    @Nullable
    public static PlayerSlot get(String name) {
        if (name.equalsIgnoreCase("any") || name.equalsIgnoreCase("*")) {
            return AnySlot.INSTANCE;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getName().equalsIgnoreCase(name)) {
                return EQUIPMENT_SLOTS.computeIfAbsent(slot, equipmentSlot -> SLOTS.computeIfAbsent(equipmentSlot.getName(), s -> new EquipmentPlayerSlot(equipmentSlot)));
            }
        }

        if (name.startsWith("accessories:")) {
            return SLOTS.computeIfAbsent(name, s -> new AccessoriesSlot(s.substring("accessories:".length())));
        }

        return null;
    }

    public static Collection<PlayerSlot> values(Level level) {
        for (String slot : AccessoriesCompat.INSTANCE.getSlots(level)) {
            var slotName = slot.startsWith("accessories:") ? slot : "accessories:" + slot;
            get(slotName);
        }

        return SLOTS.values();
    }

    public abstract List<ItemStack> getItems(LivingEntity entity);

    public abstract ItemStack getItem(LivingEntity entity, int index);

    public abstract void setItem(LivingEntity entity, int index, ItemStack stack);

    public abstract int getSize(LivingEntity entity);

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
        public ItemStack getItem(LivingEntity entity, int index) {
            int i = 0;
            for (ItemStack slot : entity.getAllSlots()) {
                if (i == index) {
                    return slot;
                }
                i++;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(LivingEntity entity, int index, ItemStack stack) {
            // unsupported
        }

        @Override
        public int getSize(LivingEntity entity) {
            int i = 0;
            for (ItemStack slot : entity.getAllSlots()) {
                i++;
            }
            return i;
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

    public static class EquipmentPlayerSlot extends PlayerSlot {

        private final EquipmentSlot slot;

        public EquipmentPlayerSlot(EquipmentSlot slot) {
            this.slot = slot;
        }

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            return Collections.singletonList(entity.getItemBySlot(this.slot));
        }

        @Override
        public ItemStack getItem(LivingEntity entity, int index) {
            return entity.getItemBySlot(this.slot);
        }

        @Override
        public void setItem(LivingEntity entity, int index, ItemStack stack) {
            entity.setItemSlot(this.slot, stack);
        }

        @Override
        public int getSize(LivingEntity entity) {
            return 1;
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

    private static class AccessoriesSlot extends PlayerSlot {

        private final String slotName;

        private AccessoriesSlot(String slotName) {
            this.slotName = slotName;
        }

        @Override
        public List<ItemStack> getItems(LivingEntity entity) {
            return AccessoriesCompat.INSTANCE.getFromSlot(entity, this.slotName);
        }

        @Override
        public ItemStack getItem(LivingEntity entity, int index) {
            return AccessoriesCompat.INSTANCE.getFromSlot(entity, this.slotName, index);
        }

        @Override
        public void setItem(LivingEntity entity, int index, ItemStack stack) {
            AccessoriesCompat.INSTANCE.setInSlot(entity, this.slotName, index, stack);
        }

        @Override
        public int getSize(LivingEntity entity) {
            return AccessoriesCompat.INSTANCE.getSlotSize(entity, this.slotName);
        }

        @Override
        public void clear(LivingEntity entity) {
            AccessoriesCompat.INSTANCE.clearSlot(entity, this.slotName);
        }

        @Override
        public Type getType() {
            return Type.ACCESSORIES;
        }

        @Override
        public String toString() {
            return "accessories:" + this.slotName;
        }
    }

    public enum Type {

        ANY_SLOT,
        EQUIPMENT_SLOT,
        ACCESSORIES;

    }

}
