package io.netty.channel;

import java.net.SocketAddress;

public interface ChannelOutboundInvoker {
  ChannelFuture bind(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
  
  ChannelFuture disconnect();
  
  ChannelFuture close();
  
  ChannelFuture deregister();
  
  ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise);
  
  ChannelFuture disconnect(ChannelPromise paramChannelPromise);
  
  ChannelFuture close(ChannelPromise paramChannelPromise);
  
  ChannelFuture deregister(ChannelPromise paramChannelPromise);
  
  ChannelOutboundInvoker read();
  
  ChannelFuture write(Object paramObject);
  
  ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise);
  
  ChannelOutboundInvoker flush();
  
  ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise);
  
  ChannelFuture writeAndFlush(Object paramObject);
  
  ChannelPromise newPromise();
  
  ChannelProgressivePromise newProgressivePromise();
  
  ChannelFuture newSucceededFuture();
  
  ChannelFuture newFailedFuture(Throwable paramThrowable);
  
  ChannelPromise voidPromise();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ChannelOutboundInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */