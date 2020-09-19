package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public interface BucketPickup {
  Fluid takeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BucketPickup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */