package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.documentation.DocumentedConfigurable;

public abstract class EnergyBeamRenderer {

    public abstract void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick);

    public static abstract class Serializer implements DocumentedConfigurable {

        public abstract EnergyBeamRenderer fromJson(JsonObject json);

    }

}
