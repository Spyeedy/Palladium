package net.threetag.palladium.addonpack.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.threetag.palladium.addonpack.builder.AddonBuilder;
import net.threetag.palladium.addonpack.builder.PoiTypeBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistryHolder;
import net.threetag.palladiumcore.util.Platform;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PoiTypeParser extends AddonParser<PoiType> {

    public PoiTypeParser() {
        super(GSON, "poi_types", Registries.POINT_OF_INTEREST_TYPE);
    }

    @Override
    public AddonBuilder<PoiType> parse(ResourceLocation id, JsonElement jsonElement) {
        JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "$");
        PoiTypeBuilder builder = new PoiTypeBuilder(id);

        builder.setBlockStates(GsonUtil.getAsResourceLocation(json, "block"));
        builder.maxTickets(GsonHelper.getAsInt(json, "max_tickets", 1));
        builder.validRange(GsonHelper.getAsInt(json, "valid_range", 1));

        return builder;
    }

    @Override
    public void postRegister(AddonBuilder<PoiType> addonBuilder) {
        if (Platform.isFabric()) {
            DeferredRegister.POI_TYPES_TO_FIX.add(new Holder(addonBuilder));
        }
    }

    private static class Holder extends RegistryHolder<PoiType, PoiType> {

        private final AddonBuilder<PoiType> addonBuilder;

        public Holder(AddonBuilder<PoiType> addonBuilder) {
            this.addonBuilder = addonBuilder;
        }

        @Override
        public ResourceLocation getId() {
            return this.addonBuilder.getId();
        }

        @Override
        public PoiType get() {
            return this.addonBuilder.get();
        }

        @Override
        public PoiType value() {
            return this.addonBuilder.get();
        }

        @Override
        public boolean isBound() {
            return false;
        }

        @Override
        public boolean is(ResourceLocation location) {
            return false;
        }

        @Override
        public boolean is(ResourceKey<PoiType> resourceKey) {
            return false;
        }

        @Override
        public boolean is(Predicate<ResourceKey<PoiType>> predicate) {
            return false;
        }

        @Override
        public boolean is(TagKey<PoiType> tagKey) {
            return false;
        }

        @Override
        public boolean is(net.minecraft.core.Holder<PoiType> holder) {
            return false;
        }

        @Override
        public Stream<TagKey<PoiType>> tags() {
            return Stream.empty();
        }

        @Override
        public Either<ResourceKey<PoiType>, PoiType> unwrap() {
            return null;
        }

        @Override
        public Optional<ResourceKey<PoiType>> unwrapKey() {
            return Optional.empty();
        }

        @Override
        public Kind kind() {
            return null;
        }

        @Override
        public boolean canSerializeIn(HolderOwner<PoiType> owner) {
            return false;
        }
    }
}
