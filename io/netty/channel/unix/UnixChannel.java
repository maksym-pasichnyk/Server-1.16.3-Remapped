package io.netty.channel.unix;

import io.netty.channel.Channel;

public interface UnixChannel extends Channel {
  FileDescriptor fd();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\UnixChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */