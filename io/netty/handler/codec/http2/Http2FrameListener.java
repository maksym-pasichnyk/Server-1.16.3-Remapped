package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2FrameListener {
  int onDataRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, ByteBuf paramByteBuf, int paramInt2, boolean paramBoolean) throws Http2Exception;
  
  void onHeadersRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, Http2Headers paramHttp2Headers, int paramInt2, boolean paramBoolean) throws Http2Exception;
  
  void onHeadersRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, Http2Headers paramHttp2Headers, int paramInt2, short paramShort, boolean paramBoolean1, int paramInt3, boolean paramBoolean2) throws Http2Exception;
  
  void onPriorityRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2, short paramShort, boolean paramBoolean) throws Http2Exception;
  
  void onRstStreamRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong) throws Http2Exception;
  
  void onSettingsAckRead(ChannelHandlerContext paramChannelHandlerContext) throws Http2Exception;
  
  void onSettingsRead(ChannelHandlerContext paramChannelHandlerContext, Http2Settings paramHttp2Settings) throws Http2Exception;
  
  void onPingRead(ChannelHandlerContext paramChannelHandlerContext, long paramLong) throws Http2Exception;
  
  void onPingAckRead(ChannelHandlerContext paramChannelHandlerContext, long paramLong) throws Http2Exception;
  
  void onPushPromiseRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2, Http2Headers paramHttp2Headers, int paramInt3) throws Http2Exception;
  
  void onGoAwayRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong, ByteBuf paramByteBuf) throws Http2Exception;
  
  void onWindowUpdateRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2) throws Http2Exception;
  
  void onUnknownFrame(ChannelHandlerContext paramChannelHandlerContext, byte paramByte, int paramInt, Http2Flags paramHttp2Flags, ByteBuf paramByteBuf) throws Http2Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */