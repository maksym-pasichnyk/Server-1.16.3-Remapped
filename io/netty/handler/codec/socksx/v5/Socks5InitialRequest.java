package io.netty.handler.codec.socksx.v5;

import java.util.List;

public interface Socks5InitialRequest extends Socks5Message {
  List<Socks5AuthMethod> authMethods();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\socksx\v5\Socks5InitialRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */