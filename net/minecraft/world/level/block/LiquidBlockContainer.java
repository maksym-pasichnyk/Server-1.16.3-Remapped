package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public interface LiquidBlockContainer {
  boolean canPlaceLiquid(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid);
  
  boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LiquidBlockContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */