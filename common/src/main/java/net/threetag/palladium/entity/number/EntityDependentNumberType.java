package net.threetag.palladium.entity.number;

import com.mojang.serialization.MapCodec;

public abstract class EntityDependentNumberType<T extends EntityDependentNumber> {

    public abstract MapCodec<T> codec();

}
