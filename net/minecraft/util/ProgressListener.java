package net.minecraft.util;

import net.minecraft.network.chat.Component;

public interface ProgressListener {
  void progressStartNoAbort(Component paramComponent);
  
  void progressStage(Component paramComponent);
  
  void progressStagePercentage(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\ProgressListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */