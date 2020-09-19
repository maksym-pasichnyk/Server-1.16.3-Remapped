package io.netty.channel;

import io.netty.util.concurrent.OrderedEventExecutor;

public interface EventLoop extends OrderedEventExecutor, EventLoopGroup {
  EventLoopGroup parent();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\EventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */