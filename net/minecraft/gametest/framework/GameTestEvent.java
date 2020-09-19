package net.minecraft.gametest.framework;

import javax.annotation.Nullable;

class GameTestEvent {
  @Nullable
  public final Long expectedDelay;
  
  public final Runnable assertion;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */