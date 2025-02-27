package net.threetag.palladium.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.mixin.RangedAttributeAccessor;
import net.threetag.palladiumcore.event.LifecycleEvents;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.EntityAttributeRegistry;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import net.threetag.palladiumcore.util.Platform;

import java.util.Objects;
import java.util.UUID;

public class PalladiumAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Palladium.MOD_ID, Registries.ATTRIBUTE);

    public static final RegistrySupplier<Attribute> FLIGHT_SPEED = ATTRIBUTES.register("flight_speed", () -> new RangedAttribute(name("flight_speed"), 0D, 0D, 32D).setSyncable(true));
    public static final RegistrySupplier<Attribute> FLIGHT_FLEXIBILITY = ATTRIBUTES.register("flight_flexibility", () -> new RangedAttribute(name("flight_flexibility"), 0D, 0D, 10D).setSyncable(true));
    public static final RegistrySupplier<Attribute> LEVITATION_SPEED = ATTRIBUTES.register("levitation_speed", () -> new RangedAttribute(name("levitation_speed"), 0D, 0D, 32D).setSyncable(true));
    public static final RegistrySupplier<Attribute> HEROIC_FLIGHT_TYPE = ATTRIBUTES.register("heroic_flight_type", () -> new RangedAttribute(name("heroic_flight_type"), 0D, 0D, 1D).setSyncable(true));

    public static final RegistrySupplier<Attribute> PUNCH_DAMAGE = ATTRIBUTES.register("punch_damage", () -> new RangedAttribute(name("punch_damage"), 0.0, 0.0, 2048.0));
    public static final RegistrySupplier<Attribute> JUMP_POWER = ATTRIBUTES.register("jump_power", () -> new RangedAttribute(name("jump_power"), 1.0, 0.0, 2048.0).setSyncable(true));
    public static final RegistrySupplier<Attribute> DESTROY_SPEED = ATTRIBUTES.register("destroy_speed", () -> new RangedAttribute(name("destroy_speed"), 1.0, 0.0, 2048.0).setSyncable(true));
    public static final RegistrySupplier<Attribute> FALL_RESISTANCE = ATTRIBUTES.register("fall_resistance", () -> new RangedAttribute(name("fall_resistance"), 1.0, 0.0, 100D));
    public static final RegistrySupplier<Attribute> LEAPING = ATTRIBUTES.register("leaping", () -> new RangedAttribute(name("leaping"), 1.0, 0.0, 100D));

    public static final UUID PUNCH_DAMAGE_MOD_UUID = UUID.fromString("b587e52f-6985-40f4-988e-48e3a7d3fdcb");

    public static void init() {
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, FLIGHT_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, FLIGHT_FLEXIBILITY);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, LEVITATION_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, HEROIC_FLIGHT_TYPE);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, PUNCH_DAMAGE);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, JUMP_POWER);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, DESTROY_SPEED);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, FALL_RESISTANCE);
        EntityAttributeRegistry.registerModification(() -> EntityType.PLAYER, LEAPING);

        events();

        if (!Platform.isModLoaded("attributefix")) {
            LifecycleEvents.SETUP.register(() -> {
                if (Attributes.ARMOR instanceof RangedAttributeAccessor accessor) {
                    accessor.palladium_setMaxValue(1024);
                }

                if (Attributes.ATTACK_KNOCKBACK instanceof RangedAttributeAccessor accessor) {
                    accessor.palladium_setMaxValue(1024);
                }
            });
        }
    }

    private static void events() {
        LivingEntityEvents.TICK.register(entity -> {
            if (entity.getAttributes().hasAttribute(PUNCH_DAMAGE.get())) {
                var punchDmg = entity.getAttributeValue(PUNCH_DAMAGE.get());
                var attackDmg = Objects.requireNonNull(entity.getAttribute(Attributes.ATTACK_DAMAGE));
                var currentMod = attackDmg.getModifier(PUNCH_DAMAGE_MOD_UUID);

                if (currentMod != null && currentMod.getAmount() != punchDmg) {
                    attackDmg.removeModifier(PUNCH_DAMAGE_MOD_UUID);
                }

                if ((currentMod == null && punchDmg > 0D) || (currentMod != null && currentMod.getAmount() != punchDmg)) {
                    attackDmg.addTransientModifier(new AttributeModifier(PUNCH_DAMAGE_MOD_UUID, "Punch Damage", punchDmg, AttributeModifier.Operation.ADDITION));
                }
            }
        });

        LivingEntityEvents.JUMP.register(entity -> {
            if (entity.getAttributes().hasAttribute(LEAPING.get())) {
                double mul = entity.getAttributeValue(LEAPING.get());

                if (mul != 1F) {
                    Vec3 vec3 = entity.getDeltaMovement();
                    vec3 = vec3.add(vec3.x * mul, 0, vec3.z * mul);
                    entity.setDeltaMovement(vec3);

                    if(entity instanceof ServerPlayer player) {
                        player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    }
                }
            }
        });
    }

    public static String name(String name) {
        return "attribute.name.generic." + Palladium.MOD_ID + "." + name;
    }

}
