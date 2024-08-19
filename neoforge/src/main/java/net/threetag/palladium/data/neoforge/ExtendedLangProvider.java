package net.threetag.palladium.data.neoforge;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class ExtendedLangProvider extends LanguageProvider {

    public ExtendedLangProvider(PackOutput packOutput, String modid, String locale) {
        super(packOutput, modid, locale);
    }

    public void addAbility(Holder<AbilitySerializer<?>> key, String name) {
        add(key.value(), name);
    }

    public void add(AbilitySerializer<?> key, String name) {
        ResourceLocation id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(key);
        add("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    protected void addAccessory(Holder<Accessory> key, String name) {
        add(key.value(), name);
    }

    public void add(Accessory key, String name) {
        ResourceLocation id = PalladiumRegistries.ACCESSORY.getKey(key);
        add("accessory." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    protected void add(AccessorySlot slot, String name) {
        add(slot.getTranslationKey(), name);
    }

    public void add(Attribute key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addAttribute(Holder<? extends Attribute> key, String name) {
        this.add(key.value(), name);
    }

}
