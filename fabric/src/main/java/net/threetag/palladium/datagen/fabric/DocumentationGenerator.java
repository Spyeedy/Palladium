package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistries;

import java.util.concurrent.CompletableFuture;

public class DocumentationGenerator implements DataProvider {

    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    public DocumentationGenerator(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        this.registryLookup = registryLookup;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registryLookup.thenAccept(provider -> {
            for (AbilitySerializer<?> serializer : PalladiumRegistries.ABILITY_SERIALIZER) {
                if (serializer instanceof Documented<Ability, ? extends Ability> doc) {
                    PalladiumRegistries.ABILITY_SERIALIZER.getResourceKey(serializer).ifPresent(key -> {
                        doc.getDocumentation(provider).build(key);
                    });
                }
            }

            CodecDocumentationBuilder.createFiles();
        });
    }

    @Override
    public String getName() {
        return "Palladium Documentation Generator";
    }
}
