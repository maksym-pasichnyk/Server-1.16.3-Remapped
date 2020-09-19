package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

interface PemEncoded extends ByteBufHolder {
  boolean isSensitive();
  
  PemEncoded copy();
  
  PemEncoded duplicate();
  
  PemEncoded retainedDuplicate();
  
  PemEncoded replace(ByteBuf paramByteBuf);
  
  PemEncoded retain();
  
  PemEncoded retain(int paramInt);
  
  PemEncoded touch();
  
  PemEncoded touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\PemEncoded.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */