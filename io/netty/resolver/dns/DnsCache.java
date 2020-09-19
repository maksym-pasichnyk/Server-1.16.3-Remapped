package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.InetAddress;
import java.util.List;

public interface DnsCache {
  void clear();
  
  boolean clear(String paramString);
  
  List<? extends DnsCacheEntry> get(String paramString, DnsRecord[] paramArrayOfDnsRecord);
  
  DnsCacheEntry cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, InetAddress paramInetAddress, long paramLong, EventLoop paramEventLoop);
  
  DnsCacheEntry cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, Throwable paramThrowable, EventLoop paramEventLoop);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */