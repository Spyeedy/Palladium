package net.threetag.palladium.power.ability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
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
import net.threetag.palladium.entity.effect.EnergyBeamEffect;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.PalladiumBlockUtil;
import net.threetag.palladium.util.property.*;
import net.threetag.palladiumcore.util.Platform;

public class EnergyBeamAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> BEAM = new ResourceLocationProperty("energy_beam").configurable("Configuration for the look of the beam. Check wiki for information.");
    public static final PalladiumProperty<Float> DAMAGE = new FloatProperty("damage").configurable("The damage dealt with aiming for entities (per tick)");
    public static final PalladiumProperty<Float> MAX_DISTANCE = new FloatProperty("max_distance").configurable("The maximum distance you can reach with your heat vision");
    public static final PalladiumProperty<Integer> SET_ON_FIRE_SECONDS = new IntegerProperty("set_on_fire_seconds").configurable("You can use this to set targeted entities on fire. If set to 0 it will not cause any.");
    public static final PalladiumProperty<Boolean> CAUSE_FIRE = new BooleanProperty("cause_fire").configurable("If enabled, targeted blocks will start to burn (fire will be placed).");
    public static final PalladiumProperty<Boolean> SMELT_BLOCKS = new BooleanProperty("smelt_blocks").configurable("If enabled, targeted blocks will turn into their smelting result (e.g. sand will turn into glass).");

    public static final PalladiumProperty<Vec3> TARGET = new Vec3Property("distance").sync(SyncType.NONE);

    public EnergyBeamAbility() {
        this.withProperty(BEAM, new ResourceLocation("example:energy_beam"))
                .withProperty(DAMAGE, 5F)
                .withProperty(MAX_DISTANCE, 30F)
                .withProperty(SET_ON_FIRE_SECONDS, 0)
                .withProperty(CAUSE_FIRE, false)
                .withProperty(SMELT_BLOCKS, false);
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(TARGET, Vec3.ZERO);
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity instanceof Player player && Platform.isClient()) {
            EnergyBeamEffect.start(player, entry.getReference());
        }
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled) {
            var hit = updateTargetPos(entity, entry, 1F);

            if (hit instanceof EntityHitResult entityHitResult) {
                var fireSecs = entry.getProperty(SET_ON_FIRE_SECONDS);

                if (fireSecs > 0) {
                    entityHitResult.getEntity().setSecondsOnFire(fireSecs);
                }

                var damageSrc = entity instanceof Player player ? entity.level().damageSources().playerAttack(player) : entity.damageSources().mobAttack(entity);
                entityHitResult.getEntity().hurt(damageSrc, entry.getProperty(DAMAGE));

                if (Platform.isClient()) {
                    this.spawnParticles(entity.level(), hit.getLocation(), entry);
                }
            } else if (hit instanceof BlockHitResult blockHitResult) {
                BlockState blockState = entity.level().getBlockState(blockHitResult.getBlockPos());

                if (entry.getProperty(SMELT_BLOCKS)) {
                    SimpleContainer simpleContainer = new SimpleContainer(new ItemStack(blockState.getBlock()));
                    entity.level().getRecipeManager().getRecipeFor(RecipeType.SMELTING, simpleContainer, entity.level()).ifPresent(recipe -> {
                        ItemStack result = recipe.assemble(simpleContainer, entity.level().registryAccess());

                        if (!result.isEmpty() && Block.byItem(result.getItem()) != Blocks.AIR) {
                            entity.level().setBlockAndUpdate(blockHitResult.getBlockPos(), Block.byItem(result.getItem()).defaultBlockState());
                        }
                    });

                    blockState = entity.level().getBlockState(blockHitResult.getBlockPos());
                }

                if (entry.getProperty(CAUSE_FIRE) && PalladiumBlockUtil.canBurn(blockState, entity.level(), blockHitResult.getBlockPos(), blockHitResult.getDirection())) {
                    BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getNormal());
                    if (entity.level().isEmptyBlock(pos)) {
                        entity.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                    }
                }

                if (Platform.isClient()) {
                    this.spawnParticles(entity.level(), hit.getLocation(), entry);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void spawnParticles(Level level, Vec3 pos, AbilityEntry entry) {
        var beam = EnergyBeamManager.INSTANCE.get(entry.getProperty(BEAM));

        if (beam != null) {
            beam.spawnParticles(level, pos);
        }
    }

    public static HitResult updateTargetPos(LivingEntity living, AbilityEntry entry, float partialTick) {
        var start = living.getEyePosition(partialTick);
        var end = start.add(EntityUtil.getLookVector(living, partialTick).scale(entry.getProperty(MAX_DISTANCE)));
        HitResult endHit = EntityUtil.rayTraceWithEntities(living, start, end, start.distanceTo(end), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, en -> true);
        entry.setUniqueProperty(TARGET, endHit.getLocation());
        return endHit;
    }

}
