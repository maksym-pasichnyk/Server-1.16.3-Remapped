package net.minecraft.world.level.block;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BonemealableBlock {
  boolean isValidBonemealTarget(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean);
  
  boolean isBonemealSuccess(Level paramLevel, Random paramRandom, BlockPos paramBlockPos, BlockState paramBlockState);
  
  void performBonemeal(ServerLevel paramServerLevel, Random paramRandom, BlockPos paramBlockPos, BlockState paramBlockState);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BonemealableBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */