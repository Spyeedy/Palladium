package net.threetag.palladium.energy.neoforge;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.energy.PalladiumEnergyStorage;
import net.threetag.palladium.item.BaseEnergyItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnergyHelperImpl {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Palladium.MOD_ID);
    private static final Supplier<DataComponentType<Integer>> ENERGY_COMPONENT = COMPONENTS.register("test_energy", () -> DataComponentType.<Integer>builder()
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.INT)
            .build());

    @SubscribeEvent
    public static void initCapability(RegisterCapabilitiesEvent e) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BaseEnergyItem energyItem) {
                e.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ctx) -> new ComponentEnergyStorage(stack, ENERGY_COMPONENT.get(), energyItem.getEnergyCapacity(stack)), item);
            }
        }
    }

    @Nullable
    public static PalladiumEnergyStorage getFromItem(ItemStack stack) {
        var storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage != null ? new NeoEnergyStorageWrapper(storage) : null;
    }

}
