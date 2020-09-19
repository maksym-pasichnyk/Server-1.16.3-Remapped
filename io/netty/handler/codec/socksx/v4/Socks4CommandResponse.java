package io.netty.handler.codec.socksx.v4;

public interface Socks4CommandResponse extends Socks4Message {
  Socks4CommandStatus status();
  
  String dstAddr();
  
  int dstPort();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v4\Socks4CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */