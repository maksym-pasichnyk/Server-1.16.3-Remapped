package io.netty.handler.codec.http;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.DecoderResultProvider;

public interface HttpObject extends DecoderResultProvider {
  @Deprecated
  DecoderResult getDecoderResult();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */