package net.minecraft.core;

import javax.annotation.Nullable;

public interface IdMap<T> extends Iterable<T> {
  int getId(T paramT);
  
  @Nullable
  T byId(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\IdMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */