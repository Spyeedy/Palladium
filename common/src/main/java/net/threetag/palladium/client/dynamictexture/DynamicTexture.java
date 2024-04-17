package net.threetag.palladium.client.dynamictexture;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.util.context.DataContext;

public abstract class DynamicTexture {

    public abstract ResourceLocation getTexture(DataContext context);

    public abstract DynamicTexture transform(ITextureTransformer textureTransformer);

    public abstract DynamicTexture addVariable(String name, ITextureVariable variable);

}

