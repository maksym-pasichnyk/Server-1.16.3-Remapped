package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResultProvider;

public interface SocksMessage extends DecoderResultProvider {
  SocksVersion version();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\SocksMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */