package io.netty.resolver.dns;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsResponseCode;
import java.net.InetSocketAddress;
import java.util.List;

public interface DnsQueryLifecycleObserver {
  void queryWritten(InetSocketAddress paramInetSocketAddress, ChannelFuture paramChannelFuture);
  
  void queryCancelled(int paramInt);
  
  DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> paramList);
  
  DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion paramDnsQuestion);
  
  DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode paramDnsResponseCode);
  
  void queryFailed(Throwable paramThrowable);
  
  void querySucceed();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsQueryLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */