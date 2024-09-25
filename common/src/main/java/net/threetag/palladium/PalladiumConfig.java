package net.threetag.palladium;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

public class PalladiumConfig {

    public static class Client {

        public static ModConfigSpec.EnumValue<AbilityBarRenderer.Position> ABILITY_BAR_POSITION;
        public static ModConfigSpec.BooleanValue ADDON_PACK_DEV_MODE;
        public static ModConfigSpec.BooleanValue ACCESSORY_BUTTON;

        public static ModConfigSpec generateConfig() {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            ABILITY_BAR_POSITION = builder.defineEnum("abilityBarPosition", AbilityBarRenderer.Position.BOTTOM_RIGHT);
            ADDON_PACK_DEV_MODE = builder.define("addonPackDevMode", false);
            ACCESSORY_BUTTON = builder.define("accessoryButton", true);
            return builder.build();
        }

    }

    public static class Server {

        public static ModConfigSpec.BooleanValue REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION;

        public static ModConfigSpec generateConfig() {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
            builder.comment("Enabled some hidden/planned content, that is currently not finished or unused");
            REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION = builder.define("worldGen.redstoneFluxCrystalGeneration", true);
            return builder.build();
        }

    }

}
