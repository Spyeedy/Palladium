package net.threetag.palladium.compat.geckolib.armor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.compat.geckolib.GeckoLibCompat;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.types.ArmorItemType;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class AddonGeoArmorItem extends ArmorItemType.ExtArmorItem implements GeoItem {

    public List<ParsedAnimationController<GeoItem>> animationControllers;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ResourceLocation modelPath;
    public TextureReference texturePath;
    public ResourceLocation animationsPath;

    public AddonGeoArmorItem(Holder<ArmorMaterial> materialIn, ArmorItem.Type type, Properties builder) {
        super(materialIn, type, builder);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        for (ParsedAnimationController<GeoItem> controller : this.animationControllers) {
            controllers.add(controller.createController(this));
        }
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeckoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    this.renderer = new GeckoArmorRenderer((AddonGeoArmorItem) itemStack.getItem());

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getRenderProvider() {
        return this.cache;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean hasCustomRenderer() {
        return false;
    }

    public static class Parser implements ItemParser.ItemTypeSerializer {

        @Override
        public AddonGeoArmorItem parse(JsonObject json, Item.Properties properties) {
            Holder<ArmorMaterial> armorMaterial = BuiltInRegistries.ARMOR_MATERIAL.holderByNameCodec().parse(JsonOps.INSTANCE, json.get("armor_material")).getOrThrow();
            ArmorItem.Type type = ArmorItem.Type.CODEC.parse(JsonOps.INSTANCE, json.get("slot")).getOrThrow();

            var item = new AddonGeoArmorItem(armorMaterial, type, properties);
            item.modelPath = GsonUtil.getAsResourceLocation(json, "armor_model", null);
            item.texturePath = GsonUtil.getAsTextureReference(json, "armor_texture", null);
            item.animationsPath = GsonUtil.getAsResourceLocation(json, "armor_animations", null);
            item.animationControllers = GsonUtil.fromListOrPrimitive(json.get("armor_animation_controller"), el -> ParsedAnimationController.controllerFromJson(el.getAsJsonObject()), Collections.emptyList());

            return item;
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("GeckoLib Armor");

            builder.addProperty("slot", ArmorItem.Type.class)
                    .description("The slot the item will fit in. Possible values: " + Arrays.toString(Arrays.stream(ArmorItem.Type.values()).map(ArmorItem.Type::getName).toArray()))
                    .required().exampleJson(new JsonPrimitive("chest"));

            builder.addProperty("armor_material", ArmorMaterial.class)
                    .description("Armor material, which defines certain characteristics about the armor. Open armor_materials.html for seeing how to make custom ones. Possible values: " + Arrays.toString(BuiltInRegistries.ARMOR_MATERIAL.keySet().toArray(new ResourceLocation[0])))
                    .required().exampleJson(new JsonPrimitive("minecraft:diamond"));

            builder.addProperty("armor_model", ResourceLocation.class)
                    .description("Path to geckolib model file. Required bones: [armorHead, armorBody, armorRightArm, armorLeftArm, armorRightLeg, armorLeftLeg, armorRightBoot, armorLeftBoot].")
                    .fallback(null).exampleJson(new JsonPrimitive("palladium:test_model.geo.json"));

            builder.addProperty("armor_texture", TextureReference.class)
                    .description("Location of the armor texture. Can also use a dynamic texture using #.")
                    .fallback(null).exampleJson(new JsonPrimitive("example:textures/models/armor/example_armor.png"));

            builder.addProperty("armor_animations", ResourceLocation.class)
                    .description("ID of the animations that will be used.")
                    .fallback(null).exampleJson(new JsonPrimitive("palladium:animations/test_animation.animation.json"));

            var animationsExample = new JsonArray();
            var extendedC = new JsonObject();
            extendedC.addProperty("name", "controller_name");
            extendedC.addProperty("animation", "animation_name");
            extendedC.addProperty("transition_tick_time", 10);
            var triggers = new JsonObject();
            triggers.addProperty("trigger_name", "animation_name");
            extendedC.add("triggers", triggers);
            animationsExample.add(extendedC);
            builder.addProperty("armor_animation_controller", List.class)
                    .description("Names of controllers for the animation.")
                    .fallbackObject(null).exampleJson(animationsExample);

            builder.addProperty("hide_second_player_layer", Boolean.class)
                    .description("If enabled, the second player layer will be hidden when worn (only on the corresponding body part)")
                    .fallback(false).exampleJson(new JsonPrimitive(true));
        }

        @Override
        public ResourceLocation getId() {
            return GeckoLibConstants.id("armor");
        }
    }
}
