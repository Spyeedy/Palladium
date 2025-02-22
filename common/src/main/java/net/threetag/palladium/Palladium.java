package net.threetag.palladium;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.icon.IconSerializers;
import net.threetag.palladium.command.PalladiumCommand;
import net.threetag.palladium.command.SuperpowerCommand;
import net.threetag.palladium.compat.accessories.AccessoriesCompatImpl;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.condition.ConditionSerializers;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.number.EntityDependentNumberTypes;
import net.threetag.palladium.item.ItemTypes;
import net.threetag.palladium.item.TabPlacement;
import net.threetag.palladium.network.DataSyncUtil;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.PowerEventHandler;
import net.threetag.palladium.power.ability.AbilityEventHandler;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.enabling.EnablingHandlerSerializers;
import net.threetag.palladium.power.ability.keybind.KeyBindTypeSerializers;
import net.threetag.palladium.power.ability.unlocking.UnlockingHandlerSerializers;
import net.threetag.palladium.power.provider.PowerProviders;
import net.threetag.palladium.registry.PalladiumRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Palladium {

    public static final String MOD_ID = "palladium";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        MidnightConfig.init(MOD_ID, PalladiumConfig.class);

        PalladiumRegistries.init();
        ItemTypes.init();

        PalladiumEntityDataTypes.DATA_TYPES.register();
        PalladiumDataComponents.DATA_COMPONENTS.register();
        EntityDependentNumberTypes.TYPES.register();
        KeyBindTypeSerializers.KEY_BIND_TYPES.register();
        UnlockingHandlerSerializers.UNLOCKING_HANDLERS.register();
        EnablingHandlerSerializers.ENABLING_HANDLERS.register();
        AbilitySerializers.ABILITIES.register();
        ConditionSerializers.CONDITION_SERIALIZERS.register();
        PowerProviders.PROVIDERS.register();
        IconSerializers.ICON_SERIALIZERS.register();

        DataSyncUtil.init();
        PalladiumNetwork.init();
        PowerEventHandler.init();
        AbilityEventHandler.init();
        PalladiumEntityData.registerEvents();
        LifecycleEvent.SETUP.register(TabPlacement::loadTabs);

        // Commands
        SuperpowerCommand.register();
        CommandRegistrationEvent.EVENT.register((dispatcher, context, selection) -> PalladiumCommand.register(dispatcher, context));

        // Compat
        if (Platform.isModLoaded("accessories")) {
            AccessoriesCompatImpl.init();
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
