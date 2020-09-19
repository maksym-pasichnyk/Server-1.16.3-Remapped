package net.minecraft.world.level;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public interface LevelSimulatedReader {
  boolean isStateAtPosition(BlockPos paramBlockPos, Predicate<BlockState> paramPredicate);
  
  BlockPos getHeightmapPos(Heightmap.Types paramTypes, BlockPos paramBlockPos);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelSimulatedReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */