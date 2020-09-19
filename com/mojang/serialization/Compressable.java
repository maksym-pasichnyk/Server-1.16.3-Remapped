package com.mojang.serialization;

public interface Compressable extends Keyable {
  <T> KeyCompressor<T> compressor(DynamicOps<T> paramDynamicOps);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Compressable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */