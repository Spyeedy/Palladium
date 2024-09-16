//package net.threetag.palladium.compat.geckolib.ability;
//
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.Item;
//import net.threetag.palladium.compat.geckolib.armor.AddonGeoArmorItem;
//import net.threetag.palladium.power.PowerHolder;
//import net.threetag.palladium.power.ability.Ability;
//import net.threetag.palladium.power.ability.AbilitySerializer;
//import net.threetag.palladium.power.ability.AbilityInstance;
//import net.threetag.palladium.util.property.PalladiumProperty;
//import net.threetag.palladium.util.property.PalladiumPropertyBuilder;
//import net.threetag.palladium.util.property.PalladiumPropertyType;
//import software.bernie.geckolib.animatable.GeoItem;
//
//public class ArmorAnimationAbility extends Ability {
//
//    public static final PalladiumProperty<ResourceLocation> ITEM = PalladiumPropertyBuilder.create("item", PalladiumPropertyType.RESOURCE_LOCATION).configurable("ID of the gecko armor item that must be worn currently.", true).build();
//    public static final PalladiumProperty<String> CONTROLLER = PalladiumPropertyBuilder.create("controller", PalladiumPropertyType.STRING).configurable("Name of the animation controller the animation is played on. Leave it as 'main' if you didnt specify one.", false, "main").build();
//    public static final PalladiumProperty<String> ANIMATION_TRIGGER = PalladiumPropertyBuilder.create("animation_trigger", PalladiumPropertyType.STRING).configurable("Name of the animation trigger", true).build();
//
//    public ArmorAnimationAbility() {
//        this.withProperty(ITEM, CONTROLLER, ANIMATION_TRIGGER);
//    }
//
//    @Override
//    public boolean isEffect() {
//        return true;
//    }
//
//    @Override
//    public void tick(LivingEntity entity, AbilityInstance<?> ability, PowerHolder holder, boolean enabled) {
//        if (enabled) {
//            if (entity.level().isClientSide) {
//                Item item = BuiltInRegistries.ITEM.get(ability.getProperty(ITEM));
//
//                if (item instanceof AddonGeoArmorItem geo) {
//                    for (EquipmentSlot slot : EquipmentSlot.values()) {
//                        if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
//                            if (entity.getItemBySlot(slot).is(item) && !entity.getItemBySlot(slot).isEmpty()) {
//                                long geoId = GeoItem.getId(entity.getItemBySlot(slot)) + entity.getId();
//                                var controller = geo.getAnimatableInstanceCache().getManagerForId(geoId).getAnimationControllers().get(ability.getProperty(CONTROLLER));
//
//                                if (controller != null) {
//                                    controller.forceAnimationReset();
//                                    controller.stop();
//                                    controller.tryTriggerAnimation(ability.getProperty(ANIMATION_TRIGGER));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
