package io.netty.handler.codec.http2;

public interface Http2StreamVisitor {
  boolean visit(Http2Stream paramHttp2Stream) throws Http2Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2StreamVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */