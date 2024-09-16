package net.threetag.palladium.util.fabric;

import net.threetag.palladium.compat.iris.fabric.IrisCompat;
import net.threetag.palladiumcore.util.Platform;

public class RenderUtilImpl {

    public static boolean isIrisShaderActive() {
        return Platform.isModLoaded("iris") && IrisCompat.isShaderPackActive();
    }
}
