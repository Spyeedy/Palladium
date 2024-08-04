package net.threetag.palladium.documentation;

import com.mojang.serialization.MapCodec;

public interface DocumentedCodec<T> extends DocumentedConfigurable {

    MapCodec<T> documentedCodec();

    @Override
    default void generateDocumentation(JsonDocumentationBuilder builder) {
//        this.documentedCodec().keys(JsonOps.INSTANCE).forea
    }
}
