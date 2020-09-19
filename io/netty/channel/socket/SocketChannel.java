package io.netty.channel.socket;

import java.net.InetSocketAddress;

public interface SocketChannel extends DuplexChannel {
  ServerSocketChannel parent();
  
  SocketChannelConfig config();
  
  InetSocketAddress localAddress();
  
  InetSocketAddress remoteAddress();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\socket\SocketChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */