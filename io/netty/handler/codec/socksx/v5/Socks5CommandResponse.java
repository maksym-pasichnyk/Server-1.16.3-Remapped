package io.netty.handler.codec.socksx.v5;

public interface Socks5CommandResponse extends Socks5Message {
  Socks5CommandStatus status();
  
  Socks5AddressType bndAddrType();
  
  String bndAddr();
  
  int bndPort();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5CommandResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */