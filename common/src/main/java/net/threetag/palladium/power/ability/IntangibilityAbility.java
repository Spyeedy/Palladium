package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.tag.PalladiumBlockTags;

import java.util.List;

public class IntangibilityAbility extends Ability {

    public static final MapCodec<IntangibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("vertical", false).forGetter(ab -> ab.vertical),
                    TagKey.codec(Registries.BLOCK).optionalFieldOf("whitelist", null).forGetter(ab -> ab.whitelist),
                    TagKey.codec(Registries.BLOCK).optionalFieldOf("blacklist", PalladiumBlockTags.PREVENTS_INTANGIBILITY).forGetter(ab -> ab.blacklist),
                    propertiesCodec(), conditionsCodec(), energyBarUsagesCodec()
            ).apply(instance, IntangibilityAbility::new));

    public final boolean vertical;
    public final TagKey<Block> whitelist, blacklist;

    public IntangibilityAbility(boolean vertical, TagKey<Block> whitelist, TagKey<Block> blacklist, AbilityProperties properties, AbilityConditions conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.vertical = vertical;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public static boolean canGoThrough(AbilityInstance<IntangibilityAbility> entry, BlockState state) {
        var whitelist = entry.getAbility().whitelist;
        var blacklist = entry.getAbility().blacklist;

        if (whitelist != null) {
            if (blacklist != null) {
                return state.is(whitelist) && !state.is(blacklist);
            } else {
                return state.is(whitelist);
            }
        } else {
            if (blacklist != null) {
                return !state.is(blacklist);
            } else {
                return true;
            }
        }
    }

    @Override
    public AbilitySerializer<IntangibilityAbility> getSerializer() {
        return AbilitySerializers.INTANGIBILITY.get();
    }

    public static class Serializer extends AbilitySerializer<IntangibilityAbility> {

        @Override
        public MapCodec<IntangibilityAbility> codec() {
            return CODEC;
        }
    }
}
