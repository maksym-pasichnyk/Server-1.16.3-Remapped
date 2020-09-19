package net.minecraft.world.entity;

import javax.annotation.Nullable;
import net.minecraft.sounds.SoundSource;

public interface Saddleable {
  boolean isSaddleable();
  
  void equipSaddle(@Nullable SoundSource paramSoundSource);
  
  boolean isSaddled();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\Saddleable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */