package io.netty.handler.codec.http2;

import io.netty.channel.Channel;

public interface Http2StreamChannel extends Channel {
  Http2FrameStream stream();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2StreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */