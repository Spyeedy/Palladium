package net.threetag.palladium.client.renderer.entity.layer;

import net.minecraft.client.Minecraft;
import net.threetag.palladium.data.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.mocha.runtime.binding.Binding;
import team.unnamed.mocha.runtime.value.ObjectProperty;
import team.unnamed.mocha.runtime.value.ObjectValue;
import team.unnamed.mocha.runtime.value.Value;

@Binding("query")
public class MoLangQuery implements ObjectValue {

    public static final MoLangQuery INSTANCE = new MoLangQuery();
    public static DataContext CONTEXT = null;

    @Binding("get_age")
    public double get_age() {
        var entity = CONTEXT.getEntity();
        return entity != null ? entity.tickCount + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks() : 0;
    }

    @Override
    public @Nullable ObjectProperty getProperty(@NotNull String name) {
        if (name.equals("get_age")) {
            return ObjectProperty.property(Value.of(get_age()), false);
        }
        return null;
    }
}
