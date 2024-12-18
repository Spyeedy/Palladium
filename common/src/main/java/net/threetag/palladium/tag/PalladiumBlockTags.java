package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;

public class PalladiumBlockTags {

    public static final TagKey<Block> PREVENTS_INTANGIBILITY = tag("prevents_intangibility");

    public static final TagKey<Block> ORES = commonTag("ores");
    public static final TagKey<Block> ORES_LEAD = commonTag("ores/lead");
    public static final TagKey<Block> ORES_TITANIUM = commonTag("ores/titanium");
    public static final TagKey<Block> ORES_VIBRANIUM = commonTag("ores/vibranium");

    public static final TagKey<Block> RAW_LEAD_BLOCKS = commonTag("storage_blocks/raw_lead");
    public static final TagKey<Block> RAW_TITANIUM_BLOCKS = commonTag("storage_blocks/raw_titanium");
    public static final TagKey<Block> RAW_VIBRANIUM_BLOCKS = commonTag("storage_blocks/raw_vibranium");

    public static final TagKey<Block> STORAGE_BLOCKS_LEAD = commonTag("storage_blocks/lead");
    public static final TagKey<Block> STORAGE_BLOCKS_VIBRANIUM = commonTag("storage_blocks/vibranium");

    private static TagKey<Block> tag(String path) {
        return tag(Palladium.MOD_ID, path);
    }

    private static TagKey<Block> tag(String domain, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(domain, path));
    }

    private static TagKey<Block> commonTag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
