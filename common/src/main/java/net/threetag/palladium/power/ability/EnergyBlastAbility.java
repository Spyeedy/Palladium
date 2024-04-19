package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.property.*;

import java.awt.*;

@Deprecated
public class EnergyBlastAbility extends Ability implements AnimationTimer {

    public static final PalladiumProperty<String> ORIGIN = new StringProperty("origin").configurable("Defines the origin point of the energy blast");
    public static final PalladiumProperty<Color> COLOR = new ColorProperty("color").configurable("Defines the color of the blast");
    public static final PalladiumProperty<Float> DAMAGE = new FloatProperty("damage").configurable("The damage dealt with aiming for entities (per tick)");
    public static final PalladiumProperty<Float> MAX_DISTANCE = new FloatProperty("max_distance").configurable("The maximum distance you can reach with your heat vision");

    public static final PalladiumProperty<Integer> ANIMATION_TIMER = new IntegerProperty("animation_timer").sync(SyncType.NONE);
    public static final PalladiumProperty<Double> DISTANCE = new DoubleProperty("distance").sync(SyncType.NONE);

    public EnergyBlastAbility() {
        this.withProperty(ORIGIN, "eyes");
        this.withProperty(COLOR, Color.RED);
        this.withProperty(DAMAGE, 1F);
        this.withProperty(MAX_DISTANCE, 30F);
    }

    @Override
    public void registerUniqueProperties(PropertyManager manager) {
        manager.register(ANIMATION_TIMER, 0);
        manager.register(DISTANCE, 0D);
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity instanceof Player player) {
            player.displayClientMessage(Component.literal("Energy Blast ability is deprecated, please switch to energy_beam!"), true);
        }
    }

    @Override
    public String getDocumentationDescription() {
        return "Deprecated, please switch to 'palladium:energy_beam'";
    }

    @Override
    public float getAnimationValue(AbilityInstance entry, float partialTick) {
        return entry.getProperty(ANIMATION_TIMER) / 5F;
    }

    @Override
    public float getAnimationTimer(AbilityInstance entry, float partialTick, boolean maxedOut) {
        if (maxedOut) {
            return 5;
        }
        return entry.getProperty(ANIMATION_TIMER);
    }
}
