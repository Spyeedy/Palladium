package net.threetag.palladium.compat.iris.fabric;

import net.irisshaders.iris.api.v0.IrisApi;

public class IrisCompat {

    public static boolean isShaderPackActive() {
        return IrisApi.getInstance().isShaderPackInUse();
    }

}
