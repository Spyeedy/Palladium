package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnergyBeamConfiguration {

    private final List<EnergyBeam> beams;

    public EnergyBeamConfiguration(List<EnergyBeam> energyBeams) {
        this.beams = energyBeams;
    }

    public void render(AbstractClientPlayer player, Vec3 anchor, Vec3 target, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        for (EnergyBeam beam : this.beams) {
            beam.render(player, anchor, target, poseStack, bufferSource, packedLightIn, isFirstPerson, partialTick);
        }
    }

    public void spawnParticles(Level level, Vec3 pos) {
        for (EnergyBeam beam : this.beams) {
            beam.spawnParticles(level, pos);
        }
    }

    public static EnergyBeamConfiguration fromJson(JsonElement json) {
        if (json.isJsonArray()) {
            List<EnergyBeam> beamList = new ArrayList<>();
            var array = json.getAsJsonArray();
            for (JsonElement jsonElement : array) {
                beamList.add(EnergyBeam.fromJson(GsonHelper.convertToJsonObject(jsonElement, "$[]")));
            }
            return new EnergyBeamConfiguration(beamList);
        } else if (json.isJsonObject()) {
            return new EnergyBeamConfiguration(Collections.singletonList(EnergyBeam.fromJson(json.getAsJsonObject())));
        } else {
            throw new JsonSyntaxException("Energy beam configuration must be either an object or array of multiple objects");
        }
    }

}
