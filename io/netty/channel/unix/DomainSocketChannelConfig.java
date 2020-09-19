package io.netty.channel.unix;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

public interface DomainSocketChannelConfig extends ChannelConfig {
  @Deprecated
  DomainSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  DomainSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  DomainSocketChannelConfig setWriteSpinCount(int paramInt);
  
  DomainSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  DomainSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  DomainSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  DomainSocketChannelConfig setAutoClose(boolean paramBoolean);
  
  @Deprecated
  DomainSocketChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  @Deprecated
  DomainSocketChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  DomainSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark paramWriteBufferWaterMark);
  
  DomainSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
  
  DomainSocketChannelConfig setReadMode(DomainSocketReadMode paramDomainSocketReadMode);
  
  DomainSocketReadMode getReadMode();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\DomainSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */