package net.threetag.palladium.documentation;

public interface DocumentedCodec<T> extends DocumentedConfigurable {

    @Override
    default void generateDocumentation(JsonDocumentationBuilder builder) {
//        this.documentedCodec().keys(JsonOps.INSTANCE).forea
    }
}
