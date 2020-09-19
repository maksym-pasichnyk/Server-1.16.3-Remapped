package net.minecraft.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockSource extends Position {
  double x();
  
  double y();
  
  double z();
  
  BlockPos getPos();
  
  BlockState getBlockState();
  
  <T extends net.minecraft.world.level.block.entity.BlockEntity> T getEntity();
  
  ServerLevel getLevel();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\BlockSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */