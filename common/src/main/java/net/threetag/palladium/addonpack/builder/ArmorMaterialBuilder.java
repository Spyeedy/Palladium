package net.threetag.palladium.addonpack.builder;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.threetag.palladium.util.CodecUtils;

import java.util.Collections;
import java.util.Map;

public class ArmorMaterialBuilder extends AddonBuilder<ArmorMaterial> {

    private Map<ArmorItem.Type, Integer> defense;
    private int enchantmentValue = 0;
    private Holder<SoundEvent> equipSound;
    private CodecUtils.SimpleIngredientSupplier repairIngredient;
    private float toughness = 0;
    private float knockbackResistance = 0;

    public ArmorMaterialBuilder(ResourceLocation id) {
        super(id);
    }

    public ArmorMaterialBuilder setDefense(Map<ArmorItem.Type, Integer> defense) {
        this.defense = defense;
        return this;
    }

    public Map<ArmorItem.Type, Integer> getDefense() {
        return defense;
    }

    public ArmorMaterialBuilder setEnchantmentValue(int enchantmentValue) {
        this.enchantmentValue = enchantmentValue;
        return this;
    }

    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    public ArmorMaterialBuilder setEquipSound(Holder<SoundEvent> equipSound) {
        this.equipSound = equipSound;
        return this;
    }

    public Holder<SoundEvent> getEquipSound() {
        return equipSound;
    }

    public ArmorMaterialBuilder setRepairIngredient(CodecUtils.SimpleIngredientSupplier repairIngredient) {
        this.repairIngredient = repairIngredient;
        return this;
    }

    public CodecUtils.SimpleIngredientSupplier getRepairIngredient() {
        return repairIngredient;
    }

    public ArmorMaterialBuilder setToughness(float toughness) {
        this.toughness = toughness;
        return this;
    }

    public float getToughness() {
        return toughness;
    }

    public ArmorMaterialBuilder setKnockbackResistance(float knockbackResistance) {
        this.knockbackResistance = knockbackResistance;
        return this;
    }

    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    protected ArmorMaterial create() {
        return new ArmorMaterial(
                this.defense, this.enchantmentValue, this.equipSound, this.repairIngredient, Collections.emptyList(), this.toughness, this.knockbackResistance
        );
    }
}
