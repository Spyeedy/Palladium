package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class IntegerPropertyVariable extends AbstractIntegerTextureVariable {

    private final String propertyKey;

    public IntegerPropertyVariable(String propertyKey, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.propertyKey = propertyKey;
    }

    public IntegerPropertyVariable(JsonObject json) {
        super(json);
        this.propertyKey = GsonHelper.getAsString(json, "property");
    }

    @Override
    public int getNumber(Entity entity) {
        var handler = EntityPropertyHandler.getHandler(entity);
        PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

        if (property instanceof IntegerProperty integerProperty) {
            return handler.get(integerProperty);
        }

        return 0;
    }
}
