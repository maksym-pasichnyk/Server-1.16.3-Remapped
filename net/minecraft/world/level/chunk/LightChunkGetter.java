package net.minecraft.world.level.chunk;

import javax.annotation.Nullable;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;

public interface LightChunkGetter {
  @Nullable
  BlockGetter getChunkForLighting(int paramInt1, int paramInt2);
  
  default void onLightUpdate(LightLayer debug1, SectionPos debug2) {}
  
  BlockGetter getLevel();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\LightChunkGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */