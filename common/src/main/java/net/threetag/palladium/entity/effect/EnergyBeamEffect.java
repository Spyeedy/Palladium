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
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.entity.EffectEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.EnergyBeamAbility;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PalladiumPropertyBuilder;
import net.threetag.palladium.util.property.PalladiumPropertyType;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Objects;

public class EnergyBeamEffect extends EntityEffect {

    public static final PalladiumProperty<AbilityReference> ABILITY = PalladiumPropertyBuilder.create("ability", PalladiumPropertyType.ABILITY_REFERENCE).build();

    @Override
    public void registerProperties(PropertyManager manager) {
        super.registerProperties(manager);
        manager.register(ABILITY, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Environment(EnvType.CLIENT)
    public void render(EffectEntity entity, Entity anchor, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTicks) {
        if (anchor instanceof AbstractClientPlayer player) {
            AbilityInstance<EnergyBeamAbility> instance = (AbilityInstance<EnergyBeamAbility>) this.get(entity, ABILITY).getInstance(player);

            if (instance != null) {
                instance.getAbility().updateTargetPos(player, instance, partialTicks);
                var beam = EnergyBeamManager.INSTANCE.get(instance.getAbility().beamId);

                if (beam != null) {
                    var entityPos = entity.getPosition(partialTicks);
                    var target = instance.get(PalladiumDataComponents.Abilities.ENERGY_BEAM_TARGET.get());
                    beam.render(player, entityPos, target, instance.getAbility().beamLengthMultiplier(instance, partialTicks), poseStack, bufferSource, packedLightIn, isFirstPerson, partialTicks);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Environment(EnvType.CLIENT)
    public void tick(EffectEntity entity, Entity anchor) {
        if (anchor instanceof AbstractClientPlayer player) {
            AbilityInstance<EnergyBeamAbility> instance = (AbilityInstance<EnergyBeamAbility>) this.get(entity, ABILITY).getInstance(player);

            if (instance != null) {
                var beam = EnergyBeamManager.INSTANCE.get(instance.getAbility().beamId);

                if (beam == null) {
                    this.stopPlaying(entity);
                    return;
                }

                boolean isDonePlaying = instance.getAnimationTimer() == null ? !instance.isEnabled() : instance.getAbility().beamLengthMultiplier(instance, 0F) <= 0F && !instance.isEnabled();
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

    @Environment(EnvType.CLIENT)
    public static void start(Player player, AbilityReference abilityReference) {
        EffectEntity effectEntity = new EffectEntity(player.level(), player, EntityEffects.ENERGY_BEAM.value());
        ABILITY.set(effectEntity, abilityReference);
        Objects.requireNonNull(Minecraft.getInstance().level).addEntity(effectEntity);
    }
}
