package net.minecraft.world.level.newbiome.context;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface Context {
  int nextRandom(int paramInt);
  
  ImprovedNoise getBiomeNoise();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\context\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */