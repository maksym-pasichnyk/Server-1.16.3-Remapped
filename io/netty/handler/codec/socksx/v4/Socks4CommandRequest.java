package io.netty.handler.codec.socksx.v4;

public interface Socks4CommandRequest extends Socks4Message {
  Socks4CommandType type();
  
  String userId();
  
  String dstAddr();
  
  int dstPort();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */