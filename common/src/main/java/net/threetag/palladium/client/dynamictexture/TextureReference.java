package net.threetag.palladium.client.dynamictexture;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TextureReference {

    public static final Codec<TextureReference> CODEC = Codec.STRING.comapFlatMap(TextureReference::read, TextureReference::toString).stable();

    private final boolean dynamic;
    private final ResourceLocation path;

    private TextureReference(boolean dynamic, ResourceLocation path) {
        this.dynamic = dynamic;
        this.path = path;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ResourceLocation getTexture(DataContext context) {
        if (this.dynamic) {
            var dyn = DynamicTextureManager.INSTANCE.get(this.path);
            return dyn != null ? dyn.getTexture(context) : null;
        } else {
            return this.path;
        }
    }

    public static TextureReference normal(ResourceLocation path) {
        return new TextureReference(false, path);
    }

    public static TextureReference dynamic(ResourceLocation path) {
        return new TextureReference(true, path);
    }

    public static TextureReference parse(String path) {
        if (path.startsWith("#")) {
            return dynamic(ResourceLocation.parse(path.substring(1)));
        }

        return normal(ResourceLocation.parse(path));
    }

    public static DataResult<TextureReference> read(String path) {
        try {
            return DataResult.success(parse(path));
        } catch (ResourceLocationException e) {
            return DataResult.error(() -> "Not a valid texture reference: " + path + " " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return (this.dynamic ? "#" : "") + this.path.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureReference that = (TextureReference) o;
        return dynamic == that.dynamic && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dynamic, path);
    }
}
