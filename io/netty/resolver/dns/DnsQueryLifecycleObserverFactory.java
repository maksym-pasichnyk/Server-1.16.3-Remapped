package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DnsQuestion;

public interface DnsQueryLifecycleObserverFactory {
  DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion paramDnsQuestion);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsQueryLifecycleObserverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */