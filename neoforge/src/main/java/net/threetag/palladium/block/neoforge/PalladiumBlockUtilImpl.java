package net.threetag.palladium.block.neoforge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class PalladiumBlockUtilImpl {

    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return blockState.isFlammable(level, pos, facing);
    }

}
