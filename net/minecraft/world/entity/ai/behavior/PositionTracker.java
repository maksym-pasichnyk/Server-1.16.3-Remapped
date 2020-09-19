package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public interface PositionTracker {
  Vec3 currentPosition();
  
  BlockPos currentBlockPosition();
  
  boolean isVisibleBy(LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PositionTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */