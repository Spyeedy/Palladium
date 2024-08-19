package net.threetag.palladium.item;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class PalladiumArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Palladium.MOD_ID, Registries.ARMOR_MATERIAL);

    public static final Holder<ArmorMaterial> VIBRANIUM_WEAVE = ARMOR_MATERIALS.register("vibranium_weave", () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 2);
        enumMap.put(ArmorItem.Type.LEGGINGS, 3);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 4);
        enumMap.put(ArmorItem.Type.HELMET, 2);
        enumMap.put(ArmorItem.Type.BODY, 4);
    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(Items.LEATHER),
            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("leather"), "", true), new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("leather"), "_overlay", false)), 0.0F, 0.0F));

}
