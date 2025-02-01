package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceKey;

import java.util.List;

public class AdvancedHolderSet<T> {

    public static <T> Codec<AdvancedHolderSet<T>> codec(ResourceKey<? extends Registry<T>> registryKey) {
        return CodecExtras.listOrPrimitive(RegistryCodecs.homogeneousList(registryKey)).xmap(AdvancedHolderSet::new, holderSet -> holderSet.holderSets);
    }

    private final List<HolderSet<T>> holderSets;

    private AdvancedHolderSet(List<HolderSet<T>> holderSets) {
        this.holderSets = holderSets;
    }

    public boolean contains(Holder<T> holder) {
        for (HolderSet<T> holderSet : this.holderSets) {
            if (holderSet.contains(holder)) {
                return true;
            }
        }

        return false;
    }
}
