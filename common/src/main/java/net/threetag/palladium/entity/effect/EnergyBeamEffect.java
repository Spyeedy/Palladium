package net.threetag.palladium.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.energybeam.EnergyBeamManager;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.EnergyBeamAbility;
import net.threetag.palladium.util.property.AbilityReferenceProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Objects;

public class EnergyBeamEffect extends EntityEffect {

    public static final PalladiumProperty<AbilityReference> ABILITY = new AbilityReferenceProperty("ability");

    @Override
    public void registerProperties(PropertyManager manager) {
        super.registerProperties(manager);
        manager.register(ABILITY, null);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(EffectEntity entity, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        if (anchor instanceof AbstractClientPlayer player) {
            AbilityInstance entry = this.get(entity, ABILITY).getEntry(player);

            if (entry != null) {
                EnergyBeamAbility.updateTargetPos(player, entry, partialTicks);
                var beam = EnergyBeamManager.INSTANCE.get(entry.getProperty(EnergyBeamAbility.BEAM));

                if (beam != null) {
                    var entityPos = entity.getPosition(partialTicks);
                    var target = entry.getProperty(EnergyBeamAbility.TARGET);
                    beam.render(player, entityPos, target, poseStack, bufferSource, packedLightIn, isFirstPerson, partialTicks);
                }
            }
        }
    }

    @Override
    public void tick(EffectEntity entity, Entity anchor) {
        if (anchor instanceof AbstractClientPlayer player) {
            AbilityInstance entry = this.get(entity, ABILITY).getEntry(player);

            if (entry != null) {
                var beam = EnergyBeamManager.INSTANCE.get(entry.getProperty(EnergyBeamAbility.BEAM));

                if (beam == null) {
                    this.stopPlaying(entity);
                    return;
                }

                boolean isDonePlaying = !entry.isEnabled();
                if (isDonePlaying != this.get(entity, IS_DONE_PLAYING)) {
                    this.stopPlaying(entity);
                }
            } else {
                this.stopPlaying(entity);
            }
        } else {
            this.stopPlaying(entity);
        }
    }

    public static void start(Player player, AbilityReference abilityReference) {
        EffectEntity effectEntity = new EffectEntity(player.level(), player, EntityEffects.ENERGY_BEAM.get());
        ABILITY.set(effectEntity, abilityReference);
        Objects.requireNonNull(Minecraft.getInstance().level).putNonPlayerEntity(0, effectEntity);
    }
}
