package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.energybeam.EnergyBeamManager;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.entity.effect.EnergyBeamEffect;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.PalladiumBlockUtil;
import net.threetag.palladiumcore.util.Platform;

import java.util.List;

public class EnergyBeamAbility extends Ability {

    public static final MapCodec<EnergyBeamAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("energy_beam").forGetter(ab -> ab.beamId),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("damage", 0F).forGetter(ab -> ab.damage),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("max_distance", 30F).forGetter(ab -> ab.maxDistance),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("set_on_fire_seconds", 0).forGetter(ab -> ab.setOnFireSeconds),
                    Codec.BOOL.optionalFieldOf("cause_fire", false).forGetter(ab -> ab.causeFire),
                    Codec.BOOL.optionalFieldOf("smelt_blocks", false).forGetter(ab -> ab.smeltBlocks),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, EnergyBeamAbility::new));

    public final ResourceLocation beamId;
    public final float damage, maxDistance;
    public final int setOnFireSeconds;
    public final boolean causeFire, smeltBlocks;

    public EnergyBeamAbility(ResourceLocation beamId, float damage, float maxDistance, int setOnFireSeconds, boolean causeFire, boolean smeltBlocks, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.beamId = beamId;
        this.damage = damage;
        this.maxDistance = maxDistance;
        this.setOnFireSeconds = setOnFireSeconds;
        this.causeFire = causeFire;
        this.smeltBlocks = smeltBlocks;
    }

    @Override
    public AbilitySerializer<EnergyBeamAbility> getSerializer() {
        return AbilitySerializers.ENERGY_BEAM.get();
    }

    @Override
    public void registerDataComponents(DataComponentMap.Builder components) {
        components.set(PalladiumDataComponents.Abilities.ENERGY_BEAM_TARGET.get(), null);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> instance, PowerHolder holder, boolean enabled) {
        var timer = instance.getAnimationTimer();
        if (enabled && entity instanceof Player player && (timer == null || timer.value() <= 0F) && Platform.isClient()) {
            EnergyBeamEffect.start(player, instance.getReference());
        }
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> instance, PowerHolder holder, boolean enabled) {
        HitResult hit = null;
        boolean hitTarget = enabled;
        var timer = instance.getAnimationTimer();

        if (timer != null) {
            hitTarget = timer.value() == timer.max();
        }

        if (enabled || hitTarget) {
            hit = updateTargetPos(entity, instance, 1F);
        }

        if (hitTarget) {
            if (hit instanceof EntityHitResult entityHitResult) {
                if (this.setOnFireSeconds > 0) {
                    entityHitResult.getEntity().setRemainingFireTicks(this.setOnFireSeconds);
                }

                if (this.damage > 0) {
                    var damageSrc = entity instanceof Player player ? entity.level().damageSources().playerAttack(player) : entity.damageSources().mobAttack(entity);
                    entityHitResult.getEntity().hurt(damageSrc, this.damage);
                }

                if (Platform.isClient()) {
                    this.spawnParticles(entity.level(), hit.getLocation());
                }
            } else if (hit instanceof BlockHitResult blockHitResult) {
                BlockState blockState = entity.level().getBlockState(blockHitResult.getBlockPos());

                if (!blockState.isAir()) {
                    if (this.smeltBlocks) {
                        SingleRecipeInput simpleContainer = new SingleRecipeInput(new ItemStack(blockState.getBlock()));
                        entity.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, simpleContainer, entity.level()).ifPresent(recipe -> {
                            ItemStack result = recipe.value().assemble(simpleContainer, entity.level().registryAccess());

                            if (!result.isEmpty() && Block.byItem(result.getItem()) != Blocks.AIR) {
                                entity.level().setBlockAndUpdate(blockHitResult.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
                            }
                        });

                        blockState = entity.level().getBlockState(blockHitResult.getBlockPos());
                    }

                    if (this.causeFire && PalladiumBlockUtil.canBurn(blockState, entity.level(), blockHitResult.getBlockPos(), blockHitResult.getDirection())) {
                        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
                        if (entity.level().isEmptyBlock(pos)) {
                            entity.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                        }
                    }

                    if (Platform.isClient()) {
                        this.spawnParticles(entity.level(), hit.getLocation());
                    }
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void spawnParticles(Level level, Vec3 pos) {
        var beam = EnergyBeamManager.INSTANCE.get(this.beamId);

        if (beam != null) {
            beam.spawnParticles(level, pos);
        }
    }

    public HitResult updateTargetPos(LivingEntity living, AbilityInstance<?> instance, float partialTick) {
        var start = living.getEyePosition(partialTick);
        var end = start.add(EntityUtil.getLookVector(living, partialTick).scale(this.maxDistance));
        HitResult endHit = EntityUtil.rayTraceWithEntities(living, start, end, start.distanceTo(end), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, en -> true);
        instance.setSilently(PalladiumDataComponents.Abilities.ENERGY_BEAM_TARGET.get(), endHit.getLocation());
        return endHit;
    }

    public float beamLengthMultiplier(AbilityInstance<?> instance, float partialTick) {
        var timer = instance.getAnimationTimer();
        if (timer != null) {
            return timer.progress(partialTick);
        } else {
            return 1F;
        }
    }

    public static class Serializer extends AbilitySerializer<EnergyBeamAbility> {

        @Override
        public MapCodec<EnergyBeamAbility> codec() {
            return CODEC;
        }
    }
}
