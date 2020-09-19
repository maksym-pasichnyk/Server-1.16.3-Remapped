package net.minecraft.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldlyContainerHolder {
  WorldlyContainer getContainer(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\WorldlyContainerHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */