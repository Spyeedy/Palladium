package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;

public abstract class AbilitySerializer<T extends Ability> {

    public abstract MapCodec<T> codec();

}
