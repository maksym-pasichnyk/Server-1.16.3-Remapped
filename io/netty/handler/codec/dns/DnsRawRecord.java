package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface DnsRawRecord extends DnsRecord, ByteBufHolder {
  DnsRawRecord copy();
  
  DnsRawRecord duplicate();
  
  DnsRawRecord retainedDuplicate();
  
  DnsRawRecord replace(ByteBuf paramByteBuf);
  
  DnsRawRecord retain();
  
  DnsRawRecord retain(int paramInt);
  
  DnsRawRecord touch();
  
  DnsRawRecord touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\DnsRawRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */