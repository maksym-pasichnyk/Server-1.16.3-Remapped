/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.handler.codec.dns.DnsRecord;
/*    */ import io.netty.handler.codec.dns.DnsRecordType;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DnsAddressResolveContext
/*    */   extends DnsResolveContext<InetAddress>
/*    */ {
/*    */   private final DnsCache resolveCache;
/*    */   
/*    */   DnsAddressResolveContext(DnsNameResolver parent, String hostname, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs, DnsCache resolveCache) {
/* 35 */     super(parent, hostname, 1, parent.resolveRecordTypes(), additionals, nameServerAddrs);
/* 36 */     this.resolveCache = resolveCache;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   DnsResolveContext<InetAddress> newResolverContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
/* 44 */     return new DnsAddressResolveContext(parent, hostname, additionals, nameServerAddrs, this.resolveCache);
/*    */   }
/*    */ 
/*    */   
/*    */   InetAddress convertRecord(DnsRecord record, String hostname, DnsRecord[] additionals, EventLoop eventLoop) {
/* 49 */     return DnsAddressDecoder.decodeAddress(record, hostname, this.parent.isDecodeIdn());
/*    */   }
/*    */ 
/*    */   
/*    */   List<InetAddress> filterResults(List<InetAddress> unfiltered) {
/* 54 */     Class<? extends InetAddress> inetAddressType = this.parent.preferredAddressType().addressType();
/* 55 */     int size = unfiltered.size();
/* 56 */     int numExpected = 0;
/* 57 */     for (int i = 0; i < size; i++) {
/* 58 */       InetAddress address = unfiltered.get(i);
/* 59 */       if (inetAddressType.isInstance(address)) {
/* 60 */         numExpected++;
/*    */       }
/*    */     } 
/* 63 */     if (numExpected == size || numExpected == 0)
/*    */     {
/* 65 */       return unfiltered;
/*    */     }
/* 67 */     List<InetAddress> filtered = new ArrayList<InetAddress>(numExpected);
/* 68 */     for (int j = 0; j < size; j++) {
/* 69 */       InetAddress address = unfiltered.get(j);
/* 70 */       if (inetAddressType.isInstance(address)) {
/* 71 */         filtered.add(address);
/*    */       }
/*    */     } 
/* 74 */     return filtered;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void cache(String hostname, DnsRecord[] additionals, DnsRecord result, InetAddress convertedResult) {
/* 80 */     this.resolveCache.cache(hostname, additionals, convertedResult, result.timeToLive(), this.parent.ch.eventLoop());
/*    */   }
/*    */ 
/*    */   
/*    */   void cache(String hostname, DnsRecord[] additionals, UnknownHostException cause) {
/* 85 */     this.resolveCache.cache(hostname, additionals, cause, this.parent.ch.eventLoop());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsAddressResolveContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */