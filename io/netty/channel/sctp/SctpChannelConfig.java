package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;

public interface SctpChannelConfig extends ChannelConfig {
  boolean isSctpNoDelay();
  
  SctpChannelConfig setSctpNoDelay(boolean paramBoolean);
  
  int getSendBufferSize();
  
  SctpChannelConfig setSendBufferSize(int paramInt);
  
  int getReceiveBufferSize();
  
  SctpChannelConfig setReceiveBufferSize(int paramInt);
  
  SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
  
  SctpChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams paramInitMaxStreams);
  
  SctpChannelConfig setConnectTimeoutMillis(int paramInt);
  
  @Deprecated
  SctpChannelConfig setMaxMessagesPerRead(int paramInt);
  
  SctpChannelConfig setWriteSpinCount(int paramInt);
  
  SctpChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  SctpChannelConfig setAutoRead(boolean paramBoolean);
  
  SctpChannelConfig setAutoClose(boolean paramBoolean);
  
  SctpChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  SctpChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  SctpChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark paramWriteBufferWaterMark);
  
  SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\SctpChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */