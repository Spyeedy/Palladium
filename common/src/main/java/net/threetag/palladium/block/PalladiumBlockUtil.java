package net.threetag.palladium.block;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class PalladiumBlockUtil {

    @ExpectPlatform
    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        throw new AssertionError();
    }

}
