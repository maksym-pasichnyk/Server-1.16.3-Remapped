package io.netty.channel.udt;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

@Deprecated
public interface UdtServerChannelConfig extends UdtChannelConfig {
  int getBacklog();
  
  UdtServerChannelConfig setBacklog(int paramInt);
  
  UdtServerChannelConfig setConnectTimeoutMillis(int paramInt);
  
  @Deprecated
  UdtServerChannelConfig setMaxMessagesPerRead(int paramInt);
  
  UdtServerChannelConfig setWriteSpinCount(int paramInt);
  
  UdtServerChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  UdtServerChannelConfig setAutoRead(boolean paramBoolean);
  
  UdtServerChannelConfig setAutoClose(boolean paramBoolean);
  
  UdtServerChannelConfig setProtocolReceiveBufferSize(int paramInt);
  
  UdtServerChannelConfig setProtocolSendBufferSize(int paramInt);
  
  UdtServerChannelConfig setReceiveBufferSize(int paramInt);
  
  UdtServerChannelConfig setReuseAddress(boolean paramBoolean);
  
  UdtServerChannelConfig setSendBufferSize(int paramInt);
  
  UdtServerChannelConfig setSoLinger(int paramInt);
  
  UdtServerChannelConfig setSystemReceiveBufferSize(int paramInt);
  
  UdtServerChannelConfig setSystemSendBufferSize(int paramInt);
  
  UdtServerChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  UdtServerChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  UdtServerChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark paramWriteBufferWaterMark);
  
  UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\UdtServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */