package io.netty.channel;

import io.netty.util.IntSupplier;

public interface SelectStrategy {
  public static final int SELECT = -1;
  
  public static final int CONTINUE = -2;
  
  int calculateStrategy(IntSupplier paramIntSupplier, boolean paramBoolean) throws Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\SelectStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */