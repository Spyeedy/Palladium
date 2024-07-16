package net.threetag.palladium.compat.kubejs.neoforge;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;
import net.threetag.palladium.compat.kubejs.neoforge.ability.AbilityBuilder;
import net.threetag.palladium.compat.kubejs.neoforge.condition.ConditionBuilder;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.entity.CustomProjectile;
import net.threetag.palladium.event.PalladiumClientEvents;
import net.threetag.palladium.event.PalladiumEvents;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.util.Easing;
import net.threetag.palladium.util.PlayerSlot;
import net.threetag.palladiumcore.registry.client.GuiLayerRegistry;
import net.threetag.palladiumcore.util.Platform;

@SuppressWarnings("unchecked")
public class PalladiumKubeJSPlugin implements KubeJSPlugin {

    public static final RegistryInfo<Ability> ABILITY = RegistryInfo.of((ResourceKey<Registry<Ability>>) Ability.REGISTRY.getRegistryKey());
    public static final RegistryInfo<ConditionSerializer> CONDITION_SERIALIZER = RegistryInfo.of((ResourceKey<Registry<ConditionSerializer>>) ConditionSerializer.REGISTRY.getRegistryKey());

    @Override
    public void init() {
        CustomProjectile.KUBEJS_EVENT_HANDLER = customProjectile -> PalladiumJSEvents.CUSTOM_PROJECTILE_TICK.post(new ProjectileTickEventJS(customProjectile));

        PalladiumEvents.REGISTER_PROPERTY.register(handler -> {
            if (handler.getEntity().level().isClientSide) {
                PalladiumJSEvents.CLIENT_REGISTER_PROPERTIES.post(new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler));
            } else {
                PalladiumJSEvents.REGISTER_PROPERTIES.post(new RegisterPalladiumPropertyEventJS(handler.getEntity(), handler));
            }
        });

        if (Platform.isClient()) {
            this.clientInit();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void clientInit() {
        PalladiumClientEvents.REGISTER_ANIMATIONS.register(registry -> {
            PalladiumJSEvents.REGISTER_ANIMATIONS.post(new RegisterAnimationsEventJS(registry));
            PalladiumJSEvents.REGISTER_GUI_OVERLAYS.post(new RegisterGuiLayerEventJS());
        });

        GuiLayerRegistry.register(Palladium.id("kubejs_gui_layers"), new RegisterGuiLayerEventJS.Layer());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        ResourceKey key = Ability.REGISTRY.getRegistryKey();
        registry.addDefault(key, AbilityBuilder.class, AbilityBuilder::new);
        key = ConditionSerializer.REGISTRY.getRegistryKey();
        registry.addDefault(key, ConditionBuilder.class, ConditionBuilder::new);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(PalladiumJSEvents.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("palladium", bindings.type() == ScriptType.CLIENT ? new PalladiumBindingClient() : new PalladiumBinding());
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        if (registry.scriptType() == ScriptType.CLIENT) {
            registry.register(Easing.class, o -> Easing.fromString(o.toString()));
            registry.register(PalladiumAnimation.PlayerModelPart.class, o -> PalladiumAnimation.PlayerModelPart.fromName(o.toString()));
            registry.register(PlayerSlot.class, o -> PlayerSlot.get(o.toString()));
        }
    }

}
