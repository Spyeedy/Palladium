package net.threetag.palladium.compat.kubejs.neoforge;

import net.threetag.palladium.client.model.animation.AnimationUtil;

@SuppressWarnings("InstantiationOfUtilityClass")
public class PalladiumBindingClient extends PalladiumBinding {

    public final AnimationUtil animations = new AnimationUtil();
    public final GuiUtilJS gui = new GuiUtilJS();

}
