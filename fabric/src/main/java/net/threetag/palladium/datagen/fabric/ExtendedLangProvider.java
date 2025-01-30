package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class ExtendedLangProvider extends FabricLanguageProvider {

    private final String modid;

    public ExtendedLangProvider(FabricDataOutput packOutput, String modid, String locale, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, locale, registryLookup);
        this.modid = modid;
    }

    public void addAbility(TranslationBuilder builder, Holder<? extends AbilitySerializer<?>> key, String name) {
        this.add(builder, key.value(), name);
    }

    public void add(TranslationBuilder builder, AbilitySerializer<?> key, String name) {
        ResourceLocation id = PalladiumRegistries.ABILITY_SERIALIZER.getKey(key);
        builder.add("ability." + Objects.requireNonNull(id).getNamespace() + "." + id.getPath(), name);
    }

    public void addConfigTitle(TranslationBuilder builder, String name) {
        builder.add(this.modid + ".midnightconfig.title", name);
    }

    public void addConfigCategory(TranslationBuilder builder, String key, String name) {
        builder.add(this.modid + ".midnightconfig.category." + key, name);
    }

    public void addConfigEntry(TranslationBuilder builder, String key, String name) {
        builder.add(this.modid + ".midnightconfig." + key, name);
    }

    public void addConfigEnum(TranslationBuilder builder, Enum<?> enum_, String name) {
        builder.add(this.modid + ".midnightconfig.enum." + enum_.getDeclaringClass().getSimpleName() + "." + enum_.name(), name);
    }

    public void addKeyMapping(TranslationBuilder builder, String key, String name) {
        builder.add("key.palladium." + key, name);
    }

}
