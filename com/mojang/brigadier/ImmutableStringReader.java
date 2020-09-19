package com.mojang.brigadier;

public interface ImmutableStringReader {
  String getString();
  
  int getRemainingLength();
  
  int getTotalLength();
  
  int getCursor();
  
  String getRead();
  
  String getRemaining();
  
  boolean canRead(int paramInt);
  
  boolean canRead();
  
  char peek();
  
  char peek(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\ImmutableStringReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */