package io.netty.handler.codec.socksx.v5;

public interface Socks5CommandRequest extends Socks5Message {
  Socks5CommandType type();
  
  Socks5AddressType dstAddrType();
  
  String dstAddr();
  
  int dstPort();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */