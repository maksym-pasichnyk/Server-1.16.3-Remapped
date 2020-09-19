package io.netty.channel.pool;

import io.netty.channel.Channel;

public interface ChannelPoolHandler {
  void channelReleased(Channel paramChannel) throws Exception;
  
  void channelAcquired(Channel paramChannel) throws Exception;
  
  void channelCreated(Channel paramChannel) throws Exception;
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\pool\ChannelPoolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */