package io.netty.channel.pool;

import io.netty.channel.Channel;

public abstract class AbstractChannelPoolHandler implements ChannelPoolHandler {
  public void channelAcquired(Channel ch) throws Exception {}
  
  public void channelReleased(Channel ch) throws Exception {}
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\pool\AbstractChannelPoolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */