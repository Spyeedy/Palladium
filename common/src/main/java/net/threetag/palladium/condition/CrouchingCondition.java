package net.threetag.palladium.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class CrouchingCondition implements Condition {

    public static final CrouchingCondition INSTANCE = new CrouchingCondition();

    public static final MapCodec<CrouchingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CrouchingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        return entity != null && entity.isCrouching();
    }

    @Override
    public ConditionSerializer<CrouchingCondition> getSerializer() {
        return ConditionSerializers.CROUCHING.get();
    }

    public static class Serializer extends ConditionSerializer<CrouchingCondition> {

        @Override
        public MapCodec<CrouchingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrouchingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is crouching.";
        }
    }

}
