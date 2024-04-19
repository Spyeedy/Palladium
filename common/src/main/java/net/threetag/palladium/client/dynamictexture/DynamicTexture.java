package net.threetag.palladium.client.dynamictexture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.dynamictexture.transformer.ITextureTransformer;
import net.threetag.palladium.client.dynamictexture.variable.ITextureVariable;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;
import java.util.Map;

public abstract class DynamicTexture {

    protected final Map<String, ITextureVariable> textureVariableMap = Maps.newHashMap();
    protected final List<ITextureTransformer> transformers = Lists.newLinkedList();

    public abstract ResourceLocation getTexture(DataContext context);

    public DynamicTexture transform(ITextureTransformer textureTransformer) {
        this.transformers.add(textureTransformer);
        return this;
    }

    public DynamicTexture addVariable(String name, ITextureVariable variable) {
        this.textureVariableMap.put(name, variable);
        return this;
    }

}

