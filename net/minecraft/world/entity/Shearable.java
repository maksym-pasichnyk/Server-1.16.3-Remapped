package net.minecraft.world.entity;

import net.minecraft.sounds.SoundSource;

public interface Shearable {
  void shear(SoundSource paramSoundSource);
  
  boolean readyForShearing();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\Shearable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */