package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class ZlibDecoder extends ByteToMessageDecoder {
  public abstract boolean isClosed();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\compression\ZlibDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */