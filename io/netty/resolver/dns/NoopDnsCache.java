/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.handler.codec.dns.DnsRecord;
/*    */ import java.net.InetAddress;
/*    */ import java.util.Collections;
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
/*    */ 
/*    */ 
/*    */ public final class NoopDnsCache
/*    */   implements DnsCache
/*    */ {
/* 32 */   public static final NoopDnsCache INSTANCE = new NoopDnsCache();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean clear(String hostname) {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
/* 51 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DnsCacheEntry cache(String hostname, DnsRecord[] additional, InetAddress address, long originalTtl, EventLoop loop) {
/* 57 */     return new NoopDnsCacheEntry(address);
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsCacheEntry cache(String hostname, DnsRecord[] additional, Throwable cause, EventLoop loop) {
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return NoopDnsCache.class.getSimpleName();
/*    */   }
/*    */   
/*    */   private static final class NoopDnsCacheEntry implements DnsCacheEntry {
/*    */     private final InetAddress address;
/*    */     
/*    */     NoopDnsCacheEntry(InetAddress address) {
/* 74 */       this.address = address;
/*    */     }
/*    */ 
/*    */     
/*    */     public InetAddress address() {
/* 79 */       return this.address;
/*    */     }
/*    */ 
/*    */     
/*    */     public Throwable cause() {
/* 84 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 89 */       return this.address.toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\NoopDnsCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */