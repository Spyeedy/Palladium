package net.threetag.palladium.item.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.addonpack.parser.ItemParser;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.item.ArmorWithRenderer;
import net.threetag.palladium.item.Openable;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.item.PalladiumItem;

import java.util.Arrays;

public class ArmorItemType implements ItemParser.ItemTypeSerializer {

    @Override
    public ArmorItem parse(JsonObject json, Item.Properties properties) {
        Holder<ArmorMaterial> armorMaterial = BuiltInRegistries.ARMOR_MATERIAL.holderByNameCodec().parse(JsonOps.INSTANCE, json.get("armor_material")).getOrThrow();
        ArmorItem.Type type = ArmorItem.Type.CODEC.parse(JsonOps.INSTANCE, json.get("slot")).getOrThrow();
        var item = new ExtArmorItem(armorMaterial, type, properties);

        item.rendererFile = GsonUtil.getAsResourceLocation(json, "armor_renderer", null);

        item.enableOpenable(
                GsonHelper.getAsBoolean(json, "openable", false),
                GsonUtil.getAsIntMin(json, "opening_time", 0, 0),
                GsonUtil.getAsResourceLocation(json, "opened_sound", null),
                GsonUtil.getAsResourceLocation(json, "closed_sound", null),
                GsonUtil.getAsResourceLocation(json, "opening_toggle_sound", null)
        );

        return item;
    }

    @Override
    public void generateDocumentation(JsonDocumentationBuilder builder) {
        builder.setTitle("Armor");

        builder.addProperty("slot", ArmorItem.Type.class)
                .description("The slot the item will fit in. Possible values: " + Arrays.toString(Arrays.stream(ArmorItem.Type.values()).map(ArmorItem.Type::getName).toArray()))
                .required().exampleJson(new JsonPrimitive("chest"));

        builder.addProperty("armor_material", ArmorMaterial.class)
                .description("Armor material, which defines certain characteristics about the armor. Open armor_materials.html for seeing how to make custom ones. Possible values: " + Arrays.toString(BuiltInRegistries.ARMOR_MATERIAL.keySet().toArray(new ResourceLocation[0])))
                .required().exampleJson(new JsonPrimitive("minecraft:diamond"));

        builder.addProperty("armor_renderer", ResourceLocation.class)
                .description("Location of the armor renderer file. Doesn't need to be specified, it will automatically look for one in a path corresponding to the item's ID: A 'test:item' will look for the armor renderer file at 'assets/test/palladium/armor_renderers/item.json'.")
                .fallback(null).exampleJson(new JsonPrimitive("test:item_renderer"));

        builder.addProperty("openable", Boolean.class)
                .description("Marks the armor piece as openable.")
                .fallback(false).exampleJson(new JsonPrimitive(false));

        builder.addProperty("opening_time", Integer.class)
                .description("Determines the time the item needs for it to be fully opened. Leave at 0 for instant. Needs 'openable' to be enabled to take effect.")
                .fallback(0).exampleJson(new JsonPrimitive(10));

        builder.addProperty("opened_sound", ResourceLocation.class)
                .description("Sound that is played when the suit has been fully opened.")
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:item.armor.equip_leather"));

        builder.addProperty("closed_sound", ResourceLocation.class)
                .description("Sound that is played when the suit has been fully closed.")
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:item.armor.equip_leather"));

        builder.addProperty("opening_toggle_sound", ResourceLocation.class)
                .description("Sound that is played when opening button has been pressed.")
                .fallback(null).exampleJson(new JsonPrimitive("minecraft:item.armor.equip_leather"));
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.withDefaultNamespace("armor");
    }

    public static class ExtArmorItem extends ArmorItem implements ArmorWithRenderer, Openable, PalladiumItem {

        protected ResourceLocation rendererFile;
        private Object renderer;
        private boolean openable = false;
        private int openingTime = 0;
        private ResourceLocation openedSound, closedSound, toggleSound;

        public ExtArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
            super(material, type, properties);
        }

        public ExtArmorItem setRenderer(ResourceLocation renderer) {
            this.rendererFile = renderer;
            return this;
        }

        public ExtArmorItem enableOpenable(boolean openable, int openingTime, ResourceLocation openedSound, ResourceLocation closedSound, ResourceLocation toggleSound) {
            this.openable = openable;
            this.openingTime = openingTime;
            this.openedSound = openedSound;
            this.closedSound = closedSound;
            this.toggleSound = toggleSound;
            return this;
        }

        @Override
        public void setCachedArmorRenderer(Object object) {
            this.renderer = object;
        }

        @Override
        public Object getCachedArmorRenderer() {
            return this.renderer;
        }

        @Override
        public ResourceLocation getArmorRendererFile() {
            return this.rendererFile != null ? this.rendererFile : ArmorWithRenderer.super.getArmorRendererFile();
        }

        @Override
        public boolean canBeOpened(LivingEntity entity, ItemStack stack) {
            return this.openable;
        }

        @Override
        public int getOpeningTime(ItemStack stack) {
            return this.openingTime;
        }

        @Override
        public void onFullyClosed(LivingEntity entity, ItemStack stack) {
            if (this.closedSound != null) {
                PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 50, this.closedSound, SoundSource.PLAYERS);
            }
        }

        @Override
        public void onFullyOpened(LivingEntity entity, ItemStack stack) {
            if (this.closedSound != null) {
                PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 50, this.openedSound, SoundSource.PLAYERS);
            }
        }

        @Override
        public void onOpeningStateChange(LivingEntity entity, ItemStack stack, boolean open) {
            if (this.toggleSound != null) {
                PlayerUtil.playSoundToAll(entity.level(), entity.getX(), entity.getEyeY(), entity.getZ(), 50, this.toggleSound, SoundSource.PLAYERS);
            }
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            if (this.openable && entity instanceof LivingEntity living) {
                Openable.onTick(living, stack);
            }
        }

    }
}