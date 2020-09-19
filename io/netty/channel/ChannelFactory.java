package io.netty.channel;

import io.netty.bootstrap.ChannelFactory;

public interface ChannelFactory<T extends Channel> extends ChannelFactory<T> {
  T newChannel();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */