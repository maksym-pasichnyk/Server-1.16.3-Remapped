package net.minecraft.world.level.chunk;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

public interface Palette<T> {
  int idFor(T paramT);
  
  boolean maybeHas(Predicate<T> paramPredicate);
  
  @Nullable
  T valueFor(int paramInt);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
  
  int getSerializedSize();
  
  void read(ListTag paramListTag);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\Palette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */