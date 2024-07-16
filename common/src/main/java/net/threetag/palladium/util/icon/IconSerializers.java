package net.threetag.palladium.util.icon;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;

public class IconSerializers {

    public static final DeferredRegister<IconSerializer<?>> ICON_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ICON_SERIALIZER);

    public static final RegistryHolder<IconSerializer<?>, ItemIcon.Serializer> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final RegistryHolder<IconSerializer<?>, ItemInSlotIcon.Serializer> ITEM_IN_SLOT = ICON_SERIALIZERS.register("item_in_slot", ItemInSlotIcon.Serializer::new);
    public static final RegistryHolder<IconSerializer<?>, IngredientIcon.Serializer> INGREDIENT = ICON_SERIALIZERS.register("ingredient", IngredientIcon.Serializer::new);
    public static final RegistryHolder<IconSerializer<?>, TexturedIcon.Serializer> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
    public static final RegistryHolder<IconSerializer<?>, CompoundIcon.Serializer> COMPOUND = ICON_SERIALIZERS.register("compound", CompoundIcon.Serializer::new);
    public static final RegistryHolder<IconSerializer<?>, ExperienceIcon.Serializer> EXPERIENCE = ICON_SERIALIZERS.register("experience", ExperienceIcon.Serializer::new);
}
