package io.netty.channel.udt;

import io.netty.channel.Channel;
import java.net.InetSocketAddress;

@Deprecated
public interface UdtChannel extends Channel {
  UdtChannelConfig config();
  
  InetSocketAddress localAddress();
  
  InetSocketAddress remoteAddress();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\UdtChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */