package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public interface Http2DataWriter {
  ChannelFuture writeData(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, ByteBuf paramByteBuf, int paramInt2, boolean paramBoolean, ChannelPromise paramChannelPromise);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2DataWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */